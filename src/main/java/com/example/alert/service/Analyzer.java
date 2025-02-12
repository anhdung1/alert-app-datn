package com.example.alert.service;

import com.example.alert.dtos.PowerSumResponse;
import com.example.alert.model.DeviceLog;
import com.example.alert.util.FileUtil;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class Analyzer {
    private static final String BASE_PATH = "D://data/";
    @Autowired
    private ObjectMapper objectMapper;
    public void analyzer(){
        List<Float> deviceAmpereList=new ArrayList<>();
        List<PowerSumResponse> powerSumResponseList= new ArrayList<>();
        JsonFactory factory = new JsonFactory();
            float preCurrent=-1;
            List<DeviceLog> deviceLogList=new ArrayList<>();
            ArrayList<Integer> deltasList=new ArrayList<>(Collections.nCopies(120, 0));
            String filePath = String.format("%s%s/%d/%d/%d/deviceLog.json", BASE_PATH, "0000000002",2024, 12,15);
            File deviceLogs = FileUtil.getFile(filePath);
            if(!deviceLogs.exists()){
                System.out.println("file không tồn tại");
                return;
            }
            try (JsonParser parser = factory.createParser(deviceLogs)) {
                if (parser.nextToken() == JsonToken.START_ARRAY) {
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        DeviceLog deviceLog = objectMapper.readValue(parser, DeviceLog.class);
                        if(deviceLogList.size()<60){
                            deviceLogList.add(deviceLog);
                            int current= (int) Math.round(deviceLog.getAmpere()/0.2);
                            deltasList.set(current,deltasList.get(current)+1);
                        }else {
                            for(int i=0;i<deltasList.size();i++){
                                if(deltasList.get(i)>15&&i*0.2>1){
                                    System.out.println(i*0.2);
                                }
                            }
                            System.out.println("het block");
                            deviceLogList=new ArrayList<>();
                            deltasList=new ArrayList<>(Collections.nCopies(120, 0));
                        }
                    }
                }
            } catch (IOException _) {
            }
    }
    public void sendData(){

    }
}
