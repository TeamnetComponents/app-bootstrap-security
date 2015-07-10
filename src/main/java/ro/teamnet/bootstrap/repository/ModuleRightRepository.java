package ro.teamnet.bootstrap.repository;

import org.springframework.data.jpa.repository.Query;
import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.extend.AppRepository;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Permission entity.
 */
public interface ModuleRightRepository extends AppRepository<ModuleRight, Long> {

    @Query("select mr from ModuleRight mr join fetch mr.module")
    public Set<ModuleRight> findAllWithModule();

    public Set<ModuleRight> findByModule(Module module);

    public List<ModuleRight> findByModuleAndRight(Module module, Short right);

}
