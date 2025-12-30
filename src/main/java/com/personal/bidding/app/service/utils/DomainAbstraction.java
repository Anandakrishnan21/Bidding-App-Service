package com.personal.bidding.app.service.utils;

public interface DomainAbstraction {
    default String handlerName() {
        return String.format("%s%sHandler", Character.toLowerCase(this.getClass().getSimpleName().charAt(0)), this.getClass().getSimpleName().substring(1));
    }
}
