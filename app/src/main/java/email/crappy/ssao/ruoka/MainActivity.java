package email.crappy.ssao.ruoka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.parse.ParseAnalytics;

import de.greenrobot.event.EventBus;
import email.crappy.ssao.ruoka.event.ToolbarMenuEvent;
import email.crappy.ssao.ruoka.mvp.FoodFragment;
import email.crappy.ssao.ruoka.settings.SettingsActivity;

/**
 * @author Santeri 'iffa'
 */
public class MainActivity extends AppCompatActivity {
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

        // Showing a fragment already, don't mess with it
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_content, new FoodFragment())
                    .commit();
        }
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // TODO: Handle in FoodFragment for easier management?
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
