package com.lidigu.repository;

import com.lidigu.entity.PrimeMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PrimeMembershipRepository extends JpaRepository<PrimeMembership, Long> {
    Optional<PrimeMembership> findByUserId(Long userId);
}
