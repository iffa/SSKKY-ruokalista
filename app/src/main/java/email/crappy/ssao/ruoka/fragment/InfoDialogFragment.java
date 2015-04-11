package email.crappy.ssao.ruoka.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * TODO (testing phase): Is this retained on configuration change?
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
        String title = savedInstanceState.getString("title");
        String message = savedInstanceState.getString("message");
        final boolean terminate = savedInstanceState.getBoolean("terminate");

        return new MaterialDialog.Builder(getActivity().getApplicationContext())
                .title(title)
                .content(message)
                .neutralText(android.R.string.ok)
                .cancelable(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        super.onNeutral(dialog);

                        if (terminate) {
                            // TODO: Perhaps handle this better?
                            System.exit(0);
                        }
                    }
                })
                .build();
    }
}
