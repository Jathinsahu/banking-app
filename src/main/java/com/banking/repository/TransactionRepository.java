package com.banking.repository;

import com.banking.entity.Transaction;
import com.banking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Transaction entity
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    /**
     * Find all transactions for a user (both sent and received) ordered by timestamp descending
     * @param user the user to find transactions for
     * @return list of transactions
     */
    @Query("SELECT t FROM Transaction t WHERE t.sender = :user OR t.receiver = :user ORDER BY t.timestamp DESC")
    List<Transaction> findAllByUser(@Param("user") User user);
    
    /**
     * Find transactions for a user within a date range
     * @param user the user to find transactions for
     * @param fromDate start date
     * @param toDate end date
     * @return list of transactions within the date range
     */
    @Query("SELECT t FROM Transaction t WHERE (t.sender = :user OR t.receiver = :user) " +
           "AND t.timestamp >= :fromDate AND t.timestamp <= :toDate ORDER BY t.timestamp DESC")
    List<Transaction> findByUserAndDateRange(@Param("user") User user, 
                                           @Param("fromDate") LocalDateTime fromDate, 
                                           @Param("toDate") LocalDateTime toDate);
    
    /**
     * Find all transactions sent by a user
     * @param sender the sender user
     * @return list of sent transactions
     */
    List<Transaction> findBySenderOrderByTimestampDesc(User sender);
    
    /**
     * Find all transactions received by a user
     * @param receiver the receiver user
     * @return list of received transactions
     */
    List<Transaction> findByReceiverOrderByTimestampDesc(User receiver);
}