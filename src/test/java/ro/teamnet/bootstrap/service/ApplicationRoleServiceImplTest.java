package ro.teamnet.bootstrap.service;


import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ro.teamnet.bootstrap.domain.ApplicationRole;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageImpl;
import ro.teamnet.bootstrap.extend.AppPageRequest;
import ro.teamnet.bootstrap.extend.Filters;
import ro.teamnet.bootstrap.repository.ModuleRepository;
import ro.teamnet.bootstrap.repository.ApplicationRoleRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ApplicationRoleServiceImplTest {

    /**
     * Role Service
     */
    @InjectMocks
    private ApplicationRoleServiceImpl service;

    @Mock
    private ApplicationRoleRepository applicationRoleRepository;

    @Mock
    private ModuleRightService moduleRightService;

    @Mock
    private ModuleRepository moduleRepository;

    @Before
    public void init(){
        initMocks(this);
        service = new ApplicationRoleServiceImpl(applicationRoleRepository, moduleRightService, moduleRepository);
    }

    /**
    * Method: {@link ApplicationRoleServiceImpl#save(java.io.Serializable)} (ro.teamnet.bootstrap.domain.Role)} .
    * When: A Role is saved
    * Then:
    */
    @Test
    public void saveRoleTest(){

        final Long id = 1l;
        final ApplicationRole applicationRole = new ApplicationRole();
        applicationRole.setId(id);
        applicationRole.setCode("ROLE_USER");

        service.save(applicationRole);
        verify(applicationRoleRepository, times(1)).save(applicationRole);

    }

    /**
     * Method: {@link ApplicationRoleServiceImpl#findAll(ro.teamnet.bootstrap.extend.AppPageable)} .
     * When: find all roles
     * Then:
     */
    @Test
    public void findAllTest(){

        final Long id = 1l;
        final ApplicationRole applicationRole = new ApplicationRole();
        applicationRole.setId(id);
        applicationRole.setCode("ROLE_USER");
        List<ApplicationRole> applicationRoles = new ArrayList<>();
        applicationRoles.add(applicationRole);

        AppPage appPage = new AppPageImpl(applicationRoles);

        AppPageRequest appPageRequest = new AppPageRequest(0,1,new Filters());

        when(applicationRoleRepository.findAll(appPageRequest)).thenReturn(appPage);
        assertEquals(appPage,service.findAll(appPageRequest) );

    }

    /**
     * Method: {@link ApplicationRoleServiceImpl#getOne(Long)} .
     * When: find one Role
     * Then:
     */
    @Test
    public void getOneTest(){

        final Long id = 1l;
        final ApplicationRole applicationRole = new ApplicationRole();
        applicationRole.setId(id);
        applicationRole.setCode("ROLE_USER");

        when(applicationRoleRepository.getOne(anyLong())).thenReturn(applicationRole);
        assertEquals(applicationRole,service.getOne(id));

    }

    /**
     * Method: {@link ApplicationRoleServiceImpl#delete(java.io.Serializable)}(Long)} .
     * When: Delete one Role
     * Then: Id Role is not found throws {@Link EmptyResultDataAccessException}
     */
    @Test
    public void deleteTest(){

        final Long id = 1l;
        final ApplicationRole applicationRole = new ApplicationRole();
        applicationRole.setId(id);
        applicationRole.setCode("ROLE_USER");

        service.delete(id);
        verify(applicationRoleRepository, times(1)).delete(id);

    }

}
