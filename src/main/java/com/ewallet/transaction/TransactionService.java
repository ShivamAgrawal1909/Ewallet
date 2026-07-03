package com.ewallet.transaction;

import com.ewallet.user.User;
import com.ewallet.user.UserRepository;
import com.ewallet.wallet.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

  
    public Transaction saveTransaction(Long fromUserId,
                                       Long toUserId,
                                       Double amount,
                                       String type,
                                       String status) {

        Transaction transaction = new Transaction();
        transaction.setFromUserId(fromUserId);
        transaction.setToUserId(toUserId);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setStatus(status);

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getMyTransactions(Long userId) {

        List<Transaction> sent = transactionRepository.findByFromUserId(userId);
        List<Transaction> received = transactionRepository.findByToUserId(userId);

        sent.addAll(received);
        return sent;
    }

  
    public List<TransactionResponse> getMyTransactionResponses(Long userId) {

        List<Transaction> transactions = getMyTransactions(userId);

        Set<Long> userIds = new HashSet<>();
        for (Transaction t : transactions) {
            if (t.getFromUserId() != null) userIds.add(t.getFromUserId());
            if (t.getToUserId() != null) userIds.add(t.getToUserId());
        }

        Map<Long, User> userMap = userRepository.findAllById(userIds)
                .stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        return transactions.stream()
                .map(t -> buildResponse(t, userMap))
                .toList();
    }

    private TransactionResponse buildResponse(Transaction t, Map<Long, User> userMap) {

        TransactionResponse response = new TransactionResponse();
        response.setId(t.getId());
        response.setFromUserId(t.getFromUserId());
        response.setToUserId(t.getToUserId());
        response.setAmount(t.getAmount());
        response.setType(t.getType());
        response.setStatus(t.getStatus());
        response.setCreatedAt(t.getCreatedAt());

        User sender = userMap.get(t.getFromUserId());
        if (sender != null) {
            response.setSenderName(sender.getName());
            response.setSenderEmail(sender.getEmail());
        }

        User receiver = userMap.get(t.getToUserId());
        if (receiver != null) {
            response.setReceiverName(receiver.getName());
            response.setReceiverEmail(receiver.getEmail());
        }

        return response;
    }

    public TransactionSummary getSummary(Long userId) {

        double totalAdded = transactionRepository.sumTotalAddedByUserId(userId);
        double totalSent = transactionRepository.sumTotalSentByUserId(userId);
        double totalReceived = transactionRepository.sumTotalReceivedByUserId(userId);
        long totalTransactions = transactionRepository.countTotalTransactionsByUserId(userId);

        double currentBalance = walletRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"))
                .getBalance();

        TransactionSummary summary = new TransactionSummary();
        summary.setCurrentBalance(currentBalance);
        summary.setTotalAdded(totalAdded);
        summary.setTotalSent(totalSent);
        summary.setTotalReceived(totalReceived);
        summary.setTotalTransactions(totalTransactions);

        return summary;
    }
}
