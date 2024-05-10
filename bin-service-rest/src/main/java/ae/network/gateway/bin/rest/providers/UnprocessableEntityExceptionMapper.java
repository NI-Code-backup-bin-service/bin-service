/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.rest.providers;

import ae.network.gateway.bin.domain.LogEventBuilder;
import ae.network.gateway.bin.domain.UnprocessableEntityException;
import ae.network.gateway.bin.rest.CustomStatus;
import ae.network.logging.loggers.ApplicationLogger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnprocessableEntityExceptionMapper implements ExceptionMapper<UnprocessableEntityException> {

    private static final ApplicationLogger LOGGER =
            ApplicationLogger.getLogger(UnprocessableEntityExceptionMapper.class);

    @Override
    public Response toResponse(UnprocessableEntityException exception) {
        LOGGER.info(
                LogEventBuilder.build(
                        "Unable to process bin because bin size is outside range",
                        exception));
        return Response.status(CustomStatus.UNPROCESSABLE_ENTITY).entity(exception.getMessage()).build();
    }

}
