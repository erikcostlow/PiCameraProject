/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.costlowcorp.cameraundertow.camera;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDriver;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionDetectorAlgorithm;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author erik
 */
public enum CameraManager {
    INSTANCE;

    private AtomicInteger todayMotions = new AtomicInteger();
    private AtomicInteger todayPhotos = new AtomicInteger();
    private AtomicInteger todayPlates = new AtomicInteger();
    private AtomicInteger todayFaces = new AtomicInteger();
    
    private double avgPctChangeForPhoto;
    

    private Webcam camera;
    private WebcamMotionDetector detector;
    
    private final Executor executor = Executors.newCachedThreadPool(r -> {
        final Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

    public void changeDriver(Class<? extends WebcamDriver> driverClass) {
        if (camera!=null && camera.isOpen()) {
            camera.close();
        }
        Webcam.setDriver(driverClass);
        prepCamera();
    }

    public BufferedImage getImage() {
        if (!camera.isOpen()) {
            camera.open();
        }
        final BufferedImage image = camera.getImage();
        //camera.close();
        return image;
    }

    public int getTodayMotions() {
        return todayMotions.intValue();
    }

    public int getTodayPhotos() {
        return todayPhotos.intValue();
    }

    public int getTodayPlates() {
        return todayPlates.intValue();
    }

    public int getTodayFaces() {
        return todayFaces.intValue();
    }

    private void prepCamera() {
        if (camera != null) {
            camera.close();
            detector.stop();
        }
        camera = Webcam.getDefault();
        final WebcamMotionDetectorAlgorithm algorithm = new ErikDetectorAlgorithm();
        detector = new WebcamMotionDetector(camera, algorithm, 500);

        detector.addMotionListener(new MotionDetector(camera));
        detector.start();
    }

    void incrementMotions() {
        todayMotions.incrementAndGet();
    }

    void addFaces(int count) {
        todayFaces.addAndGet(count);
    }

    void approxRollingAverage(double new_sample) {
        final int N = this.todayPhotos.get();
        double avg = this.avgPctChangeForPhoto;
        avg -= avg / N;
        avg += new_sample / N;

        this.avgPctChangeForPhoto=avg;
    }
    
    public void submitTask(Runnable r){
        executor.execute(r);
    }
}
