package ro.teamnet.bootstrap.repository;


import org.springframework.data.jpa.repository.Query;
import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.extend.AppRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Module entity.
 */
public interface ModuleRepository extends AppRepository<Module, Long> {

    @Query("select m from Module m")
    public List<Module> getAllModulesWithModuleRights();

}
