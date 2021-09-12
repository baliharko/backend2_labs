package baliharko.labs_solo.persistence;

import baliharko.labs_solo.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountByHolder(String holder);
    boolean existsAccountByHolder(String holder);
}
