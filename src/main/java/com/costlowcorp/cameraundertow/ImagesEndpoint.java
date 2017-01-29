/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.costlowcorp.cameraundertow;

import com.costlowcorp.cameraundertow.camera.CameraManager;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author erik
 */
@Path("/images")
public class ImagesEndpoint {
    
    private static java.nio.file.Path IMAGES_ROOT = Paths.get(".");
    
    @GET
    @Path("/current")
    @Produces("image/jpeg")
    public byte[] current(){
        final BufferedImage image = CameraManager.INSTANCE.getImage();
        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "JPG", bout);
        } catch (IOException ex) {
            Logger.getLogger(ImagesEndpoint.class.getName()).log(Level.SEVERE, "Unable to take picture", ex);
        }
        final byte[] bytes = bout.toByteArray();
        return bytes;
    }
    
    @GET
    @Path("/todayStats")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> todayStats(){
        final Map<String, String> retval = new HashMap<>();
        retval.put("todayPhotos", String.valueOf(CameraManager.INSTANCE.getTodayPhotos()));
        retval.put("todayMotions", String.valueOf(CameraManager.INSTANCE.getTodayMotions()));
        retval.put("todayPlates", String.valueOf(CameraManager.INSTANCE.getTodayPlates()));
        retval.put("todayFaces", String.valueOf(CameraManager.INSTANCE.getTodayFaces()));
        return retval;
    }
    
    @GET
    @Path("/between/{start}/{end}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PictureThumb> between(@PathParam("start") String start, @PathParam("end") String end){
        System.out.println("Between " + start + " and " + end);
        final LocalDate startDate = LocalDate.parse(start, DateTimeFormatter.ISO_DATE);
        final LocalDate endDate = LocalDate.parse(end, DateTimeFormatter.ISO_DATE);
        final List<PictureThumb> retval = new ArrayList<>();
        try {
            Files.list(IMAGES_ROOT)
                    //.filter(p -> p.getFileName().toString().startsWith(start))
                    .filter(p -> p.getFileName().toString().endsWith(".jpeg"))
                    .sorted()
                    .map(ImagesEndpoint::convert)
                    .forEach(retval::add);
        } catch (IOException ex) {
            Logger.getLogger(ImagesEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return retval;
    }
    
    @GET
    @Path("/thumb/{date}")
    @Produces("image/jpeg")
    public Response photo(@PathParam("date") String whenStr){
        final java.nio.file.Path path = find(whenStr);
        if(!Files.exists(path)){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        
        try(InputStream in = Files.newInputStream(path)) {
            final BufferedImage image = ImageIO.read(in);
            final byte[] b = new byte[2048];
            //for(int length=in.read(b); length>0; length=in.read(b)){
                //stream.write(b, 0, length);
            //}
            final int width=320, height=240;
            final Image scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage dimg = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            dimg.getGraphics().drawImage(scaled, 0, 0, null);
            ImageIO.write(dimg, "JPG", stream);
        } catch (IOException ex) {
            Logger.getLogger(ImagesEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok(stream.toByteArray()).build();
    }
    
    @GET
    @Path("/full/{date}")
    @Produces("image/jpeg")
    public Response full(@PathParam("date") String whenStr){
        final java.nio.file.Path path = find(whenStr);
        if(!Files.exists(path)){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        
        try(InputStream in = Files.newInputStream(path)) {
            final byte[] b = new byte[2048];
            for(int length=in.read(b); length>0; length=in.read(b)){
                stream.write(b, 0, length);
            }
        } catch (IOException ex) {
            Logger.getLogger(ImagesEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok(stream.toByteArray()).build();
    }
    
    private static PictureThumb convert(java.nio.file.Path path){
        final String fName = path.getFileName().toString();
        final PictureThumb pb = new PictureThumb(fName.substring(0, fName.lastIndexOf('.')), Collections.EMPTY_LIST, 0);
        return pb;
    }

    public static java.nio.file.Path getIMAGES_ROOT() {
        return IMAGES_ROOT;
    }

    public static void setIMAGES_ROOT(java.nio.file.Path newRoot) {
        ImagesEndpoint.IMAGES_ROOT = newRoot;
    }

    private java.nio.file.Path find(String whenStr) {
        final LocalDateTime when = Formats.fromTaken(whenStr);
        final String filename = Formats.toTaken(when) + ".jpeg";
        final java.nio.file.Path path = IMAGES_ROOT.resolve(filename);
        return path;
    }
}
