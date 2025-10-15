package com.personal.bidding.app.utils;

import com.personal.bidding.app.exception.EmptyFieldException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class Helper {

    public <T, S> void emptyChecker(S source, T target) {
        for (Field field : source.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(source);
                if (value != null && !ObjectUtils.isEmpty(value)) {
                    Field targetField = target.getClass().getDeclaredField(field.getName());
                    targetField.setAccessible(true);
                    targetField.set(target, value);
                }
            } catch (Exception e) {
//                do nothing
            }
        }
    }
}
