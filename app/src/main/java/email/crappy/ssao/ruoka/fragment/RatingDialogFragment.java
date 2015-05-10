package email.crappy.ssao.ruoka.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import email.crappy.ssao.ruoka.R;

/**
 * @author Santeri 'iffa'
 */
public class RatingDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MaterialDialog.Builder(getActivity())
                .title(R.string.dialog_rating_title)
                .positiveText(R.string.dialog_rating_positive)
                .negativeText(R.string.dialog_rating_negative)
                .cancelable(true)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);

                        // TODO: Send data
                        View view = dialog.getCustomView();

                        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.ratingRadioGroup);
                        int checkedRadio = radioGroup.getCheckedRadioButtonId();

                        EditText ratingEditText = (EditText) view.findViewById(R.id.ratingText);

                        Toast successToast = Toast.makeText(getActivity(), R.string.toast_rating, Toast.LENGTH_SHORT);
                        successToast.show();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .customView(R.layout.dialog_rate, true)
                .build();
    }
}
