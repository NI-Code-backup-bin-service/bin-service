/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.rest;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("/")
@Named
public class RootResource {

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(CustomMediaTypes.BINSERVICE_V1_JSON)
    @Consumes(CustomMediaTypes.BINSERVICE_V1_JSON)
    public HateoasSupport<?> get() {

        HateoasSupport<?> hateoasSupport = new HateoasSupport<>();
        String href = uriInfo.getBaseUriBuilder()
                .path(BinResource.class)
                .toTemplate();
        String exactMatchLink = href + "/{bin}";

        String longestMatchLink = href + "{?bin}";

        hateoasSupport.add(TemplatedLink.of("ni:bin", exactMatchLink));
        hateoasSupport.add(TemplatedLink.of("ni:bin-search", longestMatchLink));
        hateoasSupport.addCurie(Curie.of("ni", "http://gateway.network.ae/docs/rels/{rel}", true));
        return hateoasSupport;
    }
}
