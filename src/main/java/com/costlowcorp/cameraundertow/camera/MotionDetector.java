/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.costlowcorp.cameraundertow.camera;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.slf4j.LoggerFactory;

/**
 *
 * @author erik
 */
public class MotionDetector implements WebcamMotionListener {
    
    private final Webcam webcam;

    public MotionDetector(Webcam camera) {
        this.webcam = camera;
    }

    @Override
    public void motionDetected(WebcamMotionEvent wme) {
        final LocalDateTime now = LocalDateTime.now();
        final String nowFormatted = String.valueOf(now);//Formats.forExchange(now);
        //LoggerFactory.getLogger(MotionDetector.class).info("Motion detected at {} with pct {}", nowFormatted, wme.getArea());
        final BufferedImage img = webcam.getImage();
        CameraManager.INSTANCE.incrementMotions();
        final FaceScanTask command = new FaceScanTask(img, now);
        CameraManager.INSTANCE.submitTask(command);
    }

}
