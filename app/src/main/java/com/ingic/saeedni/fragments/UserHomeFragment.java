package com.ingic.saeedni.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.entities.ServiceEnt;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.AppConstants;
import com.ingic.saeedni.helpers.InternetHelper;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.ui.adapters.ArrayListAdapter;
import com.ingic.saeedni.ui.adapters.HomeServiceAdapter;
import com.ingic.saeedni.ui.viewbinder.HomeServiceBinder;
import com.ingic.saeedni.ui.viewbinder.UserHomeJobsBinder;
import com.ingic.saeedni.ui.views.CustomRecyclerView;
import com.ingic.saeedni.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 5/23/2017.
 */

public class UserHomeFragment extends BaseFragment  {

    public boolean isNotification = false;
    protected BroadcastReceiver broadcastReceiver;

    @BindView(R.id.mainFrame)
    LinearLayout mainFrame;
    ArrayList<ServiceEnt> jobsCollection;
    @BindView(R.id.edtSearch)
    SearchView edtSearch;
    @BindView(R.id.rvJobs)
    CustomRecyclerView rvJobs;
    private ArrayList<ServiceEnt> userservices;
    private HomeServiceAdapter madapter;
    private ArrayListAdapter<ServiceEnt> mServiceAdapter;
    private int TOTAL_CELLS_PER_ROW = 3;
    private ImageLoader imageLoader;
    private TitleBar titlebar;

    public static UserHomeFragment newInstance() {
        return new UserHomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mServiceAdapter = new ArrayListAdapter<ServiceEnt>(getDockActivity(), new HomeServiceBinder(getDockActivity(), prefHelper));

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
        return R.layout.home_user;
    }

    @Override
    public void onResume() {

        super.onResume();
        // TokenUpdater.getInstance().UpdateToken(getDockActivity(), prefHelper.getUserId(), "android", prefHelper.getFirebase_TOKEN());
        edtSearch.setQuery("", false);
        edtSearch.clearFocus();
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
        getDockActivity().releaseDrawer();
        this.titlebar = titleBar;
        titleBar.hideButtons();
        titleBar.showNotificationButton(v -> getDockActivity().replaceDockableFragment(UserNotificationsFragment.newInstance(), "UserNotificationsFragment"), prefHelper);
        titleBar.showMenuButton();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.requesr_service_home));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().refreshFirstTimeSideMenu();
        imageLoader = ImageLoader.getInstance();
        getMainActivity().refreshSideMenu();
        getMainActivity().titleBar.invalidate();
        getMainActivity().titleBar.getImageView().invalidate();
        //mainFrame.setVisibility(View.GONE);
        onNotificationReceived();
        UIHelper.hideSoftKeyboard(getDockActivity(),edtSearch);
        getMainActivity().refreshSideMenuWithnewFragment();
        edtSearch.setQuery("", false);
        edtSearch.clearFocus();
        edtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                if (rvJobs != null && rvJobs.getAdapter() != null)
                    rvJobs.getAdapter().getFilter().filter(text);
                return true;
            }
        });
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            loadingStarted();
            gethomeData();
        }


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
                    getMainActivity().refreshSideMenu();
                    if (titlebar == null) {
                        getMainActivity().titleBar.invalidate();
                        getMainActivity().titleBar.getImageView().invalidate();
                    } else {
                        titlebar.invalidate();
                        titlebar.getImageView().invalidate();
                        titlebar.showNotificationButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getDockActivity().replaceDockableFragment(UserNotificationsFragment.newInstance(), "UserNotificationsFragment");

                            }
                        }, prefHelper);
                    }

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

    private void gethomeData() {
        Call<ResponseWrapper<ArrayList<ServiceEnt>>> call = webService.getHomeServices();
        call.enqueue(new Callback<ResponseWrapper<ArrayList<ServiceEnt>>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<ArrayList<ServiceEnt>>> call, Response<ResponseWrapper<ArrayList<ServiceEnt>>> response) {
                loadingFinished();
                if (response.body().getResponse().equals("2000")) {
                    jobsCollection = new ArrayList<>();
                    jobsCollection.addAll(response.body().getResult());
                    bindData(jobsCollection);

                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<ArrayList<ServiceEnt>>> call, Throwable t) {
                loadingFinished();
                Log.e("UserHome", t.toString());
                //   UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void bindData(ArrayList<ServiceEnt> result) {

        userservices = new ArrayList<>();
        userservices.addAll(result);
        if (userservices.size() > 8) {
            userservices = new ArrayList<>(userservices.subList(0, 7));
        }
        userservices.add(new ServiceEnt("More", "المزيد", "drawable://" + R.drawable.addicon));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getDockActivity(), 2, GridLayoutManager.VERTICAL, false);

        rvJobs.bindRecyclerView(new UserHomeJobsBinder(prefHelper, (ent, position, id) -> {
            ServiceEnt serviceEnt = (ServiceEnt) ent;
            if (serviceEnt.getTitle().equalsIgnoreCase("More") || serviceEnt.getArTitle().equalsIgnoreCase("المزيد")) {
                openMoreJobsDialog();
            } else {
                addRequestServiceFragment(serviceEnt);
            }
        }), userservices, gridLayoutManager, new DefaultItemAnimator());
        rvJobs.getAdapter().setSearchOriginalList(jobsCollection);

    }


    private void addRequestServiceFragment(ServiceEnt type) {
        getDockActivity().replaceDockableFragment(RequestServiceFragment.newInstance(type, null), "RequestServiceFragment");
    }

    private void openMoreJobsDialog() {
        getDockActivity().replaceDockableFragment(UserMoreJobsFragment.newInstance(), UserMoreJobsFragment.TAG);

    }




}