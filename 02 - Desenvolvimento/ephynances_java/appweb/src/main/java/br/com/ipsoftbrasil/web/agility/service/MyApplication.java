/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ipsoftbrasil.web.agility.service;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

/**
 *
 * @author thadeu
 */
//@ApplicationPath("/rest")
public class MyApplication extends ResourceConfig {

    public MyApplication() {
        super(MultiPartFeature.class, JacksonFeature.class);

        // Now you can expect validation errors to be sent to the client.
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        // @ValidateOnExecution annotations on subclasses won't cause errors.
        property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true); // Further configuration of ResourceConfig.

//        packages(MainService.class.getPackage().getName());
    }

}
