package edu.ucar.unidata.cloudcontrol.controller.docker;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.NotModifiedException;

import edu.ucar.unidata.cloudcontrol.config.SecurityConfig;
import edu.ucar.unidata.cloudcontrol.config.WebAppContext;
import edu.ucar.unidata.cloudcontrol.domain.docker._Container;
import edu.ucar.unidata.cloudcontrol.domain.docker._Image;
import edu.ucar.unidata.cloudcontrol.domain.docker._Info;
import edu.ucar.unidata.cloudcontrol.domain.docker._ContainerBuilder;
import edu.ucar.unidata.cloudcontrol.domain.docker._ImageBuilder;
import edu.ucar.unidata.cloudcontrol.domain.docker._InfoBuilder;
import edu.ucar.unidata.cloudcontrol.service.docker.ClientManager;
import edu.ucar.unidata.cloudcontrol.service.docker.ContainerManager;
import edu.ucar.unidata.cloudcontrol.service.docker.ContainerMappingManager;
import edu.ucar.unidata.cloudcontrol.service.docker.ImageManager;
import edu.ucar.unidata.cloudcontrol.service.docker.ImageMappingManager;
import edu.ucar.unidata.cloudcontrol.service.docker.ServerManager;

import java.util.Arrays;
import java.util.List;

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
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.theInstance;

import static org.junit.Assert.assertThat;

