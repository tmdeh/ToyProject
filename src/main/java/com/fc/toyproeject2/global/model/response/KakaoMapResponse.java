package com.fc.toyproeject2.global.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoMapResponse {

  @JsonProperty("documents")
  private List<Document> documents;

  public KakaoMapResponse(List<Document> documents) {
    this.documents = documents;
  }

  @Getter
  @NoArgsConstructor
  public static class Document {
    @JsonProperty("road_address_name")
    private String roadAddressName;

    public Document(String roadAddressName) {
      this.roadAddressName = roadAddressName;
    }
  }
}