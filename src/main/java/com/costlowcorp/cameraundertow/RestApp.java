/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.costlowcorp.cameraundertow;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author erik
 */
public class RestApp extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> clazzes = new HashSet<>();
        clazzes.add(BasicRest.class);
        clazzes.add(CameraDetailsEndpoint.class);
        clazzes.add(ImagesEndpoint.class);
        return clazzes;
    }
}