package ro.teamnet.bootstrap.service;


import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;

public interface ModuleRightService {

    public Boolean save(ModuleRight moduleRight);

    public AppPage<ModuleRight> findAll(AppPageable appPageable);

    public ModuleRight getOne(Long id);

    public void delete(Long id);
}
