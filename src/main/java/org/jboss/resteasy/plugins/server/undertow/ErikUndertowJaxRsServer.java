/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.resteasy.plugins.server.undertow;

import io.undertow.server.handlers.resource.ResourceHandler;

/**
 * UndertowJaxrsWebServer Basically an UndertowJaxrsServer that allows adding
 * handlers to the root path to allow serving static web (i.e HTML/CSS,
 * non-REST) resources. Must be in the
 * org.jboss.resteasy.plugins.server.undertow package to access the
 * package-private root PathHandler in the parent class.
 */
public class ErikUndertowJaxRsServer extends UndertowJaxrsServer {

    public void addStaticResourcePath(String path, ResourceHandler handler) {
        super.root.addPrefixPath(path, handler);
    }
}
