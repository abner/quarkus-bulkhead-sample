package io.abner.quarkus.bulkhead

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import org.eclipse.microprofile.faulttolerance.Bulkhead
import org.eclipse.microprofile.faulttolerance.Fallback
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response

@Path("/api/barramento")
class GreetingResource {

    @GET
    @Bulkhead(value = 15)
    @Fallback(fallbackMethod = "falhaObtencaoResultado")
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(): Response {
        Thread.sleep(50L)
        return Response.status(200).entity("Hello RESTEasy").build()
    }

    fun falhaObtencaoResultado(): Response {
        return Response
            .status(400)
            .entity("LimitePrepreenchida")
            .build()
    }
}