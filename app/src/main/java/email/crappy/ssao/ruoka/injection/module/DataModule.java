package email.crappy.ssao.ruoka.injection.module;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Rfc3339DateJsonAdapter;
import com.squareup.moshi.Types;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import email.crappy.ssao.ruoka.data.FoodService;
import email.crappy.ssao.ruoka.data.model.FoodItem;

/**
 * @author Santeri Elo
 */
@Module
public class DataModule {
    @Provides
    @Singleton
    public Moshi provideMoshi() {
        return new Moshi.Builder().add(Date.class, new Rfc3339DateJsonAdapter()).build();
    }

    @Provides
    @Singleton
    public JsonAdapter<List<FoodItem>> provideJsonAdapter(Moshi moshi) {
        Type adapterType = Types.newParameterizedType(List.class, FoodItem.class);

        return moshi.adapter(adapterType);
    }

    @Provides
    @Singleton
    public FoodService provideFoodService(Moshi moshi) {
        return FoodService.Builder.create(moshi);
    }
}
