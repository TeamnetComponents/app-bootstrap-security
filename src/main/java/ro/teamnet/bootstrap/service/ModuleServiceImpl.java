package ro.teamnet.bootstrap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;
import ro.teamnet.bootstrap.repository.ModuleRepository;
import ro.teamnet.bootstrap.web.rest.dto.ModuleDTO;
import ro.teamnet.bootstrap.web.rest.dto.ModuleRightDTO;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing  ModuleRights.
 */
@Service
@Transactional
public class ModuleServiceImpl implements ModuleService {

    private final Logger log = LoggerFactory.getLogger(ModuleServiceImpl.class);

    @Inject
    ModuleRepository moduleRepository;

    @Inject
    ModuleRightService moduleRightService;

    @Override
    public void save(Module module) {
          moduleRepository.save(module);
    }

    @Override
    public AppPage<Module> findAll(AppPageable appPageable) {
        return moduleRepository.findAll(appPageable);
    }

    @Override
    public Module getOne(Long id) {
        return moduleRepository.getOne(id);
    }

    @Override
    public void delete(Long id) {
        moduleRepository.delete(id);
    }

    @Override
    public void update(Module module, ModuleDTO moduleDTO){
        module.setVersion(moduleDTO.getVersion());
        module.setCode(moduleDTO.getCode());
        module.setDescription(moduleDTO.getDescription());
        module.setType(moduleDTO.getType());
        module.setParentModule(moduleDTO.getParentModule());

        //update moduleRights for module
        List<ModuleRight> moduleRights = new ArrayList<>();
        for(ModuleRightDTO moduleRightDTO : moduleDTO.getModuleRights()){
            moduleRights.add(moduleRightService.getOne(moduleRightDTO.getId()));
        }

        module.setModuleRights(moduleRights);
    }
}
