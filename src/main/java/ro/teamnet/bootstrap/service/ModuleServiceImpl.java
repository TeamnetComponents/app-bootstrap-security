package ro.teamnet.bootstrap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.domain.RoleBase;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;
import ro.teamnet.bootstrap.extend.AppRepository;
import ro.teamnet.bootstrap.repository.AccountRepository;
import ro.teamnet.bootstrap.repository.ApplicationRoleRepository;
import ro.teamnet.bootstrap.repository.ModuleRepository;
import ro.teamnet.bootstrap.repository.ModuleRightRepository;
import ro.teamnet.bootstrap.web.rest.dto.ModuleDTO;
import ro.teamnet.bootstrap.web.rest.dto.ModuleRightDTO;

import javax.inject.Inject;
import java.util.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service class for managing  ModuleRights.
 */
@Service
@Transactional(value="jpaTransactionManager", readOnly = true)
public class ModuleServiceImpl extends AbstractServiceImpl<Module,Long> implements ModuleService {

    private final Logger log = LoggerFactory.getLogger(ModuleServiceImpl.class);


    private final ModuleRepository moduleRepository;

    private final ModuleRightRepository moduleRightRepository;

    private final AccountRepository accountRepository;


    @Inject
    public ModuleServiceImpl(ModuleRepository moduleRepository,ModuleRightRepository moduleRightRepository, AccountRepository accountRepository) {
        super(moduleRepository);
        this.moduleRepository=moduleRepository;
        this.moduleRightRepository=moduleRightRepository;
        this.accountRepository=accountRepository;
    }

    @Override
    @Transactional(value="jpaTransactionManager")
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
    public Set<Module> getAllModulesWithModuleRights() {
        return moduleRepository.getAllModulesWithModuleRights();
    }

    @Override
    public Module getOne(Long id) {
        Module module = moduleRepository.getOne(id);
        Set<ModuleRight> moduleRights = moduleRightRepository.findByModule(module);
        for (ModuleRight moduleRight : moduleRights) {
            moduleRight.setModule(null);
        }
        module.setModuleRights(moduleRights);
        return module;
    }

    @Override
    @Transactional(value="jpaTransactionManager")
    public boolean update(Long id, ModuleDTO moduleDTO) {
        Module moduleDb=moduleRepository.findOne(id);
        if(moduleDb == null){
            return false;
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
                    if(accountRepository.findReferencedModuleRights(moduleRight.getId()).size()>0){
                           return false;
                    }

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
                    ModuleRight moduleRightDb=moduleRightRepository.save(moduleRightTr);
                    persistentModuleRight.add(moduleRightDb);
                }
            }

            moduleDb.getModuleRights().removeAll(removeModulesRights);
            moduleDb.getModuleRights().addAll(persistentModuleRight);
            moduleRepository.save(moduleDb);

        }

        return true;
    }
//
//    @Override
//    @Transactional(value="jpaTransactionManager")
//    public Module saveModule(ModuleDTO moduleDTO){
//        Module module = new Module();
//        module.setCode(moduleDTO.getCode());
//        module.setParentModule(moduleDTO.getParentModule());
//        module.setDescription(moduleDTO.getDescription());
//        module.setType(moduleDTO.getType());
//        Collection<ModuleRight> moduleRights = new HashSet<>();
//        for(ModuleRightDTO moduleRightDTO:moduleDTO.getModuleRights()){
//            moduleRights.add(moduleRightDTO.getModuleRight());
//        }
//        module.setModuleRights(moduleRights);
//        return moduleRepository.save(module);
//    }

    @Override
    @Transactional(value="jpaTransactionManager")
    public void delete(Long id) {
        moduleRepository.delete(id);
    }

}
