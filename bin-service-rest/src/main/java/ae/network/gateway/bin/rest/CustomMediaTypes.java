/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.rest;

import javax.ws.rs.core.MediaType;

public final class CustomMediaTypes {
    private static final String BIN_SERVICE_NS = "ni.bin-service";

    public static final String BINSERVICE_V1_JSON = "application/vnd." + BIN_SERVICE_NS + ".v1+json";

    static final MediaType BINSERVICE_V1_JSON_TYPE =
            new MediaType("application", "vnd." + BIN_SERVICE_NS + ".v1+json");

    private CustomMediaTypes() {
        throw new IllegalStateException("Can't instantiate a utility class.");
    }
}
