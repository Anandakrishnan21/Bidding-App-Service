package com.personal.bidding.app.service.utils;

public interface QueryHandler<T extends Query<R>, R>{
    R handle (T query);
}
