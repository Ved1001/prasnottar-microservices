package com.ved.Result_service.Repository;

import com.ved.Result_service.Entity.Result;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@EnableDiscoveryClient
@EnableFeignClients
public interface ResultRepository extends JpaRepository<Result, Long> {

    // This method is required for the "Attempt Limit" check in your Controller
    int countByUserIdAndExamId(Long userId, String examId);
}
