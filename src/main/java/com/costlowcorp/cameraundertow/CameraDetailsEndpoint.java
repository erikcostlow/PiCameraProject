/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.costlowcorp.cameraundertow;

import com.costlowcorp.cameraundertow.camera.CameraManager;
import com.costlowcorp.raspicam.sarxos.RaspiCamDriver;
import com.costlowcorp.raspicam.sarxos.WrappedRaspiStillDriver;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamCompositeDriver;
import com.github.sarxos.webcam.WebcamDriver;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author erik
 */
@Path("/system")
public class CameraDetailsEndpoint {
    
    private static final SortedSet<String> DRIVERS;

    static {
        final SortedSet<String> drivers = new TreeSet<>(Arrays.asList(
                WebcamCompositeDriver.class.getName(),
                RaspiCamDriver.class.getName(),
                WrappedRaspiStillDriver.class.getName()
        ));
        DRIVERS = drivers;
    }
    
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> getPicturesSince(){
        final Map<String, String> retval=new HashMap<>();
        retval.put("version", "1.0-alpha");
        retval.put("currentTime", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        String myHostname = "unknown";
        String address = "unknown";
        try {
            final InetAddress inet = InetAddress.getLocalHost();
            myHostname = inet.getHostName();
            address = inet.getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(CameraDetailsEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        retval.put("hostname", myHostname);
        retval.put("address", address);
        retval.put("driver", Webcam.getDriver().getClass().getName());
        
        long jvmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        final Date started = new Date(jvmStartTime);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        
        retval.put("started", sdf.format(started));
        return retval;
    }
    
    @GET
    @Path("/driver")
    @Produces(MediaType.APPLICATION_JSON)
    public SortedSet<String> listDrivers(){
        return DRIVERS;
    }
    
    @GET
    @Path("/driver/{class}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean changeDriver(@PathParam("class") String driver){
        if(DRIVERS.contains(driver)){
            try {
                //TODO change the driver
                final Class<? extends WebcamDriver> clazz = (Class<? extends WebcamDriver>) Class.forName(driver);
                CameraManager.INSTANCE.changeDriver(clazz);
                return true;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CameraDetailsEndpoint.class.getName()).log(Level.SEVERE, "Unable to get class", ex);
            }
        }
        return false;
    }
}
