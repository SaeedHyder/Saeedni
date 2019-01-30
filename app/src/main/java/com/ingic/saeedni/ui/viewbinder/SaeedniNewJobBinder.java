package com.ingic.saeedni.ui.viewbinder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.NewJobsEnt;
import com.ingic.saeedni.helpers.BasePreferenceHelper;
import com.ingic.saeedni.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 4/24/18.
 */
public class SaeedniNewJobBinder extends ViewBinder<NewJobsEnt> {

    String title = "";
    String title1 = "";
    BasePreferenceHelper preferenceHelper;
    View.OnClickListener onClickListener;
    private ImageLoader imageLoader;

    public SaeedniNewJobBinder(BasePreferenceHelper preferenceHelper, View.OnClickListener onClickListener) {
        super(R.layout.row_item_new_jobs);
        this.preferenceHelper = preferenceHelper;

        imageLoader = ImageLoader.getInstance();
        this.onClickListener = onClickListener;
    }


    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindView(NewJobsEnt entity, int position, int grpPosition, View view, Activity activity) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (preferenceHelper.isLanguageArabic()) {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        }
        if (entity.getRequest_detail().getService_detail() != null) {
            viewHolder.txtEstimatedQuote.setText(activity.getResources().getString(R.string.aed) + " " + entity.getRequest_detail().getEstimate_from() + " " +
                    "-" + " " + entity.getRequest_detail().getEstimate_to());
            viewHolder.txtAddress.setText(entity.getRequest_detail().getAddress());
            viewHolder.txtJobName.setText(entity.getRequest_detail().getId() + "");
            viewHolder.txtPreferredDateTime.setText(entity.getRequest_detail().getDate() + "  " + entity.getRequest_detail().getTime());
            if (preferenceHelper.isLanguageArabic()) {
                viewHolder.txtService.setText(entity.getRequest_detail().getService_detail().getAr_title() + "");
            } else {
                viewHolder.txtService.setText(entity.getRequest_detail().getService_detail().getTitle() + "");
            }
            viewHolder.btnAccept.setTag(R.integer.key_recycler_object, entity);
            viewHolder.btnAccept.setTag(R.integer.key_recycler_position, position);
            viewHolder.btnReject.setTag(R.integer.key_recycler_object, entity);
            viewHolder.btnReject.setTag(R.integer.key_recycler_position, position);
            viewHolder.btnAccept.setOnClickListener(onClickListener);
            viewHolder.btnReject.setOnClickListener(onClickListener);
            viewHolder.btnViewImages.setTag(R.integer.key_recycler_object, entity);
            viewHolder.btnViewImages.setTag(R.integer.key_recycler_position, position);
            if (entity.getRequest_detail().getImage_detail().size() <= 0) {
                viewHolder.btnViewImages.setVisibility(View.GONE);
            } else {
                viewHolder.btnViewImages.setVisibility(View.VISIBLE);
                viewHolder.btnViewImages.setOnClickListener(onClickListener);
            }


        }

    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.txt_jobNameHeading)
        AnyTextView txtJobNameHeading;
        @BindView(R.id.txt_jobName)
        AnyTextView txtJobName;
        @BindView(R.id.ll_job)
        LinearLayout llJob;
        @BindView(R.id.txt_serviceHeading)
        AnyTextView txtServiceHeading;
        @BindView(R.id.txt_service)
        AnyTextView txtService;
        @BindView(R.id.ll_service)
        LinearLayout llService;
        @BindView(R.id.txt_AddressHeading)
        AnyTextView txtAddressHeading;
        @BindView(R.id.txt_address)
        AnyTextView txtAddress;
        @BindView(R.id.ll_Address)
        LinearLayout llAddress;
        @BindView(R.id.txt_preferred_dateTimeHeading)
        AnyTextView txtPreferredDateTimeHeading;
        @BindView(R.id.txt_preferred_dateTime)
        AnyTextView txtPreferredDateTime;
        @BindView(R.id.ll_preferred_dateTime)
        LinearLayout llPreferredDateTime;
        @BindView(R.id.txt_estimatedQuoteHeading)
        AnyTextView txtEstimatedQuoteHeading;
        @BindView(R.id.txt_estimatedQuote)
        AnyTextView txtEstimatedQuote;
        @BindView(R.id.ll_EstimatedQuote)
        LinearLayout llEstimatedQuote;
        @BindView(R.id.btn_accept)
        Button btnAccept;
        @BindView(R.id.btn_reject)
        Button btnReject;
        @BindView(R.id.btnViewImages)
        Button btnViewImages;
        @BindView(R.id.ll_buttons)
        LinearLayout llButtons;
        @BindView(R.id.ll_JobDetail)
        LinearLayout llJobDetail;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
