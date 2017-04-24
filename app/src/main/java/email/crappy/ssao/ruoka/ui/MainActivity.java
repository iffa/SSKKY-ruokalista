package email.crappy.ssao.ruoka.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.lukaspili.reactivebilling.ReactiveBilling;
import com.github.lukaspili.reactivebilling.model.PurchaseType;

import javax.inject.Inject;

import butterknife.BindView;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.data.DataManager;
import email.crappy.ssao.ruoka.ui.base.BaseActivity;
import email.crappy.ssao.ruoka.ui.home.HomeFragment;
import email.crappy.ssao.ruoka.ui.settings.SettingsActivity;
import rx.Subscription;
import timber.log.Timber;

/**
 * @author Santeri 'iffa'
 */
public class MainActivity extends BaseActivity {
    private Subscription billingSubscription;
    private static final String BILLING_ID = "feelgood";

    @Inject
    DataManager dataManager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.pager)
    ViewPager viewPager;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getComponent().inject(this);

        if (dataManager.getPreferencesHelper().getIsMadde()) {
            // TODO: Make this easter egg more fun
            setTheme(R.style.Theme_SSKKY_Madde);
        }

        setContentView(R.layout.activity_main);

        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            /*
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, HomeFragment.newInstance())
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .commit();
            */

            dataManager.setAlarm(this);
        }

        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(viewPager);

        billingSubscription = ReactiveBilling.getInstance(this).purchaseFlow()
                .subscribe(response -> {
                    if (response.isSuccess()) {
                        Timber.i("Product purchased succesfully - $$$");
                        response.getPurchase();
                    } else {
                        Timber.e("Product purchase failed");
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (billingSubscription != null) {
            billingSubscription.unsubscribe();
            billingSubscription = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(dataManager.getPreferencesHelper().getIsDebug() ? R.menu.activity_main_debug : R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(SettingsActivity.createIntent(this));
                break;
            case R.id.action_donate:
                ReactiveBilling.getInstance(this)
                        .startPurchase(BILLING_ID, PurchaseType.PRODUCT, null, null)
                        .compose(dataManager.schedulers())
                        .subscribe(response -> {
                            if (response.isSuccess()) {
                                Timber.d("Purchase flow started");
                            } else {
                                Timber.e("Couldn't start purchase flow");
                            }
                        }, throwable -> Timber.e(throwable, "Couldn't start purchase flow"));
                break;
            case R.id.action_clear:
                dataManager.getPreferencesHelper().clear();
                startActivity(createIntent(this));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class TabAdapter extends FragmentStatePagerAdapter {
        private static final int TABS = 2;
        private final Context context;

        TabAdapter(FragmentManager fm, Context context) {
            super(fm);

            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HomeFragment.newInstance(HomeFragment.MAIN);
                case 1:
                    return HomeFragment.newInstance(HomeFragment.POUKAMA);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return TABS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return context.getString(R.string.tab_main);
                case 1:
                    return context.getString(R.string.tab_poukama);
                default:
                    return context.getString(R.string.tab_main);
            }
        }
    }
}
