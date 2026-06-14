package org.example.backend.service;

import org.example.backend.dto.SummaryResponse;
import org.example.backend.dto.TransactionRequest;
import org.example.backend.dto.TransactionResponse;
import org.example.backend.entity.Transaction;
import org.example.backend.entity.User;
import org.example.backend.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionResponse createTransaction(TransactionRequest request, User user) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(request.getTransactionType().trim().toUpperCase());
        transaction.setAmount(request.getAmount());
        transaction.setDate(request.getDate());
        transaction.setDescription(request.getDescription());
        transaction.setDetail(request.getDetail());
        transaction.setUser(user);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapToResponse(savedTransaction);
    }

    public List<TransactionResponse> getAllTransactions(User user) {
        return transactionRepository.findAllByUserOrderByDateDescIdDesc(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TransactionResponse> searchTransactions(User user, String keyword) {
        return transactionRepository
                .findByUserAndDescriptionContainingIgnoreCaseOrUserAndDetailContainingIgnoreCaseOrderByDateDescIdDesc(
                        user, keyword, user, keyword
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public boolean deleteTransaction(User user, Long transactionId) {
        Optional<Transaction> transaction = transactionRepository.findByIdAndUser(transactionId, user);

        if (transaction.isEmpty()) {
            return false;
        }

        transactionRepository.deleteById(transactionId);
        return true;
    }

    public SummaryResponse getSummary(User user) {
        Double totalIncome = transactionRepository.getTotalIncome(user);
        Double totalExpense = transactionRepository.getTotalExpense(user);
        Double balance = totalIncome - totalExpense;

        return new SummaryResponse(totalIncome, totalExpense, balance);
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getDate(),
                transaction.getDescription(),
                transaction.getDetail()
        );
    }
}
