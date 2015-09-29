package email.crappy.ssao.ruoka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.kopfgeldjaeger.ratememaybe.RateMeMaybe;
import com.orhanobut.logger.Logger;
import com.parse.ParseAnalytics;

import de.greenrobot.event.EventBus;
import email.crappy.ssao.ruoka.event.ToolbarMenuEvent;
import email.crappy.ssao.ruoka.mvp.FoodFragment;
import email.crappy.ssao.ruoka.settings.SettingsActivity;

/**
 * @author Santeri 'iffa'
 */
public class MainActivity extends AppCompatActivity implements RateMeMaybe.OnRMMUserChoiceListener {
    public static boolean EASTER_PINK_THEME = false;
    public static boolean EASTER_YOLO = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing Parse
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        // Initialize content & theme
        setTheme();
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        // Initializing RMM
        initializeRMM();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_content, new FoodFragment())
                    .commit();
        }
    }

    private void initializeRMM() {
        //RateMeMaybe.resetData(this);
        RateMeMaybe rmm = new RateMeMaybe(this);
        rmm.setPromptMinimums(20, 10, 20, 10);
        rmm.setAdditionalListener(this);
        rmm.setDialogTitle(getResources().getString(R.string.rate_title));
        rmm.setDialogMessage(getResources().getString(R.string.rate_message));
        rmm.setPositiveBtn(getResources().getString(R.string.rate_btn_positive));
        rmm.setNeutralBtn(getResources().getString(R.string.rate_btn_neutral));
        rmm.setNegativeBtn(getResources().getString(R.string.rate_btn_negative));
        rmm.run();
    }


    @Override
    public void handlePositive() {
        Logger.d("User clicked positive in RMM");
    }

    @Override
    public void handleNeutral() {
        Logger.d("User clicked neutral in RMM");
    }

    @Override
    public void handleNegative() {
        Logger.d("User clicked negative in RMM");
        Snackbar.make(findViewById(R.id.frame_content), R.string.rate_snackbar_negative, Snackbar.LENGTH_LONG).show();
    }

    private void setTheme() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        int theme = Integer.parseInt(sp.getString(SettingsActivity.KEY_THEME, "1"));
        switch (theme) {
            case 1:
                EASTER_PINK_THEME = false;
                EASTER_YOLO = false;
                break;
            case 2:
                setTheme(R.style.AppThemeManly);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.manly));
                }
                EASTER_PINK_THEME = true;
                break;
            case 3:
                setTheme(R.style.AppThemeYolo);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.yolo));
                }
                EASTER_YOLO = true;
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        } else if (id == R.id.action_today) {
            EventBus.getDefault().post(new ToolbarMenuEvent(ToolbarMenuEvent.FOOD_TODAY_DIALOG));
            return true;
        } else if (id == R.id.action_rate) {
            EventBus.getDefault().post(new ToolbarMenuEvent(ToolbarMenuEvent.RATE_TODAY_DIALOG));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
