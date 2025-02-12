package com.example.alert.service;

import com.example.alert.dtos.DeviceLogRequest;
import com.example.alert.dtos.PowerSumResponse;
import com.example.alert.model.DeviceLog;
import com.example.alert.util.FileUtil;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DeviceLogService {

    private static final String BASE_PATH = "D://data/";
    private final ObjectMapper objectMapper;

    public DeviceLogService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    public void convertTxtToJson() {
        List<DeviceLog> dataList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss-dd/MM/yy");
        try (BufferedReader br = new BufferedReader(new FileReader(BASE_PATH+"0000000002.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(";");

                    String deviceLogId=fields[0];
                    LocalDateTime createdAt=LocalDateTime.parse(fields[1],formatter);
                    Float powerFactor=Float.parseFloat(fields[2]);;
                    Float volt=Float.parseFloat(fields[3]);
                    Float ampere=Float.parseFloat(fields[4]);
                    DeviceLog deviceLog=new DeviceLog(deviceLogId,createdAt,powerFactor,volt,ampere);
                    System.out.println(deviceLog);
                    dataList.add(deviceLog);

            }

            // Chuyển đổi danh sách thành JSON
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            String jsonString = objectMapper.writeValueAsString(dataList);

            // Ghi vào file JSON
            Files.write(Paths.get(BASE_PATH+"0000000002.json"), jsonString.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
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
    public void save( DeviceLog deviceLog) {
        try {
            String year = String.valueOf(deviceLog.getCreatedAt().getYear());
            String month = String.format("%02d", deviceLog.getCreatedAt().getMonthValue());
            String day=String.valueOf(deviceLog.getCreatedAt().getDayOfMonth());
            String folderPath = BASE_PATH + deviceLog.getDeviceLogId() + "/" + year + "/"  + month + "/" + day;
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
    public Result<List<DeviceLog>> findDeviceLogByMonth(DeviceLogRequest deviceLogRequest){
        int month= deviceLogRequest.getMonth();
        try{
            if(month>12||month<1){
                return new Result<>(null,"Dữ liệu không hợp lệ",400);
            }
            String filePath=BASE_PATH + deviceLogRequest.getDeviceName() + "/"+ deviceLogRequest.getYear() + "/" + deviceLogRequest.getMonth() + "/deviceLog.json";
            File jsonFile=FileUtil.getFile(filePath);
            List<DeviceLog>deviceLogList=objectMapper.readValue(jsonFile,new TypeReference<List<DeviceLog>>(){});

            return new Result<>(deviceLogList,"",200);
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
    public Result<List<PowerSumResponse>> powerConsumption(LocalDate startDate, LocalDate endDate, String deviceName) throws IOException {
        if (startDate.isAfter(endDate)) {
            return new Result<>(null, "Dữ liệu không hợp lệ", 400);
        }
        List<PowerSumResponse> powerSumResponseList= new ArrayList<>();
        JsonFactory factory = new JsonFactory();
        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {
            float totalPowerConsumption = 0F;
            String filePath = String.format("%s%s/%d/%d/%d/deviceLog.json", BASE_PATH, deviceName, current.getYear(), current.getMonthValue(),current.getDayOfMonth());
            File deviceLogs = FileUtil.getFile(filePath);
            if(!deviceLogs.exists()){
                current =current.plusDays(1);
                continue;
            }
            try (JsonParser parser = factory.createParser(deviceLogs)) {
                if (parser.nextToken() == JsonToken.START_ARRAY) {
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        DeviceLog deviceLog = objectMapper.readValue(parser, DeviceLog.class);
                        totalPowerConsumption += deviceLog.getVolt() * deviceLog.getAmpere();
                    }
                }
            } catch (JsonParseException e) {
                return new Result<>(null, "Lỗi khi xử lí dữ liệu", 404);
            }
            PowerSumResponse powerSumResponse=new PowerSumResponse();
            powerSumResponse.setPowerSum(totalPowerConsumption);
            powerSumResponse.setDate(current);
            powerSumResponseList.add(powerSumResponse);
            current = current.plusDays(1);
        }
        return new Result<>(powerSumResponseList, "", 200);
    }

}
