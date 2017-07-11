package edu.ucar.unidata.cloudcontrol.controller.user;

import edu.ucar.unidata.cloudcontrol.config.SecurityConfig;
import edu.ucar.unidata.cloudcontrol.config.WebAppContext;
import edu.ucar.unidata.cloudcontrol.domain.User;
import edu.ucar.unidata.cloudcontrol.domain.UserBuilder;
import edu.ucar.unidata.cloudcontrol.service.user.UserManager;
import edu.ucar.unidata.cloudcontrol.service.user.validators.EditUserValidator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
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
 * Tests for edu.ucar.unidata.cloudcontrol.controller.user.EditUserController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebAppContext.class, SecurityConfig.class})
public class EditUserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private EditUserValidator editUserValidatorMock;

    @Autowired
    private UserManager userManagerMock;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        Mockito.reset(userManagerMock);
        Mockito.reset(editUserValidatorMock);
        Mockito.when(editUserValidatorMock.supports(any(Class.class))).thenReturn(true);

        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    @WithMockUser(username = "testUserTwo", roles = {"USER"})
    public void editUser_AccessToEditUserFormWithUnauthorizedUserShouldBeDenied() throws Exception {
        mockMvc.perform(get("/dashboard/user/edit/testUserOne").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("denied"))
            .andExpect(forwardedUrl("/WEB-INF/views/denied.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "testUserOne", roles = {"USER"})
    public void editUser_AccessToEditUserFormWithAuthorizedUserShouldBeAllowed() throws Exception {
        mockMvc.perform(get("/dashboard/user/edit/testUserOne").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void editUser_AccessToEditUserFormWithAdminAsAuthorizedUserShouldBeAllowed() throws Exception {
        mockMvc.perform(get("/dashboard/user/edit/testUserOne").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void editUser_ModelShouldContainUserToEditAndEditUserAction() throws Exception {

        User testUserOne = new UserBuilder()
            .userName("testUserOne")
            .fullName("Test User One")
            .emailAddress("testUserOne@foo.bar")
            .accessLevel(1)
            .accountStatus(1)
            .build();

        when(userManagerMock.lookupUser("testUserOne")).thenReturn(testUserOne);
        mockMvc.perform(get("/dashboard/user/edit/testUserOne").with(csrf()))
            .andExpect(model().attribute("action", equalTo("editUser")))
            .andExpect(model().attribute("user", hasProperty("userName", is("testUserOne"))))
            .andExpect(model().attribute("user", hasProperty("fullName", is("Test User One"))))
            .andExpect(model().attribute("user", hasProperty("emailAddress", is("testUserOne@foo.bar"))))
            .andExpect(model().attribute("user", hasProperty("accessLevel", is(1))))
            .andExpect(model().attribute("user", hasProperty("accountStatus", is(1))));
          //.andDo(print());

        verify(userManagerMock, times(1)).lookupUser("testUserOne");
        verifyNoMoreInteractions(userManagerMock);
    }

    @Test
    @WithMockUser(username = "testUsertwo", roles = {"USER"})
    public void editUser_PostToEditUserFormWithUnauthorizedUserShouldBeDenied() throws Exception {
        mockMvc.perform(post("/dashboard/user/edit/testUserOne").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("denied"))
            .andExpect(forwardedUrl("/WEB-INF/views/denied.jsp"));
         // .andDo(print());
    }

    @Test
    @WithMockUser(username = "testUserOne", roles = {"USER"})
    public void editUser_PostToEditUserFormWithAuthorizedUserShouldBeAllowed() throws Exception {
        User testUserOne = new UserBuilder()
            .build();

        when(userManagerMock.lookupUser("testUserOne")).thenReturn(testUserOne);
        when(userManagerMock.updateUser(isA(User.class))).thenReturn(testUserOne);

        mockMvc.perform(post("/dashboard/user/edit/testUserOne").with(csrf()))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/dashboard/user/view/testUserOne"));
         //.andDo(print());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void editUser_PostToEditUserFormWithAdminAsAuthorizedUserShouldBeAllowed() throws Exception {
        User testUserOne = new UserBuilder()
            .build();

        when(userManagerMock.lookupUser("testUserOne")).thenReturn(testUserOne);
        when(userManagerMock.updateUser(isA(User.class))).thenReturn(testUserOne);

        mockMvc.perform(post("/dashboard/user/edit/testUserOne").with(csrf()))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/dashboard/user/view/testUserOne"));
         //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void editUser_UserValidationErrorShouldRenderEditUserView() throws Exception {

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Errors errors = (Errors) invocationOnMock.getArguments()[1];
                errors.reject("forcing a validation error");
                return null;
            }
        }).when(editUserValidatorMock).validate(any(), any(Errors.class));

        mockMvc.perform(post("/dashboard/user/edit/testUserOne").with(csrf()))
            .andExpect(model().attribute("action", equalTo("editUser")))
            .andExpect(model().hasErrors());
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void editUser_UserEditedSuccessfullyShouldRedirectToUserView() throws Exception {

        User testUserOne = new UserBuilder()
            .userName("testUserOne")
            .fullName("Test User One")
            .emailAddress("testUserOne@foo.bar")
            .accessLevel(1)
            .accountStatus(1)
            .build();

        when(userManagerMock.lookupUser("testUserOne")).thenReturn(testUserOne);
        when(userManagerMock.updateUser(isA(User.class))).thenReturn(testUserOne);
        mockMvc.perform(post("/dashboard/user/edit/testUserOne").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "testUserOne")
                .param("fullName", "Test User One")
                .param("emailAddress", "testUserOne@foo.bar")
                .param("accountStatus", "1")
                .param("accessLevel", "1")
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/dashboard/user/view/testUserOne"));
          //.andDo(print());

        ArgumentCaptor<User> formObjectArgument = ArgumentCaptor.forClass(User.class);
        verify(userManagerMock, times(1)).lookupUser("testUserOne");
        verify(userManagerMock).updateUser(formObjectArgument.capture());
        verifyNoMoreInteractions(userManagerMock);

        User formObject = formObjectArgument.getValue();
        assertThat(formObject.getUserId(), is(0));
        assertThat(formObject.getUserName(), is("testUserOne"));
        assertThat(formObject.getFullName(), is("Test User One"));
        assertThat(formObject.getEmailAddress(), is("testUserOne@foo.bar"));
        assertThat(formObject.getAccountStatus(), is(1));
        assertThat(formObject.getAccessLevel(), is(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void editUser_EditedUserNotFoundShouldThrowRunTimeException() throws Exception {

        User testUserOne = new UserBuilder()
            .userName("testUserOne")
            .fullName("Test User One")
            .emailAddress("testUserOne@foo.bar")
            .accessLevel(1)
            .accountStatus(1)
            .build();

        when(userManagerMock.lookupUser("testUserOne")).thenReturn(testUserOne);
        when(userManagerMock.updateUser(isA(User.class))).thenThrow(new RecoverableDataAccessException(""));
        mockMvc.perform(post("/dashboard/user/edit/testUserOne").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "testUserOne")
                .param("fullName", "Test User One")
                .param("emailAddress", "testUserOne@foo.bar")
                .param("accountStatus", "1")
                .param("accessLevel", "1")
            )
            .andExpect(model().attribute("message", containsString("RuntimeException")))
            .andExpect(status().isOk())
            .andExpect(view().name("fatalError"))
            .andExpect(forwardedUrl("/WEB-INF/views/fatalError.jsp"));
          //.andDo(print());

        verify(userManagerMock, times(1)).lookupUser("testUserOne");
        verify(userManagerMock, times(1)).updateUser(isA(User.class));
        verifyNoMoreInteractions(userManagerMock);
    }
}
