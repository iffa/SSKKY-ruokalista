package email.crappy.ssao.ruoka.fragment;

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
import email.crappy.ssao.ruoka.event.PinikkiEvent;

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
                                i.setData(Uri.parse("https://www.youtube.com/watch?v=LtRp79MQfV4"));
                                startActivity(i);
                                break;
                            case 1:
                                // Pinikki get!
                                EventBus.getDefault().post(new PinikkiEvent());
                                break;
                            case 2:
                                System.exit(0);
                                break;
                            case 3:
                                Intent kevo = new Intent(Intent.ACTION_VIEW);
                                kevo.setData(Uri.parse("http://bigassmessage.com/0bff0"));
                                startActivity(kevo);
                                break;
                            case 4:
                                // So wrong, but still so funny
                                Intent i2 = new Intent(Intent.ACTION_VIEW);
                                i2.setData(Uri.parse("https://www.youtube.com/watch?v=-1GHogrBLTE"));
                                startActivity(i2);
                                break;
                        }
                        return true;
                    }
                })
                .positiveText(R.string.dialog_easter_choose)
                .show();

    }
}
