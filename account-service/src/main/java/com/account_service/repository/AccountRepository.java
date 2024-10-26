package com.account_service.repository;

import com.account_service.model.AccountType;
import com.account_service.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Find an account by user ID and account type.
     *
     * @param userId The ID of the user.
     * @param accountType The type of the account.
     * @return An optional containing the account if found, or empty if not found.
     */
    Optional<Account> findAccountByUserIdAndAccountType(Long userId, AccountType accountType);

    /**
     * Find an account by its account number.
     *
     * @param accountNumber The account number to search for.
     * @return An optional containing the found account, or an empty optional if no account was found.
     */
    Optional<Account> findAccountByAccountNumber(String accountNumber);

    /**
     * Finds an account by user ID.
     *
     * @param userId the ID of the user
     * @return an optional account object
     */
    Optional<Account> findAccountByUserId(Long userId);

    /**
     * Finds all accounts by user ID.
     *
     * @param userId the ID of the account
     * @return a list of account objects
     */
    List<Account> findAccountsByUserId(Long userId);
}
