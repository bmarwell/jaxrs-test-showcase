package io.github.jaxrstesting.web.rest.api;

import io.github.jaxrstesting.common.value.AuthorId;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/authors")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface AuthorResource {

  @GET
  Response getAllAuthors();

  @GET
  @Path("/{authorId}")
  Response getAuthorById(@PathParam("authorId") AuthorId authorId);

  @POST
  Response createAuthor();

  @PUT
  @Path("/{authorId}")
  Response updateAuthor(@PathParam("authorId") AuthorId authorId);

  @GET
  @Path("/{authorId}/books")
  Response getAllBooksByAuthor(@PathParam("authorId") AuthorId authorId);
}
