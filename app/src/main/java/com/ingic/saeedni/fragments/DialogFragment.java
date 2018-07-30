package com.ingic.saeedni.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ingic.saeedni.R;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;

public class DialogFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener {




    private BaseFragment fragment;
    private String tag;

    private String title;
    private String text1;
    private String text2;
    private String text3;

    private boolean isShow_btndialog_1 = false;
    private boolean isShow_btndialog_2 = false;

    private View.OnClickListener dialogBtnListener1;
    private View.OnClickListener dialogBtnListener2;


    public static DialogFragment newInstance() {

        return new DialogFragment();
    }

    public void setFragment(BaseFragment fragment, String tag){
        this.fragment = fragment;
        this.tag = tag;
    }

    public void setPopupData(String title, String text1, String text2, String text3, boolean isShow_btndialog_1, boolean isShow_btndialog_2) {

        this.title = title;

        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.isShow_btndialog_1 = isShow_btndialog_1;
        this.isShow_btndialog_2 = isShow_btndialog_2;

    }

    public void setbtndialog_1_Listener(View.OnClickListener dialogBtnListener1){
        this.dialogBtnListener1 = dialogBtnListener1;
    }

    public void setbtndialog_2_Listener(View.OnClickListener dialogBtnListener2){
        this.dialogBtnListener2 = dialogBtnListener2;
    }

    public void hideButtons(){
      //  btndialog_1.setVisibility(View.GONE);
        //btndialog_2.setVisibility(View.GONE);
    }

    public void showBtndialog_1(){
     //   btndialog_1.setVisibility(View.VISIBLE);
    }

    public void showBtndialog_2(){
    /*    btndialog_2.setVisibility(View.VISIBLE);
        btndialog_2.setBackgroundResource(R.drawable.next_bttn);
        btndialog_2.setText(getString(R.string.sign_up_as_trainer));

        btndialog_1.setBackgroundResource(R.drawable.next_bttn);
        btndialog_1.setText(getString(R.string.sign_up_as_trainee));*/

    }

    public void dismissDialog(){

        DialogFragment.this.dismiss();

    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //hideButtons();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_layout, container, false);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        hideButtons();
        //setDialogText();
        setListener();

    }

  /*  private void setDialogText() {
        txtHeader.setText(title);

        if(text1.length() > 0){
            txt1.setVisibility(View.VISIBLE);
            txt1.setText(text1);
        }

        if(text2.length() > 0){
            txt2.setVisibility(View.VISIBLE);
            txt2.setText(text2);
        }

        if(text3.length() > 0){
            txt3.setVisibility(View.VISIBLE);
            txt3.setText(text3);
        }

        if(isShow_btndialog_1){
            showBtndialog_1();
        }

        if(isShow_btndialog_2){
            showBtndialog_2();
        }

    }*/

   /* public void showFragment() {
        android.support.v4.app.FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();

        transaction.replace(rl_dialog.getId(), fragment);
        transaction
                .addToBackStack(
                        getChildFragmentManager().getBackStackEntryCount() == 0 ? tag
                                : null).commit();
    }*/

    private void setListener() {
       // btndialog_1.setOnClickListener(dialogBtnListener1);
      //  btndialog_2.setOnClickListener(dialogBtnListener2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

         /*   case R.id.btndialogCalendar_1:
                DialogFragment.this.dismiss();
                break;

            case R.id.btndialogCalendar_2:
                DialogFragment.this.dismiss();
                break;*/
        }
    }
}
