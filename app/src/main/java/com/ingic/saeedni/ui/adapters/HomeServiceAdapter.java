package com.ingic.saeedni.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ingic.saeedni.R;
import com.ingic.saeedni.activities.DockActivity;
import com.ingic.saeedni.entities.ServiceEnt;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 6/3/2017.
 */

public class HomeServiceAdapter extends RecyclerView.Adapter<HomeServiceAdapter.ViewHolder> {

    private DockActivity context;
    private List<ServiceEnt> dataList;

    public HomeServiceAdapter(DockActivity context, List<ServiceEnt> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public HomeServiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_home, parent, false);

        return new HomeServiceAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeServiceAdapter.ViewHolder holder, int position) {
        holder.txtServiceName.setText(dataList.get(position).getTitle());
        Picasso.with(context)
                .load(dataList.get(position).getServiceImage())
                .placeholder(R.drawable.ac)
                .into(holder.imgService);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_service)
        ImageView imgService;
        @BindView(R.id.txt_service_name)
        AnyTextView txtServiceName;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
