/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.domain;

import ae.network.logging.LogEvent;

import javax.validation.constraints.NotNull;

public class LogEventBuilder {

    private LogEvent logEvent;

    private static final String DEFAULT_MESSAGE = "";

    public LogEventBuilder(@NotNull String message) {
        this.logEvent = new LogEvent(makeMessageSafe(message));
    }

    public LogEventBuilder(@NotNull Throwable throwable) {
        this(makeMessageSafe(throwable.getMessage()));
        this.logEvent = this.withException(throwable).build();
    }

    public static LogEvent build(String message) {
        return new LogEventBuilder(message).build();
    }

    public static LogEvent build(String message, Throwable throwable) {
        return new LogEventBuilder(message).withException(throwable).build();
    }

    public static LogEvent build(Throwable throwable) {
        return new LogEventBuilder(throwable.getMessage()).withException(throwable).build();
    }

    public LogEvent build() {
        return this.logEvent;
    }

    public LogEventBuilder withException(Throwable throwable) {
        if (throwable != null) {
            this.logEvent = this.logEvent.withData(DataKeys.EXCEPTION_MESSAGE.key, throwable.getMessage());
        }
        return this;
    }


    private static String makeMessageSafe(String message) {
        return (message != null) ? message : DEFAULT_MESSAGE;
    }

    private enum DataKeys {

        EXCEPTION_MESSAGE("exceptionMessage");

        private final String key;

        DataKeys(String key) {
            this.key = key;
        }

    }
}
