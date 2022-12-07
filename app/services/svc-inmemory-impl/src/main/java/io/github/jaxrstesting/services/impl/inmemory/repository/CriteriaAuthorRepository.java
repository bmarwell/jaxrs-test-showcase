package io.github.jaxrstesting.services.impl.inmemory.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.immutables.criteria.Criterion;
import org.immutables.criteria.backend.WriteResult;
import org.immutables.criteria.inmemory.InMemoryBackend;
import org.immutables.criteria.repository.Updater;
import org.immutables.criteria.repository.sync.SyncReader;

@ApplicationScoped
public class CriteriaAuthorRepository implements AuthorRepository {

  private final AuthorPdoRepository authorPdoRepository = new AuthorPdoRepository(new InMemoryBackend());

  public SyncReader<AuthorPdo> find(Criterion<AuthorPdo> arg0) {
    return authorPdoRepository.find(arg0);
  }

  public SyncReader<AuthorPdo> findAll() {
    return authorPdoRepository.findAll();
  }

  public WriteResult insert(AuthorPdo doc) {
    return authorPdoRepository.insert(doc);
  }

  public WriteResult insertAll(Iterable<? extends AuthorPdo> arg0) {
    return authorPdoRepository.insertAll(arg0);
  }

  public WriteResult delete(Criterion<AuthorPdo> arg0) {
    return authorPdoRepository.delete(arg0);
  }

  public WriteResult upsert(AuthorPdo doc) {
    return authorPdoRepository.upsert(doc);
  }

  public WriteResult upsertAll(Iterable<? extends AuthorPdo> arg0) {
    return authorPdoRepository.upsertAll(arg0);
  }

  public WriteResult update(AuthorPdo doc) {
    return authorPdoRepository.update(doc);
  }

  public WriteResult updateAll(Iterable<? extends AuthorPdo> arg0) {
    return authorPdoRepository.updateAll(arg0);
  }

  public Updater<AuthorPdo, WriteResult> update(Criterion<AuthorPdo> arg0) {
    return authorPdoRepository.update(arg0);
  }

}
