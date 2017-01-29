/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.costlowcorp.cameraundertow;

import java.time.LocalDateTime;
import javax.ws.rs.ext.ParamConverter;

/**
 *
 * @author erik
 */
public class LocalDateTimeConverter implements ParamConverter<LocalDateTime>{

    @Override
    public LocalDateTime fromString(String string) {
        return Formats.fromTaken(string);
    }

    @Override
    public String toString(LocalDateTime t) {
        return Formats.toTaken(t);
    }
    
}
