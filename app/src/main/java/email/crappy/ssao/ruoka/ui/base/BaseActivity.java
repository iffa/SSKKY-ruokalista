package email.crappy.ssao.ruoka.ui.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.SSKKYApplication;
import email.crappy.ssao.ruoka.injection.component.ActivityComponent;
import email.crappy.ssao.ruoka.injection.module.ActivityModule;

/**
 * @author Santeri 'iffa'
 */
@SuppressWarnings("CreateIntent")
public class BaseActivity extends AppCompatActivity {
    private ActivityComponent activityComponent;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    protected Toolbar getToolbar() {
        return toolbar;
    }

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = SSKKYApplication.getInstance(this)
                    .getComponent()
                    .plus(new ActivityModule(this));
        }
        return activityComponent;
    }
}
