package ro.teamnet.bootstrap.service;


import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;
import ro.teamnet.bootstrap.web.rest.dto.RoleDTO;

import java.util.List;

public interface RoleService {

    public void save(Role role);

    public AppPage<Role> findAll(AppPageable appPageable);

    public Role getOne(Long id);

    public void delete(Long id);

    public Role update(Role role);

    public void update(Role role, RoleDTO roleDTO);

}
