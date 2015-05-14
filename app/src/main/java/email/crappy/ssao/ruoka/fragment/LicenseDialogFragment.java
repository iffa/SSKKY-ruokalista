package email.crappy.ssao.ruoka.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import email.crappy.ssao.ruoka.R;

/**
 * @author Santeri 'iffa'
 */
public class LicenseDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MaterialDialog.Builder(getActivity())
                .title(R.string.dialog_licenses_title)
                .content(R.string.dialog_licenses_message)
                .items(R.array.dialog_licenses_items)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        switch (i) {
                            case 0:
                                intent.setData(Uri.parse("http://jakewharton.github.io/butterknife/"));
                                break;
                            case 1:
                                intent.setData(Uri.parse("https://github.com/orhanobut/logger/"));
                                break;
                            case 2:
                                intent.setData(Uri.parse("http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_9.html#License"));
                                break;
                            case 3:
                                intent.setData(Uri.parse("https://code.google.com/p/google-gson/"));
                                break;
                            case 4:
                                intent.setData(Uri.parse("https://github.com/frankiesardo/icepick/"));
                                break;
                            case 5:
                                intent.setData(Uri.parse("https://github.com/greenrobot/EventBus/"));
                                break;
                            case 6:
                                intent.setData(Uri.parse("https://github.com/smanikandan14/ThinDownloadManager/"));
                                break;
                            case 7:
                                intent.setData(Uri.parse("https://github.com/afollestad/material-dialogs/"));
                                break;
                            case 8:
                                intent.setData(Uri.parse("https://github.com/geftimov/android-player/"));
                                break;
                            case 9:
                                intent.setData(Uri.parse("https://github.com/rahatarmanahmed/CircularProgressView/"));
                                break;
                            case 10:
                                intent.setData(Uri.parse("https://github.com/gabrielemariotti/cardslib/"));
                                break;
                            case 11:
                                intent.setData(Uri.parse("https://github.com/grandcentrix/tray/"));
                                break;
                            case 12:
                                intent.setData(Uri.parse("https://github.com/anjlab/android-inapp-billing-v3/"));
                                break;
                            case 13:
                                intent.setData(Uri.parse("https://github.com/skyfishjy/android-ripple-background"));
                                break;
                        }
                        startActivity(intent);
                    }
                })
                .autoDismiss(false)
                .show();

    }
}
