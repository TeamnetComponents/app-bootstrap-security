package ro.teamnet.bootstrap.repository;


import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.extend.AppRepository;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the UserProfile entity.
 */
public interface AccountRepository extends AppRepository<Account, Long> {
    
    @Query("select u from Account u where u.activationKey = ?1")
    public Account getUserByActivationKey(String activationKey);
    
    @Query("select u from Account u where u.activated = false and u.createdDate > ?1")
    public List<Account> findNotActivatedUsersByCreationDateBefore(DateTime dateTime);

    public Account findOneByEmail(String email);

    @Query("select u from Account u left join fetch u.roles r left join fetch u.moduleRights left join fetch r.moduleRights where u.login=?1")
    public Account findAllByLogin(String login);

   @Query("select u from Account u left join u.roles r left join u.moduleRights m join r.moduleRights m1 where (m.id = ?1 or m1.id = ?1)")
   public List<Account> findReferencedModuleRights(Long id);

    public Account findByLogin(String login);

    @Query("select u from Account u left join fetch u.roles")
    public Set<Account> findAllEager();

}
