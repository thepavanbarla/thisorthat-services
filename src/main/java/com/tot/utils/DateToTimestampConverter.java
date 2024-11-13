package com.tot.utils;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;

/**
 * @author karthik on 04/12/21.
 * @project totservices
 */
@Component public class DateToTimestampConverter implements Converter<Date, Timestamp> {

    @SneakyThrows @Override public Timestamp convert(Date date) {
        return new Timestamp(date.getTime());
    }

}
