package com.sparta.bookflex.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bookflex.common.config.JwtConfig;
import com.sparta.bookflex.common.jwt.JwtProvider;
import com.sparta.bookflex.domain.user.dto.KakaoUserInfoDto;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.enums.RoleType;
import com.sparta.bookflex.domain.user.enums.UserGrade;
import com.sparta.bookflex.domain.user.enums.UserState;
import com.sparta.bookflex.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j(topic = "SocialService")
@Service
@RequiredArgsConstructor
public class SocialService {
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Value("${kakao.client.id}")
    private String client_id;

    @Transactional
    public List<String> kakaoLogin(String code) throws JsonProcessingException {
        // kakao로부터 카카오에 접속할 수 있는 accessToken을 받아온다.
        String accessTokenForKakao = getToken(code);

        // 카카오 유저 정보 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessTokenForKakao);

        // 기존에 있는 회원이 아니라면 등록 시켜주기
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        List<String> tokenList = new ArrayList<>();

        // 우리 사이트에 접속할 수 있는 토큰 발급
        String accessTokenForBookflex = jwtProvider.createToken(kakaoUser, JwtConfig.accessTokenTime);
        String refreshTokenForBookFlex = jwtProvider.createToken(kakaoUser, JwtConfig.refreshTokenTime);

        kakaoUser.updateRefreshToken(refreshTokenForBookFlex);

        tokenList.add(accessTokenForBookflex);
        tokenList.add(refreshTokenForBookFlex);

        return tokenList;
    }


    private String getToken(String code) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

             HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", client_id);
        body.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
        body.add("code", code);

             RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

        return jsonNode.get("access_token").asText();
    }

    public KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/X-WWW-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        ResponseEntity<String> userInfoResponse = restTemplate.exchange(requestEntity, String.class);

        JsonNode jsonNode = new ObjectMapper().readTree(userInfoResponse.getBody());
        Long id = jsonNode.get("id").asLong();
        String name = jsonNode.get("kakao_account").get("name").asText();
        String nickname = jsonNode.get("kakao_account").get("profile").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String phoneNumber = "0" + jsonNode.get("kakao_account").get("phone_number").asText().substring(4);
        String address = " ";
        String birthYear = jsonNode.get("kakao_account").get("birthyear").asText();
        String birthDay = jsonNode.get("kakao_account").get("birthday").asText();
        String birthYearDay = birthYear + "-" + birthDay.substring(0, 2) + "-" + birthDay.substring(2, 4);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(birthYearDay, DateTimeFormatter.ISO_DATE);
        return new KakaoUserInfoDto(id, name, nickname, email, phoneNumber, address, birthDate);
    }

    public String getKakaoUserAddress(String accessToken) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v1/user/shipping_address")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/X-WWW-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        ResponseEntity<String> userInfoResponse = restTemplate.exchange(requestEntity, String.class);

        JsonNode jsonNode = new ObjectMapper().readTree(userInfoResponse.getBody());

        String baseAddress = jsonNode.get("shipping_addresses").get("base_address").asText();
        String detailAddress = jsonNode.get("shipping_addresses").get("detail_address").asText();

        return baseAddress + " " + detailAddress;
    }

    @Transactional
    protected User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        if (kakaoUser == null) { // 등록되어 있는 카카오 유저가 없다면
            String kakaoEmail = kakaoUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) { // 같은 이메일을 가진 유저가 없다면
                kakaoUser = sameEmailUser;
                kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);
            } else { // DB에 새로운 유저를 등록해준다.
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                String email = kakaoUserInfo.getEmail();

                String username = "kakao" + kakaoId;
                kakaoUser = User.builder()
                        .username(username)
                        .password(encodedPassword)
                        .email(email)
                        .name(kakaoUserInfo.getEmail())
                        .nickname(kakaoUserInfo.getNickname())
                        .phoneNumber(kakaoUserInfo.getPhoneNumber())
                        .address(kakaoUserInfo.getAddress())
                        .birthDay(kakaoUserInfo.getBirthDay())
                        .grade(UserGrade.NORMAL)
                        .state(UserState.ACTIVE)
                        .auth(RoleType.USER).
                        build();


            }
            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }
}
