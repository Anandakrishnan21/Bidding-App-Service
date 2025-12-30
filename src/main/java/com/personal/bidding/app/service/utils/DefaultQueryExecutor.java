package com.personal.bidding.app.service.utils;

import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DefaultQueryExecutor implements QueryExecutor {

    private final Map<Class<?>, QueryHandler<?, ?>> handlerMap = new ConcurrentHashMap<>();

    public DefaultQueryExecutor(List<QueryHandler<?, ?>> handlers) {
        for (QueryHandler<?, ?> handler : handlers) {
            Class<?> queryType = resolveQueryType(handler);
            handlerMap.put(queryType, handler);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Query<R>, R> R execute(T query) {
        QueryHandler<T, R> handler = (QueryHandler<T, R>) handlerMap.get(query.getClass());

        if (handler == null) {
            throw new IllegalStateException(
                    "No QueryHandler found for query: " + query.getClass().getName()
            );
        }

        return handler.handle(query);
    }

    private Class<?> resolveQueryType(QueryHandler<?, ?> handler) {
        return (Class<?>) ((ParameterizedType)
                handler.getClass()
                        .getGenericInterfaces()[0])
                .getActualTypeArguments()[0];
    }
}
