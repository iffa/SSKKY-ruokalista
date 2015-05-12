package email.crappy.ssao.ruoka.network;

import android.net.Uri;

import com.orhanobut.logger.Logger;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import de.greenrobot.event.EventBus;
import email.crappy.ssao.ruoka.event.LoadFailEvent;
import email.crappy.ssao.ruoka.event.LoadSuccessEvent;

/**
 * @author Santeri 'iffa'
 */
public class DataLoader implements DownloadStatusListener {
    private static final String DATA_URL = "http://crappy.email/ruoka.json";
    ThinDownloadManager downloadManager;
    private static final int DOWNLOAD_THREAD_POOL_SIZE = 2;

    public void loadData(String destPath) {
        Uri downloadUri = Uri.parse(DATA_URL);
        Uri destinationUri = Uri.parse(destPath);
        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .setDestinationURI(destinationUri)
                .setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(this);

        Logger.d("Downloading data from " + DATA_URL);

        downloadManager = new ThinDownloadManager(DOWNLOAD_THREAD_POOL_SIZE);
        int downloadId = downloadManager.add(downloadRequest);

    }

    @Override
    public void onDownloadComplete(int i) {
        Logger.d("Data download completed");
        EventBus.getDefault().post(new LoadSuccessEvent());
    }

    @Override
    public void onDownloadFailed(int i, int i1, String s) {
        Logger.d("Data download failed", s);
        EventBus.getDefault().post(new LoadFailEvent(s));
    }

    @Override
    public void onProgress(int i, long l, int i1) {
        Logger.d("Loaded " + i1 + " of the data");
    }
}
