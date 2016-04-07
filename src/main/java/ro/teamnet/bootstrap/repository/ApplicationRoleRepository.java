package ro.teamnet.bootstrap.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.teamnet.bootstrap.domain.ApplicationRole;
import ro.teamnet.bootstrap.extend.AppRepository;

import java.util.Set;

/**
 * Spring Data JPA repository for the Role entity.
 */
public interface ApplicationRoleRepository extends AppRepository<ApplicationRole, Long> {

    @Query("select r from ApplicationRole r left join fetch r.moduleRights where r.code =:code")
    public ApplicationRole findByCode(@Param("code") String code);

    @Query("select r from ApplicationRole r left join fetch r.moduleRights where r.id =:id")
    ApplicationRole getOneById(@Param("id") Long id);

    @Query("select r from ApplicationRole r left join fetch r.moduleRights")
    Set<ApplicationRole> getAllWithModuleRights();
}
