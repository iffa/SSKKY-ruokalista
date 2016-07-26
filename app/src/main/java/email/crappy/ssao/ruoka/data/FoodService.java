package email.crappy.ssao.ruoka.data;

import com.squareup.moshi.Moshi;

import java.util.List;

import email.crappy.ssao.ruoka.data.model.FoodItem;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.GET;
import rx.Observable;

/**
 * @author Santeri 'iffa'
 */
public interface FoodService {
    String ENDPOINT = "https://santeri.xyz/";

    @GET("sskky.json")
    Observable<List<FoodItem>> getList();

    class Builder {
        public static FoodService create(Moshi moshi) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            return retrofit.create(FoodService.class);
        }
    }
}
