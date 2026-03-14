package com.lidigu.controller;

import com.lidigu.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("wallet_history")
    public Map<String, Object> getWalletHistory(@RequestParam String user_id) {
        return walletService.getWalletHistory(user_id);
    }

    @PostMapping("referral_history")
    public Map<String, Object> getReferralHistory(@RequestParam String user_id) {
        return walletService.getReferralHistory(user_id);
    }
}
