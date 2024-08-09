package com.sparta.bookflex.common.utill;

import com.sparta.bookflex.domain.user.dto.shipmentJsonDto;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ShipmentJsonUtil {

    public static String fromJSONtoString(String responseEntity) {
        JSONObject jsonObject = new JSONObject(responseEntity);
        String status = jsonObject
            .getJSONObject("data")
            .getJSONObject("track")
            .getJSONObject("lastEvent")
            .getJSONObject("status")
            .getString("code");

        return status;
    }

    public static List<shipmentJsonDto> fromJSONtoItems(String responseEntityJson) {
        JSONObject jsonObject = new JSONObject(responseEntityJson);

        JSONArray jsonArray = jsonObject.getJSONObject("data")
            .getJSONObject("track")
            .getJSONObject("events")
            .getJSONArray("edges");

        List<shipmentJsonDto> itemDtoList = new ArrayList<>();

        for (Object item : jsonArray) {
            JSONObject tempJson = ((JSONObject) item).getJSONObject("node");
            JSONObject itemJson = new JSONObject();
            itemJson.put("time", tempJson.getString("time"));
            itemJson.put("name", tempJson.getJSONObject("status").getString("name"));
            shipmentJsonDto itemDto = new shipmentJsonDto(itemJson);
            itemDtoList.add(itemDto);
        }

        return itemDtoList;
    }

    public static LocalDateTime getTimeParse(String time) {
        String thisTime;
        if(time.contains("+")){
            thisTime = time.replace("T", " ").substring(0, time.indexOf("+"));
        }
        else {
            thisTime = time.replace("T", " ").replace("Z","");
        }
        return LocalDateTime.parse(thisTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }
}
