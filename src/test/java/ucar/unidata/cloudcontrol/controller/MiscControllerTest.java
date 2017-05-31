package edu.ucar.unidata.cloudcontrol.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
 
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import edu.ucar.unidata.cloudcontrol.config.TestContext;
import edu.ucar.unidata.cloudcontrol.config.WebAppContext;

/**
 * Tests for edu.ucar.unidata.cloudcontrol.controller.MiscController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class, WebAppContext.class})
@WebAppConfiguration
public class MiscControllerTest {

    private MockMvc mockMvc;
 
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetWelcomePage() throws Exception {
        mockMvc.perform(get("/welcome"))
            .andExpect(status().isOk())
            .andExpect(view().name("welcome"))
            .andExpect(forwardedUrl("/WEB-INF/views/welcome.jsp"));
    }
    
    @Test
    public void testGettingStarted() throws Exception {
        mockMvc.perform(get("/gettingStarted"))
            .andExpect(status().isOk())
            .andExpect(view().name("misc"))
            .andExpect(model().attribute("action", equalTo("gettingStarted")))
            .andExpect(forwardedUrl("/WEB-INF/views/misc.jsp"));
    }
    
    @Test
    public void testAbout() throws Exception {
    mockMvc.perform(get("/about"))
            .andExpect(status().isOk())
            .andExpect(view().name("misc"))
            .andExpect(model().attribute("action", equalTo("about")))
            .andExpect(forwardedUrl("/WEB-INF/views/misc.jsp"));
    }

    
}
