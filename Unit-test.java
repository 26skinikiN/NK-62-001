package com.investment.controller;

import com.investment.model.ProjectApp;
import com.investment.model.User;
import com.investment.model.enums.ProjectAppStatus;
import com.investment.model.enums.Role;
import com.investment.repository.ProjectAppRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AppContTest {

    @InjectMocks
    private AppCont appCont;

    @Mock
    private ProjectAppRepo projectAppRepo;

    @Mock
    private Model model;

    @Mock
    private User mockUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(appCont.getUser()).thenReturn(mockUser);
    }

    @Test
    public void testApps_UserRole() {
        // Мокируем роль пользователя и проекты
        when(mockUser.getRole()).thenReturn(Role.USER);
        List<ProjectApp> userApps = Arrays.asList(new ProjectApp(), new ProjectApp());
        when(mockUser.getProjectApps()).thenReturn(userApps);

        String viewName = appCont.apps(model);

        verify(model).addAttribute("apps", userApps);
        assertEquals("apps", viewName);
    }

    @Test
    public void testApps_AdminRole() {
        // Мокируем роль администратора и все проекты
        when(mockUser.getRole()).thenReturn(Role.ADMIN);
        List<ProjectApp> allApps = Arrays.asList(new ProjectApp(), new ProjectApp(), new ProjectApp());
        when(projectAppRepo.findAll()).thenReturn(allApps);

        String viewName = appCont.apps(model);

        verify(model).addAttribute("apps", allApps);
        assertEquals("apps", viewName);
    }

    @Test
    public void testDone() {
        Long id = 1L;
        ProjectApp app = new ProjectApp();
        when(projectAppRepo.getReferenceById(id)).thenReturn(app);

        String viewName = appCont.done(id);

        verify(app).setStatus(ProjectAppStatus.DONE);
        verify(app).setAdmin(mockUser);
        verify(projectAppRepo).save(app);
        assertEquals("redirect:/apps", viewName);
    }

    @Test
    public void testReject() {
        Long id = 1L;
        ProjectApp app = new ProjectApp();
        when(projectAppRepo.getReferenceById(id)).thenReturn(app);

        String viewName = appCont.reject(id);

        verify(app).setStatus(ProjectAppStatus.REJECT);
        verify(app).setAdmin(mockUser);
        verify(projectAppRepo).save(app);
        assertEquals("redirect:/apps", viewName);
    }

    @Test
    public void testDelete() {
        Long id = 1L;

        String viewName = appCont.delete(id);

        verify(projectAppRepo).deleteById(id);
        assertEquals("redirect:/apps", viewName);
    }
}

@RunWith(MockitoJUnitRunner.class)
public class IndexContTest {

    @InjectMocks
    private IndexCont indexCont;

    @Mock
    private Model model;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIndex1() {
        String viewName = indexCont.index1();
        assertEquals("redirect:/about", viewName);
    }

    @Test
    public void testIndex2() {
        String viewName = indexCont.index2();
        assertEquals("redirect:/about", viewName);
    }

    @Test
    public void testAbout() {
        String viewName = indexCont.about(model);

        verify(indexCont).getCurrentUserAndRole(model);
        assertEquals("about", viewName);
    }
}

@RunWith(MockitoJUnitRunner.class)
public class LoginContTest {

    @InjectMocks
    private LoginCont loginCont;

    @Mock
    private Model model;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLogin() {
        String viewName = loginCont.login(model);

        verify(loginCont).getCurrentUserAndRole(model);
        assertEquals("login", viewName);
    }
}

@RunWith(MockitoJUnitRunner.class)
public class ProfileContTest {

    @InjectMocks
    private ProfileCont profileCont;

    @Mock
    private UserRepo userRepo;

    @Mock
    private Model model;

    @Mock
    private AppUser user;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(profileCont.getUser()).thenReturn(user);
    }

    @Test
    public void testProfile() {
        String viewName = profileCont.profile(model);

        verify(profileCont).getCurrentUserAndRole(model);
        assertEquals("profile", viewName);
    }

    @Test
    public void testProfileFio() {
        String fio = "New FIO";
        when(userRepo.save(user)).thenReturn(user);

        String viewName = profileCont.profileFio(fio);

        verify(user).setFio(fio);
        verify(userRepo).save(user);
        assertEquals("redirect:/profile", viewName);
    }

    @Test
    public void testPassword_MismatchedPasswords() {
        String password = "password";
        String password_repeat = "different_password";

        String viewName = profileCont.password(model, password, password_repeat);

        verify(profileCont).getCurrentUserAndRole(model);
        verify(model).addAttribute("message", "Некорректный ввод паролей!");
        assertEquals("profile", viewName);
    }

    @Test
    public void testPassword_MatchedPasswords() {
        String password = "password";
        String password_repeat = "password";
        when(userRepo.save(user)).thenReturn(user);

        String viewName = profileCont.password(model, password, password_repeat);

        verify(user).setPassword(password);
        verify(userRepo).save(user);
        assertEquals("redirect:/profile", viewName);
    }

    @Test
    public void testTheme() {
        when(user.isTheme()).thenReturn(true);
        when(userRepo.save(user)).thenReturn(user);

        String viewName = profileCont.theme();

        verify(user).setTheme(false);
        verify(userRepo).save(user);
        assertEquals("redirect:/profile", viewName);
    }
}

