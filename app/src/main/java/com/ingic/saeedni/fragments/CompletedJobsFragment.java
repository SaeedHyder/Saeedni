package com.ingic.saeedni.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.entities.TechInProgressEnt;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.helpers.InternetHelper;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.interfaces.SetOrderCounts;
import com.ingic.saeedni.ui.adapters.ArrayListAdapter;
import com.ingic.saeedni.ui.viewbinder.CompletedJobsBinder;
import com.ingic.saeedni.ui.views.AnyTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompletedJobsFragment extends BaseFragment {

    @BindView(R.id.txt_no_data)
    AnyTextView txt_no_data;
    @BindView(R.id.CompletedJobs_ListView)
    ListView CompletedJobsListView;
    SetOrderCounts orderCounts;

    private ArrayListAdapter<TechInProgressEnt> adapter;

    private ArrayList<TechInProgressEnt> userCollection = new ArrayList<>();

    public static CompletedJobsFragment newInstance() {

        return new CompletedJobsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<TechInProgressEnt>(getDockActivity(), new CompletedJobsBinder(prefHelper));
    }

    public SetOrderCounts getOrderCounts() {
        return orderCounts;
    }

    public void setOrderCounts(SetOrderCounts orderCounts) {
        this.orderCounts = orderCounts;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completedjobs, container, false);
       ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_completedjobs;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            getCompletedJobs();
        }
    }

    private void getCompletedJobs() {
        getDockActivity().onLoadingStarted();
        Call<ResponseWrapper<ArrayList<TechInProgressEnt>>> call = webService.techCompleteJobs(Integer.valueOf(prefHelper.getUserId()));

        call.enqueue(new Callback<ResponseWrapper<ArrayList<TechInProgressEnt>>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<ArrayList<TechInProgressEnt>>> call, Response<ResponseWrapper<ArrayList<TechInProgressEnt>>> response) {
                getDockActivity().onLoadingFinished();
                if (response.body().getResponse().equals("2000")) {
                    setCompletedJobsData(response.body().getResult());
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<ArrayList<TechInProgressEnt>>> call, Throwable t) {
                getDockActivity().onLoadingFinished();
                Log.e("UserSignupFragment", t.toString());
               // UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
            }
        });

    }


    private void setCompletedJobsData(ArrayList<TechInProgressEnt> result) {
      /*  userCollection.add(new CompletedJobsEnt("02","23-3-17","Al Musa","Plumbing","5","AED 55.00"));
        userCollection.add(new CompletedJobsEnt("02","23-3-17","Al Musa","Plumbing","5","AED 55.00"));
        userCollection.add(new CompletedJobsEnt("02","23-3-17","Al Musa","Plumbing","5","AED 55.00"));*/
        userCollection = new ArrayList<>();
        orderCounts.setcompleteCount(result.size());

        txt_no_data.setText(getDockActivity().getResources().getString(R.string.no_completed_job));
        if(result.size()>0)
        {
            txt_no_data.setVisibility(View.GONE);
            CompletedJobsListView.setVisibility(View.VISIBLE);
        }
        else{
            txt_no_data.setVisibility(View.VISIBLE);
            CompletedJobsListView.setVisibility(View.GONE);
        }
        userCollection.addAll(result);


        bindData(userCollection);
    }

    private void bindData(ArrayList<TechInProgressEnt> userCollection) {

        adapter.clearList();
        if (CompletedJobsListView!=null)
        CompletedJobsListView.setAdapter(adapter);
        adapter.addAll(userCollection);

    }


}
