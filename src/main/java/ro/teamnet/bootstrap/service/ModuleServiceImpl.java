package ro.teamnet.bootstrap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;
import ro.teamnet.bootstrap.repository.ModuleRepository;
import ro.teamnet.bootstrap.repository.ModuleRightRepository;
import ro.teamnet.bootstrap.web.rest.dto.ModuleDTO;
import ro.teamnet.bootstrap.web.rest.dto.ModuleRightDTO;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Service class for managing  ModuleRights.
 */
@Service
@Transactional(readOnly = true)
public class ModuleServiceImpl implements ModuleService {

    private final Logger log = LoggerFactory.getLogger(ModuleServiceImpl.class);

    @Inject
    ModuleRepository moduleRepository;

    @Inject
    ModuleRightRepository moduleRightRepository;

    @Override
    @Transactional
    public void save(Module module) {
        if(module.getId()!=null){
            Module moduleDb=moduleRepository.findOne(module.getId());
            Collection<ModuleRight> persistentModuleRight=new HashSet<>();
            Collection<ModuleRight> moduleRightList=moduleDb.getModuleRights();
            for (ModuleRight moduleRight : moduleRightList) {
                if(!module.getModuleRights().contains(moduleRight)){
                    moduleRight.setModule(null);
//                  moduleRightRepository.save(moduleRight);

                }
            }
            for(ModuleRight moduleRight:module.getModuleRights()){

                if(!moduleDb.getModuleRights().contains(moduleRight)){
                    moduleRight.setModule(moduleDb);
                    ModuleRight moduleRightDb=moduleRightRepository.save(moduleRight);
                    persistentModuleRight.add(moduleRightDb);
                }
            }
            moduleDb.getModuleRights().addAll(persistentModuleRight);
            moduleRepository.save(moduleDb);


        }else{
            Module savedModule = moduleRepository.save(module);
            for(ModuleRight moduleRight : module.getModuleRights()){
                moduleRight.setModule(savedModule);
                List<ModuleRight> moduleRights = moduleRightRepository.findByModuleAndRight(savedModule, moduleRight.getRight());
                if(moduleRights != null && moduleRights.isEmpty()){
                    moduleRightRepository.save(moduleRight);
                }
            }
        }



    }

    @Override
    public AppPage<Module> findAll(AppPageable appPageable) {
        return moduleRepository.findAll(appPageable);
    }

    @Override
    public List<Module> getAllModulesWithModuleRights() {
        List<Module> moduleList = moduleRepository.getAllModulesWithModuleRights();
        for (Module module : moduleList) {
            List<ModuleRight> moduleRights = moduleRightRepository.findByModule(module);
            for (ModuleRight moduleRight : moduleRights) {
                moduleRight.setModule(null);
            }

            module.setModuleRights(moduleRightRepository.findByModule(module));
        }
        return moduleList;
    }

    @Override
    public Module getOne(Long id) {
        return moduleRepository.getOne(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        moduleRepository.delete(id);
    }

    @Override
    @Transactional
    public void update(Module module, ModuleDTO moduleDTO){
        module.setVersion(moduleDTO.getVersion());
        module.setCode(moduleDTO.getCode());
        module.setDescription(moduleDTO.getDescription());
        module.setType(moduleDTO.getType());
        module.setParentModule(moduleDTO.getParentModule());

        //update moduleRights for module
        List<ModuleRight> moduleRights = new ArrayList<>();
        for(ModuleRightDTO moduleRightDTO : moduleDTO.getModuleRights()){
            moduleRights.add(moduleRightRepository.getOne(moduleRightDTO.getId()));
        }

        module.setModuleRights(moduleRights);
    }
}
