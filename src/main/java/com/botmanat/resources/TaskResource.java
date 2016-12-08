package com.botmanat.resources;

import com.botmanat.controller.TaskRunner;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tasks")
public class TaskResource {


    @POST
    @Produces(MediaType.TEXT_HTML)
    @Path("/subscriber-name-fetcher")
    public Response fetchSubscriberName(@Context HttpServletRequest request) {
        if (!isAuthorized(request)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return TaskRunner.getSubscriberNameTask(request);
    }

    private boolean isAuthorized(@Context HttpServletRequest request) {
        return request.getHeader("X-AppEngine-QueueName") != null;
    }
}
