package com.example.alert.service;

import com.example.alert.dtos.AlertRequest;
import com.example.alert.dtos.AlertResponse;
import com.example.alert.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    public List<AlertResponse> getAlertsByTimeAndType(AlertRequest alertRequest) {
        Pageable pageable = PageRequest.of(alertRequest.getPageNumber() , alertRequest.getPageSize());
        List<AlertResponse> alertList = alertRepository.findAlertByTimeAndType( alertRequest.getUserId(),
                alertRequest.getStartDate(),
                alertRequest.getEndDate(),
                alertRequest.getStartHour(),
                alertRequest.getEndHour(), pageable);
//        return new PageImpl<>(alertList, pageable, alertList.size());
        return alertList;
    }
}
