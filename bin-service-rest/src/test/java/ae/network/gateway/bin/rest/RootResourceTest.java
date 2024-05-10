/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.rest;

import com.jayway.jsonassert.JsonAssert;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

public class RootResourceTest {

    @Rule
    public JerseyTestRule jerseyTestRule = new JerseyTestRule(application(), "http://foo.com/");

    private ResourceConfig application() {
        return new ResourceConfiguration()
            .register(new RootResource());
    }

    @Test
    public void shouldLinkToThingsQuery() {

        Response response = jerseyTestRule.client().target("/")
            .request(CustomMediaTypes.BINSERVICE_V1_JSON_TYPE)
            .get();

        assertThat(response.getStatus()).isEqualTo(200);

        String json = response.readEntity(String.class);

        JsonAssert.with(json)
            .assertThat("$._links.curies[0].name", is("ni"))
            .assertThat("$._links.curies[0].href", is("http://gateway.network.ae/docs/rels/{rel}"))
            .assertThat("$._links.curies[0].templated", is(true))

            .assertThat("$._links.ni:bin.href", is("http://foo.com/bin/{bin}"))
            .assertThat("$._links.ni:bin.templated", is(true));

    }
}
