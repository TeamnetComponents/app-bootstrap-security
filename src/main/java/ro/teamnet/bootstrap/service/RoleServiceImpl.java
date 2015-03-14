package ro.teamnet.bootstrap.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;
import ro.teamnet.bootstrap.extend.AppRepository;
import ro.teamnet.bootstrap.repository.RoleRepository;
import ro.teamnet.bootstrap.web.rest.dto.ModuleRightDTO;
import ro.teamnet.bootstrap.web.rest.dto.RoleDTO;

import javax.inject.Inject;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing  ModuleRights.
 */
@Service
@Transactional
public class RoleServiceImpl extends AbstractServiceImpl<Role,Long> implements RoleService {


    private final RoleRepository roleRepository;

    private final ModuleRightService moduleRightService;

    @Inject
    public RoleServiceImpl(RoleRepository roleRepository,ModuleRightService moduleRightService) {
        super(roleRepository);
        this.roleRepository=roleRepository;
        this.moduleRightService=moduleRightService;
    }


    @Override
    public Role getOne(Long id) {
        return roleRepository.getOne(id);
    }


    @Override
    public Role update(Role role) {
        for(ModuleRight mr: role.getModuleRights()) {
            moduleRightService.save(mr);
        }

        return roleRepository.save(role);
    }

    @Override
    public void update(Role role, RoleDTO roleDTO) {

        role.setCode(roleDTO.getCode());
        role.setDescription(roleDTO.getDescription());
        role.setOrder(roleDTO.getOrder());
        role.setValidFrom(roleDTO.getValidFrom());
        role.setValidTo(roleDTO.getValidTo());
        role.setActive(roleDTO.getActive());
        role.setLocal(roleDTO.getLocal());

        //update moduleRights for Role
        List<ModuleRight> moduleRights = new ArrayList<>();
        for(ModuleRightDTO moduleRightDTO : roleDTO.getModuleRights()){
                moduleRights.add(moduleRightService.findOne(moduleRightDTO.getId()));
            }

        role.setModuleRights(moduleRights);
    }

}
