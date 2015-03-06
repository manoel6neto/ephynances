/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.physisbrasil.web.ephynances.service;

import javax.ws.rs.ext.ContextResolver;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

/**
 *
 * @author Thomas
 */
//@Provider
public class JsonObjectMapper implements ContextResolver<ObjectMapper> {

    final ObjectMapper defaultObjectMapper;

    public JsonObjectMapper() {
        defaultObjectMapper = createDefaultMapper();
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return defaultObjectMapper;
    }

    private static ObjectMapper createDefaultMapper() {
        final ObjectMapper result = new ObjectMapper();
        result.configure(Feature.INDENT_OUTPUT, true);

        return result;
    }

}
