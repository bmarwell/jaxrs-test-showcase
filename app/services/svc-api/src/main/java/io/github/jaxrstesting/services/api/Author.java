package io.github.jaxrstesting.services.api;

import io.github.jaxrstesting.common.value.AuthorId;
import java.time.LocalDate;
import java.util.List;

public record Author(
    AuthorId id,
    String firstName,
    String lastName,
    LocalDate birthDate,
    String profession,
    List<String> nicknames) {
}
