package edu.ucar.unidata.cloudcontrol.controller;

import edu.ucar.unidata.cloudcontrol.config.SecurityConfig;
import edu.ucar.unidata.cloudcontrol.config.WebAppContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Tests for edu.ucar.unidata.cloudcontrol.controller.AuthenticationController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebAppContext.class, SecurityConfig.class})
public class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    public void requiresAuthentication() throws Exception {
        mockMvc.perform(get("/dashboard"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/j_spring_security_check"));
          //.andDo(print());
    }

    @Test
    public void authenticationSuccess() throws Exception {
        mockMvc.perform(formLogin("/j_spring_security_check").user("userName","user").password("password","password"))
            .andExpect(status().is3xxRedirection())
            .andExpect(authenticated().withUsername("user"));
          //.andDo(print());
    }

    @Test
    public void postWithBadCSRFForbidden() throws Exception {
        mockMvc.perform(post("/j_spring_security_check").with(csrf().useInvalidToken()))
            .andExpect(status().isForbidden());
          //.andDo(print());
    }

    @Test
    public void authenticationFailedBadCredentials() throws Exception {
        mockMvc.perform(formLogin("/j_spring_security_check").user("userName","user").password("password","invalid"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login/badCredentials"))
            .andExpect(unauthenticated());
          //.andDo(print());
    }

    @Test
    public void authenticationFailedAccountLocked() throws Exception {
        mockMvc.perform(formLogin("/j_spring_security_check").user("userName","userAccountLocked").password("password","password"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login/accountLocked"))
            .andExpect(unauthenticated());
          //.andDo(print());
    }

    @Test
    public void authenticationFailedAccountDisabled() throws Exception {
        mockMvc.perform(formLogin("/j_spring_security_check").user("userName","userAccountDisabled").password("password","password"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login/accountDisabled"))
            .andExpect(unauthenticated());
          //.andDo(print());
    }

    @Test
    public void testGetLogoutPage() throws Exception {
    mockMvc.perform(logout())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login"));
            //.andDo(print());
    }

    @Test
    public void testGetDeniedPageDirectRequest() throws Exception {
    mockMvc.perform(get("/denied"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name("denied"))
            .andExpect(forwardedUrl("/WEB-INF/views/denied.jsp"));
            //.andDo(print());
    }
}
