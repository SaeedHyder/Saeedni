package com.ingic.saeedni.ui.viewbinder;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ingic.saeedni.R;
import com.ingic.saeedni.activities.DockActivity;
import com.ingic.saeedni.entities.NavigationEnt;
import com.ingic.saeedni.fragments.SideMenuFragment;
import com.ingic.saeedni.helpers.BasePreferenceHelper;
import com.ingic.saeedni.helpers.ClickableSpanHelper;
import com.ingic.saeedni.interfaces.UpdateNotificationsCount;
import com.ingic.saeedni.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.ingic.saeedni.ui.views.BadgeHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 5/24/2017.
 */

public class NavigationItemBinder extends ViewBinder<NavigationEnt> implements UpdateNotificationsCount {
    DockActivity dockActivity;
    BadgeHelper badgeHelper;
    BasePreferenceHelper prefHelper;
    UpdateNotificationsCount count;
    int badgeCount;
    ImageView countView;


    public NavigationItemBinder(DockActivity activity, SideMenuFragment fragment, BasePreferenceHelper prefHelper) {
        super(R.layout.row_item_nav);
        this.dockActivity = activity;
        this.prefHelper = prefHelper;

        fragment.setInterface(this);

    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new NavViewHolder(view);
    }

    @Override
    public void bindView(NavigationEnt entity, int position, int grpPosition, View view, Activity activity) {
        NavViewHolder viewHolder = (NavViewHolder) view.getTag();
        if (prefHelper.isLanguageArabic()){
            viewHolder.rootLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        else  viewHolder.rootLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        viewHolder.txtLanguage.setVisibility(View.GONE);
        badgeHelper = new BadgeHelper(viewHolder.imgNotificationCount, (DockActivity) activity);
        if (entity.getItem_text().equals(activity.getResources().getString(R.string.home)) && !entity.getItem_text().equals(activity.getResources().getString(R.string.english))) {
            viewHolder.txtHome.setText(entity.getItem_text());
            //viewHolder.imgUnselected.setImageResource(entity.getSelectedDrawable());
            viewHolder.txtHome.setTextColor(activity.getResources().getColor(R.color.app_blue));
            viewHolder.imgUnselected.setVisibility(View.VISIBLE);
            badgeHelper.hideBadge();
        } else {
            viewHolder.imgSelected.setVisibility(View.GONE);
            viewHolder.imgUnselected.setVisibility(View.INVISIBLE);
            viewHolder.txtHome.setText(entity.getItem_text());
            viewHolder.txtHome.setTextColor(activity.getResources().getColor(R.color.black));
            badgeHelper.hideBadge();
            if (entity.getItem_text().equals(activity.getResources().getString(R.string.notifications))) {
                badgeHelper.initBadge(activity);
                badgeHelper.addtoBadge(prefHelper.getBadgeCount());
                badgeHelper.showBadge();
            } else if (entity.getItem_text().equals(activity.getResources().getString(R.string.english)) && !entity.getItem_text().equals(activity.getResources().getString(R.string.home))) {
                viewHolder.txtLanguage.setVisibility(View.VISIBLE);
                viewHolder.txtHome.setText(activity.getResources().getString(R.string.language));
                viewHolder.txtHome.setTextColor(activity.getResources().getColor(R.color.black));
                if (prefHelper.isLanguageArabic()) {
                    String sourceString = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b> <font color='#fcc739'>" + dockActivity.getResources().getString(R.string.ar) + "</font> </b> " + " |" +
                            dockActivity.getResources().getString(R.string.eng);
                    viewHolder.txtLanguage.setText(Html.fromHtml(sourceString));
                    viewHolder.txtLanguage.setGravity(Gravity.RIGHT);
                } else {
                    String sourceString = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><font color='#fcc739'>" + dockActivity.getResources().getString(R.string.eng) + "</font></b> " + " | " +
                            dockActivity.getResources().getString(R.string.ar);
                    viewHolder.txtLanguage.setText(Html.fromHtml(sourceString));
                    viewHolder.txtLanguage.setGravity(Gravity.LEFT);

                }

            }

        }

    }

   /* private void changeBadgeSide(int GravitySide, ImageView imgBadge) {
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(imgBadge.getWidth(),
                        imgBadge.getHeight());
        params.addRule(GravitySide);

        imgBadge.setLayoutParams(params);
    }

    private void setSignupSpan(String text, String spanText, AnyTextView txtview) {

        SpannableStringBuilder stringBuilder = ClickableSpanHelper.initSpan(text);
        ClickableSpanHelper.setSpan(stringBuilder, text, spanText, new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                //  ds.setColor(getResources().getColor(R.color.white));    // you can use custom color
                ds.setTypeface(Typeface.DEFAULT_BOLD);
                ds.setUnderlineText(false);    // this remove the underline
            }

            @Override
            public void onClick(View widget) {
                if (prefHelper.isLanguageArabic()) {
                    prefHelper.putLang(dockActivity, "en");

                } else {
                    prefHelper.putLang(dockActivity, "ar");

                }
            }
        });
        ClickableSpanHelper.setColor(stringBuilder, text, spanText, "#fcc739");

        ClickableSpanHelper.setClickableSpan(txtview, stringBuilder);

    }

    private void LanguageChange(final View txtHome) {
        txtHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefHelper.isLanguageArabic()) {
                    prefHelper.putLang(dockActivity, "en");

                } else {
                    prefHelper.putLang(dockActivity, "ar");

                }
            }
        });
    }

*/
    @Override
    public void updateCount(int count) {
        badgeCount = count;
        badgeHelper.addtoBadge(count);
        badgeHelper.getImgNotificationCounter().invalidate();
        badgeHelper.showBadge();
    }

    public static class NavViewHolder extends BaseViewHolder {
        @BindView(R.id.img_selected)
        ImageView imgSelected;
        @BindView(R.id.img_unselected)
        ImageView imgUnselected;
        @BindView(R.id.txt_home)
        AnyTextView txtHome;
        @BindView(R.id.txt_language)
        AnyTextView txtLanguage;
        @BindView(R.id.txt_line)
        View txtLine;
        @BindView(R.id.ll_item_container)
        LinearLayout container;
        @BindView(R.id.imgNotificationCount)
        ImageView imgNotificationCount;
        @BindView(R.id.root_layout)
        LinearLayout rootLayout;
        NavViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
