package org.app.DAOS;

import java.util.Set;

public interface IDAO<T> {
    T getBytId(Integer id);
    Set<T> getAll();
    void create(T t);
    void update(T t);
    void delete(T t);
}
