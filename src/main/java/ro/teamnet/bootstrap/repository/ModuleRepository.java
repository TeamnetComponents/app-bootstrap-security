package ro.teamnet.bootstrap.repository;


import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.extend.AppRepository;

/**
 * Spring Data JPA repository for the Module entity.
 */
public interface ModuleRepository extends AppRepository<Module, Long> {
}
