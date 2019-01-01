package com.ingic.saeedni.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Switch;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.RegistrationResultEnt;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.WebServiceConstants;
import com.ingic.saeedni.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingFragment extends BaseFragment {
    @BindView(R.id.swt_continous_play)
    Switch swtPushNotification;
    @BindView(R.id.btnChangePassword)
    FrameLayout btnChangePassword;
    @BindView(R.id.btnAbout)
    FrameLayout btnAbout;
    @BindView(R.id.parent)
    ScrollView parent;
    Unbinder unbinder;
    @BindView(R.id.imgPhone)
    ImageView imgPhone;
    @BindView(R.id.imgPassword)
    ImageView imgPassword;
    @BindView(R.id.imgContact)
    ImageView imgContact;
    @BindView(R.id.imgAbout)
    ImageView imgAbout;
    @BindView(R.id.imgTerms)
    ImageView imgTerms;

    public static SettingFragment newInstance() {
        Bundle args = new Bundle();

        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (prefHelper.isLanguageArabic()) {
            parent.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            parent.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        if (prefHelper.isLanguageArabic()) {
            imgAbout.setRotation(180);
            imgContact.setRotation(180);
            imgPassword.setRotation(180);
            imgPhone.setRotation(180);
            imgTerms.setRotation(180);
        } else {
            imgAbout.setRotation(360);
            imgContact.setRotation(360);
            imgPassword.setRotation(360);
            imgPhone.setRotation(360);
            imgTerms.setRotation(360);
        }

        btnChangePassword.setVisibility(prefHelper.getRegistrationResult().isSocialLogin() ? View.GONE : View.VISIBLE);

        swtPushNotification.setChecked(prefHelper.getRegistrationResult().isPushNotificationOn());

        swtPushNotification.setOnCheckedChangeListener((compoundButton, b) -> {
            int status = b ? 1 : 0;
            serviceHelper.enqueueCall(webService.changePushNotificationStatus(prefHelper.getUserId(), status), WebServiceConstants.CHANGE_PUSH_STATUS);
            RegistrationResultEnt ent = prefHelper.getRegistrationResult();
            ent.setPush_notification(status);
            prefHelper.putRegistrationResult(ent);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.settings));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.btnChangePhoneNumber, R.id.btnChangePassword, R.id.btnContact, R.id.btnAbout, R.id.btnTermsCondition})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnChangePhoneNumber:
                getDockActivity().replaceDockableFragment(UserChangePhoneFragment.newInstance(false), "UserChangePhoneFragment");

                break;

            case R.id.btnContact:
                getDockActivity().replaceDockableFragment(ContactUsFragment.newInstance(), "ContactUsFragment");

                break;

            case R.id.btnChangePassword:
                getDockActivity().replaceDockableFragment(UserChangePasswordFragment.newInstance(), "UserChangePasswordFragment");
                break;
            case R.id.btnAbout:
                getDockActivity().replaceDockableFragment(AboutAppFragment.newInstance(), "AboutAppFragment");

                break;
            case R.id.btnTermsCondition:
                getDockActivity().replaceDockableFragment(TermAndConditionFragment.newInstance(), "TermAndConditionFragment");

                break;
        }
    }
}