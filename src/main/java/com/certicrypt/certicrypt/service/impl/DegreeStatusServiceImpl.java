package com.certicrypt.certicrypt.service.impl;

import com.certicrypt.certicrypt.models.DegreeStatus;
import com.certicrypt.certicrypt.repository.DegreeStatusRepository;
import com.certicrypt.certicrypt.service.DegreeStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DegreeStatusServiceImpl  implements DegreeStatusService {

    @Autowired
    private DegreeStatusRepository degreeStatusRepository;


    @Override
    public Page<DegreeStatus> getAll(Pageable pageable) {
        return  degreeStatusRepository.findAll(pageable);
    }

    @Override
    public DegreeStatus addDegreeStatus(DegreeStatus degreeStatus) {
        try{

            if(degreeStatus == null
            || degreeStatus.getStatusName() == null || degreeStatus.getStatusName().isEmpty()
            ){
                throw  new RuntimeException("Trạng thái văn bằng không hợp lệ!");

            }
            //kiem tra xem co trung khong
            DegreeStatus exitDegreeStatus1 = degreeStatusRepository.findDegreeStatusByName(degreeStatus.getStatusName());
            if(exitDegreeStatus1 != null){
                throw new RuntimeException("Trạng thái văn bằng đã tồn tại");
            }else{

                return degreeStatusRepository.save(degreeStatus);

            }



        }catch ( Exception ex ){

            throw new RuntimeException(ex);
        }
    }

    @Override
    public DegreeStatus updateDegreeStatus(DegreeStatus degreeStatus) {
        try{

            if(degreeStatus == null ||
                    degreeStatus.getStatusId() == null
                    || degreeStatus.getStatusName() == null || degreeStatus.getStatusName().isEmpty()
            ){
                throw  new RuntimeException("Trạng thái văn bằng không hợp lệ!");

            }
            //kiem tra xem co trung khong
            DegreeStatus exitDegreeStatus1 = degreeStatusRepository.findDegreeStatusByName(degreeStatus.getStatusName());
            if(exitDegreeStatus1 != null){

                exitDegreeStatus1.setStatusName(degreeStatus.getStatusName());
                if(degreeStatus.getDescription()!= null && !degreeStatus.getDescription().isEmpty()){
                    exitDegreeStatus1.setDescription(degreeStatus.getDescription());
                }
                return degreeStatusRepository.save(exitDegreeStatus1);
            }else{

                throw new RuntimeException("Không tìm tấy trạng thái văn bằng");

            }



        }catch ( Exception ex ){

            throw new RuntimeException(ex);
        }
    }

    @Override
    public DegreeStatus findById(int id) {
       try{

           return degreeStatusRepository.findById(id).orElse(null);


       }catch ( Exception ex ){
           throw new RuntimeException(ex);

       }
    }
}
