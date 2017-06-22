package debug_artist.sample.stetho;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface RandomService {

  // Just a get to google.com, we dont need to parse the results, just do a request that works forever :P
  @GET("/")
  Call<Request> get(@Query("format") String format);
}