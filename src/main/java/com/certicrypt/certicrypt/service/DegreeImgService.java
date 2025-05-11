package com.certicrypt.certicrypt.service;

import com.certicrypt.certicrypt.DTO.request.DegreeImgRequest;
import com.certicrypt.certicrypt.models.DegreeImg;

public interface DegreeImgService {
    DegreeImg addDegreeImg(DegreeImgRequest degreeImgRequest);
}
