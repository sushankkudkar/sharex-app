package com.sharex.app.infrastructure.messaging.outbox.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
public class OutboxEventDispatcher {

    private static final Logger log = LoggerFactory.getLogger(OutboxEventDispatcher.class);

    private final JdbcTemplate jdbc;
    private final KafkaTemplate<String, String> kafka;

    // tuning params
    private final int batchSize = 500;     // claim this many per run
    private final int maxAttempts = 8;
    private final long baseBackoffMillis = 500L;

    private static final String CLAIM_SQL =
            "SELECT id, event_type, payload, attempts " +
                    "FROM outbox_events " +
                    "WHERE published = false " +
                    "  AND (next_attempt_at IS NULL OR next_attempt_at <= NOW()) " +
                    "ORDER BY created_at ASC " +
                    "FOR UPDATE SKIP LOCKED " +
                    "LIMIT ?";

    public OutboxEventDispatcher(JdbcTemplate jdbc, KafkaTemplate<String, String> kafka) {
        this.jdbc = jdbc;
        this.kafka = kafka;
    }

    /**
     * Runs frequently. Make sure scheduling thread count is enough.
     */
    @Scheduled(fixedDelayString = "${outbox.dispatcher.delay-ms:500}")
    @Transactional("writeTransactionManager")
    public void dispatch() {
        List<OutboxRow> rows = jdbc.query(
                CLAIM_SQL,
                ps -> ps.setInt(1, batchSize),
                this::mapRow
        );

        if (rows.isEmpty()) return;

        try {
            kafka.executeInTransaction(kt -> {

                for (OutboxRow r : rows) {
                    kt.send(
                            r.eventType(),   // topic
                            r.id(),          // key (idempotency)
                            r.payload()      // value
                    );
                }
                return null;
            });

            markPublished(rows);
            log.info("[OUTBOX-DISPATCHER] published batch size={}", rows.size());

        } catch (Exception ex) {
            log.warn("[OUTBOX-DISPATCHER] batch failed, marking attempts", ex);
            markFailed(rows, ex);
        }
    }

    private OutboxRow mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new OutboxRow(
                rs.getString("id"),
                rs.getString("event_type"),
                rs.getString("payload"),
                rs.getInt("attempts")
        );
    }

    private void markPublished(List<OutboxRow> rows) {
        String sql = "UPDATE outbox_events SET published = true, processed_at = NOW() WHERE id = ?";
        jdbc.batchUpdate(sql, rows, rows.size(), (ps, r) -> ps.setString(1, r.id));
    }

    private void markFailed(List<OutboxRow> rows, Exception ex) {
        LocalDateTime now = LocalDateTime.now();
        String sql = "UPDATE outbox_events SET attempts = ?, next_attempt_at = ?, last_error = ? WHERE id = ?";

        jdbc.batchUpdate(sql, rows, rows.size(), (ps, r) -> {
            Objects.requireNonNull(r, "OutboxRow must not be null");
            int next = r.attempts + 1;
            if (next >= maxAttempts) {
                ps.setInt(1, next);
                ps.setObject(2, now.plusDays(365)); // manual intervention
                ps.setString(3, "MAX_ATTEMPTS: " + ex.getMessage());
            } else {
                long backoff = Math.min(baseBackoffMillis * (1L << (next - 1)), 30 * 60 * 1000L);
                ps.setInt(1, next);
                ps.setObject(2, now.plus(Duration.ofMillis(backoff)));
                ps.setString(3, ex.getMessage());
            }
            ps.setString(4, r.id);
        });
    }

    private record OutboxRow(String id, String eventType, String payload, int attempts) {}
}
