/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.costlowcorp.cameraundertow.camera;

import com.github.sarxos.webcam.WebcamMotionDetectorAlgorithm;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import org.openimaj.feature.local.matcher.FastBasicKeypointMatcher;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.math.geometry.transforms.estimation.RobustAffineTransformEstimator;
import org.openimaj.math.model.fit.RANSAC;

/**
 *
 * @author erik
 */
public class ErikDetectorAlgorithm implements WebcamMotionDetectorAlgorithm {

    private static final int SCALE_WIDTH = 320;

    private int maxPoints = 5;

    private int pointRange = 0;

    @Override
    public BufferedImage prepareImage(BufferedImage bi) {
        System.out.println("preparing " + bi + " at " + bi.getWidth() + "x" + bi.getHeight());
        final double resizePct = SCALE_WIDTH / (double)bi.getWidth();
        final int scaleHeight = (int) (resizePct * bi.getHeight());
        System.out.println("  scaling at " + resizePct + " is " + scaleHeight);
        final Image scaled = bi.getScaledInstance(SCALE_WIDTH, scaleHeight, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(SCALE_WIDTH, scaleHeight, BufferedImage.TYPE_3BYTE_BGR);
        dimg.getGraphics().drawImage(scaled, 0, 0, null);
        for (int i = 0; i < scaleHeight; i++) {

            for (int j = 0; j < SCALE_WIDTH; j++) {

                Color c = new Color(dimg.getRGB(j, i));
                int red = (int) (c.getRed() * 0.299);
                int green = (int) (c.getGreen() * 0.587);
                int blue = (int) (c.getBlue() * 0.114);
                Color newColor = new Color(red + green + blue,
                        red + green + blue, red + green + blue);

                dimg.setRGB(j, i, newColor.getRGB());
            }
        }
        return dimg;
    }

    @Override
    public boolean detect(BufferedImage bi, BufferedImage bi1) {
        return true;
    }

    @Override
    public Point getCog() {
        return new Point(0, 0);
    }

    @Override
    public double getArea() {
        return 1;
    }

    @Override
    public void setPointRange(int i) {
        this.pointRange = i;
    }

    @Override
    public void setMaxPoints(int i) {
        this.maxPoints = i;
    }

    @Override
    public int getPointRange() {
        return 0;
    }

    @Override
    public int getMaxPoints() {
        return 0;
    }

    @Override
    public ArrayList<Point> getPoints() {
        final ArrayList<Point> points = new ArrayList<>(0);
        return points;
    }

}
