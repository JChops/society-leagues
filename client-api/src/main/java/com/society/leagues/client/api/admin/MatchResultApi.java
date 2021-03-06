package com.society.leagues.client.api.admin;

import com.society.leagues.client.api.domain.PlayerMatch;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path(value = "/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface MatchResultApi {

    @Path(value = "api/admin/match/save")
    @POST
    Integer save(PlayerMatch matchResult);

    @Path(value = "api/admin/match/delete/{id}")
    @GET
    Boolean delete(@PathParam(value = "id")Integer id);

    @Path(value = "api/admin/match/get/{id}")
    @GET
    PlayerMatch get(@PathParam(value = "id")Integer id);

}
