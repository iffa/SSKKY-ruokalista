package email.crappy.ssao.ruoka.activity;

import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.orhanobut.logger.Logger;
import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import net.grandcentrix.tray.TrayAppPreferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.RuokaApplication;
import email.crappy.ssao.ruoka.event.EasterEggEvent;
import email.crappy.ssao.ruoka.event.LoadFailEvent;
import email.crappy.ssao.ruoka.event.LoadSuccessEvent;
import email.crappy.ssao.ruoka.event.PinikkiEvent;
import email.crappy.ssao.ruoka.event.RatingSaveEvent;
import email.crappy.ssao.ruoka.fragment.CardGridFragment;
import email.crappy.ssao.ruoka.fragment.EasterDialogFragment;
import email.crappy.ssao.ruoka.fragment.EasterPasswordDialogFragment;
import email.crappy.ssao.ruoka.fragment.InfoDialogFragment;
import email.crappy.ssao.ruoka.fragment.LicenseDialogFragment;
import email.crappy.ssao.ruoka.fragment.RatingDialogFragment;
import email.crappy.ssao.ruoka.fragment.SadFragment;
import email.crappy.ssao.ruoka.fragment.WelcomeFragment;
import email.crappy.ssao.ruoka.pojo.Item;
import email.crappy.ssao.ruoka.pojo.PojoUtil;
import email.crappy.ssao.ruoka.pojo.Rating;
import email.crappy.ssao.ruoka.pojo.Ruoka;
import email.crappy.ssao.ruoka.pojo.RuokaJsonObject;
import icepick.Icepick;
import icepick.Icicle;

/**
 * @author Santeri 'iffa'
 */
