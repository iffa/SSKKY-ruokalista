package email.crappy.ssao.ruoka.activity;

import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.event.LoadFailEvent;
import email.crappy.ssao.ruoka.event.LoadSuccessEvent;
import email.crappy.ssao.ruoka.fragment.InfoDialogFragment;
import email.crappy.ssao.ruoka.network.DataLoader;
import email.crappy.ssao.ruoka.pojo.PojoUtil;
import email.crappy.ssao.ruoka.pojo.RuokaJsonObject;
import icepick.Icepick;
import icepick.Icicle;

/**
 * @author Santeri 'iffa'
 */
public class MainActivity extends ActionBarActivity {
    private static final String FILE_NAME = "Data.json";
    @Icicle
    RuokaJsonObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSupportActionBar((Toolbar)ButterKnife.findById(this, R.id.toolbar));

        // If data should be downloaded -> load it
        // If data is already loaded -> generate POJO -> validate -> etc.
        if (shouldDownloadData()) {
            new DataLoader().loadData(new File(getApplicationContext().getFilesDir(), FILE_NAME).getPath());
        } else {
            try {
                data = PojoUtil.generatePojoFromJson(new File(getApplicationContext().getFilesDir(), FILE_NAME));
            } catch (FileNotFoundException e) {
                // TODO: Show error dialog/exit
                e.printStackTrace();
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Icepick.saveInstanceState(this, outState);
    }

    /**
     * Checks if local data exists and wether or not it needs to be downloaded
     * @return True if data should be downloaded
     */
    private boolean shouldDownloadData() {
        // Checking if data needs to be downloaded (if no local file exists)
        File data = new File(getApplicationContext().getFilesDir(), FILE_NAME);
        return !data.exists();
    }

    public void onEvent(LoadSuccessEvent event) {
        try {
            data = PojoUtil.generatePojoFromJson(new File(getApplicationContext().getFilesDir(), FILE_NAME));
        } catch (FileNotFoundException e) {
            // TODO: Show error dialog/exit
            e.printStackTrace();
        }
    }

    public void onEvent(LoadFailEvent event) {
        InfoDialogFragment dialog = InfoDialogFragment.newInstance(getResources().getString(R.string.error), getResources().getString(R.string.load_failed) + event.reason, true);
        dialog.show(getSupportFragmentManager(), "errorDialog");
    }
}
