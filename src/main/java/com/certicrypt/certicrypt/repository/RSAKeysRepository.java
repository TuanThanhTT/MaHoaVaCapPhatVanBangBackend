package com.certicrypt.certicrypt.repository;

import com.certicrypt.certicrypt.models.RSAKeys;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RSAKeysRepository extends JpaRepository<RSAKeys, Integer> {

    List<RSAKeys> findRSAKeysByDegree_DegreeId(Integer degreeId);
}
