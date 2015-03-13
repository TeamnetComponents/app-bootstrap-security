package ro.teamnet.bootstrap.repository;

import org.springframework.data.jpa.repository.Query;
import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.extend.AppRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Permission entity.
 */
public interface ModuleRightRepository extends AppRepository<ModuleRight, Long> {

    public List<ModuleRight> findByModule(Module module);

    public List<ModuleRight> findByModuleAndRight(Module module, Short right);
}
