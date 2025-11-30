package com.sharex.app.infrastructure.messaging.outbox.writer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import com.sharex.app.infrastructure.messaging.outbox.domain.DomainEvent;
import com.sharex.app.shared.event.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OutboxEventPublisherAdapter implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(OutboxEventPublisherAdapter.class);

    private final JdbcTemplate jdbc;
    private final ObjectMapper mapper;

    // caches for serializers & metadata
    private final ConcurrentHashMap<Class<?>, ObjectWriter> writerCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Class<?>, String> eventTypeCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Class<?>, String> aggregateCache = new ConcurrentHashMap<>();

    private static final String BATCH_INSERT_SQL =
            "INSERT INTO outbox_events " +
                    "(id, aggregate_type, event_type, payload, published, attempts, next_attempt_at, created_at) " +
                    "VALUES (?, ?, ?, ?, false, 0, NULL, ?)";

    // tuning: max events per batch (you can externalize)
    private final int maxBatchSize = 2000;

    public OutboxEventPublisherAdapter(JdbcTemplate jdbc, ObjectMapper mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    /**
     * High-throughput batch write. Call this in write-side transaction when you have a list of events to persist.
     * It's transactional: if calling code has a surrounding transaction, it's included.
     */
    @Transactional("writeTransactionManager")
    public void publishBatch(List<? extends DomainEvent> events) {
        if (events == null || events.isEmpty()) return;

        // chunk if events.size() > maxBatchSize
        int start = 0;
        while (start < events.size()) {
            int end = Math.min(events.size(), start + maxBatchSize);
            List<? extends DomainEvent> chunk = events.subList(start, end);
            executeBatchInsert(chunk);
            start = end;
        }
    }

    private void executeBatchInsert(List<? extends DomainEvent> events) {
        LocalDateTime now = LocalDateTime.now();

        jdbc.batchUpdate(BATCH_INSERT_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                DomainEvent ev = events.get(i);
                Class<?> cls = ev.getClass();

                String eventType = eventTypeCache.computeIfAbsent(cls, c -> {
                    try { return (String) c.getMethod("eventType").invoke(ev); }
                    catch (Exception e) { return c.getSimpleName(); }
                });

                String aggregate = aggregateCache.computeIfAbsent(cls, c -> {
                    String name = c.getSimpleName().toLowerCase();
                    return name.endsWith("event") ? name.substring(0, name.length() - 5) : name;
                });

                ObjectWriter writer = writerCache.computeIfAbsent(cls, mapper::writerFor);
                String payload;
                try { payload = writer.writeValueAsString(ev); }
                catch (Exception ex) { throw new RuntimeException("Failed to serialize event", ex); }

                ps.setString(1, UUID.randomUUID().toString());
                ps.setString(2, aggregate);
                ps.setString(3, eventType);
                ps.setString(4, payload);
                ps.setObject(5, now);
            }

            @Override
            public int getBatchSize() { return events.size(); }
        });

        log.info("[OUTBOX-WRITER] inserted batch size={}", events.size());
    }
}
