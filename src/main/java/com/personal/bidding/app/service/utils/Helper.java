package com.personal.bidding.app.service.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class Helper {
    public <S, T> void copyProperties(S source, T target) {
        BeanUtils.copyProperties(source, target);
    }
}
