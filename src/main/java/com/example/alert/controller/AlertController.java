package com.example.alert.controller;

import com.example.alert.dtos.AlertRequest;
import com.example.alert.dtos.AlertResponse;
import com.example.alert.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/alert")
public class AlertController {
    @Autowired
    private AlertService alertService;
    @GetMapping("/by-type")
    public ResponseEntity<?> getAlertByType(@RequestBody AlertRequest alertRequest){
        List<AlertResponse> alertResponses=alertService.getAlertsByTimeAndType(alertRequest);
        if(alertResponses.isEmpty())return ResponseEntity.notFound().build();
        return ResponseEntity.ok(alertResponses);
    }
}
