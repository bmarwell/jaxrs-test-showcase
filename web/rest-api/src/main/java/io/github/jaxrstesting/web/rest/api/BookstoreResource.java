package io.github.jaxrstesting.web.rest.api;

import io.github.jaxrstesting.web.rest.api.value.BookId;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/books")
public interface BookstoreResource {

  @GET
  Response getAllBooks();

  @POST
  Response createBook();

  @PUT
  @Path("/{bookid}")
  Response updateBook(@PathParam("bookid") BookId bookId);
}
