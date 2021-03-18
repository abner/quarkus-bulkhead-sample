package io.abner.quarkus.bulkhead

import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.Optional
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import org.eclipse.microprofile.faulttolerance.Bulkhead
import org.eclipse.microprofile.faulttolerance.Asynchronous
import org.eclipse.microprofile.faulttolerance.Fallback
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response
import kotlin.concurrent.thread

@Path("/api/hello")
class GreetingResource {

    @GET
    @Asynchronous()
    @Bulkhead(value = 200, waitingTaskQueue = 400)
    @Fallback(fallbackMethod = "fallbackHello")
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(): CompletionStage<Response> {
      val response = CompletableFuture<Response>();
      thread(start = true) {
            try
            {
               Thread.sleep(50L)
               response.complete(Response.status(200).entity("Hello RESTEasy").build())
            }
            catch (e: Exception) {
               response.completeExceptionally(e)
            }
      }
      return response
    }

    fun fallbackHello(): CompletionStage<Response> {
      val response = CompletableFuture<Response>();
      thread(start = true) {
            try
            {
                response.complete(
                    Response
                        .status(503)
                        .entity("LimitePrepreenchida")
                        .build()
                )
            }
            catch (e: Exception)
            {
               response.completeExceptionally(e)
            }
      }
      return response
    }
}