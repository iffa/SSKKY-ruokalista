package email.crappy.ssao.ruoka.ui.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.SSKKYApplication;
import email.crappy.ssao.ruoka.injection.component.ActivityComponent;
import email.crappy.ssao.ruoka.injection.component.DaggerActivityComponent;
import email.crappy.ssao.ruoka.injection.module.ActivityModule;

/**
 * @author Santeri 'iffa'
 */
public class BaseActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    @Nullable
    protected Toolbar toolbar;
    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int theme = SSKKYApplication.get(this).getComponent().dataManager().getPreferencesHelper().getThemeRes();
        setTheme(theme);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);

        setupToolbar();
    }

    private void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Nullable
    protected Toolbar getToolbar() {
        return toolbar;
    }

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(SSKKYApplication.get(this).getComponent())
                    .build();
        }
        return activityComponent;
    }
}
