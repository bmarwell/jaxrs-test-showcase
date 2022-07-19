package io.github.jaxrstesting.web.rest.impl;

import io.github.jaxrstesting.common.value.AuthorId;
import io.github.jaxrstesting.services.api.Author;
import io.github.jaxrstesting.services.api.BookstoreQueryService;
import io.github.jaxrstesting.services.api.query.AuthorByIdQuery;
import io.github.jaxrstesting.web.rest.api.AuthorResource;
import io.github.jaxrstesting.web.rest.api.value.AuthorRestDto;
import io.github.jaxrstesting.web.rest.impl.mapper.AuthorMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.Optional;

@Path("/authors")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AuthorResourceImpl implements AuthorResource {

  @Inject
  private BookstoreQueryService queryService;

  @Inject
  private AuthorMapper authorMapper;

  public AuthorResourceImpl() {}

  @Override
  @GET
  public Response getAllAuthors() {
    return null;
  }

  @GET
  @Path("/{authorId}")
  public Response getAuthorById(@PathParam("authorId") AuthorId authorId) {
    Optional<Author> author =
        this.queryService.queryAuthors(new AuthorByIdQuery(authorId)).findAny();

    if (author.isEmpty()) {
      return Response
          .status(Status.NOT_FOUND)
          .build();
    }

    AuthorRestDto authorRestDto = this.authorMapper.mapToAuthor(author.orElseThrow());

    return Response
        .ok(authorRestDto)
        .build();
  }

  @Override
  @POST
  public Response createAuthor() {
    return null;
  }

  @Override
  @PUT
  @Path("/{authorId}")
  public Response updateAuthor(@PathParam("authorId") AuthorId authorId) {
    return null;
  }

  @Override
  @GET
  @Path("/{authorId}/books")
  public Response getAllBooksByAuthor(@PathParam("authorId") AuthorId authorId) {
    return null;
  }

  protected BookstoreQueryService getQueryService() {
    return queryService;
  }

  protected void setQueryService(BookstoreQueryService queryService) {
    this.queryService = queryService;
  }

  public AuthorMapper getAuthorMapper() {
    return authorMapper;
  }

  public void setAuthorMapper(AuthorMapper authorMapper) {
    this.authorMapper = authorMapper;
  }
}
