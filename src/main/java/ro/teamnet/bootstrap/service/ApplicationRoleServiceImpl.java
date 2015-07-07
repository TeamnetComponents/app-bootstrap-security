package ro.teamnet.bootstrap.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.teamnet.bootstrap.domain.ApplicationRole;
import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.domain.util.ModuleRightTypeEnum;
import ro.teamnet.bootstrap.repository.ModuleRepository;
import ro.teamnet.bootstrap.repository.ApplicationRoleRepository;
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
public class ApplicationRoleServiceImpl extends AbstractServiceImpl<ApplicationRole,Long> implements RoleService {


    private final ApplicationRoleRepository applicationRoleRepository;

    private final ModuleRightService moduleRightService;

    private final ModuleRepository moduleRepository;

    @Inject
    public ApplicationRoleServiceImpl(ApplicationRoleRepository applicationRoleRepository, ModuleRightService moduleRightService, ModuleRepository moduleRepository) {
        super(applicationRoleRepository);
        this.applicationRoleRepository = applicationRoleRepository;
        this.moduleRightService=moduleRightService;
        this.moduleRepository=moduleRepository;
    }


    @Override
    public ApplicationRole getOne(Long id) {
        return applicationRoleRepository.getOne(id);
    }

    public ApplicationRole getOneById(Long id) {
        return applicationRoleRepository.getOneById(id);
    }


    @Override
    public ApplicationRole update(ApplicationRole applicationRole) {
        for(ModuleRight mr: applicationRole.getModuleRights()) {
            if(mr.getId()==null){
                Module moduleDb=moduleRepository.findOne(mr.getModule().getId());
                mr.setModule(null);
                moduleRightService.save(mr);
                mr.setModule(moduleDb);
            }else{
                moduleRightService.save(mr);
            }

        }

        return applicationRoleRepository.save(applicationRole);
    }

    @Override
    public ApplicationRole update(ApplicationRole applicationRole, RoleDTO roleDTO) {

        applicationRole.setCode(roleDTO.getCode());
        applicationRole.setDescription(roleDTO.getDescription());
        applicationRole.setOrder(roleDTO.getOrder());
        applicationRole.setValidFrom(roleDTO.getValidFrom());
        applicationRole.setValidTo(roleDTO.getValidTo());
        applicationRole.setActive(roleDTO.getActive());
        applicationRole.setLocal(roleDTO.getLocal());

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

        applicationRole.setModuleRights(moduleRights);

        return applicationRoleRepository.save(applicationRole);
    }

    @Override
    public Set<ApplicationRole> getAllWithModuleRights() {
        return applicationRoleRepository.getAllWithModuleRights();
    }

    @Override
    public Boolean updateRoleById(Long id, RoleDTO roleDTO) {
        ApplicationRole applicationRole = this.getOne(id);
        if (applicationRole == null) {
            return false;
        }
        this.update(applicationRole, roleDTO);
        return true;
    }
}
