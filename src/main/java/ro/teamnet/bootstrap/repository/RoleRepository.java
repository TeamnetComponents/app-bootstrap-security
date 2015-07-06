package ro.teamnet.bootstrap.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.extend.AppRepository;

import java.util.Set;

/**
 * Spring Data JPA repository for the Role entity.
 */
public interface RoleRepository extends AppRepository<Role, Long> {

    public Role findByCode(String code);

    @Query("select r from Role r left join fetch r.moduleRights where r.id =:id")
    Role getOneById(@Param("id") Long id);

    @Query("select r from Role r left join fetch r.moduleRights")
    Set<Role> getAllWithModuleRights();
}
