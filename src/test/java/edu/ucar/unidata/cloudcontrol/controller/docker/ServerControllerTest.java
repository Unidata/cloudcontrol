package edu.ucar.unidata.cloudcontrol.controller.docker;

import edu.ucar.unidata.cloudcontrol.config.SecurityConfig;
import edu.ucar.unidata.cloudcontrol.config.WebAppContext;
import edu.ucar.unidata.cloudcontrol.domain.docker._Info;
import edu.ucar.unidata.cloudcontrol.domain.docker._Version;
import edu.ucar.unidata.cloudcontrol.domain.docker._InfoBuilder;
import edu.ucar.unidata.cloudcontrol.domain.docker._VersionBuilder;
import edu.ucar.unidata.cloudcontrol.service.docker.ServerManager;

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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
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
 * Tests for edu.ucar.unidata.cloudcontrol.controller.docker.ServerController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebAppContext.class, SecurityConfig.class})
public class ServerControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ServerManager serverManagerMock;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        Mockito.reset(serverManagerMock);

        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    public void getInfo_UnauthenticatedAccessToServerInfoShouldBeRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/dashboard/docker/server/info"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/j_spring_security_check"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "testUserOne", roles = {"USER"})
    public void getInfo_AccessToServerInfoWithAuthenticatedUserShouldBeDenied() throws Exception {
        mockMvc.perform(get("/dashboard/docker/server/info").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("denied"))
            .andExpect(forwardedUrl("/WEB-INF/views/denied.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getInfo_AccessToServerInfoWithAdminAsAuthenticatedUserShouldBeAllowed() throws Exception {
        mockMvc.perform(get("/dashboard/docker/server/info").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getInfo_ModelShouldContainNoServerInfoObjectAndServerInfoAction() throws Exception {
        when(serverManagerMock.getInfo()).thenReturn(null);
        mockMvc.perform(get("/dashboard/docker/server/info").with(csrf()))
            .andExpect(model().attribute("action", equalTo("serverInfo")));
          //.andDo(print());

        verify(serverManagerMock, times(1)).getInfo();
        verifyNoMoreInteractions(serverManagerMock);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getInfo_ModelShouldContainServerInfoAndServerInfoAction() throws Exception {

        _Info testInfo = new _InfoBuilder()
            .architecture("x86_64")
            .images(4)
            .build();

        when(serverManagerMock.getInfo()).thenReturn(testInfo);
        mockMvc.perform(get("/dashboard/docker/server/info").with(csrf()))
            .andExpect(model().attribute("action", equalTo("serverInfo")))
            .andExpect(model().attribute("serverInfo", hasProperty("architecture", is("x86_64"))))
            .andExpect(model().attribute("serverInfo", hasProperty("images", is("4"))));
          //.andDo(print());

        verify(serverManagerMock, times(1)).getInfo();
        verifyNoMoreInteractions(serverManagerMock);
    }

    @Test
    public void getVersion_UnauthenticatedAccessToServerVersionShouldBeRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/dashboard/docker/server/version"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/j_spring_security_check"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "testUserOne", roles = {"USER"})
    public void getVersion_AccessToServerVersionWithAuthenticatedUserShouldBeDenied() throws Exception {
        mockMvc.perform(get("/dashboard/docker/server/version").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("denied"))
            .andExpect(forwardedUrl("/WEB-INF/views/denied.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getVersion_AccessToServerVersionWithAdminAsAuthenticatedUserShouldBeAllowed() throws Exception {
        mockMvc.perform(get("/dashboard/docker/server/version").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getVersion_ModelShouldContainNoServerVersionObjectAndServerVersionAction() throws Exception {
        when(serverManagerMock.getVersion()).thenReturn(null);
        mockMvc.perform(get("/dashboard/docker/server/version").with(csrf()))
            .andExpect(model().attribute("action", equalTo("serverVersion")));
          //.andDo(print());

        verify(serverManagerMock, times(1)).getVersion();
        verifyNoMoreInteractions(serverManagerMock);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getVersion_ModelShouldContainServerVersionAndServerVersionAction() throws Exception {

        _Version testVersion = new _VersionBuilder()
            .apiVersion("1.23")
            .build();

        when(serverManagerMock.getVersion()).thenReturn(testVersion);
        mockMvc.perform(get("/dashboard/docker/server/version").with(csrf()))
            .andExpect(model().attribute("action", equalTo("serverVersion")))
            .andExpect(model().attribute("serverVersion", hasProperty("apiVersion", is("1.23"))));
          //.andDo(print());

        verify(serverManagerMock, times(1)).getVersion();
        verifyNoMoreInteractions(serverManagerMock);
    }

}
