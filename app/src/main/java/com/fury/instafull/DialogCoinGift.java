package com.fury.instafull;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class DialogCoinGift {

    private Dialog mDialog;
    private TextView mDialogOKButton;
    private TextView machmoe_gift;
    private TextView tedad_gift;
    private Store mDialogUniversalInfoActivity;
    private String gift2,kol2;


    public DialogCoinGift(Store var1,String gift,String kol) {
        this.mDialogUniversalInfoActivity = var1;
        kol2 = kol;
        gift2 = gift;
    }

    private void initDialogButtons() {
        this.mDialogOKButton.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {

                DialogCoinGift.this.mDialog.dismiss();

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
        this.mDialog.setContentView(R.layout.dialog_universal_gift);
        this.mDialog.setCancelable(true);
        this.mDialog.show();
        this.mDialogOKButton = (TextView) this.mDialog.findViewById(R.id.dialog_universal_info_ok);
        this.machmoe_gift = (TextView) this.mDialog.findViewById(R.id.machmoe_gift);
        this.tedad_gift = (TextView) this.mDialog.findViewById(R.id.tedad_gift);

        machmoe_gift.setText(gift2);
        tedad_gift.setText(kol2);

        initDialogButtons();
    }
}
