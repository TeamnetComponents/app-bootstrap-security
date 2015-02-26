package ro.teamnet.bootstrap.service;


import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;

public interface RoleService {

    public Boolean save(Role role);

    public AppPage<Role> findAll(AppPageable appPageable);

    public Role getOne(Long id);

    public void delete(Long id);

}