@RunWith(MockitoJUnitRunner.class)
public class ProjectContTest {

    @InjectMocks
    private ProjectCont projectCont;

    @Mock
    private ProjectRepo projectRepo;

    @Mock
    private ProjectAppRepo projectAppRepo;

    @Mock
    private Model model;

    @Mock
    private MultipartFile photo;

    @Mock
    private Project project;

    @Mock
    private ProjectApp projectApp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProjects() {
        when(projectRepo.findAll()).thenReturn(Collections.singletonList(project));

        String viewName = projectCont.projects(model);

        verify(projectCont).getCurrentUserAndRole(model);
        verify(model).addAttribute("projects", Collections.singletonList(project));
        assertEquals("projects", viewName);
    }

    @Test
    public void testFilter() {
        String name = "Test Project";
        int filter = 1;
        when(projectRepo.findAllByNameContaining(name)).thenReturn(Collections.singletonList(project));
        when(project.getName()).thenReturn(name);

        String viewName = projectCont.filter(model, name, filter);

        verify(projectCont).getCurrentUserAndRole(model);
        verify(model).addAttribute("name", name);
        verify(model).addAttribute("filter", filter);
        verify(model).addAttribute("projects", Collections.singletonList(project));
        assertEquals("projects", viewName);
    }

    @Test
    public void testProject() {
        Long id = 1L;
        when(projectRepo.getReferenceById(id)).thenReturn(project);

        String viewName = projectCont.project(model, id);

        verify(projectCont).getCurrentUserAndRole(model);
        verify(model).addAttribute("project", project);
        assertEquals("project", viewName);
    }

    @Test
    public void testApp() {
        Long id = 1L;
        float sum = 100.0f;
        when(projectRepo.getReferenceById(id)).thenReturn(project);

        String viewName = projectCont.app(sum, id);

        verify(projectAppRepo).save(any(ProjectApp.class));
        assertEquals("redirect:/projects/1", viewName);
    }

    @Test
    public void testAddGet() {
        String viewName = projectCont.add(model);

        verify(projectCont).getCurrentUserAndRole(model);
        assertEquals("project_add", viewName);
    }

    @Test
    public void testAddPost_Success() throws IOException {
        String name = "Test Project";
        String founder = "Test Founder";
        String date = "2023-06-10";
        float need = 100.0f;
        String description = "Test Description";
        when(projectRepo.save(any(Project.class))).thenReturn(project);

        String viewName = projectCont.add(model, name, founder, date, need, description, photo);

        verify(projectRepo).save(any(Project.class));
        assertEquals("redirect:/projects/null", viewName); // Assuming the ID is null for the test mock
    }

    @Test
    public void testAddPost_Failure() throws IOException {
        String name = "Test Project";
        String founder = "Test Founder";
        String date = "2023-06-10";
        float need = 100.0f;
        String description = "Test Description";
        doThrow(new IOException()).when(photo).getOriginalFilename();

        String viewName = projectCont.add(model, name, founder, date, need, description, photo);

        verify(model).addAttribute("message", "Некорректные данные!");
        verify(projectCont).getCurrentUserAndRole(model);
        assertEquals("project_add", viewName);
    }

    @Test
    public void testEditGet() {
        Long id = 1L;
        when(projectRepo.getReferenceById(id)).thenReturn(project);

        String viewName = projectCont.edit(model, id);

        verify(projectCont).getCurrentUserAndRole(model);
        verify(model).addAttribute("project", project);
        assertEquals("project_edit", viewName);
    }

    @Test
    public void testEditPost_Success() throws IOException {
        Long id = 1L;
        String name = "Test Project";
        String founder = "Test Founder";
        String date = "2023-06-10";
        float need = 100.0f;
        String description = "Test Description";
        when(projectRepo.getReferenceById(id)).thenReturn(project);

        String viewName = projectCont.edit(model, name, founder, date, need, description, photo, id);

        verify(projectRepo).save(project);
        assertEquals("redirect:/projects/1", viewName);
    }

    @Test
    public void testEditPost_Failure() throws IOException {
        Long id = 1L;
        String name = "Test Project";
        String founder = "Test Founder";
        String date = "2023-06-10";
        float need = 100.0f;
        String description = "Test Description";
        doThrow(new IOException()).when(photo).getOriginalFilename();
        when(projectRepo.getReferenceById(id)).thenReturn(project);

        String viewName = projectCont.edit(model, name, founder, date, need, description, photo, id);

        verify(model).addAttribute("message", "Некорректные данные!");
        verify(projectCont).getCurrentUserAndRole(model);
        verify(model).addAttribute("project", project);
        assertEquals("project_edit", viewName);
    }

