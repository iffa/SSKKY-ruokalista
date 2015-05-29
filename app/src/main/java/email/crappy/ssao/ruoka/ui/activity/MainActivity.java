package email.crappy.ssao.ruoka.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PersistableBundle;
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

import java.io.FileNotFoundException;

import de.greenrobot.event.EventBus;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.RuokaApplication;
import email.crappy.ssao.ruoka.event.EasterEggEvent;
import email.crappy.ssao.ruoka.event.LoadFailEvent;
import email.crappy.ssao.ruoka.event.LoadSuccessEvent;
import email.crappy.ssao.ruoka.event.TogglePinkEvent;
import email.crappy.ssao.ruoka.event.RatingSaveEvent;
import email.crappy.ssao.ruoka.ui.fragment.CardGridFragment;
import email.crappy.ssao.ruoka.ui.fragment.dialog.EasterDialogFragment;
import email.crappy.ssao.ruoka.ui.fragment.dialog.EasterPasswordDialogFragment;
import email.crappy.ssao.ruoka.ui.fragment.dialog.InfoDialogFragment;
import email.crappy.ssao.ruoka.ui.fragment.dialog.LicenseDialogFragment;
import email.crappy.ssao.ruoka.ui.fragment.dialog.RatingDialogFragment;
import email.crappy.ssao.ruoka.ui.fragment.SadFragment;
import email.crappy.ssao.ruoka.ui.fragment.WelcomeFragment;
import email.crappy.ssao.ruoka.pojo.Item;
import email.crappy.ssao.ruoka.util.PojoUtil;
import email.crappy.ssao.ruoka.pojo.Rating;
import email.crappy.ssao.ruoka.pojo.Ruoka;
import email.crappy.ssao.ruoka.util.DateUtil;

/**
 * @author Santeri 'iffa'
 */
public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    BillingProcessor bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing billing & Parse
        bp = new BillingProcessor(this, RuokaApplication.BILLING_KEY, this);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        // Easter egg get!
        if (getPreferences(MODE_PRIVATE).getBoolean("pinkTheme", false) || DateUtil.isValentines()) {
            setTheme(R.style.AppThemeManly);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.manly));
            }

        }

        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        if (getSupportFragmentManager().findFragmentByTag("sadFragment") != null) {
            return;
        }

        // Show welcome screen if data is non-existent (possible first time user)
        if (RuokaApplication.shouldDownloadData()) {
            Logger.d("shouldDownloadData() = true");
            showWelcomeFragment(false);
            return;
        }

        if (RuokaApplication.data == null) {
            Logger.d("data == null");
            try {
                RuokaApplication.data = PojoUtil.generatePojoFromJson(getApplicationContext());

                if (DateUtil.isDataExpired(RuokaApplication.data.getExpiration())) {
                    showWelcomeFragment(true);
                } else {
                    showDataFragment(true);
                }
            } catch (FileNotFoundException e) {
                Logger.e("Tried to generate data from JSON but it failed", e);
            }
        }
    }

    private void showWelcomeFragment(boolean update) {
        WelcomeFragment fragment = new WelcomeFragment();
        Bundle args = new Bundle();
        args.putBoolean("update", update);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrameLayout, fragment, "welcomeFragment").commit();
    }

    private void showDataFragment(boolean anim) {
        CardGridFragment fragment = new CardGridFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (anim) {
            transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit);
        }
        transaction.replace(R.id.fragmentFrameLayout, fragment, "dataFragment").commit();
    }

    private void showErrorFragment() {
        SadFragment fragment = new SadFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit);
        transaction.replace(R.id.fragmentFrameLayout, fragment, "sadFragment").commit();
    }

    public Item getTodayItem() {
        for (Ruoka ruoka : RuokaApplication.data.getRuoka()) {
            for (Item ruokaItem : ruoka.getItems()) {
                if (DateUtil.isToday(ruokaItem.getPvm())) {
                    return ruokaItem;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    public void onEvent(LoadSuccessEvent event) {
        try {
            RuokaApplication.data = PojoUtil.generatePojoFromJson(getApplicationContext());

            if (DateUtil.isDataExpired(RuokaApplication.data.getExpiration())) {
                InfoDialogFragment.newInstance(getResources().getString(R.string.dialog_expired_title), getResources().getString(R.string.dialog_expired_message)).show(getSupportFragmentManager(), "expiredDialog");
                showErrorFragment();
            } else {
                showDataFragment(true);
            }
        } catch (FileNotFoundException e) {
            Logger.d("Tried to generate data from JSON but it failed", e);
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(LoadFailEvent event) {
        InfoDialogFragment.newInstance(getResources().getString(R.string.error), getResources().getString(R.string.load_failed) + "\n\n" + event.reason).show(getSupportFragmentManager(), "errorDialog");
        showErrorFragment();
    }

    @SuppressWarnings("unused")
    public void onEvent(EasterEggEvent event) {
        if (event.getShowEaster()) {
            new EasterDialogFragment().show(getSupportFragmentManager(), "easterDialog");
        } else {
            new EasterPasswordDialogFragment().show(getSupportFragmentManager(), "easterPasswordDialog");
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(TogglePinkEvent event) {
        SharedPreferences.Editor edit = getPreferences(MODE_PRIVATE).edit();
        edit.putBoolean("pinkTheme", !(getPreferences(MODE_PRIVATE).getBoolean("pinkTheme", false)));
        edit.commit();

        RuokaApplication.doRestart(getApplicationContext());
    }

    @SuppressWarnings("unused")
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
            InfoDialogFragment.newInstance(getResources().getString(R.string.dialog_about_title), getResources().getString(R.string.dialog_about_message)).show(getSupportFragmentManager(), "aboutDialog");
            return true;
        } else if (id == R.id.action_licenses) {
            new LicenseDialogFragment().show(getSupportFragmentManager(), "licenseDialog");
            return true;
        } else if (id == R.id.action_donate) {
            bp.loadOwnedPurchasesFromGoogle();
            bp.purchase(this, "donation");
            return true;
        }

        if (RuokaApplication.data == null) {
            return super.onOptionsItemSelected(item);
        }

        if (id == R.id.action_today) {
            Item todayItem = getTodayItem();
            if (todayItem != null) {
                InfoDialogFragment.newInstance(getResources().getString(R.string.dialog_today_title), todayItem.getKama()).show(getSupportFragmentManager(), "todayDialog");
            } else {
                InfoDialogFragment.newInstance(getResources().getString(R.string.dialog_nofood_title), getResources().getString(R.string.dialog_nofood_message)).show(getSupportFragmentManager(), "noFoodDialog");
            }
            return true;
        } else if (id == R.id.action_rate) {
            Item todayItem = getTodayItem();
            if (todayItem == null) {
                InfoDialogFragment.newInstance(getResources().getString(R.string.dialog_nofood_title), getResources().getString(R.string.dialog_nofood_message)).show(getSupportFragmentManager(), "noFoodDialog");
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

                            InfoDialogFragment.newInstance(getResources().getString(R.string.error), getResources().getString(R.string.dialog_parse_error_message)).show(getSupportFragmentManager(), "parseErrorDialog");
                            return;
                        }

                        if (rating == null) {
                            Logger.d("Rating is null in getFirstInBackground");
                            new RatingDialogFragment().show(getSupportFragmentManager(), "ratingDialog");
                        } else {
                            Logger.d("Rating is not null = already exists?");
                            InfoDialogFragment.newInstance(getResources().getString(R.string.error), getResources().getString(R.string.dialog_rated_already_message)).show(getSupportFragmentManager(), "alreadyRatedDialog");
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
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
