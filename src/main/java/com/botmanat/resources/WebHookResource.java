package com.botmanat.resources;

import com.botmanat.controller.WebHookController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/webhook")
public class WebHookResource {

    private WebHookController controller;

    public WebHookResource(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        this.controller = new WebHookController(request);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response verification(@QueryParam("hub.mode") String hubMode,
                                 @QueryParam("hub.verify_token") String token,
                                 @QueryParam("hub.challenge") String challenge) {
        return this.controller.verifyWebHook(hubMode, token, challenge);

    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    public Response listener(String string) {
        return this.controller.listener(string);

    }

    @GET
    @Path("/daily-fetch")
    public Response dailyCurrencyFetch(@Context HttpServletRequest request) {
        if (!isAuthorized(request)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return this.controller.dailyCurrencyFetch();
    }

    @GET
    @Path("/bank-rates-fetch")
    public Response bankRatesFetch(@Context HttpServletRequest request) {
        if (!isAuthorized(request)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return this.controller.fetchBankRates();
    }


    private boolean isAuthorized(@Context HttpServletRequest request) {
        return request.getHeader("X-Appengine-Cron") != null;
    }

    @GET
    @Path("/plot-data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response plot(@QueryParam("currency") String currency) {
        return this.controller.plotData(currency);
    }

    @GET
    @Path("/exchange-rates")
    @Produces(MediaType.APPLICATION_JSON)
    public Response exchangeRates() {
        return this.controller.getExchangeRates();
    }

}
