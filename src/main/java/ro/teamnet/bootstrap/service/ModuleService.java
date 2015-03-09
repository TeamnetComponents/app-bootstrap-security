package ro.teamnet.bootstrap.service;


import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;
import ro.teamnet.bootstrap.web.rest.dto.ModuleDTO;

public interface ModuleService {

    public void save(Module module);

    public AppPage<Module> findAll(AppPageable appPageable);

    public Module getOne(Long id);

    public void delete(Long id);

    public void update(Module module, ModuleDTO moduleDTO);

}
