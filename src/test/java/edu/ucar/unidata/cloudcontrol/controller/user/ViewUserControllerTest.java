package edu.ucar.unidata.cloudcontrol.controller.user;

import edu.ucar.unidata.cloudcontrol.config.SecurityConfig;
import edu.ucar.unidata.cloudcontrol.config.WebAppContext;

import edu.ucar.unidata.cloudcontrol.service.user.UserManager;

import edu.ucar.unidata.cloudcontrol.domain.User;
import edu.ucar.unidata.cloudcontrol.domain.UserBuilder;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.theInstance;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
 * Tests for edu.ucar.unidata.cloudcontrol.controller.ViewUserController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebAppContext.class, SecurityConfig.class})
public class ViewUserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserManager userManagerMock;

    @Before
    public void setUp() {
        Mockito.reset(userManagerMock);

        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    public void testListUsers_WithUnauthorizedUser() throws Exception {
        mockMvc.perform(get("/dashboard/user").with(user("user").password("password").roles("USER")))
            .andExpect(status().isOk())
            .andExpect(view().name("denied"))
            .andExpect(forwardedUrl("/WEB-INF/views/denied.jsp"));
            //.andDo(print());
    }

    @Test
    public void testListUsers_WithAuthorizedUser() throws Exception {
        mockMvc.perform(get("/dashboard/user").with(user("admin").password("password").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
            //.andDo(print());
    }

    @Test
    public void testListUsers_ModelShouldContainListUsersAction() throws Exception {
        mockMvc.perform(get("/dashboard/user").with(user("admin").password("password").roles("ADMIN")))
            .andExpect(model().attribute("action", equalTo("listUsers")));
            //.andDo(print());
    }

    @Test
    public void testListUsers_ShouldAddListOfUsersToModel() throws Exception {
        User testUserOne = new UserBuilder()
            .userId(1)
            .userName("testUserOne")
            .build();
 
        User testUserTwo = new UserBuilder()
            .userId(2)
            .userName("testUserTwo")
            .build();

        when(userManagerMock.getUserList()).thenReturn(Arrays.asList(testUserOne, testUserTwo));

        mockMvc.perform(get("/dashboard/user").with(user("admin").password("password").roles("ADMIN")))
            .andExpect(model().attribute("users", hasSize(2)))
            .andExpect(model().attribute("users", hasItem(
                allOf(
                    hasProperty("userId", is(1)),
                    hasProperty("userName", is("testUserOne"))
                )
            )))
            .andExpect(model().attribute("users", hasItem(
                allOf(
                    hasProperty("userId", is(2)),
                    hasProperty("userName", is("testUserTwo"))
                )
            )));
            //.andDo(print());
            verify(userManagerMock, times(1)).getUserList();
            verifyNoMoreInteractions(userManagerMock);
    }

    @Test
    public void testViewUser_WithUnauthorizedUser() throws Exception {
        mockMvc.perform(get("/dashboard/user/view/testUserOne").with(user("testUserTwo").password("password").roles("USER")))
            .andExpect(status().isOk())
            .andExpect(view().name("denied"))
            .andExpect(forwardedUrl("/WEB-INF/views/denied.jsp"));
            //.andDo(print());
    }

    @Test
    public void testViewUser_WithAuthorizedUser() throws Exception {
        mockMvc.perform(get("/dashboard/user/view/testUserOne").with(user("testUserOne").password("password").roles("USER")))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
            //.andDo(print());
    }

    @Test
    public void testViewUser_WithAdminAsAuthorizedUser() throws Exception {
        mockMvc.perform(get("/dashboard/user/view/testUserOne").with(user("admin").password("password").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
            //.andDo(print());
    }


    @Test
    public void testViewUser_ShouldAddRequestedUserAndViewUserActionToModel() throws Exception {
        User testUserOne = new UserBuilder()
            .userId(1)
            .userName("testUserOne")
            .build();

        when(userManagerMock.lookupUser("testUserOne")).thenReturn(testUserOne);

        mockMvc.perform(get("/dashboard/user/view/testUserOne").with(user("admin").password("password").roles("ADMIN")))
            .andExpect(model().attribute("action", equalTo("viewUser")))
            .andExpect(model().attribute("user", theInstance(testUserOne)));
            //.andDo(print());
            verify(userManagerMock, times(1)).lookupUser("testUserOne");
            verifyNoMoreInteractions(userManagerMock);
    }

    @Test
    public void testViewUser_UserNotFoundShouldThrowRunTimeException() throws Exception {
        when(userManagerMock.lookupUser("testUserOne")).thenThrow(new RecoverableDataAccessException(""));

        mockMvc.perform(get("/dashboard/user/view/testUserOne").with(user("admin").password("password").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(view().name("fatalError"))
            .andExpect(forwardedUrl("/WEB-INF/views/fatalError.jsp"));
            //.andDo(print());
            verify(userManagerMock, times(1)).lookupUser("testUserOne");
            verifyNoMoreInteractions(userManagerMock);
    }























}
