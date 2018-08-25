package com.ingic.saeedni.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.google.firebase.iid.FirebaseInstanceId;
import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.AppConstants;
import com.ingic.saeedni.global.WebServiceConstants;
import com.ingic.saeedni.helpers.DialogHelper;
import com.ingic.saeedni.helpers.InternetHelper;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ingic.saeedni.R.id.ll_history;
import static com.ingic.saeedni.R.id.ll_logout;
import static com.ingic.saeedni.R.id.ll_notification;
import static com.ingic.saeedni.R.id.ll_profile;


public class HomeFragment extends BaseFragment implements View.OnClickListener {

    public boolean isNotification = false;
    protected BroadcastReceiver broadcastReceiver;
    @BindView(R.id.ll_newJobs)
    LinearLayout ll_newJobs;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.iv_profile)
    ImageView ivProfile;
    @BindView(R.id.ll_profile)
    LinearLayout llProfile;
    @BindView(R.id.iv_newJobs)
    ImageView ivNewJobs;
    @BindView(R.id.ll_ItemsFirstRow)
    LinearLayout llItemsFirstRow;
    @BindView(R.id.iv_history)
    ImageView ivHistory;
    @BindView(ll_history)
    LinearLayout llHistory;
    @BindView(R.id.iv_notification)
    ImageView ivNotification;
    @BindView(ll_notification)
    LinearLayout llNotification;
    @BindView(R.id.ll_ItemsSecondtRow)
    LinearLayout llItemsSecondtRow;
    @BindView(R.id.iv_logout)
    ImageView ivLogout;
    @BindView(R.id.ll_logout)
    LinearLayout llLogout;
    @BindView(R.id.ll_ItemsThirdRow)
    LinearLayout llItemsThirdRow;
    @BindView(R.id.ll_ItemsParent)
    LinearLayout llItemsParent;
    @BindView(R.id.btn_notification)
    ImageView btnNotification;
    @BindView(R.id.btn_changeLanguage)
    ToggleButton btnChangeLanguage;
    private TitleBar titleBar;


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        getDockActivity().lockDrawer();
        this.titleBar = titleBar;
        titleBar.hideButtons();
        titleBar.showMenuButton();
        titleBar.showNotificationButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(TechNotificationsFragment.newInstance(), "TechNotificationsFragment");
            }
        }, prefHelper);
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.home));
        titleBar.hideTitleBar();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

      //  serviceHelper.enqueueCall(webService.updateToken(prefHelper.getUserId(),AppConstants.Device_Type, FirebaseInstanceId.getInstance().getToken()), WebServiceConstants.UPDATE_DEVICE_TOKEN);

        getMainActivity().refreshFirstTimeSideMenu();
        getMainActivity().titleBar.invalidate();
        getMainActivity().titleBar.getImageView().invalidate();
        if (getMainActivity().isNotification) {
            getMainActivity().isNotification = false;
            //showNotification();
        }

        btnChangeLanguage.setChecked(prefHelper.isLanguageArabic());
        btnChangeLanguage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    prefHelper.putLang(getDockActivity(), "ar");

                } else {
                    prefHelper.putLang(getDockActivity(), "en");

                }

            }
        });
        listners();
        onNotificationReceived();
        getMainActivity().refreshSideMenuWithnewFragment();
        getMainActivity().refreshSideMenu();
    }

    private void showNotification() {
        getDockActivity().addDockableFragment(NewJobsFragment.newInstance(), "TechNotificationsFragment");
    }

    private void onNotificationReceived() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(AppConstants.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    System.out.println("registration complete");
                    // FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    System.out.println(prefHelper.getFirebase_TOKEN());

                } else if (intent.getAction().equals(AppConstants.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    isNotification = true;
                    System.out.println(prefHelper.getFirebase_TOKEN());
                    if (titleBar==null){
                        getMainActivity().titleBar.invalidate();
                        getMainActivity().titleBar.getImageView().invalidate();
                    }else {
                        titleBar.invalidate();
                        titleBar.getImageView().invalidate();
                        titleBar.showNotificationButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getDockActivity().replaceDockableFragment(UserNotificationsFragment.newInstance(), "UserNotificationsFragment");

                            }
                        },prefHelper);
                    }

                }
            }
        };
    }

    private void listners() {

        ll_newJobs.setOnClickListener(this);
        llHistory.setOnClickListener(this);
        llNotification.setOnClickListener(this);
        llProfile.setOnClickListener(this);
        llLogout.setOnClickListener(this);
    }

    private void logoutTechnician(final DialogHelper techLogoutDialog) {
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            Call<ResponseWrapper> call = webService.logoutTechnician(prefHelper.getUserId());
            call.enqueue(new Callback<ResponseWrapper>() {
                @Override
                public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                    if (response.body().getResponse().equals("2000")) {
                        getDockActivity().popBackStackTillEntry(0);
                        prefHelper.setLoginStatus(false);
                        getDockActivity().replaceDockableFragment(UserSelectionFragment.newInstance(), "UserSelectionFragment");
                        techLogoutDialog.hideDialog();
                    } else {
                        UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                    Log.e("UserSignupFragment", t.toString());
                    //  UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
                }
            });
        }
    }    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ll_newJobs:
                getDockActivity().replaceDockableFragment(NewJobsFragment.newInstance(), "NewJobsFragment");
                break;

            case R.id.ll_history:
                getDockActivity().replaceDockableFragment(OrderHistoryFragment.newInstance(), "OrderHistoryFragment");
                break;

            case ll_notification:
                getDockActivity().replaceDockableFragment(TechNotificationsFragment.newInstance(), "TechNotificationsFragment");
                break;

            case ll_profile:
                getDockActivity().replaceDockableFragment(ProfileFragment.newInstance(), "ProfileFragment");
                break;

            case ll_logout:
                final DialogHelper techLogoutDialog = new DialogHelper(getDockActivity());
                techLogoutDialog.logoutDialoge(R.layout.logout_technician_dialog, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logoutTechnician(techLogoutDialog);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        techLogoutDialog.hideDialog();
                        getMainActivity().closeDrawer();

                    }
                });
                techLogoutDialog.setCancelable(true);
                techLogoutDialog.showDialog();

                break;


        }
    }

    @OnClick(R.id.btn_notification)
    public void onViewClicked() {
        getDockActivity().replaceDockableFragment(TechNotificationsFragment.newInstance(), "TechNotificationsFragment");
    }


}
