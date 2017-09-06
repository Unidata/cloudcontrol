package edu.ucar.unidata.cloudcontrol.controller.user;

import edu.ucar.unidata.cloudcontrol.config.SecurityConfig;
import edu.ucar.unidata.cloudcontrol.config.WebAppContext;
import edu.ucar.unidata.cloudcontrol.domain.User;
import edu.ucar.unidata.cloudcontrol.domain.UserBuilder;
import edu.ucar.unidata.cloudcontrol.service.user.UserManager;
import edu.ucar.unidata.cloudcontrol.service.user.validators.CreateUserValidator;

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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.nullValue;

import static org.junit.Assert.assertThat;

import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Tests for edu.ucar.unidata.cloudcontrol.controller.user.CreateUserController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebAppContext.class, SecurityConfig.class})
public class CreateUserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private CreateUserValidator createUserValidatorMock;

    @Autowired
    private UserManager userManagerMock;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        Mockito.reset(userManagerMock);
        Mockito.reset(createUserValidatorMock);
        Mockito.when(createUserValidatorMock.supports(any(Class.class))).thenReturn(true);

        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void createUser_AccessToCreateUserFormWithUnauthorizedUserShouldBeDenied() throws Exception {
        mockMvc.perform(get("/dashboard/user/create").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("denied"))
            .andExpect(forwardedUrl("/WEB-INF/views/denied.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createUser_AccessToCreateUserFormWithAdminAsAuthorizedUserShouldBeAllowed() throws Exception {
        mockMvc.perform(get("/dashboard/user/create").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createUser_ModelShouldContainEmptyUserAndCreateUserAction() throws Exception {
        mockMvc.perform(get("/dashboard/user/create").with(csrf()))
            .andExpect(model().attribute("action", equalTo("createUser")))
            .andExpect(model().attribute("user", hasProperty("userId", is(0))))
            .andExpect(model().attribute("user", hasProperty("userName", nullValue())))
            .andExpect(model().attribute("user", hasProperty("fullName", nullValue())))
            .andExpect(model().attribute("user", hasProperty("emailAddress", nullValue())))
            .andExpect(model().attribute("user", hasProperty("password", nullValue())))
            .andExpect(model().attribute("user", hasProperty("confirmPassword", nullValue())))
            .andExpect(model().attribute("user", hasProperty("accessLevel", is(1))))
            .andExpect(model().attribute("user", hasProperty("accountStatus", is(1))))
            .andExpect(model().attribute("user", hasProperty("dateCreated", nullValue())))
            .andExpect(model().attribute("user", hasProperty("dateModified", nullValue())));
          //.andDo(print());
    }

    @Test
    public void register_UnauthenticatedAccessToRegisterFormShouldBeSuccessful() throws Exception {
        mockMvc.perform(get("/welcome/register"))
            .andExpect(status().isOk())
            .andExpect(view().name("welcome"))
            .andExpect(forwardedUrl("/WEB-INF/views/welcome.jsp"));
          //.andDo(print());
    }

    @Test
    public void register_ModelShouldContainEmptyUserAndRegsiterAction() throws Exception {
        mockMvc.perform(get("/welcome/register"))
            .andExpect(model().attribute("action", equalTo("register")))
            .andExpect(model().attribute("user", hasProperty("userId", is(0))))
            .andExpect(model().attribute("user", hasProperty("userName", nullValue())))
            .andExpect(model().attribute("user", hasProperty("fullName", nullValue())))
            .andExpect(model().attribute("user", hasProperty("emailAddress", nullValue())))
            .andExpect(model().attribute("user", hasProperty("password", nullValue())))
            .andExpect(model().attribute("user", hasProperty("confirmPassword", nullValue())))
            .andExpect(model().attribute("user", hasProperty("accessLevel", is(1))))
            .andExpect(model().attribute("user", hasProperty("accountStatus", is(1))))
            .andExpect(model().attribute("user", hasProperty("dateCreated", nullValue())))
            .andExpect(model().attribute("user", hasProperty("dateModified", nullValue())));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void createUser_PostToCreateUserFormWithUnauthorizedUserShouldBeDenied() throws Exception {
        mockMvc.perform(post("/dashboard/user/create").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("denied"))
            .andExpect(forwardedUrl("/WEB-INF/views/denied.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createUser_PostToCreateUserFormWithAdminAsAuthorizedUserShouldBeAllowed() throws Exception {
        User testUserOne = new UserBuilder()
            .userName("testUserOne")
            .build();

        when(userManagerMock.createUser(org.mockito.ArgumentMatchers.isA(User.class))).thenReturn(testUserOne);

        mockMvc.perform(post("/dashboard/user/create").with(csrf()))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/dashboard/user/view/testUserOne"));
         //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createUser_UserValidationErrorShouldRenderCreateUserView() throws Exception {

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Errors errors = (Errors) invocationOnMock.getArguments()[1];
                errors.reject("forcing a validation error");
                return null;
            }
        }).when(createUserValidatorMock).validate(any(), any(Errors.class));

        mockMvc.perform(post("/dashboard/user/create").with(csrf()))
            .andExpect(model().attribute("action", equalTo("createUser")))
            .andExpect(model().hasErrors());
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createUser_NewUserCreatedSuccessfullyShouldRedirectToUserView() throws Exception {

        User testUserOne = new UserBuilder()
            .userId(1)
            .userName("testUserOne")
            .fullName("Test User One")
            .emailAddress("testUserOne@foo.bar")
            .password("password")
            .confirmPassword("password")
            .accessLevel(1)
            .accountStatus(1)
            .build();

        when(userManagerMock.createUser(org.mockito.ArgumentMatchers.isA(User.class))).thenReturn(testUserOne);
        mockMvc.perform(post("/dashboard/user/create").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "testUserOne")
                .param("fullName", "Test User One")
                .param("emailAddress", "testUserOne@foo.bar")
                .param("password", "password")
                .param("confirmPassword", "password")
                .param("accountStatus", "1")
                .param("accessLevel", "1")
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/dashboard/user/view/testUserOne"));
          //.andDo(print());

        ArgumentCaptor<User> formObjectArgument = ArgumentCaptor.forClass(User.class);
        verify(userManagerMock).createUser(formObjectArgument.capture());
        verifyNoMoreInteractions(userManagerMock);

        User formObject = formObjectArgument.getValue();
        assertThat(formObject.getUserId(), is(0));
        assertThat(formObject.getUserName(), is("testUserOne"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createUser_NewlyCreatedUserNotFoundShouldThrowDataRetrievalFailureException() throws Exception {
        when(userManagerMock.createUser(org.mockito.ArgumentMatchers.isA(User.class))).thenThrow(new DataRetrievalFailureException("Unable to create new User"));
        mockMvc.perform(post("/dashboard/user/create").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "testUserOne")
                .param("fullName", "Test User One")
                .param("emailAddress", "testUserOne@foo.bar")
                .param("password", "password")
                .param("confirmPassword", "password")
                .param("accountStatus", "1")
                .param("accessLevel", "1")
            )
            .andExpect(model().attribute("exception", org.hamcrest.Matchers.isA(DataRetrievalFailureException.class)))
            .andExpect(status().isOk())
            .andExpect(view().name("fatalError"))
            .andExpect(forwardedUrl("/WEB-INF/views/fatalError.jsp"));
          //.andDo(print());

        ArgumentCaptor<User> formObjectArgument = ArgumentCaptor.forClass(User.class);
        verify(userManagerMock).createUser(formObjectArgument.capture());
        verifyNoMoreInteractions(userManagerMock);
    }

    @Test
    public void register_UnauthenticatedPostsToRegisterFormShouldBeSuccessful() throws Exception {
        User testUserOne = new UserBuilder()
            .userName("testUserOne")
            .build();

        when(userManagerMock.createUser(org.mockito.ArgumentMatchers.isA(User.class))).thenReturn(testUserOne);

        mockMvc.perform(post("/welcome/register").with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"));
          //.andDo(print());
    }

    @Test
    public void register_UserValidationErrorShouldRenderCreateUserView() throws Exception {

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Errors errors = (Errors) invocationOnMock.getArguments()[1];
                errors.reject("forcing a validation error");
                return null;
            }
        }).when(createUserValidatorMock).validate(any(), any(Errors.class));

        mockMvc.perform(post("/welcome/register").with(csrf()))
            .andExpect(model().attribute("action", equalTo("register")))
            .andExpect(model().hasErrors());
          //.andDo(print());
    }

    @Test
    public void register_NewUserCreatedSuccessfullyShouldRedirectToUserView() throws Exception {

        User testUserOne = new UserBuilder()
            .userId(1)
            .userName("testUserOne")
            .fullName("Test User One")
            .emailAddress("testUserOne@foo.bar")
            .password("password")
            .confirmPassword("password")
            .accessLevel(1)
            .accountStatus(1)
            .build();

        when(userManagerMock.createUser(org.mockito.ArgumentMatchers.isA(User.class))).thenReturn(testUserOne);
        mockMvc.perform(post("/welcome/register").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "testUserOne")
                .param("fullName", "Test User One")
                .param("emailAddress", "testUserOne@foo.bar")
                .param("password", "password")
                .param("confirmPassword", "password")
                .param("accountStatus", "1")
                .param("accessLevel", "1")
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"));
          //.andDo(print());

        ArgumentCaptor<User> formObjectArgument = ArgumentCaptor.forClass(User.class);
        verify(userManagerMock).createUser(formObjectArgument.capture());
        verifyNoMoreInteractions(userManagerMock);

        User formObject = formObjectArgument.getValue();
        assertThat(formObject.getUserId(), is(0));
        assertThat(formObject.getUserName(), is("testUserOne"));
    }

    @Test
    public void register_NewlyCreatedUserNotFoundShouldThrowDataRetrievalFailureException() throws Exception {
        when(userManagerMock.createUser(org.mockito.ArgumentMatchers.isA(User.class))).thenThrow(new DataRetrievalFailureException("Unable to create new User"));
        mockMvc.perform(post("/welcome/register").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "testUserOne")
                .param("fullName", "Test User One")
                .param("emailAddress", "testUserOne@foo.bar")
                .param("password", "password")
                .param("confirmPassword", "password")
                .param("accountStatus", "1")
                .param("accessLevel", "1")
            )
            .andExpect(model().attribute("exception", org.hamcrest.Matchers.isA(DataRetrievalFailureException.class)))
            .andExpect(status().isOk())
            .andExpect(view().name("fatalError"))
            .andExpect(forwardedUrl("/WEB-INF/views/fatalError.jsp"));
          //.andDo(print());

        ArgumentCaptor<User> formObjectArgument = ArgumentCaptor.forClass(User.class);
        verify(userManagerMock).createUser(formObjectArgument.capture());
        verifyNoMoreInteractions(userManagerMock);
    }
}
