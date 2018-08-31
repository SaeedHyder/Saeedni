package com.ingic.saeedni.ui.viewbinder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.NewJobsEnt;
import com.ingic.saeedni.helpers.BasePreferenceHelper;
import com.ingic.saeedni.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by  on 5/22/2017.
 */

public class NewJobsitemBinder  extends ViewBinder<NewJobsEnt> {

    private ImageLoader imageLoader;
    String title="";
    String title1="";
    BasePreferenceHelper preferenceHelper;

    public NewJobsitemBinder( BasePreferenceHelper preferenceHelper) {
        super(R.layout.newjobs_item);
        this.preferenceHelper=preferenceHelper;

        imageLoader = ImageLoader.getInstance();
    }


    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new NewJobsitemBinder.ViewHolder(view);
    }

    @Override
    public void bindView(NewJobsEnt entity, int position, int grpPosition, View view, Activity activity) {

        NewJobsitemBinder.ViewHolder viewHolder = (NewJobsitemBinder.ViewHolder) view.getTag();


        if(entity.getRequest_detail().getService_detail()!=null) {
            if (entity.getRequest_detail().getService_detail() != null) {
                if(!preferenceHelper.isLanguageArabic()){
                title = entity.getRequest_detail().getService_detail().getTitle()+"";}
                else {
                    title = entity.getRequest_detail().getService_detail().getTitle()+"";
                }
                if (entity.getRequest_detail().getServics_list().size()>0) {
                    if(!preferenceHelper.isLanguageArabic()){
                        title1 = entity.getRequest_detail().getServics_list().get(0).getService_detail().getTitle()+"";
                    }
                    else
                    {
                        title1 = entity.getRequest_detail().getServics_list().get(0).getService_detail().getTitle()+"";
                    }

                }
                viewHolder.txt_jobNotification.setText(title + "/" + title1);
            }
        }
        else{
        viewHolder.txt_jobNotification.setText("No Title");}

    }

    public static class ViewHolder extends BaseViewHolder {

        private ImageView iv_Notificationlogo;
        private AnyTextView txt_jobNotification;
        private ImageView iv_next;

        public ViewHolder(View view) {
            iv_Notificationlogo= (ImageView) view.findViewById(R.id.iv_Notificationlogo);
            txt_jobNotification = (AnyTextView) view.findViewById(R.id.txt_jobNotification);
            iv_next=(ImageView)view.findViewById(R.id.iv_next);
        }
    }
}
