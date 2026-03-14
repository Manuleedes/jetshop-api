package com.lidigu.repository;

import com.lidigu.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    List<UserAddress> findByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE UserAddress a SET a.defaultAddress = 0 WHERE a.userId = ?1 AND a.defaultAddress = 1")
    void resetDefaultAddress(Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE UserAddress a SET a.defaultAddress = 0 WHERE a.userId = ?1")
    void resetAllDefaultAddressesForUser(Long userId);
}
