package ro.teamnet.bootstrap.service;


import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.extend.*;
import ro.teamnet.bootstrap.repository.ModuleRepository;
import ro.teamnet.bootstrap.repository.ModuleRightRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ModuleServiceImplTest {

    /**
     * Module Service
     */
    @InjectMocks
    private ModuleServiceImpl service;

    @Mock
    private ModuleRepository moduleRepository;

    @Mock
    private ModuleRightRepository moduleRightRepository;

    @Before
    public void init(){
        initMocks(this);
        service = new ModuleServiceImpl(moduleRepository, moduleRightRepository);
    }

    /**
     * Method: {@link ModuleServiceImpl#save(ro.teamnet.bootstrap.domain.Module)} .
     * When: A Role is saved
     * Then:
     */
    @Test
    public void saveModuleTest(){

        final Long id = 1l;
        final Short type = 1;
        final Module module = new Module();
        module.setId(id);
        module.setType(type);

        when(moduleRepository.findOne(module.getId())).thenReturn(module);
        service.save(module);
        verify(moduleRepository, times(1)).save(module);

    }

    /**
     * Method: {@link ModuleServiceImpl#findAll(ro.teamnet.bootstrap.extend.AppPageable)} .
     * When: find all roles
     * Then:
     */
    @Test
    public void findAllTest(){

        final Long id = 1l;
        final Short type = 1;
        final Module module = new Module();
        module.setId(id);
        module.setType(type);
        List<Module> modules = new ArrayList<>();
        modules.add(module);

        AppPage appPage = new AppPageImpl(modules);

        AppPageRequest appPageRequest = new AppPageRequest(0,1,new Filters());

        when(moduleRepository.findAll(appPageRequest)).thenReturn(appPage);
        assertEquals(appPage,service.findAll(appPageRequest) );

    }

    /**
     * Method: {@link ModuleServiceImpl#getOne(Long)} .
     * When: find one Role
     * Then:
     */
    @Test
    public void getOneTest(){

        final Long id = 1l;
        final Short type = 1;
        final Module module = new Module();
        module.setId(id);
        module.setType(type);

        when(moduleRepository.getOne(anyLong())).thenReturn(module);
        assertEquals(module,service.getOne(id));

    }

    /**
     * Method: {@link ModuleServiceImpl#delete(Long)} .
     * When: Delete one Role
     * Then: Id Role is not found throws {@Link EmptyResultDataAccessException}
     */
    @Test
    public void deleteTest(){

        final Long id = 1l;
        final Short type = 1;
        final Module module = new Module();
        module.setId(id);
        module.setType(type);

        service.delete(id);
        verify(moduleRepository, times(1)).delete(id);

    }
}
