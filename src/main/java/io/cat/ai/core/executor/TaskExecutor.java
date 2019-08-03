package io.cat.ai.core.executor;

import java.util.List;

@SuppressWarnings("unchecked")
public interface TaskExecutor<T, B, H> {

    void executeSingle(T t, H handler, T... args);

    void executeBatch(T t, H handler, List<B> batch);
}
