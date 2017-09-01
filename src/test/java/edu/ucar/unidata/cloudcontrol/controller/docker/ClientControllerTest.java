package edu.ucar.unidata.cloudcontrol.controller.docker;

import edu.ucar.unidata.cloudcontrol.config.SecurityConfig;
import edu.ucar.unidata.cloudcontrol.config.WebAppContext;
import edu.ucar.unidata.cloudcontrol.domain.docker.ClientConfig;
import edu.ucar.unidata.cloudcontrol.domain.docker.ClientConfigBuilder;
import edu.ucar.unidata.cloudcontrol.service.docker.ClientManager;
import edu.ucar.unidata.cloudcontrol.service.docker.validators.ClientConfigValidator;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Errors;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.theInstance;

import static org.junit.Assert.assertThat;

import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Tests for edu.ucar.unidata.cloudcontrol.controller.docker.ClientController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebAppContext.class, SecurityConfig.class})
public class ClientControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ClientConfigValidator clientConfigValidatorMock;

    @Autowired
    private ClientManager clientManagerMock;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        Mockito.reset(clientManagerMock);
        Mockito.reset(clientConfigValidatorMock);
        Mockito.when(clientConfigValidatorMock.supports(any(Class.class))).thenReturn(true);

        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    public void getDashboardPage_UnauthenticatedAccessToDashboardShouldBeRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/dashboard"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/j_spring_security_check"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "testUserOne", roles = {"USER"})
    public void getDashboardPage_AccessToDashboardWithAuthenticatedUserShouldBeAllowed() throws Exception {
        mockMvc.perform(get("/dashboard").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getDashboardPage_AccessToDashboardWithAdminAsAuthenticatedUserShouldBeAllowed() throws Exception {
        mockMvc.perform(get("/dashboard").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getDashboardPage_ModelShouldContainEmptyClientConfigAndConfigureClientAction() throws Exception {
        mockMvc.perform(get("/dashboard").with(csrf()))
            .andExpect(model().attribute("action", equalTo("configureClient")))
            .andExpect(model().attribute("clientConfig", hasProperty("id", is(0))))
            .andExpect(model().attribute("clientConfig", hasProperty("dockerHost", nullValue())))
            .andExpect(model().attribute("clientConfig", hasProperty("dockerCertPath", nullValue())))
            .andExpect(model().attribute("clientConfig", hasProperty("dockerTlsVerify", is(1))))
            .andExpect(model().attribute("clientConfig", hasProperty("createdBy", nullValue())))
            .andExpect(model().attribute("clientConfig", hasProperty("lastUpdatedBy", nullValue())))
            .andExpect(model().attribute("clientConfig", hasProperty("dateCreated", nullValue())))
            .andExpect(model().attribute("clientConfig", hasProperty("dateModified", nullValue())));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getDashboardPage_ModelShouldContainRequestedClientConfigAndNoSpecifiedAction() throws Exception {
        ClientConfig testClientConfigOne = new ClientConfigBuilder()
            .id(1)
            .dockerHost("tcp://127.0.0.1:2375")
            .dockerCertPath("/foo/bar/baz/.docker")
            .dockerTlsVerify(1)
            .createdBy("admin")
            .lastUpdatedBy("anotherAdmin")
            .build();

        ClientConfig testClientConfigTwo = new ClientConfigBuilder()
            .id(1)
            .dockerHost("tcp://0.0.0.0:2375")
            .dockerCertPath("/foo/bar/baz/.docker")
            .dockerTlsVerify(1)
            .createdBy("admin")
            .lastUpdatedBy("anotherAdmin")
            .build();

        when(clientManagerMock.getAllClientConfigs()).thenReturn(Arrays.asList(testClientConfigOne, testClientConfigTwo));
        mockMvc.perform(get("/dashboard").with(csrf()))
            .andExpect(model().attribute("clientConfig", theInstance(testClientConfigOne)));
          //.andDo(print());

        verify(clientManagerMock, times(1)).getAllClientConfigs();
        verifyNoMoreInteractions(clientManagerMock);
    }

    @Test
    public void configure_UnauthenticatedAccessToClientConfigurationShouldBeRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/dashboard/docker/client/configure"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/j_spring_security_check"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "testUserOne", roles = {"USER"})
    public void configure_AccessToClientConfigurationWithAuthenticatedUserShouldBeDenied() throws Exception {
        mockMvc.perform(get("/dashboard/docker/client/configure").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("denied"))
            .andExpect(forwardedUrl("/WEB-INF/views/denied.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void configure_AccessToClientConfigurationWithAdminAsAuthenticatedUserShouldBeAllowed() throws Exception {
        mockMvc.perform(get("/dashboard/docker/client/configure").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void configure_ModelShouldContainEmptyClientConfigAndConfigureClientAction() throws Exception {
        mockMvc.perform(get("/dashboard/docker/client/configure").with(csrf()))
            .andExpect(model().attribute("action", equalTo("configureClient")))
            .andExpect(model().attribute("clientConfig", hasProperty("id", is(0))))
            .andExpect(model().attribute("clientConfig", hasProperty("dockerHost", nullValue())))
            .andExpect(model().attribute("clientConfig", hasProperty("dockerCertPath", nullValue())))
            .andExpect(model().attribute("clientConfig", hasProperty("dockerTlsVerify", is(1))))
            .andExpect(model().attribute("clientConfig", hasProperty("createdBy", nullValue())))
            .andExpect(model().attribute("clientConfig", hasProperty("lastUpdatedBy", nullValue())))
            .andExpect(model().attribute("clientConfig", hasProperty("dateCreated", nullValue())))
            .andExpect(model().attribute("clientConfig", hasProperty("dateModified", nullValue())));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void configure_ModelShouldContainRequestedClientConfigAndViewClientConfigAction() throws Exception {
        ClientConfig testClientConfigOne = new ClientConfigBuilder()
            .id(1)
            .dockerHost("tcp://127.0.0.1:2375")
            .dockerCertPath("/foo/bar/baz/.docker")
            .dockerTlsVerify(1)
            .createdBy("admin")
            .lastUpdatedBy("anotherAdmin")
            .build();

        ClientConfig testClientConfigTwo = new ClientConfigBuilder()
            .id(1)
            .dockerHost("tcp://0.0.0.0:2375")
            .dockerCertPath("/foo/bar/baz/.docker")
            .dockerTlsVerify(1)
            .createdBy("admin")
            .lastUpdatedBy("anotherAdmin")
            .build();

        when(clientManagerMock.getAllClientConfigs()).thenReturn(Arrays.asList(testClientConfigOne, testClientConfigTwo));
        mockMvc.perform(get("/dashboard/docker/client/configure").with(csrf()))
            .andExpect(model().attribute("action", equalTo("viewClientConfig")))
            .andExpect(model().attribute("clientConfig", theInstance(testClientConfigOne)));
          //.andDo(print());

        verify(clientManagerMock, times(1)).getAllClientConfigs();
        verifyNoMoreInteractions(clientManagerMock);
    }

    @Test
    public void listClientConfigs_UnauthenticatedAccessToListOfClientConfigurationsShouldBeRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/dashboard/docker/client"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/j_spring_security_check"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "testClientOne", roles = {"USER"})
    public void listClientConfigs_AccessToListOfClientConfigurationsWithAuthenticatedUserShouldBeDenied() throws Exception {
        mockMvc.perform(get("/dashboard/docker/client").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("denied"))
            .andExpect(forwardedUrl("/WEB-INF/views/denied.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void listClientConfigs_AccessToListOfClientConfigurationsWithAdminAsAuthenticatedUserShouldBeAllowed() throws Exception {
        mockMvc.perform(get("/dashboard/docker/client").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void listClientConfigs_ModelShouldContainListOfRequestedClientConfigsAndListClientConfigsAction() throws Exception {
        ClientConfig testClientConfigOne = new ClientConfigBuilder()
            .id(1)
            .dockerHost("tcp://127.0.0.1:2375")
            .dockerCertPath("/foo/bar/baz/.docker")
            .dockerTlsVerify(1)
            .createdBy("admin")
            .lastUpdatedBy("anotherAdmin")
            .build();

        ClientConfig testClientConfigTwo = new ClientConfigBuilder()
            .id(1)
            .dockerHost("tcp://0.0.0.0:2375")
            .dockerCertPath("/foo/bar/baz/.docker")
            .dockerTlsVerify(1)
            .createdBy("admin")
            .lastUpdatedBy("anotherAdmin")
            .build();

        when(clientManagerMock.getAllClientConfigs()).thenReturn(Arrays.asList(testClientConfigOne, testClientConfigTwo));
        mockMvc.perform(get("/dashboard/docker/client").with(csrf()))
            .andExpect(model().attribute("action", equalTo("listClientConfigs")))
            .andExpect(model().attribute("clientConfig", theInstance(testClientConfigOne)));
          //.andDo(print());

        verify(clientManagerMock, times(1)).getAllClientConfigs();
        verifyNoMoreInteractions(clientManagerMock);
    }

    @Test
    public void viewClientConfig_UnauthenticatedAccessToClientConfigurationInformationShouldBeRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/dashboard/docker/client/view/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/j_spring_security_check"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "testClientOne", roles = {"USER"})
    public void viewClientConfig_AccessToClientConfigurationInformationWithAuthenticatedUserShouldBeDenied() throws Exception {
        mockMvc.perform(get("/dashboard/docker/client/view/1").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("denied"))
            .andExpect(forwardedUrl("/WEB-INF/views/denied.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void viewClientConfig_AccessToClientConfigurationInformationWithAdminAsAuthenticatedUserShouldBeAllowed() throws Exception {
        mockMvc.perform(get("/dashboard/docker/client/view/1").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void viewClientConfig_ModelShouldContainRequestedClientConfigAndViewClientConfigAction() throws Exception {
        ClientConfig testClientConfigOne = new ClientConfigBuilder()
            .id(1)
            .dockerHost("tcp://127.0.0.1:2375")
            .dockerCertPath("/foo/bar/baz/.docker")
            .dockerTlsVerify(1)
            .createdBy("admin")
            .lastUpdatedBy("anotherAdmin")
            .build();

        when(clientManagerMock.lookupById(1)).thenReturn(testClientConfigOne);
        mockMvc.perform(get("/dashboard/docker/client/view/1").with(csrf()))
            .andExpect(model().attribute("action", equalTo("viewClientConfig")))
            .andExpect(model().attribute("clientConfig", theInstance(testClientConfigOne)));
          //.andDo(print());

        verify(clientManagerMock, times(1)).lookupById(1);
        verifyNoMoreInteractions(clientManagerMock);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void viewClientConfig_RequestedClientConfigNotFoundShouldThrowDataRetrievalFailureException() throws Exception {
        when(clientManagerMock.lookupById(1)).thenThrow(new DataRetrievalFailureException("Unable to find ClientConfig with id 1"));
        mockMvc.perform(get("/dashboard/docker/client/view/1").with(csrf()))
            .andExpect(model().attribute("exception", org.hamcrest.Matchers.isA(DataRetrievalFailureException.class)))
            .andExpect(status().isOk())
            .andExpect(view().name("fatalError"))
            .andExpect(forwardedUrl("/WEB-INF/views/fatalError.jsp"));
          //.andDo(print());

        verify(clientManagerMock, times(1)).lookupById(1);
        verifyNoMoreInteractions(clientManagerMock);
    }

    @Test
    public void createClientConfig_UnauthenticatedPostToCreateClientConfigFormShouldBeForbidden() throws Exception {
        mockMvc.perform(post("/dashboard/docker/client/configure"))
           .andExpect(status().isForbidden());
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "testClientOne", roles = {"USER"})
    public void createClientConfig_PostToCreateClientConfigFormWithAuthenticatedUserShouldBeDenied() throws Exception {
        mockMvc.perform(post("/dashboard/docker/client/configure").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("denied"))
            .andExpect(forwardedUrl("/WEB-INF/views/denied.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createClientConfig_PostToCreateClientConfigFormWithAdminAsAuthenticatedUserShouldBeAllowed() throws Exception {
        mockMvc.perform(post("/dashboard/docker/client/configure").with(csrf()))
          .andExpect(status().is3xxRedirection())
          .andExpect(redirectedUrl("/dashboard/docker/client/view/0"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createClientConfig_ClientConfigValidationErrorShouldRenderConfigureClientForm() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Errors errors = (Errors) invocationOnMock.getArguments()[1];
                errors.reject("forcing a validation error");
                return null;
            }
        }).when(clientConfigValidatorMock).validate(any(), any(Errors.class));

        mockMvc.perform(post("/dashboard/docker/client/configure").with(csrf()))
            .andExpect(model().attribute("action", equalTo("configureClient")))
            .andExpect(model().hasErrors());
         // .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createClientConfig_ClientConfigCreatedSuccessfullyShouldRedirectToViewOfClientConfig() throws Exception {
        when(clientManagerMock.createClientConfig(org.mockito.ArgumentMatchers.isA(ClientConfig.class))).thenReturn(1);
        mockMvc.perform(post("/dashboard/docker/client/configure").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("dockerHost", "tcp://127.0.0.1:2375")
                .param("dockerCertPath", "/foo/bar/baz/.docker")
                .param("dockerTlsVerify", "1")
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/dashboard/docker/client/view/1"));
          //.andDo(print());

        ArgumentCaptor<ClientConfig> formObjectArgument = ArgumentCaptor.forClass(ClientConfig.class);
        verify(clientManagerMock).createClientConfig(formObjectArgument.capture());
        verifyNoMoreInteractions(clientManagerMock);

        ClientConfig formObject = formObjectArgument.getValue();
        assertThat(formObject.getId(), is(0));
        assertThat(formObject.getDockerHost(), is("tcp://127.0.0.1:2375"));
        assertThat(formObject.getDockerCertPath(), is("/foo/bar/baz/.docker"));
        assertThat(formObject.getDockerTlsVerify(), is(1));
        assertThat(formObject.getCreatedBy(), is("admin"));
        assertThat(formObject.getLastUpdatedBy(), is("admin"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createClientConfig_ClientConfigNotCreatedShouldThrowDataRetrievalFailureException() throws Exception {
        when(clientManagerMock.createClientConfig(org.mockito.ArgumentMatchers.isA(ClientConfig.class))).thenThrow(new DataRetrievalFailureException("Unable to create ClientConfig"));
        mockMvc.perform(post("/dashboard/docker/client/configure").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("dockerHost", "tcp://127.0.0.1:2375")
                .param("dockerCertPath", "/foo/bar/baz/.docker")
                .param("dockerTlsVerify", "1")
            )
            .andExpect(model().attribute("exception", org.hamcrest.Matchers.isA(DataRetrievalFailureException.class)))
            .andExpect(status().isOk())
            .andExpect(view().name("fatalError"))
            .andExpect(forwardedUrl("/WEB-INF/views/fatalError.jsp"));
          //.andDo(print());

        verify(clientManagerMock, times(1)).createClientConfig(org.mockito.ArgumentMatchers.isA(ClientConfig.class));
        verifyNoMoreInteractions(clientManagerMock);
    }

    @Test
    public void editClientConfig_UnauthenticatedAccessToEditClientConfigFormShouldBeRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/dashboard/docker/client/edit/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/j_spring_security_check"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "testClientOne", roles = {"USER"})
    public void editClientConfig_AccessToEditClientConfigFormWithAuthenticatedUserShouldBeDenied() throws Exception {
        mockMvc.perform(get("/dashboard/docker/client/edit/1").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("denied"))
            .andExpect(forwardedUrl("/WEB-INF/views/denied.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void editClientConfig_AccessToEditClientConfigFormWithAdminAsAuthenticatedUserShouldBeAllowed() throws Exception {
        mockMvc.perform(get("/dashboard/docker/client/edit/1").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void editClientConfig_ModelShouldContainRequestedClientConfigAndEditClientConfigAction() throws Exception {
        ClientConfig testClientConfigOne = new ClientConfigBuilder()
            .id(1)
            .dockerHost("tcp://127.0.0.1:2375")
            .dockerCertPath("/foo/bar/baz/.docker")
            .dockerTlsVerify(1)
            .createdBy("admin")
            .lastUpdatedBy("anotherAdmin")
            .build();

        when(clientManagerMock.lookupById(1)).thenReturn(testClientConfigOne);
        mockMvc.perform(get("/dashboard/docker/client/edit/1").with(csrf()))
            .andExpect(model().attribute("action", equalTo("editClientConfig")))
            .andExpect(model().attribute("clientConfig", theInstance(testClientConfigOne)));
          //.andDo(print());

        verify(clientManagerMock, times(1)).lookupById(1);
        verifyNoMoreInteractions(clientManagerMock);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void editClientConfig_RequestedClientConfigNotFoundShouldThrowDataRetrievalFailureException() throws Exception {
        when(clientManagerMock.lookupById(1)).thenThrow(new DataRetrievalFailureException("Unable to find ClientConfig with id 1"));
        mockMvc.perform(get("/dashboard/docker/client/edit/1").with(csrf()))
            .andExpect(model().attribute("exception", org.hamcrest.Matchers.isA(DataRetrievalFailureException.class)))
            .andExpect(status().isOk())
            .andExpect(view().name("fatalError"))
            .andExpect(forwardedUrl("/WEB-INF/views/fatalError.jsp"));
          //.andDo(print());

        verify(clientManagerMock, times(1)).lookupById(1);
        verifyNoMoreInteractions(clientManagerMock);
    }

    @Test
    public void editClientConfig_UnauthenticatedPostToEditClientConfigFormShouldBeForbidden() throws Exception {
        mockMvc.perform(post("/dashboard/docker/client/edit/1"))
           .andExpect(status().isForbidden());
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "testClientOne", roles = {"USER"})
    public void editClientConfig_PostToEditClientConfigFormWithAuthenticatedUserShouldBeDenied() throws Exception {
        mockMvc.perform(post("/dashboard/docker/client/edit/1").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("denied"))
            .andExpect(forwardedUrl("/WEB-INF/views/denied.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void editClientConfig_PostToEditClientConfigFormWithAdminAsAuthenticatedUserShouldBeAllowed() throws Exception {
        ClientConfig testClientConfigOne = new ClientConfigBuilder()
            .id(1)
            .dockerHost("tcp://127.0.0.1:2375")
            .dockerCertPath("/foo/bar/baz/.docker")
            .dockerTlsVerify(1)
            .createdBy("admin")
            .lastUpdatedBy("anotherAdmin")
            .build();

        when(clientManagerMock.lookupById(1)).thenReturn(testClientConfigOne);
        mockMvc.perform(post("/dashboard/docker/client/edit/1").with(csrf()))
          .andExpect(status().is3xxRedirection())
          .andExpect(redirectedUrl("/dashboard/docker/client/view/1"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void editClientConfig_ClientConfigValidationErrorShouldRenderConfigureClientForm() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Errors errors = (Errors) invocationOnMock.getArguments()[1];
                errors.reject("forcing a validation error");
                return null;
            }
        }).when(clientConfigValidatorMock).validate(any(), any(Errors.class));

        mockMvc.perform(post("/dashboard/docker/client/edit/1").with(csrf()))
            .andExpect(model().attribute("action", equalTo("editClientConfig")))
            .andExpect(model().hasErrors());
         // .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void editClientConfig_ClientConfigEditedSuccessfullyShouldRedirectToViewOfClientConfig() throws Exception {
        ClientConfig testClientConfigOne = new ClientConfigBuilder()
            .id(1)
            .dockerHost("tcp://127.0.0.1:2375")
            .dockerCertPath("/foo/bar/baz/.docker")
            .dockerTlsVerify(1)
            .createdBy("admin")
            .lastUpdatedBy("admin")
            .build();

        when(clientManagerMock.lookupById(1)).thenReturn(testClientConfigOne);
        doNothing().when(clientManagerMock).updateClientConfig(org.mockito.ArgumentMatchers.isA(ClientConfig.class));
        mockMvc.perform(post("/dashboard/docker/client/edit/1").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("dockerHost", "tcp://0.0.0.0:2375")
                .param("dockerCertPath", "/baz/bar/foo/.docker")
                .param("dockerTlsVerify", "1")
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/dashboard/docker/client/view/1"));
         //.andDo(print());

        ArgumentCaptor<ClientConfig> formObjectArgument = ArgumentCaptor.forClass(ClientConfig.class);
        verify(clientManagerMock, times(1)).lookupById(1);
        verify(clientManagerMock).updateClientConfig(formObjectArgument.capture());
        verifyNoMoreInteractions(clientManagerMock);

        ClientConfig formObject = formObjectArgument.getValue();
        assertThat(formObject.getId(), is(1));
        assertThat(formObject.getDockerHost(), is("tcp://0.0.0.0:2375"));
        assertThat(formObject.getDockerCertPath(), is("/baz/bar/foo/.docker"));
        assertThat(formObject.getDockerTlsVerify(), is(1));
        assertThat(formObject.getCreatedBy(), is("admin"));
        assertThat(formObject.getLastUpdatedBy(), is("admin"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void editClientConfig_ClientConfigNotEditedShouldThrowDataRetrievalFailureException() throws Exception {
        ClientConfig testClientConfigOne = new ClientConfigBuilder()
            .id(1)
            .dockerHost("tcp://127.0.0.1:2375")
            .dockerCertPath("/foo/bar/baz/.docker")
            .dockerTlsVerify(1)
            .createdBy("admin")
            .lastUpdatedBy("admin")
            .build();

        when(clientManagerMock.lookupById(1)).thenReturn(testClientConfigOne);
        doThrow(new DataRetrievalFailureException("Unable to update ClientConfig.  No ClientConfig with id 1")).when(clientManagerMock).updateClientConfig(org.mockito.ArgumentMatchers.isA(ClientConfig.class));
        mockMvc.perform(post("/dashboard/docker/client/edit/1").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("dockerHost", "tcp://127.0.0.1:2375")
                .param("dockerCertPath", "/foo/bar/baz/.docker")
                .param("dockerTlsVerify", "1")
            )
            .andExpect(model().attribute("exception", org.hamcrest.Matchers.isA(DataRetrievalFailureException.class)))
            .andExpect(status().isOk())
            .andExpect(view().name("fatalError"))
            .andExpect(forwardedUrl("/WEB-INF/views/fatalError.jsp"));
          //.andDo(print());
        verify(clientManagerMock, times(1)).lookupById(1);
        verify(clientManagerMock, times(1)).updateClientConfig(org.mockito.ArgumentMatchers.isA(ClientConfig.class));
        verifyNoMoreInteractions(clientManagerMock);
    }
}
