/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.rest.providers;

import ae.network.gateway.bin.domain.LogEventBuilder;
import ae.network.gateway.bin.domain.UnknownEntityException;
import ae.network.logging.loggers.ApplicationLogger;
import com.google.common.collect.ImmutableMap;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnknownEntityExceptionMapper implements ExceptionMapper<UnknownEntityException> {

    private static final ApplicationLogger LOGGER =
            ApplicationLogger.getLogger(UnknownEntityExceptionMapper.class);

    @Override
    public Response toResponse(UnknownEntityException exception) {
        LOGGER.info(
                LogEventBuilder.build(
                        "Unable to find requested bin",
                        exception));
        return Response.status(Response.Status.NOT_FOUND)
                .entity(ImmutableMap.of("message", exception.getMessage()))
                .build();
    }
}
