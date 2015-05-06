package email.crappy.ssao.ruoka.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;

import email.crappy.ssao.ruoka.R;

/**
 * @author Santeri 'iffa'
 */
public class LoadingDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new MaterialDialog.Builder(getActivity())
                .title(R.string.dialog_progress_title)
                .content(R.string.dialog_progress_content)
                .progress(true, 0)
                .build();
    }
}
