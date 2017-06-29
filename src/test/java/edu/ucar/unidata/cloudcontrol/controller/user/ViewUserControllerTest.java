package edu.ucar.unidata.cloudcontrol.controller.user;

import edu.ucar.unidata.cloudcontrol.config.SecurityConfig;
import edu.ucar.unidata.cloudcontrol.config.WebAppContext;
import edu.ucar.unidata.cloudcontrol.domain.User;
import edu.ucar.unidata.cloudcontrol.domain.UserBuilder;
import edu.ucar.unidata.cloudcontrol.service.user.UserManager;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;

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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
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
    private UserManager userManagerMock;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        Mockito.reset(userManagerMock);

        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void listUsers_AccessToUserListWithUnauthorizedUserShouldBeDenied() throws Exception {
        mockMvc.perform(get("/dashboard/user").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("denied"))
            .andExpect(forwardedUrl("/WEB-INF/views/denied.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void listUsers_AccessToUserListWithAuthorizedUseShouldBeAllowed() throws Exception {
        mockMvc.perform(get("/dashboard/user").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void listUsers_ModelShouldContainListUsersAction() throws Exception {
        mockMvc.perform(get("/dashboard/user").with(csrf()))
            .andExpect(model().attribute("action", equalTo("listUsers")));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void listUsers_ShouldAddListOfUsersToModel() throws Exception {
        User testUserOne = new UserBuilder()
            .userId(1)
            .userName("testUserOne")
            .build();
 
        User testUserTwo = new UserBuilder()
            .userId(2)
            .userName("testUserTwo")
            .build();

        when(userManagerMock.getUserList()).thenReturn(Arrays.asList(testUserOne, testUserTwo));

        mockMvc.perform(get("/dashboard/user").with(csrf()))
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
    @WithMockUser(username = "testUserTwo", roles = {"USER"})
    public void viewUser_AccessToUserInformationWithUnauthorizedUserShouldBeDenied() throws Exception {
        mockMvc.perform(get("/dashboard/user/view/testUserOne").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("denied"))
            .andExpect(forwardedUrl("/WEB-INF/views/denied.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "testUserOne", roles = {"USER"})
    public void viewUser_AccessToUserInformationWithAuthorizedUserShouldBeAllowed() throws Exception {
        mockMvc.perform(get("/dashboard/user/view/testUserOne").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void viewUser_WithAdminAsAuthorizedUser() throws Exception {
        mockMvc.perform(get("/dashboard/user/view/testUserOne").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
          //.andDo(print());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void viewUser_ShouldAddRequestedUserAndViewUserActionToModel() throws Exception {
        User testUserOne = new UserBuilder()
            .userId(1)
            .userName("testUserOne")
            .build();

        when(userManagerMock.lookupUser("testUserOne")).thenReturn(testUserOne);

        mockMvc.perform(get("/dashboard/user/view/testUserOne").with(csrf()))
            .andExpect(model().attribute("action", equalTo("viewUser")))
            .andExpect(model().attribute("user", theInstance(testUserOne)));
          //.andDo(print());
            verify(userManagerMock, times(1)).lookupUser("testUserOne");
            verifyNoMoreInteractions(userManagerMock);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void viewUser_UserNotFoundShouldThrowRunTimeException() throws Exception {
        when(userManagerMock.lookupUser("testUserOne")).thenThrow(new RecoverableDataAccessException(""));

        mockMvc.perform(get("/dashboard/user/view/testUserOne").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("fatalError"))
            .andExpect(forwardedUrl("/WEB-INF/views/fatalError.jsp"));
          //.andDo(print());
            verify(userManagerMock, times(1)).lookupUser("testUserOne");
            verifyNoMoreInteractions(userManagerMock);
    }























}
