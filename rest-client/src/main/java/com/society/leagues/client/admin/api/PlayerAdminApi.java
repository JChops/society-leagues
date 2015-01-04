package com.society.leagues.client.admin.api;

import com.society.leagues.client.api.domain.Player;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Path(value = "/api/admin/player")
@Consumes(MediaType.APPLICATION_JSON)
public interface PlayerAdminApi {

    @Path(value = "create")
    @POST
    Player create(Player player);

    @Path(value = "delete/{id}")
    @POST
    Boolean delete(@PathParam(value = "id") Integer id);

    @Path(value = "modify")
    @POST
    Player modify(Player player);

}