package com.sparta.bookflex.domain.shipment.service;

import com.sparta.bookflex.common.utill.ShipmentJsonUtil;
import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.orderbook.repository.OrderBookRepository;
import com.sparta.bookflex.domain.orderbook.service.OrderBookService;
import com.sparta.bookflex.domain.shipment.dto.ShipmentResDto;
import com.sparta.bookflex.domain.shipment.dto.TotalShipmentResDto;
import com.sparta.bookflex.domain.shipment.entity.Shipment;
import com.sparta.bookflex.domain.shipment.repository.ShipmentRepository;
import com.sparta.bookflex.domain.user.dto.shipmentJsonDto;
import com.sparta.bookflex.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final RestTemplate restTemplate;
    private final ShipmentRepository shipmentRepository;
    private final OrderBookService orderBookService;
    private final OrderBookRepository orderBookRepository;

    @Transactional
    public void createShipment(OrderBook orderBook, User user) {
        List<LocalDateTime> timelist = getShipAndDeliverTime(orderBook.getTrackingNumber(), orderBook.getCarrier());

        Shipment shipment = Shipment.builder()
                .address(user.getAddress())
                .orderBook(orderBook)
                .carrier(orderBook.getCarrier())
                .trackingNumber(orderBook.getTrackingNumber())
                .shippedAt(timelist.get(0))
                .deliveredAt(timelist.get(1))
                .user(user)
                .build();
        shipmentRepository.save(shipment);
        orderBook.updateShipment(shipment);
        user.setShipmentInfo(shipment);
        orderBookRepository.save(orderBook);
    }
    public TotalShipmentResDto getAllShipmentInfo(int page, int size, boolean isAsc){

        Sort.Direction direction = isAsc ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, "createdAt");
        Pageable pageable = PageRequest.of(page-1, size, sort);

        Page<Shipment> shipmentPage = shipmentRepository.findAll(pageable);
        long totalCount = shipmentRepository.count();

        Page<ShipmentResDto> shipmentResDtos = shipmentPage.map(Shipment::toShipmentResDto).map((status) ->
        {
            status.updateStatus(getShipmentStatus(status.getTrackingNumber(), status.getCarrierName()));
            return status;
        });

        return new TotalShipmentResDto(shipmentResDtos, totalCount);
    }

    public TotalShipmentResDto getUserShipmentInfo(int page, int size, boolean isAsc, User user){

        Sort.Direction direction = isAsc ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, "createdAt");
        Pageable pageable = PageRequest.of(page-1, size, sort);

        Page<Shipment> shipmentPage = shipmentRepository.findAllByUserId(user.getId(), pageable);
        long totalCount = shipmentRepository.userShipInfoCount(user.getId());

        Page<ShipmentResDto> shipmentResDtos = shipmentPage.map(Shipment::toShipmentResDto).map((status) ->
        {
            status.updateStatus(getShipmentStatus(status.getTrackingNumber(), status.getCarrierName()));
            return status;
        });

        return new TotalShipmentResDto(shipmentResDtos, totalCount);
    }

//    public ShipmentResDto getShipment(ShipmentReqDto reqDto, User user) {
//
//        List<shipmentJsonDto> dtoList = shipmentCheck(reqDto.getTrackingNumber(), reqDto.getCarrier());
//
//        ShipmentResDto tempDto = shipmentStatus(reqDto.getTrackingNumber(), reqDto.getCarrier());
//
//        int listSize = dtoList.size();
//         LocalDateTime shippedAt = LocalDateTime.now();
//         LocalDateTime deliveredAt = LocalDateTime.now();
//        for (int i = 0; i < listSize; i++) {
//            if(i==0){
//                shippedAt = ShipmentJsonUtil.getTimeParse(dtoList.get(i).getTime());
//            }
//            else if(i == listSize-1) {
//                deliveredAt = ShipmentJsonUtil.getTimeParse(dtoList.get(i).getTime());
//            }
//        }
//
//        ShipmentResDto dto = new ShipmentResDto(shippedAt, deliveredAt, tempDto.getStatus());
//
//        return dto;
//    }

    public List<LocalDateTime> getShipAndDeliverTime(String trackingNumber, String carrier){
        List<shipmentJsonDto> dtoList = shipmentCheck(trackingNumber, carrier);

        int listSize = dtoList.size();
        LocalDateTime shippedAt = LocalDateTime.now();
        LocalDateTime deliveredAt = LocalDateTime.now();
        for (int i = 0; i < listSize; i++) {
            if(i==0){
                shippedAt = ShipmentJsonUtil.getTimeParse(dtoList.get(i).getTime());
            }
            else if(i == listSize-1) {
                deliveredAt = ShipmentJsonUtil.getTimeParse(dtoList.get(i).getTime());
            }
        }
        List<LocalDateTime> timeList = new ArrayList<>();
        timeList.add(shippedAt);
        timeList.add(deliveredAt);

        return timeList;
    }

    public String getShipmentStatus(String trackingNumber, String carrier) {

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
        String status = ShipmentJsonUtil.fromJSONtoString(responseEntity.getBody());

        return status;
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

        List<shipmentJsonDto> dtoList = ShipmentJsonUtil.fromJSONtoItems(responseEntity.getBody());

        return dtoList;
    }

}
