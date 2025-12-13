package com.sharex.app.shared.web.filter;

public final class CorrelationId {

    private CorrelationId() {}

    public static final String HEADER_NAME = "X-Request-Id";
    public static final String MDC_KEY = "requestId";
}
