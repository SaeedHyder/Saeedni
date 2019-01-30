package com.ingic.saeedni.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.NavigationEnt;
import com.ingic.saeedni.entities.RegistrationResultEnt;
import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.entities.TechProfileEnt;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.AppConstants;
import com.ingic.saeedni.helpers.DialogHelper;
import com.ingic.saeedni.helpers.InternetHelper;
import com.ingic.saeedni.helpers.TokenUpdater;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.interfaces.UpdateNotificationsCount;
import com.ingic.saeedni.ui.adapters.ArrayListAdapter;
import com.ingic.saeedni.ui.viewbinder.NavigationItemBinder;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.ingic.saeedni.ui.views.TitleBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SideMenuFragment extends BaseFragment {

    public boolean isNotificationTap = false;
    protected BroadcastReceiver broadcastReceiver;
    @BindView(R.id.CircularImageSharePop)
    CircleImageView CircularImageSharePop;
    @BindView(R.id.txt_userName)
    AnyTextView txtUserName;
    @BindView(R.id.txt_useremail)
    AnyTextView txtUseremail;
    @BindView(R.id.nav_listview)
    ListView navListview;
    int notificationCount;
    UpdateNotificationsCount updateNotificationsCount;
    @BindView(R.id.btn_userImage)
    ImageView btnUserImage;
    private ArrayList<NavigationEnt> navigationItemList = new ArrayList<>();
    private ArrayListAdapter<NavigationEnt> madapter;
    private ArrayList<NavigationEnt> navigationItemListTech = new ArrayList<>();
    private ArrayListAdapter<NavigationEnt> madapterTech;

    public static SideMenuFragment newInstance() {
        return new SideMenuFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (prefHelper.getUserType().equals("user")) {
            madapter = new ArrayListAdapter<NavigationEnt>(getDockActivity(), new NavigationItemBinder(getDockActivity(), this, prefHelper));
        } else if (prefHelper.getUserType().equals("technician")) {
            madapterTech = new ArrayListAdapter<NavigationEnt>(getDockActivity(), new NavigationItemBinder(getDockActivity(), this, prefHelper));

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_sidemenu;
    }

    @Override
    public void onResume() {

        super.onResume();
        //TokenUpdater.getInstance().UpdateToken(getDockActivity(), prefHelper.getUserId(), "android", prefHelper.getFirebase_TOKEN());

        LocalBroadcastManager.getInstance(getDockActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter(AppConstants.REGISTRATION_COMPLETE));

        LocalBroadcastManager.getInstance(getDockActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter(AppConstants.PUSH_NOTIFICATION));

    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getDockActivity()).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setlistItemClickListener();


        if (prefHelper.getUserType().equals("user")) {
            binddataUser();

        } else if (prefHelper.getUserType().equals("technician")) {
            bindDataTech();

        }
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            if (prefHelper.getUserType().equals("user")) {
                getUserProfile();
            } else if (prefHelper.getUserType().equals("technician")) {
                //   getTechProfile();
            }
        }


        onNotificationReceived();


    }

    public void refreshMenuOption() {
        if (madapter != null) {
            madapter.notifyDataSetChanged();
        }
        if (madapterTech != null) {
            madapterTech.notifyDataSetChanged();
        }
    }

    private void bindDataTech() {

        navigationItemListTech.add(new NavigationEnt(R.drawable.home_yellow, R.drawable.home, getDockActivity().getResources().getString(R.string.home)));
        navigationItemListTech.add(new NavigationEnt(R.drawable.profile_yellow, R.drawable.profile, getDockActivity().getResources().getString(R.string.profile)));
        navigationItemListTech.add(new NavigationEnt(R.drawable.notification_yellow, R.drawable.notification,
                getDockActivity().getResources().getString(R.string.notifications), notificationCount));
        navigationItemListTech.add(new NavigationEnt(R.drawable.jobs_technician, R.drawable.jobs_technician, getDockActivity().getResources().getString(R.string.New_Jobs)));
        navigationItemListTech.add(new NavigationEnt(R.drawable.language1, R.drawable.language, getDockActivity().getResources().getString(R.string.english)));
        navigationItemListTech.add(new NavigationEnt(R.drawable.about_yellow, R.drawable.about, getDockActivity().getResources().getString(R.string.about_app)));
        navigationItemListTech.add(new NavigationEnt(R.drawable.logout, R.drawable.logout, getDockActivity().getResources().getString(R.string.logout)));
        bindviewTech();

    }

    private void bindviewTech() {

        madapterTech.clearList();
        navListview.setAdapter(madapterTech);
        madapterTech.addAll(navigationItemListTech);
        madapterTech.notifyDataSetChanged();
    }

    private void getTechProfile() {
        if (prefHelper.isLogin() && prefHelper.getUserType().equals("technician")) {
            getDockActivity().onLoadingStarted();
            Call<ResponseWrapper<RegistrationResultEnt>> call = webService.techProfile(Integer.parseInt(prefHelper.getUserId()));

            call.enqueue(new Callback<ResponseWrapper<RegistrationResultEnt>>() {
                @Override
                public void onResponse(Call<ResponseWrapper<RegistrationResultEnt>> call, Response<ResponseWrapper<RegistrationResultEnt>> response) {
                    getDockActivity().onLoadingFinished();
                    if (response.body() != null) {
                        // UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.something_went_wrong));
                        return;
                    }
                    if (response.body().getResponse().equals("2000")) {
                        setProfileData(response.body().getResult());
                    } else {
                        UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseWrapper<RegistrationResultEnt>> call, Throwable t) {
                    Log.e("EntryCodeFragment", t.toString());
                    // UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
                }
            });

        }
    }

    private void setProfileData(TechProfileEnt result) {
        //prefHelper.putRegistrationResult(result);
        Picasso.with(getDockActivity()).load(result.getProfile_image()).into(CircularImageSharePop);
        txtUserName.setText(result.getFull_name());
        txtUseremail.setText(result.getEmail());

    }

    public void setInterface(UpdateNotificationsCount notificationsCount) {
        updateNotificationsCount = notificationsCount;
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
                    // getMainActivity().isNotification = true;
                    notificationCount = prefHelper.getBadgeCount();
                    updateNotificationsCount.updateCount(prefHelper.getBadgeCount());
                    getMainActivity().refreshSideMenu();
                    getMainActivity().titleBar.invalidate();
                    getMainActivity().titleBar.getImageView().invalidate();
                   /* getMainActivity().notificationIntent();
                    if (getMainActivity().isNotification) {
                       getMainActivity().isNotification = false;
                       getDockActivity().addDockableFragment(UserNotificationsFragment.newInstance(), "UserNotificationsFragment");
                    }*/

                    System.out.println(prefHelper.getFirebase_TOKEN());


                }
            }
        };
    }

    private void getUserProfile() {
        if (prefHelper.isLogin() && prefHelper.getUserType().equals("user")) {
            Call<ResponseWrapper<RegistrationResultEnt>> call = webService.getUserProfile(prefHelper.getUserId());
            call.enqueue(new Callback<ResponseWrapper<RegistrationResultEnt>>() {
                @Override
                public void onResponse(Call<ResponseWrapper<RegistrationResultEnt>> call, Response<ResponseWrapper<RegistrationResultEnt>> response) {
                    if (response.body() == null) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.server_error));
                        return;
                    }
                    if (response.body().getResponse() == null) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.server_error));
                        return;
                    }
                    if (response.body().getResponse().equals("2000")) {
                        setProfileData(response.body().getResult());
                    } else {

                        UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseWrapper<RegistrationResultEnt>> call, Throwable t) {
                    Log.e("EntryCodeFragment", t.toString());
                    //  UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
                }
            });
        }
    }

    private void setProfileData(RegistrationResultEnt result) {
        prefHelper.putRegistrationResult(result);
        Picasso.with(getDockActivity()).load(result.getProfileImage()).fit().into(CircularImageSharePop);
        Picasso.with(getDockActivity()).load(result.getProfileImage()).fit().into(btnUserImage);
        if (result.getFirstName() == null || result.getFirstName().equals("")) {
            txtUserName.setText(result.getFullName());
        } else
            txtUserName.setText(result.getFirstName() + " " + result.getLastName());
        txtUseremail.setText(result.getEmail());

    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case "en":
                prefHelper.putLang(getDockActivity(), "en");
                break;
            case "ar":
                prefHelper.putLang(getDockActivity(), "ar");
                break;
        }
    }

    private void setlistItemClickListener() {
        navListview.setOnItemClickListener((parent, view, position, id) -> {
            try {
                if (prefHelper.getUserType().equals("user")) {
                    if (navigationItemList.get(position).getItem_text().equals(getDockActivity().getResources().getString(R.string.home))) {
                        getDockActivity().popBackStackTillEntry(0);
                        getDockActivity().replaceDockableFragment(UserHomeFragment.newInstance(), "UserHomeFragment");
                    } else if (navigationItemList.get(position).getItem_text().equals(getDockActivity().getResources().getString(R.string.my_profile))) {
                        getDockActivity().replaceDockableFragment(UserProfileFragment.newInstance(), "UserProfileFragment");
                    } else if (navigationItemList.get(position).getItem_text().equals(getDockActivity().getResources().getString(R.string.notifications))) {
                        getDockActivity().replaceDockableFragment(UserNotificationsFragment.newInstance(), "UserHomeFragment");
                    } else if (navigationItemList.get(position).getItem_text().equals(getDockActivity().getResources().getString(R.string.my_job))) {
                        getDockActivity().replaceDockableFragment(UserJobsFragment.newInstance(), "UserjobsFragment");
                    } else if (navigationItemList.get(position).getItem_text().equals(getDockActivity().getResources().getString(R.string.english))) {
                        serviceHelper.enqueueCall(webService.updateUserLanguage(prefHelper.getUserId(), prefHelper.isLanguageArabic() ? "en" : "ar"), prefHelper.isLanguageArabic() ? "en" : "ar");
                    } else if (navigationItemList.get(position).getItem_text().equals(getDockActivity().getResources().getString(R.string.settings))) {
                        getDockActivity().replaceDockableFragment(SettingFragment.newInstance(), "SettingFragment");
                    } else if (navigationItemList.get(position).getItem_text().equals(getDockActivity().getResources().getString(R.string.user_logout))) {

                        final DialogHelper deleteAccount = new DialogHelper(getDockActivity());
                        deleteAccount.deteleAccountDialoge(R.layout.logout_technician_dialog, v -> logOutService(deleteAccount), v -> {
                            deleteAccount.hideDialog();
                            getMainActivity().closeDrawer();
                        });

                        deleteAccount.setCancelable(true);
                        deleteAccount.showDialog();


                    }

                } else {
                    if (navigationItemListTech.get(position).getItem_text().equals(getDockActivity().getResources().getString(R.string.home))) {
                        getDockActivity().popBackStackTillEntry(0);
                        getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                    } else if (navigationItemListTech.get(position).getItem_text().equals(getDockActivity().getResources().getString(R.string.notifications))) {
                        getDockActivity().replaceDockableFragment(TechNotificationsFragment.newInstance(), "TechNotificationsFragment");
                    } else if (navigationItemListTech.get(position).getItem_text().equals(getDockActivity().getResources().getString(R.string.profile))) {
                        getDockActivity().replaceDockableFragment(ProfileFragment.newInstance(), "ProfileFragment");
                    } else if (navigationItemListTech.get(position).getItem_text().equals(getDockActivity().getResources().getString(R.string.New_Jobs))) {
                        getDockActivity().replaceDockableFragment(NewJobsFragment.newInstance(), "NewJobsFragment");
                    } else if (navigationItemListTech.get(position).getItem_text().equals(getDockActivity().getResources().getString(R.string.english))) {
                        serviceHelper.enqueueCall(webService.updateUserLanguage(prefHelper.getUserId(), prefHelper.isLanguageArabic() ? "en" : "ar"), prefHelper.isLanguageArabic() ? "en" : "ar");
                    } else if (navigationItemListTech.get(position).getItem_text().equals(getDockActivity().getResources().getString(R.string.about_app))) {
                        getDockActivity().replaceDockableFragment(AboutAppFragment.newInstance(), "AboutFragment");
                    } else if (navigationItemListTech.get(position).getItem_text().equals(getDockActivity().getResources().getString(R.string.user_logout))) {
                        final DialogHelper techLogoutDialog = new DialogHelper(getDockActivity());
                        techLogoutDialog.logoutDialoge(R.layout.logout_technician_dialog, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                techLogout(techLogoutDialog);
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

                    }

                }
            } catch (Exception e) {
                refreshMenuOption();
            }

        });
    }

    private void techLogout(final DialogHelper techLogoutDialog) {
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            Call<ResponseWrapper> call = webService.logoutTechnician(prefHelper.getUserId());
            call.enqueue(new Callback<ResponseWrapper>() {
                @Override
                public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                    if (response.body().getResponse().equals("2000")) {
                        prefHelper.setLoginStatus(false);
                        getDockActivity().popBackStackTillEntry(0);
                        getDockActivity().replaceDockableFragment(UserSelectionFragment.newInstance(), "UserSelectionFragment");
                        techLogoutDialog.hideDialog();
                    } else {
                        UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                    Log.e("SideMenuFragment", t.toString());
                    //  UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
                }
            });
        }
    }

    private void logOutService(final DialogHelper deleteAccount) {
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            getMainActivity().onLoadingStarted();
            Call<ResponseWrapper> call = webService.logoutUser(prefHelper.getUserId());
            call.enqueue(new Callback<ResponseWrapper>() {
                @Override
                public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                    if (response.body().getResponse().equals("2000")) {
                        getMainActivity().onLoadingFinished();
                        prefHelper.setLoginStatus(false);
                        deleteAccount.hideDialog();
                        getDockActivity().popBackStackTillEntry(0);
                        getDockActivity().replaceDockableFragment(UserSelectionFragment.newInstance(), "UserSelectionFragment");
                    } else {
                        getMainActivity().onLoadingFinished();
                        UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                    getMainActivity().onLoadingFinished();
                    Log.e("SideMenuFragment", t.toString());
                    //  UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
                }
            });
        }
    }

    private void binddataUser() {
        navigationItemList.add(new NavigationEnt(R.drawable.home_yellow, R.drawable.home, getDockActivity().getResources().getString(R.string.home)));
        navigationItemList.add(new NavigationEnt(R.drawable.profile_yellow, R.drawable.profile, getDockActivity().getResources().getString(R.string.my_profile)));
        navigationItemList.add(new NavigationEnt(R.drawable.notification_yellow, R.drawable.notification,
                getString(R.string.notifications), notificationCount));
        navigationItemList.add(new NavigationEnt(R.drawable.jobs_yellow, R.drawable.jobs, getDockActivity().getResources().getString(R.string.my_job)));
        navigationItemList.add(new NavigationEnt(R.drawable.about_yellow, R.drawable.about, getDockActivity().getResources().getString(R.string.settings)));
        navigationItemList.add(new NavigationEnt(R.drawable.language1, R.drawable.language, getDockActivity().getResources().getString(R.string.english)));
        navigationItemList.add(new NavigationEnt(R.drawable.logout_yellow, R.drawable.logout, getDockActivity().getResources().getString(R.string.user_logout)));

        bindview();
    }

    private void bindview() {
        madapter.clearList();
        navListview.setAdapter(madapter);
        madapter.addAll(navigationItemList);
        madapter.notifyDataSetChanged();
    }


}
