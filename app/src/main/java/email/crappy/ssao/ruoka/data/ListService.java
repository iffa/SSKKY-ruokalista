package email.crappy.ssao.ruoka.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import email.crappy.ssao.ruoka.data.model.ListResponse;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;

/**
 * @author Santeri 'iffa'
 */
public interface ListService {
    String ENDPOINT = "https://santeri.xyz/app/";

    @GET("sskky")
    Observable<ListResponse> getList();

    class Builder {
        public static ListService create() {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            return retrofit.create(ListService.class);
        }
    }
}
