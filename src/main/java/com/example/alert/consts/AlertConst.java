package com.example.alert.consts;

public class AlertConst {
    public enum Type{
        TurnOn("Thiết bị được bật"),
        TurnOff("Thiết bị đã tắt"),
        AnomalyDevice("Bất thường thiết bị");
        private final String value;
        Type(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }
}
