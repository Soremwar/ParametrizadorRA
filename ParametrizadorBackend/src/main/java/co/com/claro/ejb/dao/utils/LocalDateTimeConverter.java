/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Conversor de LocalDateTime a Date
 * @author andres
 */
@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Date>{

    @Override
    public Date convertToDatabaseColumn(LocalDateTime x) {
        Instant instant = Instant.from(x);
        return Date.from(instant);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Date y) {
        Instant instant = y.toInstant();
        return LocalDateTime.from(instant);    
    }
    
}
