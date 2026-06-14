package org.example.backend.repository;

import org.example.backend.entity.Transaction;
import org.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByUserOrderByDateDescIdDesc(User user);

    Optional<Transaction> findByIdAndUser(Long id, User user);

    List<Transaction> findByUserAndDescriptionContainingIgnoreCaseOrUserAndDetailContainingIgnoreCaseOrderByDateDescIdDesc(
            User user1,
            String description,
            User user2,
            String detail
    );

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user = :user AND t.transactionType = 'INCOME'")
    Double getTotalIncome(User user);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user = :user AND t.transactionType = 'EXPENSE'")
    Double getTotalExpense(User user);
}
