package edu.ucar.unidata.cloudcontrol.controller.docker;

import edu.ucar.unidata.cloudcontrol.config.SecurityConfig;
import edu.ucar.unidata.cloudcontrol.config.WebAppContext;
import edu.ucar.unidata.cloudcontrol.domain.docker._Container;
import edu.ucar.unidata.cloudcontrol.domain.docker._ContainerBuilder;
import edu.ucar.unidata.cloudcontrol.service.docker.ContainerManager;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Tests for edu.ucar.unidata.cloudcontrol.controller.docker.ContainerController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebAppContext.class, SecurityConfig.class})
public class ContainerControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ContainerManager containerManagerMock;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        Mockito.reset(containerManagerMock);

        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    public void getContainerList_UnauthenticatedAccessToContainerListShouldBeRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/dashboard/docker/container/list"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/j_spring_security_check"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "testClientOne", roles = {"USER"})
    public void getContainerList_AccessToContainerListWithAuthenticatedUserShouldBeDenied() throws Exception {
        mockMvc.perform(get("/dashboard/docker/container/list").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("denied"))
            .andExpect(forwardedUrl("/WEB-INF/views/denied.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getContainerList_AccessToContainerListWithAdminAsAuthenticatedUserShouldBeAllowed() throws Exception {
        mockMvc.perform(get("/dashboard/docker/container/list").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getContainerList_ModelShouldContainNoContainerList() throws Exception {
        when(containerManagerMock.getContainerList()).thenReturn(null);
        mockMvc.perform(get("/dashboard/docker/container/list").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
          //.andDo(print());

        verify(containerManagerMock, times(1)).getContainerList();
        verifyNoMoreInteractions(containerManagerMock);
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getContainerList_ModelShouldContainListOfContainers() throws Exception {

        _Container testContainerOne = new _ContainerBuilder()
            .id("77af4d6b9913")
            .build();

        _Container testContainerTwo = new _ContainerBuilder()
            .id("b6fa739cedf5")
            .build();

        when(containerManagerMock.getContainerList()).thenReturn(Arrays.asList(testContainerOne, testContainerTwo));
        mockMvc.perform(get("/dashboard/docker/container/list").with(csrf()))
            .andExpect(model().attribute("containerList", hasSize(2)))
            .andExpect(model().attribute("containerList", hasItem(
                allOf(
                    hasProperty("id", is("77af4d6b9913"))
                )
            )))
            .andExpect(model().attribute("containerList", hasItem(
                allOf(
                    hasProperty("id", is("b6fa739cedf5"))
                )
            )));
          //.andDo(print());

        verify(containerManagerMock, times(1)).getContainerList();
        verifyNoMoreInteractions(containerManagerMock);
    }

}
