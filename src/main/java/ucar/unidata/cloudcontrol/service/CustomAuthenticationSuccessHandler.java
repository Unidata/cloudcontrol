package edu.ucar.unidata.cloudcontrol.service;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Service class used to handle a successful user authentication.
 */
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	protected static Logger logger = Logger.getLogger(CustomAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
		Cookie[] cookie = httpServletRequest.getCookies();

        HttpSession session = httpServletRequest.getSession();
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = authUser.getUsername();
		
        //set our response to OK status
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        //since we have created our custom success handler, its up to us to where
        //we will redirect the user after successfully login
        httpServletResponse.sendRedirect("welcome");
    }
}