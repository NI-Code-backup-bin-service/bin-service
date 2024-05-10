/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.rest;

import ae.network.gateway.bin.domain.BinInfo;
import ae.network.gateway.bin.domain.BinInfoQueryOperations;
import ae.network.gateway.bin.domain.BinSearchRequest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import java.util.Optional;

@Path("/bin")
@Named
public class BinResource {

    private final BinInfoQueryOperations queryOperations;

    @Inject
    BinResource(BinInfoQueryOperations queryOperations) {
        this.queryOperations = queryOperations;
    }

    @GET
    @Produces(CustomMediaTypes.BINSERVICE_V1_JSON)
    @Path("/{ref}")
    public BinInfo getBin(@PathParam("ref") String bin) {
        return queryOperations.findExactMatch(bin);
    }

    @GET
    @Produces(CustomMediaTypes.BINSERVICE_V1_JSON)
    public BinInfo searchBin(@QueryParam("bin") String bin) {
        Optional<BinInfo> optBinInfo = queryOperations.findLongestMatch(bin);
        return optBinInfo.orElse(null);
    }

    @POST
    @Produces(CustomMediaTypes.BINSERVICE_V1_JSON)
    @Consumes(CustomMediaTypes.BINSERVICE_V1_JSON)
    public BinInfo searchBin(BinSearchRequest searchRequest) {
        Optional<BinInfo> optBinInfo = queryOperations.findLongestMatch(searchRequest);
        return optBinInfo.orElse(null);
    }
}
