package io.github.jaxrstesting.web.rest.impl;

import io.github.jaxrstesting.common.value.BookId;
import io.github.jaxrstesting.services.api.BookstoreQueryService;
import io.github.jaxrstesting.web.rest.api.BookstoreResource;
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

@Path("/books")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookResourceImpl implements BookstoreResource {

  @Inject private BookstoreQueryService queryService;

  public BookResourceImpl() {}

  @Override
  @GET
  public Response getAllBooks() {
    return null;
  }

  @Override
  @POST
  public Response createBook() {
    return null;
  }

  @Override
  @PUT
  @Path("/{bookId}")
  public Response updateBook(@PathParam("bookId") BookId bookId) {
    return null;
  }

  protected BookstoreQueryService getQueryService() {
    return queryService;
  }

  protected void setQueryService(BookstoreQueryService queryService) {
    this.queryService = queryService;
  }
}
