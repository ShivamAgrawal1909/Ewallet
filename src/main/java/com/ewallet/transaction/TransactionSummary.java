package com.ewallet.transaction;

import lombok.Data;
import java.io.Serializable;

@Data
public class TransactionSummary implements Serializable {
    private Double currentBalance;
    private Double totalAdded;
    private Double totalSent;
    private Double totalReceived;
    private Long totalTransactions;
}
