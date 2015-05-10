package email.crappy.ssao.ruoka.activity;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.orhanobut.logger.Logger;

import net.grandcentrix.tray.TrayAppPreferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.RuokaApplication;
import email.crappy.ssao.ruoka.event.EasterEggEvent;
import email.crappy.ssao.ruoka.event.LoadFailEvent;
import email.crappy.ssao.ruoka.event.LoadStartEvent;
import email.crappy.ssao.ruoka.event.LoadSuccessEvent;
import email.crappy.ssao.ruoka.event.PinikkiEvent;
import email.crappy.ssao.ruoka.fragment.CardGridFragment;
import email.crappy.ssao.ruoka.fragment.EasterDialogFragment;
import email.crappy.ssao.ruoka.fragment.InfoDialogFragment;
import email.crappy.ssao.ruoka.fragment.LicenseDialogFragment;
import email.crappy.ssao.ruoka.fragment.LoadingDialogFragment;
import email.crappy.ssao.ruoka.fragment.WelcomeFragment;
import email.crappy.ssao.ruoka.network.DataLoader;
import email.crappy.ssao.ruoka.pojo.Item;
import email.crappy.ssao.ruoka.pojo.PojoUtil;
import email.crappy.ssao.ruoka.pojo.Ruoka;
import email.crappy.ssao.ruoka.pojo.RuokaJsonObject;
import icepick.Icepick;
import icepick.Icicle;

/**
 * TODO: Notification every day at 10am
 *
 * @author Santeri 'iffa'
 */
public class MainActivity extends ActionBarActivity implements BillingProcessor.IBillingHandler {
    public static final String FILE_NAME = "Data.json";
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

        // Easter egg get!
        if (appPreferences.getBoolean("easterFun", false)) {
            setTheme(R.style.AppThemeManly);
        }

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSupportActionBar((Toolbar) ButterKnife.findById(this, R.id.toolbar));

        // Show welcome screen if data is non-existent (possible first time user)
        if (shouldDownloadData(getApplicationContext())) {
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

    public void downloadData(boolean showDialog) {
        if (showDialog) {
            LoadingDialogFragment dialog = new LoadingDialogFragment();
            dialog.show(getSupportFragmentManager(), "loadingDialog");
        }

        new DataLoader().loadData(new File(getApplicationContext().getFilesDir(), FILE_NAME).getPath());
    }

    private void showData() {
        CardGridFragment fragment = new CardGridFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit);
        transaction.replace(R.id.fragmentFrameLayout, fragment, "dataFragment").commit();
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
    protected void onPause() {
        super.onPause();
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

        if (data == null) {
            return super.onOptionsItemSelected(item);
        }

        if (id == R.id.action_about) {
            InfoDialogFragment dialog = InfoDialogFragment.newInstance(getResources().getString(R.string.dialog_about_title), getResources().getString(R.string.dialog_about_message), false);
            showDialog(dialog, "aboutDialog");
            return true;
        } else if (id == R.id.action_today) {
            Item todayItem = null;
            for (Ruoka ruoka : data.getRuoka()) {
                for (Item ruokaItem  : ruoka.getItems()) {
                    if(isToday(ruokaItem.getPvm())) {
                        todayItem = ruokaItem;
                        break;
                    }
                }
            }

            if (todayItem != null) {
                InfoDialogFragment dialog = InfoDialogFragment.newInstance(getResources().getString(R.string.dialog_today_title), todayItem.getKama(), false);
                showDialog(dialog, "todayDialog");
            }
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


    /**
     * Checks if local data exists and wether or not it needs to be downloaded
     *
     * @return True if data should be downloaded
     */
    public static boolean shouldDownloadData(Context context) {
        // Checking if data needs to be downloaded (if no local file exists)
        File data = new File(context.getFilesDir(), FILE_NAME);
        return !data.exists();
    }

    public boolean dataExpired() {
        Logger.d("Data expiration date: " + data.getExpiration());
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

    public void onEvent(LoadSuccessEvent event) {
        try {
            data = PojoUtil.generatePojoFromJson(getApplicationContext());

            if (getSupportFragmentManager().findFragmentByTag("loadingDialog") != null) {
                ((LoadingDialogFragment) getSupportFragmentManager().findFragmentByTag("loadingDialog")).dismiss();
            }

            if (dataExpired()) {
                data = null;
                InfoDialogFragment dialog = InfoDialogFragment.newInstance(getResources().getString(R.string.dialog_expired_title), getResources().getString(R.string.dialog_expired_message), true);
                showDialog(dialog, "expiredDialog");
            } else {
                showData();
            }
        } catch (FileNotFoundException e) {
            // Very unlikely to happen
            Logger.d("Tried to generate data from JSON but it failed", e);
        }
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

    public void onEvent(LoadFailEvent event) {
        if (getSupportFragmentManager().findFragmentByTag("loadingDialog") != null) {
            ((LoadingDialogFragment) getSupportFragmentManager().findFragmentByTag("loadingDialog")).dismiss();
        }
        InfoDialogFragment dialog = InfoDialogFragment.newInstance(getResources().getString(R.string.error), getResources().getString(R.string.load_failed) + "\n\n" + event.reason, true);
        showDialog(dialog, "errorDialog");
    }

    // Very manly easter egg get!
    public void onEvent(EasterEggEvent event) {
        EasterDialogFragment dialog = new EasterDialogFragment();
        showDialog(dialog, "easterDialog");
    }

    public void onEvent(PinikkiEvent event) {
        appPreferences.put("easterFun", !appPreferences.getBoolean("easterFun", false));
        System.exit(0);
    }

    public void onEvent(LoadStartEvent event) {
        downloadData(event.getShowDialog());
    }

    void showDialog(DialogFragment fragment, String tag) {
        fragment.show(getSupportFragmentManager(), tag);
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
