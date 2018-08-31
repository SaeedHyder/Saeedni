package com.ingic.saeedni.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.NotificationEnt;
import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.entities.countEnt;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.helpers.InternetHelper;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.ui.adapters.ArrayListAdapter;
import com.ingic.saeedni.ui.viewbinder.UserNotificationitemBinder;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.ingic.saeedni.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserNotificationsFragment extends BaseFragment {
    @BindView(R.id.lv_UserNotification)
    ListView lvUserNotification;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    final Handler handler = new Handler();


    private ArrayListAdapter<NotificationEnt> adapter;
    private ArrayList<NotificationEnt> userCollection;

    public static UserNotificationsFragment newInstance() {
        return new UserNotificationsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefHelper.setBadgeCount(0);
        adapter = new ArrayListAdapter<NotificationEnt>(getDockActivity(), new UserNotificationitemBinder(getDockActivity(), webService, prefHelper));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_user_notification;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void getNotificationCount() {
        prefHelper.setBadgeCount(0);
        getMainActivity().refreshSideMenu();
        getMainActivity().titleBar.invalidate();
        getMainActivity().titleBar.getImageView().invalidate();

      /*  Call<ResponseWrapper<countEnt>> callback = webService.getNotificationCount(prefHelper.getUserId());
        callback.enqueue(new Callback<ResponseWrapper<countEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<countEnt>> call, Response<ResponseWrapper<countEnt>> response) {

                prefHelper.setBadgeCount(response.body().getResult().getCount());
                if (getMainActivity() != null)
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getMainActivity().refreshSideMenu();
                        }
                    }, 1000);

                Log.e(UserNotificationsFragment.class.getSimpleName(), "aasd" + prefHelper.getUserId() + response.body().getResult().getCount());
                //  SendNotification(response.body().getResult().getCount(), json);
            }

            @Override
            public void onFailure(Call<ResponseWrapper<countEnt>> call, Throwable t) {
                Log.e(UserNotificationsFragment.class.getSimpleName(), t.toString());
                System.out.println(t.toString());
            }
        });*/


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            loadingStarted();
            getNotification();
        }
        NotificationItemListner();
        getNotificationCount();
        prefHelper.setBadgeCount(0);
        getMainActivity().refreshSideMenu();

    }

    private void getNotification() {
        Call<ResponseWrapper<ArrayList<NotificationEnt>>> call = webService.getNotification(prefHelper.getUserId());
        call.enqueue(new Callback<ResponseWrapper<ArrayList<NotificationEnt>>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<ArrayList<NotificationEnt>>> call, Response<ResponseWrapper<ArrayList<NotificationEnt>>> response) {
                loadingFinished();
                if (response.body().getResponse().equals("2000")) {
                    setNotificationData(response.body().getResult());
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<ArrayList<NotificationEnt>>> call, Throwable t) {
                loadingFinished();
                Log.e("UserSignupFragment", t.toString());
                //UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
            }
        });
    }

    private void setNotificationData(ArrayList<NotificationEnt> result) {
        userCollection = new ArrayList<>();
        if (result.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            lvUserNotification.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            lvUserNotification.setVisibility(View.VISIBLE);
        }
        userCollection.addAll(result);
        bindData(userCollection);
    }

    private void NotificationItemListner() {

        lvUserNotification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

    }


    private void bindData(ArrayList<NotificationEnt> userCollection) {

        adapter.clearList();
        lvUserNotification.setAdapter(adapter);
        adapter.addAll(userCollection);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        getDockActivity().lockDrawer();
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.Notifications));

    }


}
