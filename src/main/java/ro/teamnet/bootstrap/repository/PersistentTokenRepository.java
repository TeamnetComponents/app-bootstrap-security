package ro.teamnet.bootstrap.repository;

import org.joda.time.LocalDate;
import ro.teamnet.bootstrap.domain.PersistentToken;
import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.extend.AppRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the PersistentToken entity.
 */
public interface PersistentTokenRepository extends AppRepository<PersistentToken, String> {

    public List<PersistentToken> findByAccount(Account account);

    public List<PersistentToken> findByTokenDateBefore(LocalDate localDate);

}
