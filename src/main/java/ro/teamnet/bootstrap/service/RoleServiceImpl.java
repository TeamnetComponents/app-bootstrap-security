package ro.teamnet.bootstrap.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;
import ro.teamnet.bootstrap.repository.RoleRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * Service class for managing  ModuleRights.
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Inject
    RoleRepository roleRepository;

    public void save(Role role) {
         roleRepository.save(role);
    }

    public AppPage<Role> findAll(AppPageable appPageable){
        return roleRepository.findAll(appPageable);
    }

    public Role getOne(Long id) {
        return roleRepository.getOne(id);
    }

    public void delete(Long id) {
        roleRepository.delete(id);
    }
}
