package ro.teamnet.bootstrap.service;


import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;

import java.util.List;

public interface ModuleRightService {

    public ModuleRight save(ModuleRight moduleRight);

    public AppPage<ModuleRight> findAll(AppPageable appPageable);

    public ModuleRight getOne(Long id);

    public void delete(Long id);

    public List getModuleRightCodes();
}
