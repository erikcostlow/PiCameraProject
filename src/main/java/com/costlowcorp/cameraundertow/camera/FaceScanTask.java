/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.costlowcorp.cameraundertow.camera;

import com.costlowcorp.cameraundertow.Formats;
import com.costlowcorp.cameraundertow.ImagesEndpoint;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.face.detection.keypoints.FKEFaceDetector;
import org.openimaj.image.processing.face.detection.keypoints.KEDetectedFace;
import org.openimaj.image.processing.face.util.KEDetectedFaceRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author erik
 */
public class FaceScanTask implements Runnable{
    private static final Logger LOG = LoggerFactory.getLogger(FaceScanTask.class.getSimpleName());
    
    private static final FKEFaceDetector DETECTOR = new FKEFaceDetector();
    private static final KEDetectedFaceRenderer KERenderer = new KEDetectedFaceRenderer();
    
    private final BufferedImage buffImg;
    private final LocalDateTime when;
    
    public FaceScanTask(BufferedImage image, LocalDateTime when){
        //this.mbf=image;
        this.buffImg = image;
        this.when=when;
    }
    
    @Override
    public void run() {
        final FImage fImage = ImageUtilities.createFImage(buffImg);
        final List<KEDetectedFace> faces = DETECTOR.detectFaces(fImage);
        if(!faces.isEmpty()){
            CameraManager.INSTANCE.addFaces(faces.size());
            final MBFImage mbf = ImageUtilities.createMBFImage(buffImg, true);
            LOG.info("Found {} faces at {}", faces.size(), when);
            faces.stream().forEach(f -> KERenderer.drawDetectedFace(mbf, 5, f));
            final String filename = Formats.toTaken(when) + ".jpeg";
            final Path outPath = ImagesEndpoint.getIMAGES_ROOT().resolve(filename);
            try(OutputStream out = Files.newOutputStream(outPath)){
                ImageUtilities.write(mbf, "JPG", out);
            } catch (IOException ex) {
                LOG.warn("Error scanning for faces", ex);
            }
        }
    }
    
}
