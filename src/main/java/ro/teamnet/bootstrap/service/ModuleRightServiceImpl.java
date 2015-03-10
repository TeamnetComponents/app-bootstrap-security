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


/**
 * Service class for managing  ModuleRights.
 */
@Service
@Transactional
public class ModuleRightServiceImpl implements ModuleRightService {

    private final Logger log = LoggerFactory.getLogger(ModuleRightServiceImpl.class);

    @Inject
    ModuleRightRepository moduleRightRepository;

    @Override
    public ModuleRight save(ModuleRight moduleRight) {
        return moduleRightRepository.save(moduleRight);
    }

    @Override
    public AppPage<ModuleRight> findAll(AppPageable appPageable){
        return moduleRightRepository.findAll(appPageable);
    }

    @Override
    public ModuleRight getOne(Long id) {
        return moduleRightRepository.getOne(id);
    }

    @Override
    public void delete(Long id) {
        moduleRightRepository.delete(id);
    }

}
