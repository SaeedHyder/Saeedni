package com.ingic.saeedni.ui.viewbinder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.ServiceEnt;
import com.ingic.saeedni.helpers.BasePreferenceHelper;
import com.ingic.saeedni.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 5/28/18.
 */
public class MoreJobsBinder extends RecyclerViewBinder<ServiceEnt> {
    private BasePreferenceHelper prefHelp;
    private View.OnClickListener itemClickListener;
    ImageLoader imageLoader;


    public MoreJobsBinder(BasePreferenceHelper prefHelp, View.OnClickListener clickListener) {
        super(R.layout.row_item_more_jobs);
        this.imageLoader = ImageLoader.getInstance();
        this.prefHelp = prefHelp;
        itemClickListener = clickListener;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final ServiceEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        if (prefHelp.isLanguageArabic())
            holder.txtJobs.setText(entity.getTitle());
        else
            holder.txtJobs.setText(entity.getTitle());

        imageLoader.displayImage(entity.getServiceImage(),holder.imgItem);
        holder.txtJobs.setTag(entity);
        holder.txtJobs.setOnClickListener(itemClickListener);
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.txt_jobs)
        AnyTextView txtJobs;
        @BindView(R.id.imgItem)
        ImageView imgItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
