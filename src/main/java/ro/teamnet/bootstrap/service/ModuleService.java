package ro.teamnet.bootstrap.service;


import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;
import ro.teamnet.bootstrap.web.rest.dto.ModuleDTO;

import java.util.List;

public interface ModuleService extends AbstractService<Module,Long>{

    public Module getOne(Long id);

    public void update(Module module, ModuleDTO moduleDTO);

    public List<Module> getAllModulesWithModuleRights();

}
