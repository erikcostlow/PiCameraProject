 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.costlowcorp.cameraundertow;

import com.costlowcorp.cameraundertow.camera.CameraManager;
import com.github.sarxos.webcam.WebcamDriver;
import static io.undertow.Handlers.resource;
import io.undertow.server.handlers.resource.PathResourceManager;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.undertow.Undertow;  
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.jboss.resteasy.plugins.server.undertow.ErikUndertowJaxRsServer;

/**
 *
 * @author erik
 */
public class App {

    public static void main(String[] args) {
        final Properties config = initialize();
        
        final int port = Integer.valueOf(config.getProperty("port", "8080"));
        final String imagesRootName = config.getProperty("images.dir", ".");
        final Path imagesRoot = Paths.get(imagesRootName);
        ImagesEndpoint.setIMAGES_ROOT(imagesRoot);
        final String driver = config.getProperty("camera.driver", "com.github.sarxos.webcam.ds.buildin.WebcamDefaultDriver");
        final Class<? extends WebcamDriver> camDriver;
        try {
            camDriver = (Class<? extends WebcamDriver>) Class.forName(driver);
            CameraManager.INSTANCE.changeDriver(camDriver);
        } catch (ClassNotFoundException ex) {
            System.err.println("No class" + driver + " for driver");
            System.exit(1);
            return;
        }
        CameraManager.INSTANCE.changeDriver(camDriver);
        
        ErikUndertowJaxRsServer ut = new ErikUndertowJaxRsServer();
        ut.deploy(new RestApp(), "rest");
        ut.addStaticResourcePath( "/", resource( new ClassPathResourceManager( App.class.getClassLoader(), "web" ) ) );
        ut.start(
                Undertow.builder()
                        .addHttpListener(port, "0.0.0.0")
                        .setHandler(resource(new PathResourceManager(Paths.get(System.getProperty("user.home")), 100))
                                .setDirectoryListingEnabled(true))
        );
        
        System.out.println("Ready");
        System.out.println("Started on " + port);
        try {
            final int in = System.in.read();
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, "Unable to wait", ex);
        }
        ut.stop();
        System.out.println("Done");
    }

    private static Properties initialize() {
        final Properties retval = new Properties();
        final Path path = Paths.get("config.properties");
        try(InputStream in = Files.newInputStream(path)){
            retval.load(in);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, "Unable to read config", ex);
        }
        return retval;
    }
}
