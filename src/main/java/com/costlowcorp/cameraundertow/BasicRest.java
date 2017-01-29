/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.costlowcorp.cameraundertow;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author erik
 */
@Path("/basic")
public class BasicRest {
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getPicturesSince(){
        final LocalDateTime now = LocalDateTime.now();
        final List<String> times = Arrays.asList(String.valueOf(now));
        return times;
    }
}
