package ro.teamnet.bootstrap.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.domain.util.ModuleRightTypeEnum;
import ro.teamnet.bootstrap.repository.ModuleRepository;
import ro.teamnet.bootstrap.repository.RoleRepository;
import ro.teamnet.bootstrap.web.rest.dto.ModuleRightDTO;
import ro.teamnet.bootstrap.web.rest.dto.RoleDTO;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public Role getOneById(Long id) {
        return roleRepository.getOneById(id);
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
    public Role update(Role role, RoleDTO roleDTO) {

        role.setCode(roleDTO.getCode());
        role.setDescription(roleDTO.getDescription());
        role.setOrder(roleDTO.getOrder());
        role.setValidFrom(roleDTO.getValidFrom());
        role.setValidTo(roleDTO.getValidTo());
        role.setActive(roleDTO.getActive());
        role.setLocal(roleDTO.getLocal());

        //update moduleRights for Role
        List<ModuleRight> moduleRights = new ArrayList<>();
        for(ModuleRightDTO mrDTO : roleDTO.getModuleRights()) {
            if(mrDTO.getId() != null) {
                moduleRights.add(moduleRightService.findOne(mrDTO.getId()));
            } else {
                Module module = moduleRepository.findOne(mrDTO.getModule().getId());
                Short right = ModuleRightTypeEnum.READ_ACCESS.getRight();

                moduleRights.addAll(moduleRightService.findByModuleAndRight(module, right));
            }
        }

        role.setModuleRights(moduleRights);

        return roleRepository.save(role);
    }

    @Override
    public Set<Role> getAllWithModuleRights() {
        return roleRepository.getAllWithModuleRights();
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
