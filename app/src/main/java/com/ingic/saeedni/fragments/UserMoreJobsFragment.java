package com.ingic.saeedni.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.entities.ServiceEnt;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.ui.viewbinder.MoreJobsBinder;
import com.ingic.saeedni.ui.views.CustomRecyclerView;
import com.ingic.saeedni.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 5/28/18.
 */
public class UserMoreJobsFragment extends BaseFragment {
    public static final String TAG = "UserMoreJobsFragment";
    @BindView(R.id.rv_jobs)
    CustomRecyclerView rvJobs;
    Unbinder unbinder;
    private ArrayList<ServiceEnt> jobsCollection;

    public static UserMoreJobsFragment newInstance() {
        Bundle args = new Bundle();

        UserMoreJobsFragment fragment = new UserMoreJobsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_jobs, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_more_jobs;
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
                    bindJobs(jobsCollection);

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
    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        getDockActivity().releaseDrawer();
        titleBar.hideButtons();
        titleBar.showNotificationButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(UserNotificationsFragment.newInstance(), "UserNotificationsFragment");

            }
        }, prefHelper);
        titleBar.showMenuButton();
        titleBar.setSubHeading(getString(R.string.requesr_service_home));
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gethomeData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void bindJobs(ArrayList<ServiceEnt> jobsCollection) {
        if (rvJobs!=null&&jobsCollection!=null) {
            rvJobs.bindRecyclerView(new MoreJobsBinder(prefHelper, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getDockActivity().replaceDockableFragment(RequestServiceFragment.newInstance((ServiceEnt) v.getTag(), null), "RequestServiceFragment");
                        }
                    }), jobsCollection,
                    new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false), new DefaultItemAnimator());
        }
    }
}