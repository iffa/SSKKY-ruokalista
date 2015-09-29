package email.crappy.ssao.ruoka.model;

import retrofit.http.GET;
import rx.Observable;

/**
 * @author Santeri Elo
 */
public interface FoodApi {
    @GET("/ruoka.json")
    Observable<RuokaJsonObject> getFood();

    @GET("/ruoka_debug.json")
    Observable<RuokaJsonObject> getFoodDebug();
}
