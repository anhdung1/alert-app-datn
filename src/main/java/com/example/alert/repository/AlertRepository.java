package com.example.alert.repository;

import com.example.alert.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert,Long>,CustomAlertRepository {
}
