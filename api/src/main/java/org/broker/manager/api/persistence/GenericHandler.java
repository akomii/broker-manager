package org.broker.manager.api.persistence;

import java.util.List;
import java.util.Optional;

public interface GenericHandler<T, ID> {
    
    void save(T entity);
    
    void delete(ID id);
    
    Optional<T> get(ID id);
    
    List<T> getAll();
}
