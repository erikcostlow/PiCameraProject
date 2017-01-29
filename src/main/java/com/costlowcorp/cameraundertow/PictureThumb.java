/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.costlowcorp.cameraundertow;

import java.util.List;

/**
 *
 * @author erik
 */
public class PictureThumb {
    
    private final String whenTaken;
    
    private final List<String> plates;
    
    private final int faces;
    
    public PictureThumb(String whenTaken, List<String> plates, int faces){
        this.whenTaken=whenTaken;
        this.plates=plates;
        this.faces=faces;
    }

    public String getWhenTaken() {
        return whenTaken;
    }

    public List<String> getPlates() {
        return plates;
    }

    public int getFaces() {
        return faces;
    }
}
