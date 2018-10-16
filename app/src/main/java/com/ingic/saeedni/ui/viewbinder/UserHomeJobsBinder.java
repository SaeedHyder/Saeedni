package com.ingic.saeedni.ui.viewbinder;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.ServiceEnt;
import com.ingic.saeedni.helpers.BasePreferenceHelper;
import com.ingic.saeedni.interfaces.RecyclerItemListener;
import com.ingic.saeedni.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserHomeJobsBinder extends RecyclerViewBinder<ServiceEnt> {
    private BasePreferenceHelper preferenceHelper;
    private ImageLoader imageLoader;
    private RecyclerItemListener itemClickListener;
    private int itemHeight = 0;

    public UserHomeJobsBinder(BasePreferenceHelper preferenceHelper, RecyclerItemListener itemClickListener) {
        super(R.layout.rowa_item_user_home);
        this.preferenceHelper = preferenceHelper;
        this.imageLoader = ImageLoader.getInstance();
        this.itemClickListener = itemClickListener;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(ServiceEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        if (itemHeight == 0) {
            DisplayMetrics matrics = context.getResources().getDisplayMetrics();
            // itemHeight = (matrics.heightPixels / 4);
            itemHeight = (matrics.heightPixels / 4);
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHeight);
        /*layoutParams.height = itemHeight;
        layoutParams.weight = LinearLayout.LayoutParams.MATCH_PARENT;*/
        holder.parent.setLayoutParams(layoutParams);
        holder.txtItem.setText(preferenceHelper.isLanguageArabic() ? entity.getArTitle() : entity.getTitle());
        imageLoader.displayImage(entity.getServiceImage(), holder.imgItem);
        holder.itemView.setOnClickListener(view -> {
            if (itemClickListener != null)
                itemClickListener.onItemClicked(entity, position, view.getId());
        });
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.imgItem)
        ImageView imgItem;
        @BindView(R.id.txtItem)
        AnyTextView txtItem;
        @BindView(R.id.parenContainer)
        CardView parenContainer;
        @BindView(R.id.parent)
        LinearLayout parent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
