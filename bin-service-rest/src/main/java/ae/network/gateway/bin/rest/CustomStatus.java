/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.rest;

import javax.ws.rs.core.Response;

public enum CustomStatus implements Response.StatusType {

    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity");

    private final int code;
    private final String reason;
    private final Response.Status.Family family;

    CustomStatus(final int statusCode, final String reasonPhrase) {
        this.code = statusCode;
        this.reason = reasonPhrase;
        this.family = Response.Status.Family.familyOf(statusCode);
    }

    @Override
    public int getStatusCode() {
        return code;
    }

    @Override
    public Response.Status.Family getFamily() {
        return family;
    }

    @Override
    public String getReasonPhrase() {
        return reason;
    }
}