package edu.ucar.unidata.cloudcontrol.controller.user;

import edu.ucar.unidata.cloudcontrol.config.SecurityConfig;
import edu.ucar.unidata.cloudcontrol.config.WebAppContext;
import edu.ucar.unidata.cloudcontrol.domain.User;
import edu.ucar.unidata.cloudcontrol.domain.UserBuilder;
import edu.ucar.unidata.cloudcontrol.service.user.UserManager;
import edu.ucar.unidata.cloudcontrol.service.user.validators.PasswordValidator;

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
 * Tests for edu.ucar.unidata.cloudcontrol.controller.user.ResetPasswordController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebAppContext.class, SecurityConfig.class})
public class ResetPasswordControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private PasswordValidator passwordValidatorMock;

    @Autowired
    private UserManager userManagerMock;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        Mockito.reset(userManagerMock);
        Mockito.reset(passwordValidatorMock);
        Mockito.when(passwordValidatorMock.supports(any(Class.class))).thenReturn(true);

        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    @WithMockUser(username = "testUserTwo", roles = {"USER"})
    public void editUserPassword_AccessToResetPasswordFormWithUnauthorizedUserShouldBeDenied() throws Exception {
        mockMvc.perform(get("/dashboard/user/password/testUserOne").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("denied"))
            .andExpect(forwardedUrl("/WEB-INF/views/denied.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "testUserOne", roles = {"USER"})
    public void editUserPassword_AccessToResetPasswordFormWithAuthorizedUserShouldBeAllowed() throws Exception {
        mockMvc.perform(get("/dashboard/user/password/testUserOne").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void editUserPassword_AccessToResetPasswordFormWithAdminAsAuthorizedUserShouldBeAllowed() throws Exception {
        mockMvc.perform(get("/dashboard/user/password/testUserOne").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void editUserPassword_ModelShouldContainUserWithPasswordToEditAndResetPasswordAction() throws Exception {
        User testUserOne = new UserBuilder()
            .password("password")
            .confirmPassword("password")
            .build();

        when(userManagerMock.lookupUser("testUserOne")).thenReturn(testUserOne);
        mockMvc.perform(get("/dashboard/user/password/testUserOne").with(csrf()))
            .andExpect(model().attribute("action", equalTo("resetPassword")))
            .andExpect(model().attribute("user", hasProperty("password", is("password"))))
            .andExpect(model().attribute("user", hasProperty("confirmPassword", is("password"))));
          //.andDo(print());

        verify(userManagerMock, times(1)).lookupUser("testUserOne");
        verifyNoMoreInteractions(userManagerMock);
    }

    @Test
    @WithMockUser(username = "testUsertwo", roles = {"USER"})
    public void editUserPassword_PostToResetPasswordFormWithUnauthorizedUserShouldBeDenied() throws Exception {
        mockMvc.perform(post("/dashboard/user/password/testUserOne").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("denied"))
            .andExpect(forwardedUrl("/WEB-INF/views/denied.jsp"));
         // .andDo(print());
    }

    @Test
    @WithMockUser(username = "testUserOne", roles = {"USER"})
    public void editUserPassword_PostToResetPasswordFormWithAuthorizedUserShouldBeAllowed() throws Exception {
        User testUserOne = new UserBuilder()
            .build();

        when(userManagerMock.lookupUser("testUserOne")).thenReturn(testUserOne);
        doNothing().when(userManagerMock).updatePassword(org.mockito.ArgumentMatchers.isA(User.class));
        mockMvc.perform(post("/dashboard/user/password/testUserOne").with(csrf()))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/dashboard/user/view/testUserOne"));
         //.andDo(print());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void editUserPassword_PostToResetPasswordFormWithAdminAsAuthorizedUserShouldBeAllowed() throws Exception {
        User testUserOne = new UserBuilder()
            .build();

        when(userManagerMock.lookupUser("testUserOne")).thenReturn(testUserOne);
        doNothing().when(userManagerMock).updatePassword(org.mockito.ArgumentMatchers.isA(User.class));
        mockMvc.perform(post("/dashboard/user/password/testUserOne").with(csrf()))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/dashboard/user/view/testUserOne"));
         //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void editUserPassword_UserPasswordValidationErrorShouldRenderResetPasswordView() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Errors errors = (Errors) invocationOnMock.getArguments()[1];
                errors.reject("forcing a validation error");
                return null;
            }
        }).when(passwordValidatorMock).validate(any(), any(Errors.class));

        mockMvc.perform(post("/dashboard/user/password/testUserOne").with(csrf()))
            .andExpect(model().attribute("action", equalTo("resetPassword")))
            .andExpect(model().hasErrors());
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void editUserPassword_UserPasswordEditedSuccessfullyShouldRedirectToUserView() throws Exception {
        User testUserOne = new UserBuilder()
            .password("password")
            .confirmPassword("password")
            .build();

        when(userManagerMock.lookupUser("testUserOne")).thenReturn(testUserOne);
        doNothing().when(userManagerMock).updatePassword(org.mockito.ArgumentMatchers.isA(User.class));
        mockMvc.perform(post("/dashboard/user/password/testUserOne").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("password", "password")
                .param("confirmPassword", "password")
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/dashboard/user/view/testUserOne"));
          //.andDo(print());

        ArgumentCaptor<User> formObjectArgument = ArgumentCaptor.forClass(User.class);
        verify(userManagerMock, times(1)).lookupUser("testUserOne");
        verify(userManagerMock).updatePassword(formObjectArgument.capture());
        verifyNoMoreInteractions(userManagerMock);

        User formObject = formObjectArgument.getValue();
        assertThat(formObject.getPassword(), is("password"));
        assertThat(formObject.getConfirmPassword(), is("password"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void editUserPassword_UnableToEditUserPasswordShouldThrowDataRetrievalFailureException() throws Exception {
        User testUserOne = new UserBuilder()
            .password("password")
            .confirmPassword("password")
            .build();

        when(userManagerMock.lookupUser("testUserOne")).thenReturn(testUserOne);
        doThrow(new DataRetrievalFailureException("Unable to find User with userId 1")).when(userManagerMock).updatePassword(org.mockito.ArgumentMatchers.isA(User.class));
        mockMvc.perform(post("/dashboard/user/password/testUserOne").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("password", "password")
                .param("confirmPassword", "password")
            )
            .andExpect(model().attribute("exception", org.hamcrest.Matchers.isA(DataRetrievalFailureException.class)))
            .andExpect(status().isOk())
            .andExpect(view().name("fatalError"))
            .andExpect(forwardedUrl("/WEB-INF/views/fatalError.jsp"));
          //.andDo(print());

        ArgumentCaptor<User> formObjectArgument = ArgumentCaptor.forClass(User.class);
        verify(userManagerMock, times(1)).lookupUser("testUserOne");
        verify(userManagerMock).updatePassword(formObjectArgument.capture());
        verifyNoMoreInteractions(userManagerMock);
    }
}
