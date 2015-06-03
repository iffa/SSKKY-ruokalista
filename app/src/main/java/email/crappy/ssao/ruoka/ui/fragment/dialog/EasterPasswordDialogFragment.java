package email.crappy.ssao.ruoka.ui.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import de.greenrobot.event.EventBus;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.event.EasterEggEvent;

/**
 * @author Santeri 'iffa'
 */
public class EasterPasswordDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MaterialDialog.Builder(getActivity())
                .title(R.string.dialog_password_title)
                .autoDismiss(false)
                .negativeText(R.string.cancel)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .inputMaxLength(16)
                .input(getResources().getString(R.string.dialog_password_hint), "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (input.toString().equals("ict14amasterrace")) {
                            EventBus.getDefault().post(new EasterEggEvent(true));
                            dialog.dismiss();
                        } else {
                            Toast toast = Toast.makeText(getActivity(), R.string.toast_wrong_password, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                })
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                    }
                })
                .build();

    }
}
