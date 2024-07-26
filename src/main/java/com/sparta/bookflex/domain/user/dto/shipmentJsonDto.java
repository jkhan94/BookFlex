package com.sparta.bookflex.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@Getter
@NoArgsConstructor
public class shipmentJsonDto {
    private String time;
    private String name;

    public shipmentJsonDto(JSONObject jsonObject) {
        if(jsonObject.has("time")){
            this.time =jsonObject.getString("time");
        }
        if(jsonObject.has("name")){
            this.name = jsonObject.getString("name");
        }
    }
}