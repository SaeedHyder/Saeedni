package com.ingic.saeedni.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingic.saeedni.R;
import com.ingic.saeedni.activities.MainActivity;
import com.ingic.saeedni.helpers.BasePreferenceHelper;

public class TitleBar extends RelativeLayout {

    private TextView txtTitle;
    private ImageView btnLeft;
    private ImageView btnRight;
    private ImageView imgBadge;


    private View.OnClickListener menuButtonListener;
    private OnClickListener backButtonListener;

    private MainActivity context;
    private BadgeHelper badgeHelper;


    public TitleBar(MainActivity context) {
        super(context);
        this.context = context;
        initLayout(context);
    }
    public void  setMainActivityContext(MainActivity context){
        this.context = context;
    }
    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
        if (attrs != null)
            initAttrs(context, attrs);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout(context);
        if (attrs != null)
            initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
    }

    private void bindViews() {

        txtTitle = (TextView) this.findViewById(R.id.txt_subHead);
        btnRight = (ImageView) this.findViewById(R.id.btnRight);
        btnLeft = (ImageView) this.findViewById(R.id.btnLeft);
        imgBadge = (ImageView) this.findViewById(R.id.img_badge);

    }

    private void initLayout(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.header_main, this);
        bindViews();
    }

    public void hideButtons() {
        txtTitle.setVisibility(View.GONE);
        btnLeft.setVisibility(View.GONE);
        btnRight.setVisibility(View.GONE);
        imgBadge.setVisibility(View.GONE);

    }

    public void showBackButton() {
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setImageResource(R.drawable.backbtn);
        btnLeft.setOnClickListener(backButtonListener);

    }

    public void showNotificationButton(OnClickListener onClickListener, BasePreferenceHelper prefHelper) {
        btnRight.setVisibility(View.VISIBLE);
        imgBadge.setVisibility(View.VISIBLE);
        btnRight.setImageResource(R.drawable.s_notification_title);
        btnRight.setOnClickListener(onClickListener);

        badgeHelper = new BadgeHelper(imgBadge, getContext());
        badgeHelper.initBadge(context);
        badgeHelper.addtoBadge(prefHelper.getBadgeCount());
           /* if (prefHelper.getLang().equals("ar")) {
                badgeHelper.changeBadgeGravity(RelativeLayout.ALIGN_PARENT_START);
            } else {
                badgeHelper.changeBadgeGravity(RelativeLayout.ALIGN_PARENT_END);
            }*/
        badgeHelper.showBadge();

        //btnRight.setOnClickListener(backButtonListener);

    }

    public ImageView getImageView() {
        return imgBadge;
    }

    public void showsaveButton(OnClickListener onClickListener) {
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setImageResource(R.drawable.tick);
        btnRight.setOnClickListener(onClickListener);
        //btnRight.setOnClickListener(backButtonListener);

    }

    public void showMenuButton() {
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(menuButtonListener);
        btnLeft.setImageResource(R.drawable.nav);
    }

    public void setSubHeading(String heading) {
        txtTitle.setVisibility(View.VISIBLE);
        txtTitle.setText(heading);

    }

    public TextView getTxtTitle() {
        return txtTitle;
    }

    public void showTitleBar() {
        context.addTitleBarPadding();
        this.setVisibility(View.VISIBLE);
    }

    public void hideTitleBar() {
        context.removeTitleBarPadding();
        this.setVisibility(View.GONE);
    }

    public void setMenuButtonListener(View.OnClickListener listener) {
        menuButtonListener = listener;
    }

    public void setBackButtonListener(View.OnClickListener listener) {
        backButtonListener = listener;
    }


}
