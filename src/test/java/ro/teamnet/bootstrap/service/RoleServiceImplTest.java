package ro.teamnet.bootstrap.service;


import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageImpl;
import ro.teamnet.bootstrap.extend.AppPageRequest;
import ro.teamnet.bootstrap.extend.Filters;
import ro.teamnet.bootstrap.repository.ModuleRepository;
import ro.teamnet.bootstrap.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RoleServiceImplTest {

    /**
     * Role Service
     */
    @InjectMocks
    private RoleServiceImpl service;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModuleRightService moduleRightService;

    @Mock
    private ModuleRepository moduleRepository;

    @Before
    public void init(){
        initMocks(this);
        service = new RoleServiceImpl(roleRepository, moduleRightService, moduleRepository);
    }

    /**
    * Method: {@link RoleServiceImpl#save(java.io.Serializable)} (ro.teamnet.bootstrap.domain.Role)} .
    * When: A Role is saved
    * Then:
    */
    @Test
    public void saveRoleTest(){

        final Long id = 1l;
        final Role role = new Role();
        role.setId(id);
        role.setCode("ROLE_USER");

        service.save(role);
        verify(roleRepository, times(1)).save(role);

    }

    /**
     * Method: {@link RoleServiceImpl#findAll(ro.teamnet.bootstrap.extend.AppPageable)} .
     * When: find all roles
     * Then:
     */
    @Test
    public void findAllTest(){

        final Long id = 1l;
        final Role role = new Role();
        role.setId(id);
        role.setCode("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        AppPage appPage = new AppPageImpl(roles);

        AppPageRequest appPageRequest = new AppPageRequest(0,1,new Filters());

        when(roleRepository.findAll(appPageRequest)).thenReturn(appPage);
        assertEquals(appPage,service.findAll(appPageRequest) );

    }

    /**
     * Method: {@link RoleServiceImpl#getOne(Long)} .
     * When: find one Role
     * Then:
     */
    @Test
    public void getOneTest(){

        final Long id = 1l;
        final Role role = new Role();
        role.setId(id);
        role.setCode("ROLE_USER");

        when(roleRepository.getOne(anyLong())).thenReturn(role);
        assertEquals(role,service.getOne(id));

    }

    /**
     * Method: {@link RoleServiceImpl#delete(java.io.Serializable)}(Long)} .
     * When: Delete one Role
     * Then: Id Role is not found throws {@Link EmptyResultDataAccessException}
     */
    @Test
    public void deleteTest(){

        final Long id = 1l;
        final Role role = new Role();
        role.setId(id);
        role.setCode("ROLE_USER");

        service.delete(id);
        verify(roleRepository, times(1)).delete(id);

    }

}
