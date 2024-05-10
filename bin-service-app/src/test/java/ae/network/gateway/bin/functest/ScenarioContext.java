/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.functest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ScenarioContext {

    private String responseBody;
    private Response response;
    private String accessToken;
    private String mediaType = MediaType.APPLICATION_JSON;

    public void consume(Response response) {
        this.response = response;
        responseBody = response.readEntity(String.class);
    }

    public Response getResponse() {
        return response;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaType() {
        return mediaType;
    }
}
