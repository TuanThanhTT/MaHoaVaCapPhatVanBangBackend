package com.certicrypt.certicrypt.repository;

import com.certicrypt.certicrypt.models.DegreeImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DegreeImgRepository extends JpaRepository<DegreeImg, Integer> {

    DegreeImg findDegreeImgByDegree_DegreeId(Integer degreeId);

    List<DegreeImg> findAllByDegree_DegreeId(Integer degreeDegreeId);
}
