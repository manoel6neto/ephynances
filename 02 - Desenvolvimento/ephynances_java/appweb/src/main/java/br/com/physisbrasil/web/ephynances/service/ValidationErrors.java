/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.physisbrasil.web.ephynances.service;

import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import javax.ejb.EJBException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

//@Provider
public final class ValidationErrors implements ExceptionMapper<EJBException> {

    @Override
    public Response toResponse(EJBException exception) {

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                entity(JsfUtil.getExceptionMessage(exception)).
                type(MediaType.TEXT_PLAIN).
                build();

    }
}
