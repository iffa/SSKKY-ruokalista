package com.kopfgeldjaeger.ratememaybe;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;

public class RateMeMaybeFragment extends DialogFragment {
    private RMMFragInterface mInterface;
    private String title;
    private String message;
    private String positiveBtn;
    private String neutralBtn;
    private String negativeBtn;

    public interface RMMFragInterface {
        void _handlePositiveChoice();

        void _handleNeutralChoice();

        void _handleNegativeChoice();

        void _handleCancel();
    }

    public void setData(int customIcon, String title, String message,
                        String positiveBtn, String neutralBtn, String negativeBtn,
                        RMMFragInterface myInterface) {
        this.title = title;
        this.message = message;
        this.positiveBtn = positiveBtn;
        this.neutralBtn = neutralBtn;
        this.negativeBtn = negativeBtn;
        this.mInterface = myInterface;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fragment including variables will survive orientation changes
        this.setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        builder.title(title);
        builder.content(message);
        builder.positiveText(positiveBtn);
        builder.neutralText(negativeBtn);
        builder.negativeText(neutralBtn);
        builder.cancelable(false);
        builder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);
                mInterface._handlePositiveChoice();

            }

            @Override
            public void onNeutral(MaterialDialog dialog) {
                super.onNeutral(dialog);
                mInterface._handleNegativeChoice();
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                super.onNegative(dialog);
                mInterface._handleNeutralChoice();
            }
        });

        return builder.build();
    }
}