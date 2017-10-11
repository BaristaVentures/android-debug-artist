package debug_artist.sample.stetho;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import debug_artist.sample.BaseActivity;
import debug_artist.sample.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StethoActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stetho);
    setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    // Check BaseActivity for Debug Artist Initialization

    findViewById(R.id.requestButton).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        requestInfo();
      }
    });
  }

  private void requestInfo() {
    showMessage("Sending request");

    OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
        .addNetworkInterceptor(new StethoInterceptor())     // <---- Put the interceptor
        .build();

    Retrofit mRetrofit = new Retrofit.Builder()
        .client(mOkHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.ipify.org")
        .build();

    RandomService mRandomService = mRetrofit.create(RandomService.class);

    mRandomService.get("json").enqueue(new Callback<Request>() {
      @Override
      public void onResponse(Call<Request> call, Response<Request> response) {
        showMessage("It Works!");
      }

      @Override
      public void onFailure(Call<Request> call, Throwable throwable) {
        showMessage("Error: " + throwable.toString());
      }
    });
  }

  private void showMessage(String message) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
  }
}
