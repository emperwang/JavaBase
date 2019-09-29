package com.wk.web.controller;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
public class TestJerseyController {

    @GET
    @Path("/str")
    @Produces(MediaType.APPLICATION_JSON)
    public String Index(){
        return "this is jersey";
    }

}
