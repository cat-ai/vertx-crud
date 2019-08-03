package io.cat.ai.core.crud;

@SuppressWarnings("unchecked")
interface AsyncCrud<T, R> {

    void select(T t, R... r);

    void update(T t, R... r);

    void delete(T t, R... r);

    void insert(T t, R... r);
}
