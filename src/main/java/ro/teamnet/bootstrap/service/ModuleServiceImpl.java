package ro.teamnet.bootstrap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;
import ro.teamnet.bootstrap.extend.AppRepository;
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
public class ModuleServiceImpl extends AbstractServiceImpl<Module,Long> implements ModuleService {

    private final Logger log = LoggerFactory.getLogger(ModuleServiceImpl.class);


    private final ModuleRepository moduleRepository;

    private final ModuleRightRepository moduleRightRepository;

    @Inject
    public ModuleServiceImpl(ModuleRepository moduleRepository,ModuleRightRepository moduleRightRepository) {
        super(moduleRepository);
        this.moduleRepository=moduleRepository;
        this.moduleRightRepository=moduleRightRepository;
    }

    @Override
    @Transactional
    public Module save(Module module) {
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
            return moduleDb;

        }else{
            Module savedModule = moduleRepository.save(module);
            for(ModuleRight moduleRight : module.getModuleRights()){
                moduleRight.setModule(savedModule);
                List<ModuleRight> moduleRights = moduleRightRepository.findByModuleAndRight(savedModule, moduleRight.getRight());
                if(moduleRights != null && moduleRights.isEmpty()){
                    moduleRightRepository.save(moduleRight);
                }
            }
            return savedModule;
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
        Module module = moduleRepository.getOne(id);
        List<ModuleRight> moduleRights = moduleRightRepository.findByModule(module);
        for (ModuleRight moduleRight : moduleRights) {
            moduleRight.setModule(null);
        }
        module.setModuleRights(moduleRights);
        return module;
    }

    @Override
    @Transactional

    public void update(Long id, ModuleDTO moduleDTO) {
        Module moduleDb=moduleRepository.findOne(id);
        if(moduleDb == null){
            return;
        }
        moduleDb.setVersion(moduleDTO.getVersion());
        moduleDb.setCode(moduleDTO.getCode());
        moduleDb.setDescription(moduleDTO.getDescription());
        moduleDb.setType(moduleDTO.getType());
        moduleDb.setParentModule(moduleDTO.getParentModule());


        if(moduleDTO.getId()!=null){
            Collection<ModuleRight> removeModulesRights=new HashSet<>();
            Collection<ModuleRight> persistentModuleRight=new HashSet<>();
            Collection<ModuleRight> moduleRightList=moduleDb.getModuleRights();
            for (ModuleRight moduleRight : moduleRightList) {
                boolean found=false;
                for(ModuleRightDTO moduleRightDTO:moduleDTO.getModuleRights()){
                    found=found||moduleRightDTO.getRight().equals(moduleRight.getRight());
                }

                if(!found){

                    removeModulesRights.add(moduleRight);
                    moduleRightRepository.delete(moduleRight);
                    //moduleRight.setModule(null);
//                  moduleRightRepository.save(moduleRight);
                }
            }
            for(ModuleRightDTO moduleRight:moduleDTO.getModuleRights()){
                boolean found=false;
                for(ModuleRight moduleRight1:moduleDb.getModuleRights()){
                    found=found||moduleRight1.getRight().equals(moduleRight.getRight());
                }
                if(!found){
                    ModuleRight moduleRightTr=new ModuleRight();
                    moduleRightTr.setRight(moduleRight.getRight());
                    moduleRightTr.setModule(moduleDb);
                    moduleRightTr.setVersion(1L);
                    ModuleRight moduleRightDb=moduleRightRepository.save(moduleRightTr);
                    persistentModuleRight.add(moduleRightDb);
                }
            }
            moduleDb.getModuleRights().removeAll(removeModulesRights);
            moduleDb.getModuleRights().addAll(persistentModuleRight);
            moduleRepository.save(moduleDb);

        }


    }

    @Override
    @Transactional
    public void delete(Long id) {
        moduleRepository.delete(id);
    }

}
