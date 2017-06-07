package edu.ucar.unidata.cloudcontrol.controller;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

import edu.ucar.unidata.cloudcontrol.categories.UnitTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static edu.ucar.unidata.cloudcontrol.config.ControllerTestConfig.jspViewResolver;

import static org.hamcrest.Matchers.equalTo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Tests for edu.ucar.unidata.cloudcontrol.controller.MiscController
 */
@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class MiscControllerTest {

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new MiscController())
            .setViewResolvers(jspViewResolver())
            .build();
    }
/*
        @Test
        public void testShouldReturnHttpStatusCodeOk() throws Exception {
            mockMvc.perform(get("/welcome"))
                   .andExpect(status().isOk());
        }
*/

    public class GetWelcomePage {
        @Test
        public void shouldReturnHttpStatusCodeOk() throws Exception {
            mockMvc.perform(get("/welcome"))
                   .andExpect(status().isOk());
        }

        @Test
        public void shouldRenderWelcomeView() throws Exception {
            mockMvc.perform(get("/welcome"))
                  .andExpect(view().name("welcome"));
        }

        @Test
        public void shouldForwardToWelcomeURL() throws Exception {
            mockMvc.perform(get("/welcome"))
                  .andExpect(forwardedUrl("/WEB-INF/views/welcome.jsp"));
        }
    }


    public class GettingStarted {
        @Test
        public void shouldReturnHttpStatusCodeOk() throws Exception {
            mockMvc.perform(get("/gettingStarted"))
                   .andExpect(status().isOk());
        }

        @Test
        public void shouldRenderMiscView() throws Exception {
            mockMvc.perform(get("/gettingStarted"))
                  .andExpect(view().name("misc"));
        }

        @Test
        public void shouldHaveActionOfGettingStarted() throws Exception {
            mockMvc.perform(get("/gettingStarted"))
                  .andExpect(model().attribute("action", equalTo("gettingStarted")));
        }

        @Test
        public void shouldForwardToMiscURL() throws Exception {
            mockMvc.perform(get("/gettingStarted"))
                  .andExpect(forwardedUrl("/WEB-INF/views/misc.jsp"));
        }
    }


    public class About {
        @Test
        public void shouldReturnHttpStatusCodeOk() throws Exception {
            mockMvc.perform(get("/about"))
                   .andExpect(status().isOk());
        }

        @Test
        public void shouldRenderMiscView() throws Exception {
            mockMvc.perform(get("/about"))
                  .andExpect(view().name("misc"));
        }

        @Test
        public void shouldHaveActionOfAbout() throws Exception {
            mockMvc.perform(get("/about"))
                  .andExpect(model().attribute("action", equalTo("about")));
        }

        @Test
        public void shouldForwardToMiscURL() throws Exception {
            mockMvc.perform(get("/about"))
                  .andExpect(forwardedUrl("/WEB-INF/views/misc.jsp"));
        }
    }




}
