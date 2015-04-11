package email.crappy.ssao.ruoka.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * @author Santeri 'iffa'
 */
public class InfoDialogFragment extends DialogFragment {
    /**
     * Returns an instance of InfoDialogFragment with the specified title and message
     * @param title Title
     * @param message Message
     * @return InfoDialogFragment with given title and message
     */
    public static InfoDialogFragment newInstance(String title, String message) {
        InfoDialogFragment f = new InfoDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        f.setArguments(args);
        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = savedInstanceState.getString("title");
        String message = savedInstanceState.getString("message");

        return new MaterialDialog.Builder(getActivity().getApplicationContext())
                .title(title)
                .content(message)
                .positiveText(android.R.string.ok)
                .build();
    }
}
