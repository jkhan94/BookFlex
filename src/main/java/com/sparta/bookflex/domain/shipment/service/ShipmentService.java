package com.sparta.bookflex.domain.shipment.service;

import com.sparta.bookflex.domain.shipment.dto.ShipmentReqDto;
import com.sparta.bookflex.domain.shipment.dto.ShipmentResDto;
import com.sparta.bookflex.domain.shipment.enums.ShipmentEnum;
import com.sparta.bookflex.domain.user.dto.shipmentJsonDto;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private static final Logger log = LoggerFactory.getLogger(ShipmentService.class);
    private final RestTemplate restTemplate;

    public ShipmentResDto getShipment(ShipmentReqDto reqDto) {

        List<shipmentJsonDto> dtoList = shipmentCheck(reqDto.getTrackingNumber(), reqDto.getCarrier());

        ShipmentResDto tempDto = getStatus(reqDto);

        int listSize = dtoList.size();
         LocalDateTime shippedAt = LocalDateTime.now();
         LocalDateTime deliveredAt = LocalDateTime.now();
        for (int i = 0; i < listSize; i++) {
            if(i==0){
                shippedAt = getTimeParse(dtoList.get(i).getTime());
            }
            else if(i == listSize-1) {
                deliveredAt = getTimeParse(dtoList.get(i).getTime());
            }
        }

        ShipmentResDto dto = new ShipmentResDto(shippedAt, deliveredAt, tempDto.getStatus());

//        for (shipmentJsonDto item : dtoList) {
//            ShipmentResDto dto = new ShipmentResDto(item.getTime(), item.getName());
//            shipmentResDtoList.add(dto);
//        }

        return dto;
    }

    public LocalDateTime getTimeParse(String time) {
        String thisTime;
        if(time.contains("+")){
            thisTime = time.replace("T", " ").substring(0, time.indexOf("+"));
        }
        else {
            thisTime = time.replace("T", " ").replace("Z","");
        }
        return LocalDateTime.parse(thisTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    public ShipmentResDto getStatus(ShipmentReqDto reqDto) {
        return shipmentStatus(reqDto.getTrackingNumber(), reqDto.getCarrier());
    }

    public ShipmentResDto shipmentStatus(String trackingNumber, String carrier) {

        URI uri = UriComponentsBuilder
            .fromUriString("https://apis.tracker.delivery")
            .path("/graphql")
            .encode()
            .build()
            .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "TRACKQL-API-KEY 7j787gni6dptele0t6uglp8q34:1eoeo1ioih6efa0tbungp1r69lncuhhf9i6a6o1pip2b2k7a83le");
        headers.add("Accept-Language", "ko");

        String query = "query Track($carrierId: ID!, $trackingNumber: String!) { " +
            "  track(carrierId: $carrierId, trackingNumber: $trackingNumber) { " +
            "    lastEvent { " +
            "      time " +
            "      status { " +
            "        code " +
            "      } " +
            "    } " +
            "  } " +
            "}";
        Map<String, Object> variables = new HashMap<>();
        variables.put("carrierId", carrier);
        variables.put("trackingNumber", trackingNumber);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", query);
        requestBody.put("variables", variables);

        RequestEntity<Map<String, Object>> requestEntity = RequestEntity
            .post(uri)
            .headers(headers)
            .body(requestBody);

        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
        String status = fromJSONtoString(responseEntity.getBody());
        ShipmentResDto dto = new ShipmentResDto(DateTime.now().toString(), status);
        return dto;
    }

    public List<shipmentJsonDto> shipmentCheck(String trackingNumber, String carrier) {
        URI uri = UriComponentsBuilder
            .fromUriString("https://apis.tracker.delivery")
            .path("/graphql")
            .encode()
            .build()
            .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "TRACKQL-API-KEY 7j787gni6dptele0t6uglp8q34:1eoeo1ioih6efa0tbungp1r69lncuhhf9i6a6o1pip2b2k7a83le");
        headers.add("Accept-Language", "ko");

        String query = "query Track($carrierId: ID!, $trackingNumber: String!) { " +
            "  track(carrierId: $carrierId, trackingNumber: $trackingNumber) { " +
            "    lastEvent { " +
            "      time " +
            "      status { " +
            "        code " +
            "        name " +
            "      } " +
            "      description " +
            "    } " +
            "    events(last: 10) { " +
            "      edges { " +
            "        node { " +
            "          time " +
            "          status { " +
            "            code " +
            "            name " +
            "          } " +
            "          description " +
            "        } " +
            "      } " +
            "    } " +
            "  } " +
            "}";
        Map<String, Object> variables = new HashMap<>();
        variables.put("carrierId", carrier);
        variables.put("trackingNumber", trackingNumber);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", query);
        requestBody.put("variables", variables);

        RequestEntity<Map<String, Object>> requestEntity = RequestEntity
            .post(uri)
            .headers(headers)
            .body(requestBody);

        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        List<shipmentJsonDto> dtoList = fromJSONtoItems(responseEntity.getBody());
        for (shipmentJsonDto item : dtoList) {
            log.info(item.getName());
        }

        return dtoList;
    }

    public String fromJSONtoString(String responseEntity) {
        JSONObject jsonObject = new JSONObject(responseEntity);
        String status = jsonObject
            .getJSONObject("data")
            .getJSONObject("track")
            .getJSONObject("lastEvent")
            .getJSONObject("status")
            .getString("code");

        return status;
    }

    public List<shipmentJsonDto> fromJSONtoItems(String responseEntityJson) {
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
}
