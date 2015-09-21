package email.crappy.ssao.ruoka.ui.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import de.greenrobot.event.EventBus;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.event.RatingSaveEvent;
import email.crappy.ssao.ruoka.model.Rating;

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
                .autoDismiss(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        View view = dialog.getCustomView();

                        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.ratingRadioGroup);
                        int checkedRadio = radioGroup.getCheckedRadioButtonId();

                        EditText ratingEditText = (EditText) view.findViewById(R.id.ratingText);

                        Rating rating = new Rating();
                        rating.setUuidString();

                        String opinion;
                        String description = ratingEditText.getText().toString();
                        switch (checkedRadio) {
                            case R.id.ratingPositiveRadio:
                                opinion = "POSITIVE";
                                break;
                            case R.id.ratingNegativeRadio:
                                opinion = "NEGATIVE";
                                break;
                            case R.id.ratingNeutralRadio:
                                opinion = "NEUTRAL";
                                break;
                            default:
                                Toast fillRadioToast = Toast.makeText(getActivity(), R.string.toast_fill_radio, Toast.LENGTH_LONG);
                                fillRadioToast.show();
                                return;
                        }

                        EventBus.getDefault().post(new RatingSaveEvent(description, opinion));
                        dialog.dismiss();
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
