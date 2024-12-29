package com.example.alert.repository;

import com.example.alert.dtos.AlertResponse;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CustomAlertRepository {
    List<AlertResponse> findAlertByTimeAndType(Long usersId,
                                               LocalDate startDate,
                                               LocalDate endDate,
                                               LocalTime startHour,
                                               LocalTime endHour,
                                               Pageable pageable);
}
