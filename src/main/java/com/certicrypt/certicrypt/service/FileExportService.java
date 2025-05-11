package com.certicrypt.certicrypt.service;

import com.certicrypt.certicrypt.DTO.response.MessageCheckDegree;
import com.certicrypt.certicrypt.DTO.response.VerifyDegree;
import jakarta.servlet.http.HttpServletRequest;

import java.io.File;

public interface FileExportService {
    String exportDegree(Integer  id);
    MessageCheckDegree checkValidDegree(File fileDegree, HttpServletRequest request);
}
