package com.fury.instafull;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class DialogOneAd {

    private Dialog mDialog;
    private TextView mDialogOKButton;
    private AdVideo mDialogUniversalInfoActivity;


    public DialogOneAd(AdVideo var1) {
        this.mDialogUniversalInfoActivity = var1;

    }

    private void initDialogButtons() {
        this.mDialogOKButton.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
                DialogOneAd.this.mDialog.dismiss();
            }
        });
    }


    public void dismissDialog() {
        this.mDialog.dismiss();
    }

    public void showDialog() {
        if (this.mDialog == null) {
            this.mDialog = new Dialog(this.mDialogUniversalInfoActivity, R.style.CustomDialogTheme);
        }
        this.mDialog.setContentView(R.layout.dialog_universal_adcolony);
        this.mDialog.setCancelable(true);
        this.mDialog.show();
        this.mDialogOKButton = (TextView) this.mDialog.findViewById(R.id.dialog_universal_info_ok);

        initDialogButtons();
    }
}
