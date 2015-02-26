package ro.teamnet.bootstrap.repository;

import ro.teamnet.bootstrap.extend.AppRepository;
import ro.teamnet.bootstrap.domain.Employee;

/**
 * Spring Data JPA repository for the Employee entity.
 */
public interface EmployeeRepository extends AppRepository<Employee, Long> {
}
