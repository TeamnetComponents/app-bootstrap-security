package ro.teamnet.bootstrap.repository;

import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.extend.AppRepository;

/**
 * Spring Data JPA repository for the Role entity.
 */
public interface RoleRepository extends AppRepository<Role, Long> {

    public Role findByCode(String code);

}
