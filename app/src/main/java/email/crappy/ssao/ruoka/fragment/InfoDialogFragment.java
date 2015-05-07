package email.crappy.ssao.ruoka.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;

/**
 * Generic DialogFragment for showing a title and a message with an OK-button.
 * Additional boolean if app needs to be terminated right after the dialog (for errors)
 *
 * @author Santeri 'iffa'
 */
public class InfoDialogFragment extends DialogFragment {
    /**
     * Returns an instance of InfoDialogFragment with the specified title and message
     * @param title Title
     * @param message Message
     * @param terminate True if app should terminate after the dialog is dealt with
     * @return InfoDialogFragment with given title and message
     */
    public static InfoDialogFragment newInstance(String title, String message, boolean terminate) {
        InfoDialogFragment f = new InfoDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        args.putBoolean("terminate", terminate);
        f.setArguments(args);
        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() == null) {
            Logger.d("getArguments() == null... why");
        }

        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        final boolean terminate = getArguments().getBoolean("terminate");

        return new MaterialDialog.Builder(getActivity())
                .title(title)
                .content(message)
                .positiveText(android.R.string.ok)
                .cancelable(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);

                        if (terminate) {
                            // TODO: Perhaps handle this better?
                            System.exit(0);
                        }
                    }
                })
                .build();
    }
}
