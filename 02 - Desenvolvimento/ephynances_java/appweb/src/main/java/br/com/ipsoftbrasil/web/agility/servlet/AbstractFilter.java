/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ipsoftbrasil.web.agility.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Thadeu
 */
public class AbstractFilter {

    public static final String USER_KEY = "usuario_logado";

    public AbstractFilter() {
        super();
    }

    protected void doLogin(ServletRequest request, ServletResponse response, HttpServletRequest req) throws ServletException, IOException {
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.sendRedirect(req.getContextPath() + "/login.xhtml");
    }

    protected void accessDenied(ServletRequest request, ServletResponse response, HttpServletRequest req) throws ServletException, IOException {
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.sendRedirect(req.getContextPath() + "/accessDenied.xhtml");
    }
}
