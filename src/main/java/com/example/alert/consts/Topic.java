package com.example.alert.consts;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Topic {
   public final static  String loginTopic="login/client";
    public final static  String historyTopic="history/client";
    public final static  String deviceTopic="device/client";
    public final static  String deviceLogTopic="device-log/client";
    public final static  String powerConsumption="power-consumption/client";
    public final static  String createUserTopic="create/client";
    public final static  String editUserTopic="edit-user-info/client";
    public final static  String changePasswordTopic="edit-password/client";
    public final static  String deleteUserTopic="delete-user/client";
    public final static  String getListUserTopic= "get-list-user/client";
    // Không thêm nếu không dùng
    public static List<String> getAllTopics() {
        List<String> topics = new ArrayList<>();
        Field[] fields = Topic.class.getDeclaredFields();

        for (Field field : fields) {
            if (field.getType() == String.class) {  // Chỉ lấy các trường kiểu String
                try {
                    topics.add((String) field.get(null));  // Lấy giá trị của trường
                } catch (IllegalAccessException e) {
                    System.out.println(e.toString());
                }
            }
        }
        return topics;
    }
}
