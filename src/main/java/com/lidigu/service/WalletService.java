package com.lidigu.service;

import com.lidigu.entity.User;
import com.lidigu.entity.WalletTransaction;
import com.lidigu.repository.UserRepository;
import com.lidigu.repository.WalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class WalletService {

    @Autowired
    private WalletTransactionRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getWalletHistory(String user_id) {
        Long userId = Long.parseLong(user_id);
        Map<String, Object> response = new HashMap<>();
        List<WalletTransaction> transactions = walletRepository.findByUserIdOrderByCreatedAtDesc(userId);

        response.put("status", "success");
        response.put("flag", "wallet_history_fetched");
        response.put("message", "Wallet history fetched successfully");
        response.put("transactions", transactions);
        return response;
    }

    public Map<String, Object> getReferralHistory(String user_id) {
        Long userId = Long.parseLong(user_id);
        Map<String, Object> response = new HashMap<>();
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            response.put("status", "error");
            response.put("flag", "user_not_found");
            response.put("message", "User not found");
            return response;
        }

        String referralCode = userOpt.get().getReferralCode();
        List<User> referredUsers = userRepository.findByReferredBy(referralCode);

        List<Map<String, Object>> referrals = new ArrayList<>();
        for (User referred : referredUsers) {
            List<WalletTransaction> transactions = walletRepository.findByUserIdOrderByCreatedAtDesc(userId);
            BigDecimal bonusEarned = transactions.stream()
                    .filter(t -> "REFERRAL_BONUS".equalsIgnoreCase(t.getType())
                            && t.getDescription().contains("Referral bonus"))
                    .map(WalletTransaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> refMap = new HashMap<>();
            refMap.put("name", referred.getFirstName() + " " + referred.getLastName());
            refMap.put("email", referred.getEmail());
            refMap.put("joined_at", referred.getCreatedAt());
            refMap.put("bonus_earned", bonusEarned);
            referrals.add(refMap);
        }

        if (referrals.isEmpty()) {
            response.put("status", "error");
            response.put("flag", "no_referrals");
            response.put("message", "No referral history found.");
        } else {
            response.put("status", "success");
            response.put("flag", "referral_history_found");
            response.put("message", "referral history found.");
            response.put("total_referrals", referrals.size());
            response.put("referrals", referrals);
        }
        return response;
    }
}
