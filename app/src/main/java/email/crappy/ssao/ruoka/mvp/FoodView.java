package email.crappy.ssao.ruoka.mvp;

import android.content.SharedPreferences;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import email.crappy.ssao.ruoka.model.Ruoka;

/**
 * @author Santeri Elo
 */
public interface FoodView extends MvpLceView<List<Ruoka>> {
    public SharedPreferences getPreferences();
}
