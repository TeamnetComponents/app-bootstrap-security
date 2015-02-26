package ro.teamnet.bootstrap.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;
import ro.teamnet.bootstrap.repository.ModuleRightRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Service
@Transactional
public class ModuleRightServiceImpl implements ModuleRightService {

    private final Logger log = LoggerFactory.getLogger(ModuleRightServiceImpl.class);

    @Inject
    ModuleRightRepository moduleRightRepository;

    public Boolean save(ModuleRight moduleRight) {
        ModuleRight savedModuleRight = moduleRightRepository.save(moduleRight);
        if(savedModuleRight != null){
            return true;
        }
        return false;
    }

    public AppPage<ModuleRight> findAll(AppPageable appPageable){
        return moduleRightRepository.findAll(appPageable);
    }

    public ModuleRight getOne(Long id) {
        return moduleRightRepository.getOne(id);
    }

    public void delete(Long id) {
        moduleRightRepository.delete(id);
    }

}
