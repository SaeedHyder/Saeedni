package com.ingic.saeedni.ui.viewbinder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.FeedBackEnt;
import com.ingic.saeedni.entities.UserComleteJobsEnt;
import com.ingic.saeedni.global.AppConstants;
import com.ingic.saeedni.helpers.BasePreferenceHelper;
import com.ingic.saeedni.helpers.DateHelper;
import com.ingic.saeedni.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.ingic.saeedni.ui.views.CustomRatingBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 5/25/2017.
 */

public class UserCompleteJobsBinder extends ViewBinder<UserComleteJobsEnt> {
    private BasePreferenceHelper preferenceHelper;

    public UserCompleteJobsBinder(BasePreferenceHelper prefhelper) {
        super(R.layout.row_item_user_complete_jobs);
        preferenceHelper = prefhelper;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public void bindView(UserComleteJobsEnt entity, int position, int grpPosition, View view, Activity activity) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (preferenceHelper.isLanguageArabic()) {
            viewHolder.root_layout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else
            viewHolder.root_layout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        viewHolder.txtJobNoText.setText(String.valueOf(position + 1));
            if (preferenceHelper.isLanguageArabic()) {
                viewHolder.txtJobTitleText.setText(entity.getServiceDetail().getArTitle() + "");
            } else {
                viewHolder.txtJobTitleText.setText(entity.getServiceDetail().getTitle() + "");
            }
        if (!preferenceHelper.isLanguageArabic()) {
            viewHolder.txtJobCompletedText.setText(DateHelper.dateFormat(entity.getDate(), AppConstants.DateFormat_DMY, AppConstants.DateFormat_YMD) + "");
        } else {
            viewHolder.txtJobCompletedText.setText(entity.getDate() + "");
        }
        if (entity.getStatus() == AppConstants.STATUS_COMPLETED) {
            viewHolder.txt_StatusText.setText(R.string.complete);
            viewHolder.txt_StatusText.setTextColor(activity.getResources().getColor(R.color.dark_green));
        } else if (entity.getStatus() == AppConstants.STATUS_CANCEL) {
            viewHolder.txt_StatusText.setText(R.string.cancel);
            viewHolder.txt_StatusText.setTextColor(activity.getResources().getColor(R.color.forgot_pass_color));
        }
        if (entity.getAssign_technician_details() != null) {
            viewHolder.txtClientNameText.setText(entity.getAssign_technician_details().getTechnician_details().getFullName());
            viewHolder.txt_companyNameText.setText(entity.getAssign_technician_details().getTechnician_details().getCompany_name());
        } else {
            viewHolder.txtClientNameText.setText(R.string.no_technician_error);
            viewHolder.txt_companyNameText.setText(R.string.no_technician_error);
        }
        viewHolder.txtEarningText.setText(entity.getTotal_amount());
        String sourceString = "<b>" + "<font color=#095587>" + view.getContext().getResources().getString(R.string.description) + "</font>" + "</b> " + "   " + entity.getDiscription();
        viewHolder.txtDescriptionText.setText(Html.fromHtml(sourceString));
        if (entity.getFeedbackdetail() != null)
            for (FeedBackEnt ent : entity.getFeedbackdetail()) {
                if (ent.getType().equals(AppConstants.TECHNICIAN)) {
                    viewHolder.rbAddRating.setScore(ent.getRate());
                }
                //Uncomment this if you wanted to show User Rating that is given by Technician
                /*if (ent.getType().equals(AppConstants.USER)) {
                    viewHolder.rbAddRating.setScore(ent.getRate());
                }*/
            }

        viewHolder.rbAddRating.setOnTouchListener((view1, motionEvent) -> {
            viewHolder.rbAddRating.setFocusable(false);
            return true;
        });

    }

    public static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.txt_jobNoText)
        AnyTextView txtJobNoText;
        @BindView(R.id.txt_jobCompletedText)
        AnyTextView txtJobCompletedText;
        @BindView(R.id.txt_clientNameText)
        AnyTextView txtClientNameText;
        @BindView(R.id.txt_JobTitleText)
        AnyTextView txtJobTitleText;
        @BindView(R.id.rbAddRating)
        CustomRatingBar rbAddRating;
        @BindView(R.id.txt_EarningText)
        AnyTextView txtEarningText;
        @BindView(R.id.txt_description_text)
        AnyTextView txtDescriptionText;
        @BindView(R.id.txt_companyNameText)
        AnyTextView txt_companyNameText;
        @BindView(R.id.txt_StatusText)
        AnyTextView txt_StatusText;
        @BindView(R.id.root_layout)
        LinearLayout root_layout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
