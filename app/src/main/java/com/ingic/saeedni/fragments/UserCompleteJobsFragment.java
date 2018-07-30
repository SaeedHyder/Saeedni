package com.ingic.saeedni.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.entities.UserComleteJobsEnt;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.AppConstants;
import com.ingic.saeedni.helpers.InternetHelper;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.interfaces.SetOrderCounts;
import com.ingic.saeedni.ui.adapters.ArrayListAdapter;
import com.ingic.saeedni.ui.viewbinder.UserCompleteJobsBinder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 5/24/2017.
 */

public class UserCompleteJobsFragment extends BaseFragment {
   /* @BindView(R.id.txt_noresult)
    AnyTextView txtNoresult;*/
    @BindView(R.id.CompletedJobs_ListView)
    ListView CompletedJobsListView;
    SetOrderCounts orderCounts;
    private ArrayListAdapter<UserComleteJobsEnt> adapter;
    private ArrayList<UserComleteJobsEnt> userCollection = new ArrayList<>();
    private BroadcastReceiver broadcastReceiver;

    public static UserCompleteJobsFragment newInstance() {
        return new UserCompleteJobsFragment();
    }

    public SetOrderCounts getOrderCounts() {
        return orderCounts;
    }

    public void setOrderCounts(SetOrderCounts orderCounts) {
        this.orderCounts = orderCounts;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_completedjobs;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<UserComleteJobsEnt>(getDockActivity(), new UserCompleteJobsBinder(prefHelper));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completedjobs, container, false);
      ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onNotificationReceived();
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            getCompletedJobs();
        }
    }
    @Override
    public void onResume() {

        super.onResume();
        // TokenUpdater.getInstance().UpdateToken(getDockActivity(), prefHelper.getUserId(), "android", prefHelper.getFirebase_TOKEN());

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
                    getMainActivity().titleBar.invalidate();
                    getMainActivity().titleBar.getImageView().invalidate();
                    if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                        getCompletedJobs();
                    }

                    System.out.println(prefHelper.getFirebase_TOKEN());


                }
            }
        };
    }

    private void getCompletedJobs() {
        getDockActivity().onLoadingStarted();
        Call<ResponseWrapper<ArrayList<UserComleteJobsEnt>>> call = webService.getUserCompleted(prefHelper.getUserId());

        call.enqueue(new Callback<ResponseWrapper<ArrayList<UserComleteJobsEnt>>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<ArrayList<UserComleteJobsEnt>>> call, Response<ResponseWrapper<ArrayList<UserComleteJobsEnt>>> response) {
                getDockActivity().onLoadingFinished();
                if (response.body().getResponse().equals("2000")) {
                    setCompletedJobsData(response.body().getResult());
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<ArrayList<UserComleteJobsEnt>>> call, Throwable t) {
                getDockActivity().onLoadingFinished();
                Log.e("EntryCodeFragment", t.toString());
                //UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
            }
        });

    }


    private void setCompletedJobsData(ArrayList<UserComleteJobsEnt> result) {
        userCollection = new ArrayList<>();

      /*  if (result.size() <= 0) {
            UIHelper.showShortToastInCenter(getDockActivity(), "No Data to Show");
        } else {
            CompletedJobsListView.setVisibility(View.GONE);
        }
*/
        userCollection.addAll(result);
        orderCounts.setcompleteCount(result.size());
      /*  userCollection.add(new UserComleteJobsEnt("01", "24-3-17", "Al Musa", "Electrical", 4, "AED 55.00",getString(R.string.dummy_desciption)));
        userCollection.add(new UserComleteJobsEnt("02", "25-3-17", "Al Musa", "Plumbing", 3, "AED 55.00",getString(R.string.dummy_desciption)));
        userCollection.add(new UserComleteJobsEnt("03", "26-3-17", "Al Musa", "Cleaning", 5, "AED 55.00",getString(R.string.dummy_desciption)));*/

        bindData(userCollection);
    }

    private void bindData(ArrayList<UserComleteJobsEnt> userCollection) {

        adapter.clearList();
        CompletedJobsListView.setAdapter(adapter);
        adapter.addAll(userCollection);
        adapter.notifyDataSetChanged();
    }




}
