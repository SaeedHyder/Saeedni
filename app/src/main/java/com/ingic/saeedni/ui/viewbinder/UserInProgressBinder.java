package com.ingic.saeedni.ui.viewbinder;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ingic.saeedni.R;
import com.ingic.saeedni.activities.DockActivity;
import com.ingic.saeedni.entities.RequestTechnicianEnt;
import com.ingic.saeedni.entities.UserInProgressEnt;
import com.ingic.saeedni.fragments.RequestServiceFragment;
import com.ingic.saeedni.global.AppConstants;
import com.ingic.saeedni.helpers.BasePreferenceHelper;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.interfaces.CallUser;
import com.ingic.saeedni.interfaces.onCancelJobListner;
import com.ingic.saeedni.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.saeedni.ui.views.AnyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 5/25/2017.
 */

public class UserInProgressBinder extends ViewBinder<UserInProgressEnt> {
    DockActivity context;
    CallUser callUser;
    onCancelJobListner onCancelJobListner;
    BasePreferenceHelper preferenceHelper;

    public UserInProgressBinder(CallUser callUser, DockActivity context, onCancelJobListner onCancelJobListner, BasePreferenceHelper preferenceHelper) {
        super(R.layout.row_item_user_inprogress);
        this.callUser = callUser;
        this.context = context;
        this.onCancelJobListner = onCancelJobListner;
        this.preferenceHelper = preferenceHelper;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new InProgressViewHolder(view);
    }

    @Override
    public void bindView(final UserInProgressEnt entity, final int position, int grpPosition, View view, Activity activity) {
        InProgressViewHolder viewHolder = (InProgressViewHolder) view.getTag();
        RequestTechnicianEnt technicianEnt = null;

        if (preferenceHelper.isLanguageArabic()) {
            viewHolder.root_layout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else
            viewHolder.root_layout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        if (entity.getAssign_technician_details() != null) {
            technicianEnt = entity.getAssign_technician_details();
        }
        if (technicianEnt != null && entity.getStatus() == AppConstants.TECH_ACCEPT_ASSIGN_JOB) {
            viewHolder.ivEditBtn.setVisibility(View.GONE);
            viewHolder.txtTechNameText.setText(technicianEnt.getTechnician_details().getFullName());
            viewHolder.txtNumberText.setText(technicianEnt.getTechnician_details().getPhoneNo());
            viewHolder.btnCallUser.setBackground(context.getResources().getDrawable(R.drawable.button_background));
        } else {
            viewHolder.ivEditBtn.setVisibility(View.VISIBLE);
            viewHolder.txtTechNameText.setText(context.getString(R.string.no_technician_error));
            viewHolder.txtNumberText.setText(context.getString(R.string.no_number_tech));
            viewHolder.btnCallUser.setBackground(context.getResources().getDrawable(R.drawable.yellow_button_background));

        }
        if (entity.getServicsList().size() > 0) {
            if (preferenceHelper.isLanguageArabic()) {
//                viewHolder.txtJobTitleText.setText(entity.getServicsList().get(0).getServiceEnt().getArTitle());
                viewHolder.txtJobTitleText.setText(entity.getServiceDetail().getTitle()+"");
            } else {
                viewHolder.txtJobTitleText.setText(entity.getServiceDetail().getTitle()+"");

//                viewHolder.txtJobTitleText.setText(entity.getServicsList().get(0).getServiceEnt().getTitle());
            }
        }
        if (!preferenceHelper.isLanguageArabic()) {
            if (entity.getTotal().equals("")) {
//                viewHolder.txtAmountText.setText(context.getString(R.string.aed) + " " + entity.getEstimateFrom() + " " + activity.getResources().getString(R.string.to) + " " + entity.getEstimateTo());
                viewHolder.txtAmountText.setText(context.getString(R.string.aed) + " " + 0);
            } else {
                viewHolder.txtAmountText.setText(context.getString(R.string.aed) + " " + entity.getTotal_amount());

            }
        } else {
            if (entity.getTotal().equals("")) {
                //viewHolder.txtAmountText.setText(entity.getEstimateTo() + " " + activity.getResources().getString(R.string.to) + " " + entity.getEstimateFrom() + " " + context.getString(R.string.aed));
                viewHolder.txtAmountText.setText(context.getString(R.string.aed) + " " + 0);
            } else {
                viewHolder.txtAmountText.setText(entity.getTotal_amount() + " " + context.getString(R.string.aed));

            }
        }


        viewHolder.ivEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity.getServiceDetail() != null)
                    context.replaceDockableFragment(RequestServiceFragment.newInstance(entity.getServiceDetail(), entity), "RequestServiceFragment");
            }
        });
        viewHolder.btnCancelJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelJobListner.onCancelJob(position);

            }
        });
        final RequestTechnicianEnt finalTechnicianEnt = technicianEnt;
        viewHolder.btnCallUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalTechnicianEnt != null && entity.getStatus() == AppConstants.TECH_ACCEPT_ASSIGN_JOB) {
                    callUser.CallOnUserNumber(finalTechnicianEnt.getTechnician_details().getPhoneNo());
                } else {
                    UIHelper.showShortToastInCenter(context, context.getString(R.string.assignText));
                }
            }
        });

    }

    public static class InProgressViewHolder extends BaseViewHolder {
        @BindView(R.id.txt_tech_name_Text)
        AnyTextView txtTechNameText;
        @BindView(R.id.iv_editBtn)
        ImageView ivEditBtn;
        @BindView(R.id.txt_JobTitleText)
        AnyTextView txtJobTitleText;
        @BindView(R.id.txt_amountText)
        AnyTextView txtAmountText;
        @BindView(R.id.txt_number_text)
        AnyTextView txtNumberText;
        @BindView(R.id.btn_callUser)
        Button btnCallUser;
        @BindView(R.id.btn_cancelJob)
        Button btnCancelJob;
        @BindView(R.id.root_layout)
        LinearLayout root_layout;

        public InProgressViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
