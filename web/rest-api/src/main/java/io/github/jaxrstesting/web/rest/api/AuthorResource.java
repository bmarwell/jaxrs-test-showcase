package io.github.jaxrstesting.web.rest.api;

import io.github.jaxrstesting.web.rest.api.value.AuthorId;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/authors")
public interface AuthorResource {

  @GET
  Response getAllAuthors();

  @POST
  Response createAuthor();

  @PUT
  @Path("/{authorId}")
  Response updateAuthor(@PathParam("authorId") AuthorId authorId);

  @GET
  @Path("/{authorId}/books")
  Response getAllBooksByAuthor(@PathParam("authorId") AuthorId authorId);
}
