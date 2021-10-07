package com.fury.instafull;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class DialogCoinSettingCoin {

    private Dialog mDialog;
    private TextView mDialogOKButton;
    private TextView machmoe_gift;
    private TextView dialog_universal_info_title;
    private TextView dialog_universal_info_text;
    private TextView dialog_universal_info_cancel;
    private Setting mDialogUniversalInfoActivity;
    private String gift2,title2,text2,bok,bca;
    private boolean n2;
    private int i2;

    public DialogCoinSettingCoin(Setting var1,String title,String text, String gift, String bo,String bc,boolean n,int i) {
        this.mDialogUniversalInfoActivity = var1;
        gift2 = gift;
        title2 = title;
        text2 = text;
        bok = bo;
        bca = bc;
        n2 = n;
        i2 = i;

    }

    private void initDialogButtons() {
        dialog_universal_info_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                    DialogCoinSettingCoin.this.mDialog.dismiss();
                if (i2 == 1){
                    mDialogUniversalInfoActivity.auto_on.setChecked(false);
                }else if (i2 == 1000){
                    mDialogUniversalInfoActivity.auto_on.setChecked(false);
                }
            }
        });
        this.mDialogOKButton.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
                if (n2){
                    mDialogUniversalInfoActivity.one_play_editor.putBoolean("AUTO_ON", true);
                    mDialogUniversalInfoActivity.one_play_editor.apply();
                    if (i2 == 1){
                        mDialogUniversalInfoActivity.auto_on.setChecked(true);
                        mDialogUniversalInfoActivity.coin = mDialogUniversalInfoActivity.coin - 5000;
                        mDialogUniversalInfoActivity.coinint = String.valueOf(mDialogUniversalInfoActivity.coin);
                        mDialogUniversalInfoActivity.CoinSingle.setText(mDialogUniversalInfoActivity.coinint);
                        mDialogUniversalInfoActivity.one_play_editor.putInt("COIN", mDialogUniversalInfoActivity.coin);
                        mDialogUniversalInfoActivity.one_play_editor.commit();
                    }else if (i2 == 1000){
                        mDialogUniversalInfoActivity.auto_on.setChecked(true);
                    }
                    DialogCoinSettingCoin.this.mDialog.dismiss();
                }else {
                    Intent s = new Intent(mDialogUniversalInfoActivity, Store.class);
                    mDialogUniversalInfoActivity.startActivity(s);
                    DialogCoinSettingCoin.this.mDialog.dismiss();
                }

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
        this.mDialog.setContentView(R.layout.dialog_universal_setting);
        this.mDialog.setCancelable(true);
        this.mDialog.show();
        this.mDialogOKButton = (TextView) this.mDialog.findViewById(R.id.dialog_universal_info_ok);
        this.machmoe_gift = (TextView) this.mDialog.findViewById(R.id.machmoe_gift);
        this.dialog_universal_info_title = (TextView) this.mDialog.findViewById(R.id.dialog_universal_info_title);
        this.dialog_universal_info_text = (TextView) this.mDialog.findViewById(R.id.dialog_universal_info_text);
        this.dialog_universal_info_cancel = (TextView) this.mDialog.findViewById(R.id.dialog_universal_info_cancel);

        machmoe_gift.setText(gift2);
        dialog_universal_info_title.setText(title2);
        dialog_universal_info_text.setText(text2);
        mDialogOKButton.setText(bok);
        dialog_universal_info_cancel.setText(bca);

        initDialogButtons();
    }
}
