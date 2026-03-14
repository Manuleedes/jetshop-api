package com.lidigu.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "wallet_transactions")
public class WalletTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_transactions_id")
    private Long walletTransactionsId;

    @Column(name = "wallet_id")
    private Integer walletId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "referred_user_id")
    private Long referredUserId;

    private BigDecimal amount;
    private String type; // REFERRAL_BONUS, ORDER_REFUND, PROMO, credit, debit, etc.

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "balance_after_transaction")
    private BigDecimal balanceAfterTransaction;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