    @Test
    public void testDelete() {
        Long id = 1L;

        String viewName = projectCont.delete(id);

        verify(projectRepo).deleteById(id);
        assertEquals("redirect:/projects", viewName);
    }
}

@RunWith(MockitoJUnitRunner.class)
public class RegContTest {

    @InjectMocks
    private RegCont regCont;

    @Mock
    private UserRepo userRepo;

    @Mock
    private Model model;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegGet() {
        String viewName = regCont.reg(model);

        verify(regCont).getCurrentUserAndRole(model);
        assertEquals("reg", viewName);
    }

    @Test
    public void testRegPost_PasswordMismatch() {
        String username = "testuser";
        String password = "password";
        String passwordRepeat = "differentPassword";
        String fio = "Test User";

        String viewName = regCont.reg(model, username, password, passwordRepeat, fio);

        verify(regCont).getCurrentUserAndRole(model);
        verify(model).addAttribute("message", "Некорректный ввод паролей!");
        assertEquals("reg", viewName);
    }

    @Test
    public void testRegPost_UserAlreadyExists() {
        String username = "testuser";
        String password = "password";
        String passwordRepeat = "password";
        String fio = "Test User";
        when(userRepo.findByUsername(username)).thenReturn(new AppUser());

        String viewName = regCont.reg(model, username, password, passwordRepeat, fio);

        verify(regCont).getCurrentUserAndRole(model);
        verify(model).addAttribute("message", "Пользователь с таким логином уже существует!");
        assertEquals("reg", viewName);
    }

    @Test
    public void testRegPost_Success_Admin() {
        String username = "testuser";
        String password = "password";
        String passwordRepeat = "password";
        String fio = "Test User";
        when(userRepo.findByUsername(username)).thenReturn(null);
        when(userRepo.findAll()).thenReturn(Collections.emptyList());

        String viewName = regCont.reg(model, username, password, passwordRepeat, fio);

        verify(userRepo).save(any(AppUser.class));
        assertEquals("redirect:/login", viewName);
    }

    @Test
    public void testRegPost_Success_User() {
        String username = "testuser";
        String password = "password";
        String passwordRepeat = "password";
        String fio = "Test User";
        when(userRepo.findByUsername(username)).thenReturn(null);
        when(userRepo.findAll()).thenReturn(Collections.singletonList(new AppUser()));

        String viewName = regCont.reg(model, username, password, passwordRepeat, fio);

        verify(userRepo).save(any(AppUser.class));
        assertEquals("redirect:/login", viewName);
    }
}
@RunWith(MockitoJUnitRunner.class)
public class StatContTest {

    @InjectMocks
    private StatCont statCont;

    @Mock
    private ProjectAppRepo projectAppRepo;

    @Mock
    private Model model;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStat() {
        ProjectAppStatus[] appStatuses = ProjectAppStatus.values();
        List<ProjectApp> projectAppList = Collections.singletonList(new ProjectApp());

        for (ProjectAppStatus status : appStatuses) {
            when(projectAppRepo.findAllByStatus(status)).thenReturn(projectAppList);
        }

        String viewName = statCont.stat(model);

        verify(statCont).getCurrentUserAndRole(model);
        verify(model).addAttribute(eq("appStatusesNames"), any(String[].class));
        verify(model).addAttribute(eq("appStatusesValues"), any(int[].class));
        assertEquals("stat", viewName);

        for (ProjectAppStatus status : appStatuses) {
            verify(projectAppRepo).findAllByStatus(status);
        }
    }
}

@RunWith(MockitoJUnitRunner.class)
public class UserContTest {

    @InjectMocks
    private UserCont userCont;

    @Mock
    private UserRepo userRepo;

    @Mock
    private Model model;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUsers() {
        List<AppUser> userList = Collections.singletonList(new AppUser());
        when(userRepo.findAll()).thenReturn(userList);

        String viewName = userCont.users(model);

        verify(userCont).getCurrentUserAndRole(model);
        verify(model).addAttribute("users", userList);
        verify(model).addAttribute("roles", Role.values());
        assertEquals("users", viewName);
    }

    @Test
    public void testEdit() {
        Long id = 1L;
        Role role = Role.ADMIN;
        AppUser user = new AppUser();
        when(userRepo.getReferenceById(id)).thenReturn(user);

        String viewName = userCont.edit(id, role);

        verify(user).setRole(role);
        verify(userRepo).save(user);
        assertEquals("redirect:/users", viewName);
    }

    @Test
    public void testDelete() {
        Long id = 1L;

        String viewName = userCont.delete(id);

        verify(userRepo).deleteById(id);
        assertEquals("redirect:/users", viewName);
    }

    @Test
    public void testEnabled() {
        Long id = 1L;
        AppUser user = new AppUser();
        when(userRepo.getReferenceById(id)).thenReturn(user);

        String viewName = userCont.enabled(id);

        verify(user).setEnabled(!user.isEnabled());
        verify(userRepo).save(user);
        assertEquals("redirect:/users", viewName);
    }
}