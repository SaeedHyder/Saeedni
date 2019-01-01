package com.ingic.saeedni.ui.viewbinder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ingic.saeedni.R;
import com.ingic.saeedni.activities.DockActivity;
import com.ingic.saeedni.entities.RequestDetail;
import com.ingic.saeedni.entities.serviceList;
import com.ingic.saeedni.fragments.EditJobTechFragment;
import com.ingic.saeedni.global.AppConstants;
import com.ingic.saeedni.helpers.BasePreferenceHelper;
import com.ingic.saeedni.helpers.DateHelper;
import com.ingic.saeedni.interfaces.CallUser;
import com.ingic.saeedni.interfaces.MarkAsComplete;
import com.ingic.saeedni.ui.viewbinders.abstracts.ExpandableListViewBinder;
import com.ingic.saeedni.ui.views.AnyTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by  on 6/6/2017.
 */

public class InprogressExpandBinder extends ExpandableListViewBinder<RequestDetail, RequestDetail> {

    DockActivity context;
    CallUser callUser;
    MarkAsComplete complete;
    private BasePreferenceHelper preferenceHelper;

    public InprogressExpandBinder(DockActivity context, CallUser callUser, MarkAsComplete complete, BasePreferenceHelper prefHelper) {

        super(R.layout.inprogress_tech_parent, R.layout.inprogress_chlid);
        this.context = context;
        this.callUser = callUser;
        this.complete = complete;
        this.preferenceHelper = prefHelper;

    }


    @Override
    public BaseGroupViewHolder createGroupViewHolder(View view) {
        return new parentViewHolder(view);
    }

