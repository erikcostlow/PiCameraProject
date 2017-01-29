/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.costlowcorp.cameraundertow;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author erik
 */
@Provider
public class LocalDateTimeConverterProvider implements ParamConverterProvider{

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> clazz, Type type, Annotation[] antns) {
        System.out.println("Checking conversion of " + clazz.getName());
        if(LocalDateTime.class.equals(clazz)){
            return (ParamConverter<T>) new LocalDateTimeConverter();
        }
        return null;
    }
    
}
