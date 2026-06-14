package org.example.backend.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.backend.dto.SummaryResponse;
import org.example.backend.dto.TransactionRequest;
import org.example.backend.dto.TransactionResponse;
import org.example.backend.entity.User;
import org.example.backend.service.SessionUserService;
import org.example.backend.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionService transactionService;
    private final SessionUserService sessionUserService;

    public TransactionController(TransactionService transactionService, SessionUserService sessionUserService) {
        this.transactionService = transactionService;
        this.sessionUserService = sessionUserService;
    }

    @PostMapping
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionRequest request, HttpSession session) {
        User user = sessionUserService.getLoggedInUser(session);
        if (user == null) {
            return ResponseEntity.status(401).body("User belum login");
        }

        TransactionResponse response = transactionService.createTransaction(request, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllTransactions(HttpSession session) {
        User user = sessionUserService.getLoggedInUser(session);
        if (user == null) {
            return ResponseEntity.status(401).body("User belum login");
        }

        List<TransactionResponse> responses = transactionService.getAllTransactions(user);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchTransactions(@RequestParam String keyword, HttpSession session) {
        User user = sessionUserService.getLoggedInUser(session);
        if (user == null) {
            return ResponseEntity.status(401).body("User belum login");
        }

        return ResponseEntity.ok(transactionService.searchTransactions(user, keyword));
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getSummary(HttpSession session) {
        User user = sessionUserService.getLoggedInUser(session);
        if (user == null) {
            return ResponseEntity.status(401).body("User belum login");
        }

        SummaryResponse response = transactionService.getSummary(user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id, HttpSession session) {
        User user = sessionUserService.getLoggedInUser(session);
        if (user == null) {
            return ResponseEntity.status(401).body("User belum login");
        }

        boolean deleted = transactionService.deleteTransaction(user, id);

        if (deleted) {
            return ResponseEntity.ok("Transaksi berhasil dihapus");
        }

        return ResponseEntity.badRequest().body("Transaksi tidak ditemukan");
    }
}