import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Tests for edu.ucar.unidata.cloudcontrol.controller.docker.ImageController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebAppContext.class, SecurityConfig.class})
public class ImageControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ClientManager clientManagerMock;

    @Autowired
    private ContainerManager containerManagerMock;

    @Autowired
    private ContainerMappingManager containerMappingManagerMock;

    @Autowired
    private ImageManager imageManagerMock;

    @Autowired
    private ImageMappingManager imageMappingManagerMock;

    @Autowired
    private ServerManager serverManagerMock;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        Mockito.reset(clientManagerMock);
        Mockito.reset(containerManagerMock);
        Mockito.reset(containerMappingManagerMock);
        Mockito.reset(imageManagerMock);
        Mockito.reset(imageMappingManagerMock);
        Mockito.reset(serverManagerMock);

        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    public void getImageList_UnauthenticatedAccessToImageListShouldBeRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/dashboard/docker/image/list"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/j_spring_security_check"));
         // .andDo(print());
    }

    @Test
    @WithMockUser(username = "testUserOne", roles = {"USER"})
    public void getImageList_AccessToImageListWithAuthenticatedUserShouldBeAllowed() throws Exception {
        mockMvc.perform(get("/dashboard/docker/image/list").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getImageList_AccessToImageListWithAdminAsAuthenticatedUserShouldBeAllowed() throws Exception {
        mockMvc.perform(get("/dashboard/docker/image/list").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getImageList_ModelShouldContainListOfUnfilteredImagesAndListImagesActionForUserWithAdminRole() throws Exception {

        _Image testImageOne = new _ImageBuilder()
            .id("77af4d6b9913")
            .isVisibleToUsers(true)
            .build();

        _Image testImageTwo = new _ImageBuilder()
            .id("b6fa739cedf5")
            .isVisibleToUsers(false)
            .build();

        when(imageManagerMock.getImageList()).thenReturn(Arrays.asList(testImageOne, testImageTwo));

        mockMvc.perform(get("/dashboard/docker/image/list").with(csrf()))
            .andExpect(model().attribute("action", equalTo("listImages")))
            .andExpect(model().attribute("imageList", hasSize(2)))
            .andExpect(model().attribute("imageList", hasItem(
                theInstance(testImageOne)
            )))
            .andExpect(model().attribute("imageList", hasItem(
                theInstance(testImageTwo)
            )));
          //.andDo(print());

        verify(imageManagerMock, times(1)).getImageList();
        verifyNoMoreInteractions(imageManagerMock);
    }

    @Test
    @WithMockUser(username = "testUserOne", roles = {"USER"})
    public void getImageList_ModelShouldContainListOfFilteredImagesAndListImagesActionForUserWithUserRole() throws Exception {

        _Image testImageOne = new _ImageBuilder()
            .id("77af4d6b9913")
            .isVisibleToUsers(true)
            .build();

        _Image testImageTwo = new _ImageBuilder()
            .id("b6fa739cedf5")
            .isVisibleToUsers(false)
            .build();

        List<_Image> unfilteredUsers = Arrays.asList(testImageOne, testImageTwo);

        when(imageManagerMock.getImageList()).thenReturn(unfilteredUsers);
        when(imageMappingManagerMock.filterByImageMapping(unfilteredUsers)).thenReturn(Arrays.asList(testImageTwo));

        mockMvc.perform(get("/dashboard/docker/image/list").with(csrf()))
            .andExpect(model().attribute("action", equalTo("listImages")))
            .andExpect(model().attribute("imageList", hasSize(1)))
            .andExpect(model().attribute("imageList", hasItem(
                theInstance(testImageTwo)
            )));
          //.andDo(print());

        verify(imageManagerMock, times(1)).getImageList();
        verify(imageMappingManagerMock, times(1)).filterByImageMapping(unfilteredUsers);
        verifyNoMoreInteractions(imageManagerMock);
        verifyNoMoreInteractions(imageMappingManagerMock);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getImageList_ModelShouldContainNoListOfImagesAndJustTheListImagesAction() throws Exception {

        when(imageManagerMock.getImageList()).thenReturn(null);
        when(serverManagerMock.getInfo()).thenReturn(null);

        mockMvc.perform(get("/dashboard/docker/image/list").with(csrf()))
            .andExpect(model().attribute("action", equalTo("listImages")))
            .andExpect(model().attribute("imageList", is(nullValue())));
          //.andDo(print());

        verify(imageManagerMock, times(1)).getImageList();
        verify(serverManagerMock, times(1)).getInfo();
        verifyNoMoreInteractions(imageManagerMock);
        verifyNoMoreInteractions(serverManagerMock);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getImageList_ModelShouldContainAnEmptyListOfImagesAndListImagesAction() throws Exception {

        _Info testInfo = new _InfoBuilder()
            .architecture("x86_64")
            .images(0)
            .build();

        when(imageManagerMock.getImageList()).thenReturn(null);
        when(serverManagerMock.getInfo()).thenReturn(testInfo);

        mockMvc.perform(get("/dashboard/docker/image/list").with(csrf()))
            .andExpect(model().attribute("action", equalTo("listImages")))
            .andExpect(model().attribute("imageList", is(empty())));
          //.andDo(print());

        assertThat(testInfo.getImages(), is("0"));

        verify(imageManagerMock, times(1)).getImageList();
        verify(serverManagerMock, times(1)).getInfo();
        verifyNoMoreInteractions(imageManagerMock);
        verifyNoMoreInteractions(serverManagerMock);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getImageList_ConflictingImageNumberShouldThrowRunTimeException() throws Exception {

        _Info testInfo = new _InfoBuilder()
            .architecture("x86_64")
            .images(4)
            .build();

        when(imageManagerMock.getImageList()).thenReturn(null);
        when(serverManagerMock.getInfo()).thenReturn(testInfo);

        mockMvc.perform(get("/dashboard/docker/image/list").with(csrf()))
            .andExpect(model().attribute("message", containsString("RuntimeException")))
            .andExpect(status().isOk())
            .andExpect(view().name("fatalError"))
            .andExpect(forwardedUrl("/WEB-INF/views/fatalError.jsp"));
          //.andDo(print());

        verify(imageManagerMock, times(1)).getImageList();
        verify(serverManagerMock, times(1)).getInfo();
        verifyNoMoreInteractions(imageManagerMock);
        verifyNoMoreInteractions(serverManagerMock);
    }

    @Test
    public void startImage_UnauthenticatedAjaxGetRequestToStartImageShouldBeRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/dashboard/docker/image/b6fa739cedf5/start"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("http://localhost/j_spring_security_check"));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void startImage_AjaxGetRequestToStartImageWithAdminAsAuthenticatedUserShouldBeAllowed() throws Exception {
        mockMvc.perform(get("/dashboard/docker/image/b6fa739cedf5/start").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Error: unable to start image.")));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "testUserOne", roles = {"USER"})
    public void startImage_AjaxGetRequestToStartPublicImageWithAuthenticatedUserShouldBeAllowed() throws Exception {

        when(imageMappingManagerMock.isVisibleToUser("77af4d6b9913")).thenReturn(true);
        mockMvc.perform(get("/dashboard/docker/image/77af4d6b9913/start").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Error: unable to start image.")));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "testUserOne", roles = {"USER"})
    public void startImage_AjaxGetRequestToStartNonPublicImageWithAuthenticatedUserShouldBeDenied() throws Exception {

        when(imageMappingManagerMock.isVisibleToUser("b6fa739cedf5")).thenReturn(false);
        mockMvc.perform(get("/dashboard/docker/image/b6fa739cedf5/start").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Error: you are not allowed to start this image.")));
          //.andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void startImage_ImageStartedSuccessfullyShouldReturnContainerId() throws Exception {

        when(containerManagerMock.startContainer("b6fa739cedf5", "admin")).thenReturn("8dfafdbc3a40");
        mockMvc.perform(get("/dashboard/docker/image/b6fa739cedf5/start").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("8dfafdbc3a40")));
         // .andDo(print());

        verify(containerManagerMock, times(1)).startContainer("b6fa739cedf5", "admin");
        verifyNoMoreInteractions(containerManagerMock);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void startImage_ImageContainerAlreadyRunningShouldThrowRunTimeException() throws Exception {

        when(containerManagerMock.startContainer("b6fa739cedf5", "admin")).thenThrow(new NotModifiedException("Container 8dfafdbc3a40 is already running."));
        mockMvc.perform(get("/dashboard/docker/image/b6fa739cedf5/start").with(csrf()))
            .andExpect(model().attribute("message", containsString("RuntimeException")))
            .andExpect(status().isOk())
            .andExpect(view().name("fatalError"))
            .andExpect(forwardedUrl("/WEB-INF/views/fatalError.jsp"));
          //.andDo(print());

        verify(containerManagerMock, times(1)).startContainer("b6fa739cedf5", "admin");
        verifyNoMoreInteractions(containerManagerMock);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void startImage_StartedImageContainerNotFoundShouldThrowRunTimeException() throws Exception {

        when(containerManagerMock.startContainer("b6fa739cedf5", "admin")).thenThrow(new NotFoundException("Unable to find and start container 8dfafdbc3a40."));
        mockMvc.perform(get("/dashboard/docker/image/b6fa739cedf5/start").with(csrf()))
            .andExpect(model().attribute("message", containsString("RuntimeException")))
            .andExpect(status().isOk())
            .andExpect(view().name("fatalError"))
            .andExpect(forwardedUrl("/WEB-INF/views/fatalError.jsp"))
          .andDo(print());

        verify(containerManagerMock, times(1)).startContainer("b6fa739cedf5", "admin");
        verifyNoMoreInteractions(containerManagerMock);
    }
}
