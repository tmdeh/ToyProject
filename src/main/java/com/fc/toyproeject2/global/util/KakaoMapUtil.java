package com.fc.toyproeject2.global.util;

import com.fc.toyproeject2.global.exception.error.ValidationErrorCode;
import com.fc.toyproeject2.global.exception.type.ValidationException;
import com.fc.toyproeject2.global.model.response.KakaoMapResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoMapUtil {

  @Value("${kakao.map.api-key}")
  private String kakaoApiKey;
  private static final String KAKAO_MAP_API_URL = "https://dapi.kakao.com/v2/local/search/keyword.json?query=";

  private final RestTemplate restTemplate;

  public KakaoMapUtil(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public String getPlaceAddress(String placeName) {
    try {
      String url = KAKAO_MAP_API_URL + placeName;

      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", "KakaoAK " + kakaoApiKey);
      HttpEntity<String> entity = new HttpEntity<>(headers);

      ResponseEntity<KakaoMapResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, KakaoMapResponse.class);
      KakaoMapResponse kakaoMapResponse = response.getBody();

      if (kakaoMapResponse != null && kakaoMapResponse.getDocuments() != null && !kakaoMapResponse.getDocuments().isEmpty()) {
        for (KakaoMapResponse.Document place : kakaoMapResponse.getDocuments()) {
          String roadAddressName = place.getRoadAddressName();
          if (roadAddressName != null && roadAddressName.contains(placeName)) {
            return roadAddressName;
          }
        }
        return kakaoMapResponse.getDocuments().get(0).getRoadAddressName();
      } else {
        throw new ValidationException(ValidationErrorCode.INVALID_PLACE_NAME);
      }
    } catch (Exception e) {
      throw new ValidationException(ValidationErrorCode.INVALID_PLACE_NAME);
    }
  }
}
