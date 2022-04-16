package me.saket.dank.ui.media.gfycat;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonClass;
import com.squareup.moshi.Moshi;

@JsonClass(generateAdapter = true, generator = "avm")
@AutoValue
public abstract class GfycatResponse {

  @Json(name = "gfyItem")
  public abstract Data data();

  public static JsonAdapter<GfycatResponse> jsonAdapter(Moshi moshi) {
    return new AutoValue_GfycatResponse.MoshiJsonAdapter(moshi);
  }

  public static GfycatResponse create(Data data) {
    return new AutoValue_GfycatResponse(data);
  }

  @AutoValue
  public abstract static class Data {

    @Json(name = "gfyId")
    public abstract String threeWordId();

    @Json(name = "content_urls")
    public abstract ContentUrls urls();

    public static JsonAdapter<Data> jsonAdapter(Moshi moshi) {
      return new AutoValue_GfycatResponse_Data.MoshiJsonAdapter(moshi);
    }

    public static Data create(String id, ContentUrls urls) {
      return new AutoValue_GfycatResponse_Data(id, urls);
    }
  }

  @AutoValue
  public abstract static class ContentUrls {
    @Json(name = "mp4")
    public abstract Urls highQualityUrl();

    @Json(name = "max1mbGif")
    public abstract Urls lowQualityUrl();

    public static JsonAdapter<ContentUrls> jsonAdapter(Moshi moshi) {
      return new AutoValue_GfycatResponse_ContentUrls.MoshiJsonAdapter(moshi);
    }

    public static ContentUrls create(Urls highQualityUrl, Urls lowQualityUrl) {
      return new AutoValue_GfycatResponse_ContentUrls(highQualityUrl, lowQualityUrl);
    }
  }

  @AutoValue
  public abstract static class Urls {
    @Json(name = "url")
    public abstract String url();

    public static JsonAdapter<Urls> jsonAdapter(Moshi moshi) {
      return new AutoValue_GfycatResponse_Urls.MoshiJsonAdapter(moshi);
    }

    public static Urls create(String url) {
      return new AutoValue_GfycatResponse_Urls(url);
    }
  }
}
