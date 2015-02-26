package ro.teamnet.bootstrap.repository;

import ro.teamnet.bootstrap.domain.EagleAccount;
import ro.teamnet.bootstrap.extend.AppRepository;

/**
 * Spring Data JPA repository for the EagleAccount entity.
 */
public interface EagleUserRepository  extends AppRepository<EagleAccount, Long >{
}
