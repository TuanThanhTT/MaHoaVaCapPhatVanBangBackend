package com.certicrypt.certicrypt.service.impl;

import com.certicrypt.certicrypt.DTO.response.QueryDegreeVerify;
import com.certicrypt.certicrypt.mapper.QueryDegreeVerifyMapper;
import com.certicrypt.certicrypt.models.Degree;
import com.certicrypt.certicrypt.models.DegreeQueryLog;
import com.certicrypt.certicrypt.models.User;
import com.certicrypt.certicrypt.repository.DegreeQueryLogRepository;
import com.certicrypt.certicrypt.repository.DegreeRepository;
import com.certicrypt.certicrypt.service.DegreeQuerylogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DegreeQuerylogImpl  implements DegreeQuerylogService {

    private final DegreeQueryLogRepository degreeQueryLogRepository;
    private final DegreeRepository degreeRepository;

    public DegreeQuerylogImpl(DegreeQueryLogRepository degreeQueryLogRepository, DegreeRepository degreeRepository) {
        this.degreeQueryLogRepository = degreeQueryLogRepository;
        this.degreeRepository = degreeRepository;
    }


    @Override
    public DegreeQueryLog addDegreeQueryLog(User user, Integer degreeID, HttpServletRequest  request, String status) {
        try{
            String clientIp = getClientIpAddress(request);
            String deviceInfo = request.getHeader("User-Agent");
            if (deviceInfo == null || deviceInfo.isEmpty()) {
                deviceInfo = "Unknown";
            }

            DegreeQueryLog degreeQueryLog = new DegreeQueryLog();
            Degree degree = degreeRepository.findById(degreeID).orElse(null);
            if (degree == null) {
                throw  new RuntimeException("Van bang rong");
            }
            degreeQueryLog.setDegree(degree);
            degreeQueryLog.setQueryTime(LocalDateTime.now());
            degreeQueryLog.setQueryStatus(status);
            degreeQueryLog.setIpAddress(clientIp);
            degreeQueryLog.setDeviceInfo(deviceInfo);
            degreeQueryLog.setUploadedImage("");
            degreeQueryLog.setUser(user);

            return degreeQueryLogRepository.save(degreeQueryLog);

        }catch (Exception e){
            throw  new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Page<QueryDegreeVerify> getAllDegreeQueryLog(Pageable pageable) {
        try {
            Page<DegreeQueryLog> degreeQueryLogs = degreeQueryLogRepository.findAll(pageable);

            // Map từng DegreeQueryLog -> QueryDegreeVerify
            Page<QueryDegreeVerify> queryDegreeVerifies = degreeQueryLogs.map(QueryDegreeVerifyMapper::dto);

            return queryDegreeVerifies;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        // Kiểm tra header X-Forwarded-For (dùng khi có proxy/load balancer)
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress != null && !ipAddress.isEmpty() && !"unknown".equalsIgnoreCase(ipAddress)) {
            // X-Forwarded-For có thể chứa nhiều IP (client, proxy1, proxy2,...), lấy IP đầu tiên
            return ipAddress.split(",")[0].trim();
        }

        // Kiểm tra các header khác
        ipAddress = request.getHeader("Proxy-Client-IP");
        if (ipAddress != null && !ipAddress.isEmpty() && !"unknown".equalsIgnoreCase(ipAddress)) {
            return ipAddress;
        }

        ipAddress = request.getHeader("WL-Proxy-Client-IP");
        if (ipAddress != null && !ipAddress.isEmpty() && !"unknown".equalsIgnoreCase(ipAddress)) {
            return ipAddress;
        }

        // Nếu không có header proxy, lấy IP trực tiếp
        return request.getRemoteAddr();
    }


}
