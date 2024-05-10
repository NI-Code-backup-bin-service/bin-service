/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.domain;

import ae.network.logging.LogEvent;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class LogEventBuilderTest {

    private final String message = "Something happened";
    private final Throwable throwable = new RuntimeException("Goodness me!");

    @Test
    public void shouldConstructWithProvidedMessage() {

        LogEvent logEvent = LogEventBuilder.build(message);

        assertThat(logEvent.getMessage(), is(message));
    }

    @Test
    public void shouldConstructProvidedMessageEvenIfNull() {

        LogEvent logEvent = LogEventBuilder.build((String) null);

        assertThat(logEvent.getMessage(), is(""));
    }

    @Test
    public void shouldConstructWithProvidedMessageAndThrowable() {

        LogEvent logEvent = LogEventBuilder.build(message, throwable);

        Map<String, String> expectedData = new HashMap<>();
        expectedData.put("exceptionMessage", throwable.getMessage());

        assertThat(logEvent.getMessage(), is(message));
        assertThat(logEvent.getData(), is(expectedData));

    }

    @Test
    public void shouldConstructWithProvidedMessageAndThrowableEvenBothNull() {

        LogEvent logEvent = LogEventBuilder.build(null, null);

        Map<String, String> expectedData = new HashMap<>();

        assertThat(logEvent.getMessage(), is(""));
        assertThat(logEvent.getData(), is(expectedData));

    }

    @Test
    public void shouldConstructLogBuilderWithOnlyThrowable() {
        LogEventBuilder logEventBuilder = new LogEventBuilder(throwable);

        Map<String, String> expectedData = new HashMap<>();
        expectedData.put("exceptionMessage", throwable.getMessage());

        LogEvent logEvent = logEventBuilder.build();

        assertThat(logEvent.getMessage(), is(throwable.getMessage()));
        assertThat(logEvent.getData(), is(expectedData));
    }

}