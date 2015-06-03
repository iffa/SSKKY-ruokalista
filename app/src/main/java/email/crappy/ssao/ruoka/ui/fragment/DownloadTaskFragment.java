package email.crappy.ssao.ruoka.ui.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.orhanobut.logger.Logger;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import de.greenrobot.event.EventBus;
import email.crappy.ssao.ruoka.RuokaApplication;
import email.crappy.ssao.ruoka.event.DownloadCompleteEvent;

/**
 * @author Santeri 'iffa'
 */
public class DownloadTaskFragment extends Fragment implements DownloadStatusListener {
    private static final String DATA_URL = "http://crappy.email/ruoka.json";
    ThinDownloadManager downloadManager;
    private static final int DOWNLOAD_THREAD_POOL_SIZE = 2;
    private boolean mRunning;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        downloadManager = new ThinDownloadManager(DOWNLOAD_THREAD_POOL_SIZE);
    }

    public void loadData() {
        Uri downloadUri = Uri.parse(DATA_URL);
        Uri destinationUri = Uri.parse(RuokaApplication.DATA_PATH);
        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .setDestinationURI(destinationUri)
                .setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(this);

        Logger.d("Downloading data from " + DATA_URL);
        downloadManager.add(downloadRequest);
    }

    public void setRunning(boolean running) {
        mRunning = running;
    }

    public boolean isRunning() {
        return mRunning;
    }

    @Override
    public void onDownloadComplete(int i) {
        mRunning = false;
        EventBus.getDefault().post(new DownloadCompleteEvent(true, null));
    }

    @Override
    public void onDownloadFailed(int i, int i1, String reason) {
        mRunning = false;
        EventBus.getDefault().post(new DownloadCompleteEvent(false, reason));
    }

    @Override
    public void onProgress(int i, long l, int i1) {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setTargetFragment(null, -1);
    }
}
