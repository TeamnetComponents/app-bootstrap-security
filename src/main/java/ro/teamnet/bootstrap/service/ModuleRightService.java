package ro.teamnet.bootstrap.service;


import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.domain.ModuleRight;

import java.util.List;

public interface ModuleRightService extends AbstractService<ModuleRight,Long>{

    public List<ModuleRight> findAll();

    public List getModuleRightCodes();

    public List<ModuleRight> findByModuleAndRight(Module module, Short right);
}
