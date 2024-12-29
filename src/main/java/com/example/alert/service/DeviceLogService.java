package com.example.alert.service;

import com.example.alert.model.DeviceLog;
import com.example.alert.util.FileUtil;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class DeviceLogService {

    private static final String BASE_PATH = "D://data/";
    private final ObjectMapper objectMapper;

    // Khởi tạo ObjectMapper một lần
    public DeviceLogService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    public void mapperDeviceLog(String json){
        List<DeviceLog> deviceLogs=new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss-dd/MM/yy");
        String[] lines=json.split("\n");
        for(String line :lines){
            String[] fields=line.split(";");
            String deviceLogId=fields[0];
            LocalDateTime createdAt=LocalDateTime.parse(fields[1],formatter);
            Float powerFactor=Float.parseFloat(fields[2]);;
            Float volt=Float.parseFloat(fields[3]);
            Float ampere=Float.parseFloat(fields[4]);
            DeviceLog deviceLog=new DeviceLog(deviceLogId,createdAt,powerFactor,volt,ampere);
            save(deviceLog);
        }

    }

//    public void getFile() throws IOException {
//        String data=FileUtil.getFileTxt("C:/Users/luutu/Documents/Zalo Received Files/141124_Nha rieng.txt");
//        mapperDeviceLog(data);
//    }
    public void save( DeviceLog deviceLog) {
        try {
            String year = String.valueOf(deviceLog.getCreatedAt().getYear());
            String month = String.format("%02d", deviceLog.getCreatedAt().getMonthValue());
            String folderPath = BASE_PATH + deviceLog.getDeviceLogId() + "/" + year + "/" + month;
            String filePath = folderPath + "/deviceLog.json";
            if (!FileUtil.existsDirectories(folderPath)) {
                FileUtil.createDirectories(folderPath);
            }
            File jsonFile = FileUtil.getFile(filePath);
            List<DeviceLog> logs = new ArrayList<>();
            if (jsonFile.exists() && jsonFile.length() > 0) {
                logs = objectMapper.readValue(jsonFile, new TypeReference<List<DeviceLog>>() {});
            }
            logs.add(deviceLog);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, logs);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lưu DeviceLog: " + e.getMessage(), e);
        }
    }
    public List<DeviceLog> findDeviceLogByMonth(int month, int year,String deviceName){
        try{
            String filePath=BASE_PATH + deviceName + "/"+ year + "/" + month + "/deviceLog.json";
            File jsonFile=FileUtil.getFile(filePath);
            return objectMapper.readValue(jsonFile,new TypeReference<List<DeviceLog>>(){});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void getData(){
        try{
            String filePath=BASE_PATH + "0000000003" + "/"+ "2024" + "/" + "11" + "/deviceLog.json";
            File jsonFile=FileUtil.getFile(filePath);
            List<DeviceLog> deviceLogs= objectMapper.readValue(jsonFile,new TypeReference<List<DeviceLog>>(){});
            List<Float> powers=new ArrayList<>();
            List<String> deltasList=new ArrayList<>();
            for(DeviceLog deviceLog:deviceLogs){
                Float power=deviceLog.getPowerFactor()*deviceLog.getVolt()*deviceLog.getAmpere();
                powers.add(power);
            }
            deviceLogs.clear();
            for(int i=0;i<powers.size()-1;i++){
                if(powers.get(i+1)!=0){
                    float delta= (powers.get(i) - powers.get(i+1))/ powers.get(i+1);
                    if(delta<0)delta=-delta;
                    deltasList.add(String.valueOf(delta));
                }

            }
            powers.clear();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("D://data/delta.txt", true))) {
                writer.write(String.valueOf(deltasList)); // Ghi dữ liệu vào file
                writer.newLine();  // Xuống dòng
                System.out.println("Ghi dữ liệu thành công vào file " + filePath);
            } catch (IOException e) {
                System.err.println("Lỗi khi ghi file: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void getPower(){
        try{
            String filePath=BASE_PATH + "0000000003" + "/"+ "2024" + "/" + "11" + "/deviceLog.json";
            File jsonFile=FileUtil.getFile(filePath);
            List<DeviceLog> deviceLogs= objectMapper.readValue(jsonFile,new TypeReference<List<DeviceLog>>(){});
            List<Float> powers=new ArrayList<>();
            ArrayList<Integer> deltasList=new ArrayList<>(Collections.nCopies(512, 0));
            for(DeviceLog deviceLog:deviceLogs){
                Float power=deviceLog.getPowerFactor()*deviceLog.getVolt()*deviceLog.getAmpere();
                powers.add(power);
            }
            deviceLogs.clear();
            for (Float power : powers) {
                int j = Math.round(power / 20);
                deltasList.set(j, deltasList.get(j) + 1);
            }
            powers.clear();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("D://data/matdo.txt", true))) {
                writer.write(String.valueOf(deltasList)); // Ghi dữ liệu vào file
                writer.newLine();  // Xuống dòng
                System.out.println("Ghi dữ liệu thành công vào file " + filePath);
            } catch (IOException e) {
                System.err.println("Lỗi khi ghi file: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Float powerConsumption(LocalDateTime startDate, LocalDateTime endDate,String deviceId) throws IOException {
        float powerConsumptions=0F;
        String startYear=String.valueOf(startDate.getYear());
        String endYear=String.valueOf(endDate.getYear());
        String startMonth=String.valueOf(startDate.getMonth());
        String endMonth=String.valueOf(startDate.getMonth());
        if(startYear.equals(endYear)){
                JsonFactory factory = new JsonFactory();
                for(int i=Integer.parseInt(startMonth);i<Integer.parseInt(endMonth);i++){
                    String filePath=BASE_PATH + deviceId+ "/" + startYear + "/" + i +"deviceLog.json";
                    File deviceLogs=FileUtil.getFile(filePath);
                    try (JsonParser parser = factory.createParser(deviceLogs)) {
                        if (parser.nextToken() == JsonToken.START_ARRAY) {
                            while (parser.nextToken() != JsonToken.END_ARRAY) {
                                DeviceLog deviceLog = objectMapper.readValue(parser, DeviceLog.class);
                                if(startDate.isBefore(deviceLog.getCreatedAt())){
                                    powerConsumptions+= deviceLog.getVolt()*deviceLog.getPowerFactor()*deviceLog.getAmpere();
                                }
                                if(endDate.isBefore(deviceLog.getCreatedAt())){
                                    return powerConsumptions;
                                }
                            }
                        }
                    } catch (JsonParseException e) {
                        throw new RuntimeException(e);
                    }
                }
        }
        return 0f;
    }

}
