package com.example.alert.dtos;
import java.time.LocalDateTime;

public class AlertResponse {
    private LocalDateTime createAt;
    private String message;
    private String type;
    public AlertResponse(LocalDateTime createAt,String message, String type){
        this.createAt=createAt;
        this.message=message;
        this.type=type;
    }
    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
