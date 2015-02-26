package ro.teamnet.bootstrap.repository;


import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Query;
import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.extend.AppRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the UserProfile entity.
 */
public interface AccountRepository extends AppRepository<Account, Long> {
    
    @Query("select u from Account u where u.activationKey = ?1")
    public Account getUserByActivationKey(String activationKey);
    
    @Query("select u from Account u where u.activated = false and u.createdDate > ?1")
    public List<Account> findNotActivatedUsersByCreationDateBefore(DateTime dateTime);

    public Account findOneByEmail(String email);

    public Account findByLogin(String login);

}
