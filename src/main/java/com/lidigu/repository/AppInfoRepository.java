package com.lidigu.repository;

import com.lidigu.entity.AppInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AppInfoRepository extends JpaRepository<AppInfo, Long> {
    @Query("SELECT a FROM AppInfo a ORDER BY a.id DESC LIMIT 1")
    Optional<AppInfo> findLatest();
}
