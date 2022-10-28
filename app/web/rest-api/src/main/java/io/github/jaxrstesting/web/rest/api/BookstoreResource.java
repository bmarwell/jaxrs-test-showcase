package io.github.jaxrstesting.web.rest.api;

import io.github.jaxrstesting.common.value.BookId;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/books")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface BookstoreResource {

  @GET
  Response getAllBooks();

  @POST
  Response createBook();

  @PUT
  @Path("/{bookId}")
  Response updateBook(@PathParam("bookId") BookId bookId);
}
