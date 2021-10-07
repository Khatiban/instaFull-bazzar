package com.fury.instafull;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class DialogAbout {

    private Dialog mDialog;
    private ImageView mDialogOKButton;
    private Setting mDialogUniversalInfoActivity;


    public DialogAbout(Setting var1) {
        this.mDialogUniversalInfoActivity = var1;

    }

    private void initDialogButtons() {
        this.mDialogOKButton.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {

                DialogAbout.this.mDialog.dismiss();


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
        this.mDialog.setContentView(R.layout.dialog_about);
        this.mDialog.setCancelable(true);
        this.mDialog.show();
        this.mDialogOKButton = (ImageView) this.mDialog.findViewById(R.id.dialog_universal_info_ok);

        initDialogButtons();
    }
}
