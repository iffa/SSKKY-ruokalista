package email.crappy.ssao.ruoka.ui.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.event.ShareFoodEvent;

/**
 * @author Santeri 'iffa'
 */
public class ShareFoodDialogFragment extends DialogFragment {
    public static ShareFoodDialogFragment newInstance(ArrayList<String> dates) {
        ShareFoodDialogFragment f = new ShareFoodDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("dates", dates);
        f.setArguments(args);
        return f;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ArrayList<String> dates = getArguments().getStringArrayList("dates");
        final CharSequence[] chars = dates.toArray(new CharSequence[dates.size()]);
        return new MaterialDialog.Builder(getActivity())
                .items(chars)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        EventBus.getDefault().post(new ShareFoodEvent(charSequence));
                    }
                })
                .title(R.string.dialog_share_description)
                .build();
    }
}
