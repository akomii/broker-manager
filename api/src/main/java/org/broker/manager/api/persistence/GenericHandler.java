package org.broker.manager.api.persistence;

import java.util.List;
import java.util.Optional;

public interface GenericHandler<T, K> {

  void save(T entity);

  void delete(K id);

  Optional<T> get(K id);

  List<T> getAll();
}
