package com.lidigu.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "app_info")
public class AppInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "version_code")
    private Integer versionCode;

    @Column(name = "version_name")
    private String versionName;

    @Column(name = "is_maintenance_mode")
    private Boolean isMaintenanceMode;

    @Column(name = "maintenance_message")
    private String maintenanceMessage;

    @Column(name = "is_update_required")
    private Boolean isUpdateRequired;

    @Column(name = "update_message")
    private String updateMessage;

    @Column(name = "promo_banner_url")
    private String promoBannerUrl;

    @Column(name = "promo_banner_link")
    private String promoBannerLink;

    @Column(name = "app_currency")
    private String appCurrency;

    @Column(name = "razorpay_key")
    private String razorpayKey;

    @Column(name = "support_email")
    private String supportEmail;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "app_language")
    private String appLanguage;

    @Column(name = "show_login_popup")
    private Boolean showLoginPopup;

    @Column(name = "home_notice_message")
    private String homeNoticeMessage;

    @Column(name = "faq_url")
    private String faqUrl;

    @Column(name = "terms_url")
    private String termsUrl;

    @Column(name = "privacy_url")
    private String privacyUrl;
}
