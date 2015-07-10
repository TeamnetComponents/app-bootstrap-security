package ro.teamnet.bootstrap.repository;


import org.springframework.data.jpa.repository.Query;
import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.extend.AppRepository;

import java.util.Set;

/**
 * Spring Data JPA repository for the Module entity.
 */
public interface ModuleRepository extends AppRepository<Module, Long> {

    @Query("select m from Module m left join fetch m.moduleRights")
    public Set<Module> getAllModulesWithModuleRights();

}
