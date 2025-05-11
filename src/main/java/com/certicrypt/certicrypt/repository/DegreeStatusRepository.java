package com.certicrypt.certicrypt.repository;

import com.certicrypt.certicrypt.models.DegreeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DegreeStatusRepository  extends JpaRepository<DegreeStatus, Integer> {

    @Query("SELECT d FROM DegreeStatus d WHERE d.statusName = :name")
    DegreeStatus findDegreeStatusByName(@Param("name") String name);



}
