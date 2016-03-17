package email.crappy.ssao.ruoka.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.ui.base.BaseActivity;

/**
 * @author Santeri 'iffa'
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        setContentView(R.layout.activity_main);
    }
}
