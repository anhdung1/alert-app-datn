package com.example.alert.controller;

import com.example.alert.dtos.AlertRequest;
import com.example.alert.dtos.AlertResponse;
import com.example.alert.service.AlertService;
import com.example.alert.service.Result;
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
        Result<List<AlertResponse>> result=alertService.getAlertsByTimeAndType(alertRequest,"");
        if(!result.isSuccess())return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }
}
