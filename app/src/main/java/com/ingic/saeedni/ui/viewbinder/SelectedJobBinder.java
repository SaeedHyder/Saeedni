package com.ingic.saeedni.ui.viewbinder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.ServiceEnt;
import com.ingic.saeedni.helpers.BasePreferenceHelper;
import com.ingic.saeedni.interfaces.onDeleteImage;
import com.ingic.saeedni.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.saeedni.ui.views.AnyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 5/23/2017.
 */

public class SelectedJobBinder extends ViewBinder<ServiceEnt> {
    private onDeleteImage onDeleteImage;
    BasePreferenceHelper preferenceHelper;

    public SelectedJobBinder(onDeleteImage onDeleteImage, BasePreferenceHelper preferenceHelper) {
        super(R.layout.selectedjobs_row_item);
        this.onDeleteImage = onDeleteImage;
        this.preferenceHelper=preferenceHelper;
    }


    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new SelectedJobsViewHolder(view);
    }

    @Override
    public void bindView(ServiceEnt entity, final int position, int grpPosition, View view, Activity activity) {
        SelectedJobsViewHolder viewHolder = (SelectedJobsViewHolder)view.getTag();
        if (!preferenceHelper.isLanguageArabic()) {
            viewHolder.txtJobselectedtext.setText(entity.getTitle());
        }
        else {
            viewHolder.txtJobselectedtext.setText(entity.getTitle());
        }
        viewHolder.deleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteImage.OnDeleteJobs(position);
            }
        });
    }

    public static class SelectedJobsViewHolder extends BaseViewHolder{
        @BindView(R.id.txt_jobselectedtext)
        AnyTextView txtJobselectedtext;
        @BindView(R.id.delete_text)
        ImageView deleteText;

        SelectedJobsViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