    @Override
    public BaseGroupViewHolder createChildViewHolder(View view) {
        return new childViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindGroupView(final RequestDetail entity, final int position, int grpPosition, int childCount, View view, Activity activity) {

        parentViewHolder parentViewHolder = (InprogressExpandBinder.parentViewHolder) view.getTag();
        if (preferenceHelper.isLanguageArabic()) {
            parentViewHolder.root_layout_parent.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            parentViewHolder.root_layout_parent.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        if (childCount <= 0) {
            parentViewHolder.hideView.setVisibility(View.GONE);
            parentViewHolder.llEstimatedQuotation.setVisibility(View.GONE);
            parentViewHolder.llBottomBtns.setVisibility(View.VISIBLE);
        } else {
            parentViewHolder.hideView.setVisibility(View.VISIBLE);
            parentViewHolder.llEstimatedQuotation.setVisibility(View.GONE);
            parentViewHolder.llBottomBtns.setVisibility(View.GONE);
        }
        parentViewHolder.txtJobNoText.setText(entity.getId() + "");
        //parentViewHolder.txtJobPostedText.setText(entity.getDate()+"");
        if (!preferenceHelper.isLanguageArabic()) {
            parentViewHolder.txtJobPostedText.setText(DateHelper.dateFormat(entity.getDate(), AppConstants.DateFormat_DMY, AppConstants.DateFormat_YMD) + "");
        } else {
            parentViewHolder.txtJobPostedText.setText(entity.getDate() + "");
        }
        if (entity.getUser_detail() != null)
            parentViewHolder.txtClientNameText.setText(entity.getUser_detail().getFull_name() + "");
        if (!preferenceHelper.isLanguageArabic()) {
            parentViewHolder.txtEstimatedQuotationText.setText(context.getResources().getString(R.string.between_aed) + " " + entity.getEstimate_from() + " " + view.getContext().getResources().getString(R.string.to) + " " + entity.getEstimate_to());
        } else {
            parentViewHolder.txtEstimatedQuotationText.setText(context.getResources().getString(R.string.between) + " " + entity.getEstimate_from() + " " + view.getContext().getResources().getString(R.string.to) + " " + entity.getEstimate_to() + " " + context.getResources().getString(R.string.aed));
        }
        if (entity.getService_detail() != null) {
            if (preferenceHelper.isLanguageArabic()) {
                parentViewHolder.txtJobTitleText.setText(entity.getService_detail().getAr_title() + "");
            } else {
                parentViewHolder.txtJobTitleText.setText(entity.getService_detail().getTitle() + "");
            }

        } else {
            parentViewHolder.txtJobTitleText.setText("");
        }
        if (entity.getTotal().equals("")) {
            parentViewHolder.txtEarningText.setText(context.getResources().getString(R.string.aed) + " " + " 0");
        } else {
            parentViewHolder.txtEarningText.setText(context.getResources().getString(R.string.aed) + " " + entity.getTotal());
        }
        parentViewHolder.txtAddressText.setText(entity.getAddress() + "");

        setTextStyle(parentViewHolder);

        parentViewHolder.btnCallUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity.getUser_detail() != null) {
                    callUser.CallOnUserNumber(entity.getUser_detail().getPhone_no());
                }
            }
        });

        parentViewHolder.ivEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.replaceDockableFragment(EditJobTechFragment.newInstance(entity), "EditJobTechFragment");
            }
        });

        parentViewHolder.btnAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.replaceDockableFragment(EditJobTechFragment.newInstance(String.valueOf(entity.getId()), entity.getUser_detail().getFull_name()), "EditJobTechFragment");
            }
        });
        parentViewHolder.btnMarkAsComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complete.markAsComplete(position, String.valueOf(entity.getId()));
            }
        });


    }

    private void setTextStyle(parentViewHolder parentViewHolder) {
        parentViewHolder.txtJobNo.setTypeface(null, Typeface.BOLD);
        parentViewHolder.txtJobPosted.setTypeface(null, Typeface.BOLD);
        parentViewHolder.txtClientName.setTypeface(null, Typeface.BOLD);
        parentViewHolder.txtJobTitle.setTypeface(null, Typeface.BOLD);
        parentViewHolder.txtEarning.setTypeface(null, Typeface.BOLD);
        parentViewHolder.txtAddress.setTypeface(null, Typeface.BOLD);
        parentViewHolder.txtEstimatedQuotation.setTypeface(null, Typeface.BOLD);

    }

    @Override
    public void bindChildView(final RequestDetail entity, final int position, final int grpPosition, int childCount, View view, Activity activity) {

        childViewHolder childViewHolder = (InprogressExpandBinder.childViewHolder) view.getTag();
        if (preferenceHelper.isLanguageArabic()) {
            childViewHolder.root_layout_child.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            childViewHolder.root_layout_child.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        ArrayList<String> subFieldTitles = new ArrayList<>();

        if (position == childCount - 1) {
            childViewHolder.llBottomEarning.setVisibility(View.VISIBLE);
        } else {
            childViewHolder.llBottomEarning.setVisibility(View.GONE);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (serviceList item : entity.getServics_list()) {
            if (preferenceHelper.isLanguageArabic()) {
                if (item.getService_detail() != null && item.getService_detail().getAr_title() != null) {
                    stringBuilder.append(item.getService_detail().getAr_title() + System.getProperty("line.separator"));
                    subFieldTitles.add(item.getService_detail().getAr_title());
                }
            } else {
                if (item.getService_detail() != null && item.getService_detail().getTitle() != null) {
                    stringBuilder.append(item.getService_detail().getTitle() + System.getProperty("line.separator"));
                    subFieldTitles.add(item.getService_detail().getTitle());
                }
            }
        }

        childViewHolder.txtJob1.setText(stringBuilder.toString());
        childViewHolder.txtAddEarningText.setText(context.getResources().getString(R.string.aed) + " " + entity.getTotal());
        childViewHolder.txtTotalEarningText.setText(context.getResources().getString(R.string.aed) + " " + entity.getTotal_amount());

        childViewHolder.btnMarkAsCompleteBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complete.markAsComplete(grpPosition, String.valueOf(entity.getParent_id()));
            }
        });

        childViewHolder.btnAddJobbottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.replaceDockableFragment(EditJobTechFragment.newInstance(String.valueOf(entity.getParent_id()), entity.getUser_detail().getFull_name()), "EditJobTechFragment");
            }
        });

        childViewHolder.ivEditJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.replaceDockableFragment(EditJobTechFragment.newInstance(entity), "EditJobTechFragment");
            }
        });

        setTextStyleChild(childViewHolder);


    }

    private void setTextStyleChild(childViewHolder childViewHolder) {

        childViewHolder.txtTotalEarning.setTypeface(null, Typeface.BOLD);
        childViewHolder.txtAddEarning.setTypeface(null, Typeface.BOLD);

    }


    static class parentViewHolder extends BaseGroupViewHolder {
        @BindView(R.id.txt_jobNo)
        AnyTextView txtJobNo;
        @BindView(R.id.txt_jobNoText)
        AnyTextView txtJobNoText;
        @BindView(R.id.iv_editBtn)
        ImageView ivEditBtn;
        @BindView(R.id.ll_job)
        LinearLayout llJob;
        @BindView(R.id.txt_jobPosted)
        AnyTextView txtJobPosted;
        @BindView(R.id.txt_jobPostedText)
        AnyTextView txtJobPostedText;
        @BindView(R.id.ll_jobPosted)
        LinearLayout llJobPosted;
        @BindView(R.id.txt_ClientName)
        AnyTextView txtClientName;
        @BindView(R.id.txt_clientNameText)
        AnyTextView txtClientNameText;
        @BindView(R.id.ll_clientName)
        LinearLayout llClientName;
        @BindView(R.id.txt_JobTitle)
        AnyTextView txtJobTitle;
        @BindView(R.id.txt_JobTitleText)
        AnyTextView txtJobTitleText;
        @BindView(R.id.ll_JobTitle)
        LinearLayout llJobTitle;
        @BindView(R.id.txt_Earning)
        AnyTextView txtEarning;
        @BindView(R.id.txt_EarningText)
        AnyTextView txtEarningText;
        @BindView(R.id.ll_Earning)
        LinearLayout llEarning;
        @BindView(R.id.txt_Address)
        AnyTextView txtAddress;
        @BindView(R.id.txt_AddressText)
        AnyTextView txtAddressText;
        @BindView(R.id.ll_Address)
        LinearLayout llAddress;
        @BindView(R.id.btn_callUser)
        Button btnCallUser;
        @BindView(R.id.btn_addJob)
        Button btnAddJob;
        @BindView(R.id.ll_buttons)
        LinearLayout llButtons;
        @BindView(R.id.btn_markAsComplete)
        Button btnMarkAsComplete;
        @BindView(R.id.txt_line)
        View txtLine;
        @BindView(R.id.ll_BottomBtns)
        LinearLayout llBottomBtns;
        @BindView(R.id.hideView)
        View hideView;
        @BindView(R.id.txt_EstimatedQuotation)
        AnyTextView txtEstimatedQuotation;
        @BindView(R.id.txt_EstimatedQuotationText)
        AnyTextView txtEstimatedQuotationText;
        @BindView(R.id.ll_EstimatedQuotation)
        LinearLayout llEstimatedQuotation;
        @BindView(R.id.root_layout_parent)
        CardView root_layout_parent;

        parentViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public static class childViewHolder extends BaseGroupViewHolder {
        @BindView(R.id.iv_editJobBtn)
        ImageView ivEditJobBtn;
        @BindView(R.id.ll_profileItems)
        LinearLayout llProfileItems;
        @BindView(R.id.txt_job1)
        AnyTextView txtJob1;
        @BindView(R.id.txt_job2)
        AnyTextView txtJob2;
        @BindView(R.id.txt_job3)
        AnyTextView txtJob3;
        @BindView(R.id.txt_AddEarning)
        AnyTextView txtAddEarning;
        @BindView(R.id.txt_AddEarningText)
        AnyTextView txtAddEarningText;
        @BindView(R.id.ll_AddEarning)
        LinearLayout llAddEarning;
        @BindView(R.id.txt_TotalEarning)
        AnyTextView txtTotalEarning;
        @BindView(R.id.txt_TotalEarningText)
        AnyTextView txtTotalEarningText;
        @BindView(R.id.ll_TotalEarning)
        LinearLayout llTotalEarning;
        @BindView(R.id.btn_AddJobbottom)
        Button btnAddJobbottom;
        @BindView(R.id.btn_markAsCompleteBottom)
        Button btnMarkAsCompleteBottom;
        @BindView(R.id.ll_Bottombuttons)
        LinearLayout llBottombuttons;
        @BindView(R.id.ll_AdditionalJob)
        LinearLayout llAdditionalJob;
        @BindView(R.id.ll_BottomEarning)
        LinearLayout llBottomEarning;
        @BindView(R.id.root_layout_child)
        LinearLayout root_layout_child;

        childViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
