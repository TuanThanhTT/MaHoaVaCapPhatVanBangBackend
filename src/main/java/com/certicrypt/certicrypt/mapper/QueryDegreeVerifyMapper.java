package com.certicrypt.certicrypt.mapper;

import com.certicrypt.certicrypt.DTO.response.QueryDegreeVerify;
import com.certicrypt.certicrypt.models.DegreeQueryLog;

import java.time.format.DateTimeFormatter;

public class QueryDegreeVerifyMapper {
    public static QueryDegreeVerify dto(DegreeQueryLog degreeQueryLog) {
        QueryDegreeVerify queryDegreeVerify = new QueryDegreeVerify();
        queryDegreeVerify.setId(degreeQueryLog.getQueryId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        queryDegreeVerify.setTimeQuery(degreeQueryLog.getQueryTime().format(formatter));

        queryDegreeVerify.setStatus(degreeQueryLog.getQueryStatus());
        queryDegreeVerify.setIdAddress(degreeQueryLog.getIpAddress());
        queryDegreeVerify.setDeviceInfo(degreeQueryLog.getDeviceInfo());
        if(degreeQueryLog.getUser() == null){
            queryDegreeVerify.setUserName("Tài khoản khách");
        }else{
            queryDegreeVerify.setUserName(degreeQueryLog.getUser().getUserName());
        }
        queryDegreeVerify.setStudentName(degreeQueryLog.getDegree().getStudent().getFullName());
        return queryDegreeVerify;

    }
}
