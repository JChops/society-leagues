package com.society.leagues.client.api;

import com.society.leagues.client.api.admin.UserAdminApi;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(value = "/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UserApi extends UserAdminApi,UserClientApi {
}
