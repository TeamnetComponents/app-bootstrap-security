package ro.teamnet.bootstrap.service;


import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;

import java.util.List;

public interface ModuleRightService extends AbstractService<ModuleRight,Long>{

    public List getModuleRightCodes();
}
