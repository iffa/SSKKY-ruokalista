package email.crappy.ssao.ruoka.util;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import email.crappy.ssao.ruoka.RuokaApplication;
import email.crappy.ssao.ruoka.model.FoodApi;
import email.crappy.ssao.ruoka.model.RuokaJsonObject;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Class that handles all Retrofit activity (getting food data from the server)
 *
 * @author Santeri Elo
 */
public class RetrofitService {
    private static final long SIZE_OF_CACHE = 15 * 1024 * 1024;

    public void getFood(Observer<RuokaJsonObject> observer, boolean network) {
        Retrofit retrofit = createRetrofit(network);
        FoodApi foodApi = retrofit.create(FoodApi.class);

        foodApi.getFood()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private Retrofit createRetrofit(boolean network) {
        Cache cache = new Cache(RuokaApplication.cacheDir, SIZE_OF_CACHE);
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setCache(cache);
        httpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        httpClient.setReadTimeout(30, TimeUnit.SECONDS);

        if (network) {
            httpClient.networkInterceptors().add(NORMAL_CACHE_INTERCEPTOR);
        } else {
            httpClient.networkInterceptors().add(ONLY_CACHE_INTERCEPTOR);
        }


        String API_ENDPOINT = "http://crappy.email"; // TODO: change to santeri.xyz
        return new Retrofit.Builder()
                .baseUrl(API_ENDPOINT)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private static final Interceptor NORMAL_CACHE_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    // tbh I've no idea what those things do, I hope it works
                    .header("Cache-Control", String.format("max-age=%d, only-if-cached, max-stale=%d", 5184000, 0)) // 60 days, TODO: tweak if needed
                    .build();
        }
    };

    private static final Interceptor ONLY_CACHE_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .header("Cache-Control", String.format("only-if-cached, max-stale=%d", 5184000)) // TODO: tweak if needed
                    .build();
        }
    };
}
