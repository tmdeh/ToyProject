package com.fc.toyproeject2.global.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fc.toyproeject2.global.config.KakaoMapConfig;
import com.fc.toyproeject2.global.exception.type.ValidationException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = KakaoMapConfig.class)
class KakaoMapUtilTest {

  @Value("${kakao.map.api-key}")
  private String kakaoApiKey;

  @Autowired
  private RestTemplate restTemplate;

  @InjectMocks
  private KakaoMapUtil kakaoMapUtil;

  private MockRestServiceServer mockServer;

  @BeforeEach
  void setUp() {
    mockServer = MockRestServiceServer.createServer(restTemplate);
    kakaoMapUtil = new KakaoMapUtil(restTemplate);
    ReflectionTestUtils.setField(kakaoMapUtil, "kakaoApiKey", kakaoApiKey);
  }

  @Test
  void getPlaceAddressSuccess() {
    // given
    String placeName = "Test Place";
    String roadAddressName = "Test Road Address";
    String encodedPlaceName = URLEncoder.encode(placeName, StandardCharsets.UTF_8).replace("+", "%20");
    String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + encodedPlaceName;

    // Mock 서버 설정: 주어진 URL로 GET 요청을 기대하고, Authorization 헤더가 포함되며, 성공적인 응답을 모킹
    mockServer.expect(once(), requestTo(url))
        .andExpect(method(HttpMethod.GET))
        .andExpect(header("Authorization", "KakaoAK " + kakaoApiKey))
        .andRespond(withSuccess(
            "{\"documents\":[{\"road_address_name\":\"" + roadAddressName + "\"}]}",
            MediaType.APPLICATION_JSON));

    // when
    // getPlaceAddress 메서드가 호출되면, 내부적으로 RestTemplate이 외부 API에 요청을 보냄
    // 이 요청은 MockRestServiceServer에 의해 가로채짐
    // MockRestServiceServer는 가로챈 요청이 설정된 기대 사항(Mock 서버 설정)과 일치하는지 확인
    // 요청이 기대 사항과 일치하면, MockRestServiceServer는 사전에 설정된 모킹된 응답을 반환.
    // RestTemplate은 이 응답을 받아 처리.
    String result = kakaoMapUtil.getPlaceAddress(placeName);

    // then
    assertEquals(roadAddressName, result);
  }

  @Test
  void getPlaceAddressInvalidPlaceName() {
    // given
    String placeName = "Invalid Place";
    String encodedPlaceName = URLEncoder.encode(placeName, StandardCharsets.UTF_8).replace("+", "%20");
    String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + encodedPlaceName;

    mockServer.expect(once(), requestTo(url))
        .andExpect(method(HttpMethod.GET))
        .andExpect(header("Authorization", "KakaoAK " + kakaoApiKey))
        .andRespond(withSuccess("{\"documents\":[]}", MediaType.APPLICATION_JSON));

    // when
    ValidationException exception = assertThrows(ValidationException.class, () -> kakaoMapUtil.getPlaceAddress(placeName));

    // then
    String actualMessage = exception.getMessage().replaceAll("^\\d+\\s*", ""); // 숫자와 공백 제거
    String expectedMessage = "유효하지 않은 장소 이름입니다.";

    assertEquals(expectedMessage, actualMessage);
  }
}
