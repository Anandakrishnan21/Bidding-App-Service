package com.personal.bidding.app.service.utils;

public interface QueryExecutor {
    <T extends Query<R>, R> R execute(T query);
}
