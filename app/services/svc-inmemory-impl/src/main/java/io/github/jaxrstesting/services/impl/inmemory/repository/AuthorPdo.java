package io.github.jaxrstesting.services.impl.inmemory.repository;

import io.github.jaxrstesting.common.value.AuthorId;
import java.time.LocalDate;
import java.util.List;
import org.immutables.criteria.Criteria;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(jdkOnly = true, stagedBuilder = true)
@Criteria
@Criteria.Repository
public interface AuthorPdo {

  @Criteria.Id
  AuthorId id();

  String firstName();

  String lastName();

  LocalDate birthDate();

  String profession();

  List<String> nicknames();
}
