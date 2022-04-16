package me.saket.dank.ui.media.redgifs;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonClass;
import com.squareup.moshi.Moshi;

@JsonClass(generateAdapter = true, generator = "avm")
@AutoValue
public abstract class RedgifsResponse {

  @Json(name = "gif")
  public abstract Data data();

  public static JsonAdapter<RedgifsResponse> jsonAdapter(Moshi moshi) {
    return new AutoValue_RedgifsResponse.MoshiJsonAdapter(moshi);
  }

  public static RedgifsResponse create(Data data) {
    return new AutoValue_RedgifsResponse(data);
  }

  @AutoValue
  public abstract static class Data {

    @Json(name = "id")
    public abstract String threeWordId();

    @Json(name = "urls")
    public abstract Urls urls();

    public static JsonAdapter<Data> jsonAdapter(Moshi moshi) {
      return new AutoValue_RedgifsResponse_Data.MoshiJsonAdapter(moshi);
    }

    public static Data create(String id, Urls urls) {
      return new AutoValue_RedgifsResponse_Data(id, urls);
    }
  }

  @AutoValue
  public abstract static class Urls {
    @Json(name = "hd")
    public abstract String highQualityUrl();

    @Json(name = "sd")
    public abstract String lowQualityUrl();

    public static JsonAdapter<Urls> jsonAdapter(Moshi moshi) {
      return new AutoValue_RedgifsResponse_Urls.MoshiJsonAdapter(moshi);
    }

    public static Urls create(String highQualityUrl, String lowQualityUrl) {
      return new AutoValue_RedgifsResponse_Urls(highQualityUrl, lowQualityUrl);
    }
  }
}
