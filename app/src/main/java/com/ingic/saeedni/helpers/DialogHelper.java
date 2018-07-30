package com.ingic.saeedni.helpers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.ServiceEnt;
import com.ingic.saeedni.ui.viewbinder.MoreJobsBinder;
import com.ingic.saeedni.ui.views.AnyEditTextView;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.ingic.saeedni.ui.views.CustomRatingBar;
import com.ingic.saeedni.ui.views.CustomRecyclerView;

import java.util.ArrayList;

/**
 * Created on 5/24/2017.
 */

public class DialogHelper {
    private Dialog dialog;
    private Context context;

    public DialogHelper(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);
    }

    public Dialog initForgotPasswordDialog(int layoutID, View.OnClickListener onclicklistener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button closeButton = (Button) dialog.findViewById(R.id.btn_close);
        closeButton.setOnClickListener(onclicklistener);
        return this.dialog;
    }

    public Dialog initJobRefusalDialog(int layoutID, View.OnClickListener onclicklistener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button closeButton = (Button) dialog.findViewById(R.id.btn_submit);
        closeButton.setOnClickListener(onclicklistener);
        return this.dialog;
    }

    public Dialog initCancelQuotationDialog(int layoutID, View.OnClickListener onclicklistener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        AnyEditTextView msg = (AnyEditTextView) dialog.findViewById(R.id.ed_msg);
        Button closeButton = (Button) dialog.findViewById(R.id.btn_submit);
        closeButton.setOnClickListener(onclicklistener);
        return this.dialog;
    }

    public Dialog initMoreJobsDialog(ArrayList<ServiceEnt> jobsCollections, BasePreferenceHelper preferenceHelper, View.OnClickListener clickListener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(R.layout.dialog_more);
        CustomRecyclerView recyclerView = (CustomRecyclerView) dialog.findViewById(R.id.rv_jobs);
        recyclerView.bindRecyclerView(new MoreJobsBinder(preferenceHelper, clickListener), jobsCollections,
                new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false), new DefaultItemAnimator());
        Button closeButton = (Button) dialog.findViewById(R.id.btn_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return this.dialog;
    }

    public Dialog initMarkCompleteDialog(String price, View.OnClickListener clickListener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(R.layout.dialog_mark_complete);
        AnyTextView priceTextView = (AnyTextView) dialog.findViewById(R.id.txtPrice);
        priceTextView.setText(price);
        Button closeButton = (Button) dialog.findViewById(R.id.btn_submit);
        closeButton.setOnClickListener(clickListener);
        final CustomRatingBar rating = (CustomRatingBar) dialog.findViewById(R.id.rbAddRating);
        rating.setOnScoreChanged(new CustomRatingBar.IRatingBarCallbacks() {
            @Override
            public void scoreChanged(float score) {
                if (score < 1.0f)
                    rating.setScore(1.0f);
            }
        });
        return this.dialog;
    }

    public AnyEditTextView getEditTextView(int ResID) {
        return (AnyEditTextView) dialog.findViewById(ResID);

    }

    public Dialog logoutDialoge(int layoutID, View.OnClickListener yes, View.OnClickListener no) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button Yes = (Button) dialog.findViewById(R.id.btn_yes);
        Yes.setOnClickListener(yes);
        Button No = (Button) dialog.findViewById(R.id.btn_no);
        No.setOnClickListener(no);
        return this.dialog;
    }

    public Dialog deteleAccountDialoge(int layoutID, View.OnClickListener yes, View.OnClickListener no) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button Yes = (Button) dialog.findViewById(R.id.btn_yes);
        Yes.setOnClickListener(yes);
        Button No = (Button) dialog.findViewById(R.id.btn_no);
        No.setOnClickListener(no);
        return this.dialog;
    }

    public Dialog initCancelJobDialog(int layoutID, View.OnClickListener onokclicklistener, View.OnClickListener oncancelclicklistener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button okbutton = (Button) dialog.findViewById(R.id.btn_ok);
        okbutton.setOnClickListener(onokclicklistener);
        Button cancelbutton = (Button) dialog.findViewById(R.id.btn_cancle);
        cancelbutton.setOnClickListener(oncancelclicklistener);
        return this.dialog;
    }

    public Dialog initRequestSendDialog(int layoutID, View.OnClickListener onclicklistener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button closeButton = (Button) dialog.findViewById(R.id.btn_close);
        closeButton.setOnClickListener(onclicklistener);
        return this.dialog;
    }

    public Dialog initSignUpDialog(int layoutID, View.OnClickListener onclicklistener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button closeButton = (Button) dialog.findViewById(R.id.btn_close);
        closeButton.setOnClickListener(onclicklistener);
        return this.dialog;
    }


    public Dialog initJobDetailDialog(int layoutID, View.OnClickListener onclicklistener, String title, String person_name,
                                      View.OnClickListener arriveclickListener, View.OnClickListener completeclickListener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button closeButton = (Button) dialog.findViewById(R.id.btn_submit);
        closeButton.setOnClickListener(onclicklistener);
        AnyTextView txttitle = (AnyTextView) dialog.findViewById(R.id.txt_problem_name);
        txttitle.setText(title);
        AnyTextView txtperson = (AnyTextView) dialog.findViewById(R.id.txt_personname);
        txtperson.setText(person_name);
        AnyTextView arrive = (AnyTextView) dialog.findViewById(R.id.txt_arrval_time);
        arrive.setOnClickListener(arriveclickListener);
        AnyTextView complete = (AnyTextView) dialog.findViewById(R.id.txt_complete_time);
        complete.setOnClickListener(completeclickListener);
        return this.dialog;
    }

    public AnyTextView getTimeTextview(int ID) {
        return (AnyTextView) dialog.findViewById(ID);

    }

    public Dialog initRatingDialog(int layoutID, View.OnClickListener onclicklistener, String title, String message, BasePreferenceHelper preferenceHelper) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        if (preferenceHelper.isLanguageArabic()) {
            this.dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            this.dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        Button closeButton = (Button) dialog.findViewById(R.id.btn_close);
        closeButton.setOnClickListener(onclicklistener);
        AnyTextView txttitle = (AnyTextView) dialog.findViewById(R.id.txtHeader);
        txttitle.setText(title);
        AnyTextView txtmessage = (AnyTextView) dialog.findViewById(R.id.notwell_tv);
        txtmessage.setText(message);
        final CustomRatingBar rating = (CustomRatingBar) dialog.findViewById(R.id.rbAddRating);
        rating.setOnScoreChanged(new CustomRatingBar.IRatingBarCallbacks() {
            @Override
            public void scoreChanged(float score) {
                if (score < 1.0f)
                    rating.setScore(1.0f);
            }
        });
        return this.dialog;
    }

    public float getRating(int ratingBarID) {
        CustomRatingBar ratingBar = (CustomRatingBar) dialog.findViewById(ratingBarID);
        return ratingBar.getScore();
    }

    public String getEditText(int editTextID) {
        AnyEditTextView editTextView = (AnyEditTextView) dialog.findViewById(editTextID);
        KeyboardHide.hideSoftKeyboard(dialog.getContext(), editTextView);
        return editTextView.getText().toString();
    }

    public void showDialog() {

        dialog.show();
    }

    public void setCancelable(boolean isCancelable) {
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelable);
    }

    public void hideDialog() {
        dialog.dismiss();
    }
}
