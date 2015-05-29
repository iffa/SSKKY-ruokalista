package email.crappy.ssao.ruoka.ui.fragment.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import de.greenrobot.event.EventBus;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.event.TogglePinkEvent;

/**
 * @author Santeri 'iffa'
 */
public class EasterDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MaterialDialog.Builder(getActivity())
                .items(R.array.dialog_easter_items)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse("https://www.youtube.com/watch?v=y6120QOlsfU"));
                                startActivity(i);
                                break;
                            case 1:
                                EventBus.getDefault().post(new TogglePinkEvent());
                                break;
                            case 2:
                                Intent kevo = new Intent(Intent.ACTION_VIEW);
                                kevo.setData(Uri.parse("http://bigassmessage.com/0bff0"));
                                startActivity(kevo);
                                break;
                        }
                        return true;
                    }
                })
                .positiveText(R.string.dialog_easter_choose)
                .show();

    }
}
