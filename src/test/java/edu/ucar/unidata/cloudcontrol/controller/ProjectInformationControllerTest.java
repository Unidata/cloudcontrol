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

import static org.hamcrest.Matchers.equalTo;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Tests for edu.ucar.unidata.cloudcontrol.controller.ProjectInformationController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
//@ContextConfiguration("/applicationContext.xml")
@ContextConfiguration(classes = {WebAppContext.class, SecurityConfig.class})
public class ProjectInformationControllerTest {

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
    public void getWelcomePageUnauthenticatedAccessShouldBeSuccessful() throws Exception {
        mockMvc.perform(get("/welcome"))
            .andExpect(status().isOk())
            .andExpect(view().name("welcome"))
            .andExpect(forwardedUrl("/WEB-INF/views/welcome.jsp"));
          //.andDo(print());
    }

    @Test
    public void gettingStartedUnauthenticatedAccessShouldBeSuccessful() throws Exception {
        mockMvc.perform(get("/gettingStarted"))
            .andExpect(status().isOk())
            .andExpect(view().name("projectInformation"))
            .andExpect(forwardedUrl("/WEB-INF/views/projectInformation.jsp"));
          //.andDo(print());
    }

    @Test
    public void gettingStartedModelShouldContainGettingStartedAction() throws Exception {
        mockMvc.perform(get("/gettingStarted"))
            .andExpect(model().attribute("action", equalTo("gettingStarted")));
          //.andDo(print());
    }

    @Test
    public void aboutUnauthenticatedAccessShouldBeSuccessful() throws Exception {
    mockMvc.perform(get("/about"))
            .andExpect(status().isOk())
            .andExpect(view().name("projectInformation"))
            .andExpect(forwardedUrl("/WEB-INF/views/projectInformation.jsp"));
          //.andDo(print());
    }

    @Test
    public void aboutModelShouldContainAboutAction() throws Exception {
    mockMvc.perform(get("/about"))
            .andExpect(model().attribute("action", equalTo("about")));
          //.andDo(print());
    }
}
