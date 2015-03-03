package ro.teamnet.bootstrap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;
import ro.teamnet.bootstrap.repository.ModuleRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * Service class for managing  ModuleRights.
 */
@Service
@Transactional
public class ModuleServiceImpl implements ModuleService {

    private final Logger log = LoggerFactory.getLogger(ModuleServiceImpl.class);

    @Inject
    ModuleRepository moduleRepository;

    @Override
    public Boolean save(Module module) {
        Module savedModule = moduleRepository.save(module);
        if(savedModule != null){
            return true;
        }
        return false;
    }

    @Override
    public AppPage<Module> findAll(AppPageable appPageable) {
        return moduleRepository.findAll(appPageable);
    }

    @Override
    public Module getOne(Long id) {
        return moduleRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        moduleRepository.delete(id);
    }
}