public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    public TrayAppPreferences appPreferences;
    BillingProcessor bp;
    @Icicle
    public RuokaJsonObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        // Initializing billing & preferences
        appPreferences = new TrayAppPreferences(this);
        bp = new BillingProcessor(this, RuokaApplication.BILLING_KEY, this);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        // Easter egg get!
        if (appPreferences.getBoolean("easterFun", false)) {
            setTheme(R.style.AppThemeManly);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.manly));
            }

        }

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSupportActionBar((Toolbar) ButterKnife.findById(this, R.id.toolbar));

        // Show welcome screen if data is non-existent (possible first time user)
        if (shouldDownloadData()) {
            WelcomeFragment fragment = new WelcomeFragment();
            Bundle args = new Bundle();
            args.putBoolean("update", false);
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentFrameLayout, fragment, "welcomeFragment").commit();
        }

        // If data is already loaded -> generate POJO -> validate -> etc.
        if (data == null) {
            try {
                data = PojoUtil.generatePojoFromJson(getApplicationContext());

                if (dataExpired()) {
                    data = null;
                    WelcomeFragment fragment = new WelcomeFragment();
                    Bundle args = new Bundle();
                    args.putBoolean("update", true);
                    fragment.setArguments(args);
                    getSupportFragmentManager().beginTransaction().add(R.id.fragmentFrameLayout, fragment, "welcomeFragment").commit();
                } else {
                    showData();
                }
            } catch (FileNotFoundException e) {
                Logger.d("Tried to generate data from JSON but it failed", e);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            InfoDialogFragment dialog = InfoDialogFragment.newInstance(getResources().getString(R.string.dialog_about_title), getResources().getString(R.string.dialog_about_message));
            showDialog(dialog, "aboutDialog");
            return true;
        } else if (id == R.id.action_licenses) {
            LicenseDialogFragment dialog = new LicenseDialogFragment();
            showDialog(dialog, "licenseDialog");
            return true;
        } else if (id == R.id.action_donate) {
            bp.loadOwnedPurchasesFromGoogle();
            bp.purchase(this, "donation");
            return true;
        }

        if (data == null) {
            return super.onOptionsItemSelected(item);
        }

        if (id == R.id.action_today) {
            Item todayItem = getTodayItem();
            if (todayItem != null) {
                InfoDialogFragment dialog = InfoDialogFragment.newInstance(getResources().getString(R.string.dialog_today_title), todayItem.getKama());
                showDialog(dialog, "todayDialog");
            } else {
                InfoDialogFragment dialog = InfoDialogFragment.newInstance(getResources().getString(R.string.dialog_nofood_title), getResources().getString(R.string.dialog_nofood_message));
                showDialog(dialog, "noFoodDialog");
            }
            return true;
        } else if (id == R.id.action_rate) {
            Item todayItem = getTodayItem();
            if (todayItem == null) {
                InfoDialogFragment dialog = InfoDialogFragment.newInstance(getResources().getString(R.string.dialog_nofood_title), getResources().getString(R.string.dialog_nofood_message));
                showDialog(dialog, "noFoodDialog");
            } else {
                ParseQuery<Rating> query = Rating.getQuery();
                query.whereEqualTo("date", todayItem.getPvm());
                query.whereEqualTo("user", ParseUser.getCurrentUser());
                query.getFirstInBackground(new GetCallback<Rating>() {
                    @Override
                    public void done(Rating rating, com.parse.ParseException e) {
                        Logger.d("Got first in background");
                        if (e != null && e.getCode() != com.parse.ParseException.OBJECT_NOT_FOUND) {
                            Logger.d("ParseException: " + e.toString());
                            // ERROR IS BIG!! fukc
                            InfoDialogFragment dialog = InfoDialogFragment.newInstance(getResources().getString(R.string.error), getResources().getString(R.string.dialog_parse_error_message));
                            showDialog(dialog, "parseErrorDialog");
                            return;
                        }

                        if (rating == null) {
                            Logger.d("Rating is null in getFirstInBackground");
                            RatingDialogFragment dialog = new RatingDialogFragment();
                            showDialog(dialog, "ratingDialog");
                        } else {
                            Logger.d("Rating is not null = already exists?");
                            InfoDialogFragment dialog = InfoDialogFragment.newInstance(getResources().getString(R.string.error), getResources().getString(R.string.dialog_rated_already_message));
                            showDialog(dialog, "alreadyRatedDialog");
                        }
                    }
                });
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    public void restartActivity() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void showData() {
        CardGridFragment fragment = new CardGridFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit);
        transaction.replace(R.id.fragmentFrameLayout, fragment, "dataFragment").commit();
    }

    private void showSadFace() {
        SadFragment fragment = new SadFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit);
        transaction.replace(R.id.fragmentFrameLayout, fragment, "sadFragment").commit();
    }

    void showDialog(DialogFragment fragment, String tag) {
        fragment.show(getSupportFragmentManager(), tag);
    }

    public Item getTodayItem() {
        Item todayItem = null;
        for (Ruoka ruoka : data.getRuoka()) {
            for (Item ruokaItem : ruoka.getItems()) {
                if (isToday(ruokaItem.getPvm())) {
                    todayItem = ruokaItem;
                    break;
                }
            }
        }
        return todayItem;
    }

    public static boolean shouldDownloadData() {
        File data = new File(RuokaApplication.DATA_PATH);
        return !data.exists();
    }

    public boolean dataExpired() {
        GregorianCalendar current = new GregorianCalendar();
        GregorianCalendar expire = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        try {
            expire.setTime(sdf.parse(data.getExpiration()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Logger.d("expire: " + expire.get(GregorianCalendar.DAY_OF_MONTH) + " " + expire.get(GregorianCalendar.MONTH) + " " + expire.get(GregorianCalendar.YEAR));
        Logger.d("current: " + current.get(GregorianCalendar.DAY_OF_MONTH) + " " + current.get(GregorianCalendar.MONTH) + " " + current.get(GregorianCalendar.YEAR));

        return current.after(expire);

    }

    public static boolean isToday(String date) {
        int day = Integer.parseInt(date.split("\\.")[0]);
        int month = Integer.parseInt(date.split("\\.")[1]);

        GregorianCalendar calendar = new GregorianCalendar();
        if (calendar.get(Calendar.DAY_OF_MONTH) == day && calendar.get(Calendar.MONTH) == (month - 1)) {
            return true;
        }
        return false;

    }

    public void onEvent(LoadSuccessEvent event) {
        try {
            data = PojoUtil.generatePojoFromJson(getApplicationContext());

            if (dataExpired()) {
                data = null;
                InfoDialogFragment dialog = InfoDialogFragment.newInstance(getResources().getString(R.string.dialog_expired_title), getResources().getString(R.string.dialog_expired_message));
                showDialog(dialog, "expiredDialog");
                showSadFace();
            } else {
                showData();
            }
        } catch (FileNotFoundException e) {
            Logger.d("Tried to generate data from JSON but it failed", e);
        }
    }

    public void onEvent(LoadFailEvent event) {
        InfoDialogFragment dialog = InfoDialogFragment.newInstance(getResources().getString(R.string.error), getResources().getString(R.string.load_failed) + "\n\n" + event.reason);
        showDialog(dialog, "errorDialog");
        showSadFace();
    }

    public void onEvent(EasterEggEvent event) {
        if (event.getShowEaster()) {
            EasterDialogFragment dialog = new EasterDialogFragment();
            showDialog(dialog, "easterDialog");
        } else {
            EasterPasswordDialogFragment dialog = new EasterPasswordDialogFragment();
            showDialog(dialog, "easterPasswordDialog");
        }
    }

    public void onEvent(PinikkiEvent event) {
        appPreferences.put("easterFun", !appPreferences.getBoolean("easterFun", false));
        restartActivity();
    }

    public void onEvent(RatingSaveEvent event) {
        Rating rating = new Rating();
        rating.setUuidString();

        Item todayItem = getTodayItem();
        rating.setDescription(event.getDescription());
        rating.setOpinion(event.getOpinion());
        rating.setDate(todayItem.getPvm());
        rating.setFood(todayItem.getKama());
        rating.setUser(ParseUser.getCurrentUser());

        ParseUser.getCurrentUser().put("rating", rating);
        ParseUser.getCurrentUser().saveInBackground();

        Toast successToast = Toast.makeText(this, R.string.toast_rating, Toast.LENGTH_SHORT);
        successToast.show();
    }

    @Override
    public void onProductPurchased(String s, TransactionDetails transactionDetails) {

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int i, Throwable throwable) {

    }

    @Override
    public void onBillingInitialized() {

    }
}
