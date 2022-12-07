package io.github.jaxrstesting.services.impl.inmemory.repository;

import java.io.Serializable;
import org.immutables.criteria.repository.Repository;
import org.immutables.criteria.repository.sync.SyncRepository;

public interface AuthorRepository extends Serializable, Repository<AuthorPdo>, SyncRepository.Readable<AuthorPdo>,
    SyncRepository.Writable<AuthorPdo> {}
