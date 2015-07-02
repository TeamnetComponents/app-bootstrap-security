package ro.teamnet.bootstrap.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.repository.ModuleRepository;
import ro.teamnet.bootstrap.repository.RoleRepository;
import ro.teamnet.bootstrap.web.rest.dto.ModuleRightDTO;
import ro.teamnet.bootstrap.web.rest.dto.RoleDTO;

import javax.inject.Inject;
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

    private final ModuleRepository moduleRepository;

    @Inject
    public RoleServiceImpl(RoleRepository roleRepository,ModuleRightService moduleRightService,ModuleRepository moduleRepository) {
        super(roleRepository);
        this.roleRepository=roleRepository;
        this.moduleRightService=moduleRightService;
        this.moduleRepository=moduleRepository;
    }


    @Override
    public Role getOne(Long id) {
        return roleRepository.getOne(id);
    }


    @Override
    public Role update(Role role) {
        for(ModuleRight mr: role.getModuleRights()) {
            if(mr.getId()==null){
                Module moduleDb=moduleRepository.findOne(mr.getModule().getId());
                mr.setModule(null);
                moduleRightService.save(mr);
                mr.setModule(moduleDb);
            }else{
                moduleRightService.save(mr);
            }

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

    @Override
    public Boolean updateRoleById(Long id, RoleDTO roleDTO) {
        Role role = this.getOne(id);
        if (role == null) {
            return false;
        }
        this.update(role, roleDTO);
        return true;
    }
}
