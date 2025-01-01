package com.example.alert.service;

import com.example.alert.dtos.AlertRequest;
import com.example.alert.dtos.AlertResponse;
import com.example.alert.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AlertService {
    @Autowired
    private AlertRepository alertRepository;
    public Result<List<AlertResponse>> getAlertsByTimeAndType(AlertRequest alertRequest) {
        LocalDate startDate=alertRequest.getStartDate();
        LocalDate endDate=alertRequest.getStartDate();
        LocalTime startHour=alertRequest.getStartHour();
        LocalTime endHour=alertRequest.getEndHour();
        if(startDate.isAfter(endDate)){
            return  new Result<>(null,"Dữ liệu không hợp lệ",400);
        }
        if(startHour!=null&&endHour!=null){
            if(startDate.equals(endDate)&&startHour.isAfter(endHour)){
                return  new Result<>(null,"Dữ liệu không hợp lệ",400);
            }
        }
        Pageable pageable = PageRequest.of(alertRequest.getPageNumber() , alertRequest.getPageSize());
        List<AlertResponse> alertList = alertRepository.findAlertByTimeAndType( alertRequest.getUserId(),
                alertRequest.getStartDate(),
                alertRequest.getEndDate(),
                alertRequest.getStartHour(),
                alertRequest.getEndHour(), pageable);
//        return new PageImpl<>(alertList, pageable, alertList.size());
        return new Result<>(alertList,"",200);
    }
}
