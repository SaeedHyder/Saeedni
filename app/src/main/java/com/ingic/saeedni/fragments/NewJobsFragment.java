package com.ingic.saeedni.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.JobRequestEnt;
import com.ingic.saeedni.entities.NewJobsEnt;
import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.AppConstants;
import com.ingic.saeedni.helpers.DialogHelper;
import com.ingic.saeedni.helpers.InternetHelper;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.ui.adapters.ArrayListAdapter;
import com.ingic.saeedni.ui.viewbinder.SaeedniNewJobBinder;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.ingic.saeedni.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by saeedhyder on 5/22/2017.
 */

public class NewJobsFragment extends BaseFragment {

    @BindView(R.id.lv_NewJobs)
    ListView lv_NewJobs;

    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;

    private ArrayListAdapter<NewJobsEnt> adapter;
    private ArrayList<NewJobsEnt> userCollection;
    private View.OnClickListener jobsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final NewJobsEnt ent = (NewJobsEnt) v.getTag(R.integer.key_recycler_object);
            int position = (int) v.getTag(R.integer.key_recycler_position);
            switch (v.getId()) {
                case R.id.btn_accept:
                    jobAccept(ent);
                    break;
                case R.id.btn_reject:
                    final DialogHelper RefusalDialog = new DialogHelper(getDockActivity());
                    RefusalDialog.initJobRefusalDialog(R.layout.job_refusal_dialog, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                                if (RefusalDialog.getEditTextView(R.id.ed_msg).getText().toString().trim().equals("")) {
                                    RefusalDialog.getEditTextView(R.id.ed_msg).setError(getString(R.string.enter_message));
                                } else {
                                    jobReject(RefusalDialog.getEditText(R.id.ed_msg), RefusalDialog, ent);
                                }
                            }

                            //RefusalDialog.hideDialog();
                        }
                    });
                    RefusalDialog.setCancelable(true);
                    RefusalDialog.showDialog();
                    break;
            }
        }
    };

    public static NewJobsFragment newInstance() {
        return new NewJobsFragment();
    }

    private void jobReject(String reason, final DialogHelper refusalDialog, NewJobsEnt newJobJson) {

        if (!reason.equals("")) {
            getDockActivity().onLoadingStarted();
            Call<ResponseWrapper<JobRequestEnt>> call = webService.rejectJob(newJobJson.getId(),
                    prefHelper.getUserId(), newJobJson.getRequest_id(), AppConstants.TECH_REJECT_JOB, reason);
            call.enqueue(new Callback<ResponseWrapper<JobRequestEnt>>() {
                @Override
                public void onResponse(Call<ResponseWrapper<JobRequestEnt>> call, Response<ResponseWrapper<JobRequestEnt>> response) {
                    getDockActivity().onLoadingFinished();
                    if (response.body().getResponse().equals("2000")) {
                        refusalDialog.hideDialog();
                        getDockActivity().popBackStackTillEntry(0);
                        getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");

                    } else {
                        UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseWrapper<JobRequestEnt>> call, Throwable t) {
                    getDockActivity().onLoadingFinished();
                    Log.e("EntryCodeFragment", t.toString());
                    // UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
                }
            });
        } else {
            UIHelper.showShortToastInCenter(getDockActivity(), "Enter the Reason");
        }

    }

    private void jobAccept(NewJobsEnt newJobJson) {
        getDockActivity().onLoadingStarted();
        Call<ResponseWrapper<JobRequestEnt>> call = webService.acceptJob(newJobJson.getId(), prefHelper.getUserId(), newJobJson.getRequest_id(),
                AppConstants.TECH_ACCEPT_JOB);
        call.enqueue(new Callback<ResponseWrapper<JobRequestEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<JobRequestEnt>> call, Response<ResponseWrapper<JobRequestEnt>> response) {
                getDockActivity().onLoadingFinished();
                if (response.body().getResponse().equals("2000")) {
                    getDockActivity().popBackStackTillEntry(1);
                    getDockActivity().replaceDockableFragment(OrderHistoryFragment.newInstance(), "HomeFragment");
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<JobRequestEnt>> call, Throwable t) {
                getDockActivity().onLoadingFinished();
                Log.e("EntryCodeFragment", t.toString());
                //  UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
            }
        });


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArrayListAdapter<NewJobsEnt>(getDockActivity(), new SaeedniNewJobBinder(prefHelper, jobsClickListener));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_newjobs, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_newjobs;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        getDockActivity().lockDrawer();
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.New_Jobs));

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            getNewJobs();
        }
        selectNewJobsListItem();


    }

    private void getNewJobs() {
        loadingStarted();
        Call<ResponseWrapper<ArrayList<NewJobsEnt>>> call = webService.newJobs(Integer.valueOf(prefHelper.getUserId()));

        call.enqueue(new Callback<ResponseWrapper<ArrayList<NewJobsEnt>>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<ArrayList<NewJobsEnt>>> call, Response<ResponseWrapper<ArrayList<NewJobsEnt>>> response) {
                loadingFinished();
                if (response.body().getResponse().equals("2000")) {
                    setNewJobsData(response.body().getResult());
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<ArrayList<NewJobsEnt>>> call, Throwable t) {
                loadingFinished();
                Log.e("UserSignupFragment", t.toString());
                // UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
            }
        });

    }

    private void setNewJobsData(ArrayList<NewJobsEnt> result) {
        if (result.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            lv_NewJobs.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            lv_NewJobs.setVisibility(View.VISIBLE);
        }
        userCollection = new ArrayList<>();
        userCollection.addAll(result);

        bindData(userCollection);
    }

    private void bindData(ArrayList<NewJobsEnt> userCollection) {

        adapter.clearList();
        lv_NewJobs.setAdapter(adapter);
        adapter.addAll(userCollection);
        adapter.notifyDataSetChanged();
    }

    private void selectNewJobsListItem() {

        lv_NewJobs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //getDockActivity().addDockableFragment(NewJobDetail.newInstance(userCollection.get(position)), "NewJobDetail");
            }
        });

    }
}