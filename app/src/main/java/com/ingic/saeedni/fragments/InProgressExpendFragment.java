package com.ingic.saeedni.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.JobRequestEnt;
import com.ingic.saeedni.entities.RequestDetail;
import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.entities.TechInProgressEnt;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.AppConstants;
import com.ingic.saeedni.helpers.DialogHelper;
import com.ingic.saeedni.helpers.InternetHelper;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.interfaces.CallUser;
import com.ingic.saeedni.interfaces.MarkAsComplete;
import com.ingic.saeedni.interfaces.SetOrderCounts;
import com.ingic.saeedni.ui.ArrayListExpandableAdapter;
import com.ingic.saeedni.ui.viewbinder.InprogressExpandBinder;
import com.ingic.saeedni.ui.views.AnyTextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InProgressExpendFragment extends BaseFragment implements MarkAsComplete, CallUser {
    @BindView(R.id.txt_noresult)
    AnyTextView txtNoresult;
    @BindView(R.id.elv_inprogress)
    ExpandableListView elvInprogress;
    SetOrderCounts orderCounts;


    private ArrayListExpandableAdapter<RequestDetail, RequestDetail> adapter;
    private ArrayList<RequestDetail> collectionGroup;
    private ArrayList<RequestDetail> collectionChild;


    private ArrayList<TechInProgressEnt> userCollection;
    private HashMap<RequestDetail, ArrayList<RequestDetail>> listDataChild;

    public static InProgressExpendFragment newInstance() {
        return new InProgressExpendFragment();
    }

    public SetOrderCounts getOrderCounts() {
        return orderCounts;
    }

    public void setOrderCounts(SetOrderCounts orderCounts) {
        this.orderCounts = orderCounts;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
        return R.layout.fragment_inprogress_expand;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userCollection = new ArrayList<>();
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            getInProgressJobsData();
        }
    }

    private void getInProgressJobsData() {

        getDockActivity().onLoadingStarted();
        Call<ResponseWrapper<ArrayList<TechInProgressEnt>>> call = webService.techInProgress(Integer.valueOf(prefHelper.getUserId()));

        call.enqueue(new Callback<ResponseWrapper<ArrayList<TechInProgressEnt>>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<ArrayList<TechInProgressEnt>>> call, Response<ResponseWrapper<ArrayList<TechInProgressEnt>>> response) {
                getDockActivity().onLoadingFinished();
                if (response.body().getResponse().equals("2000")) {
                    userCollection.addAll(response.body().getResult());
                    setInProgressJobsData(userCollection);
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<ArrayList<TechInProgressEnt>>> call, Throwable t) {
                getDockActivity().onLoadingFinished();
                Log.e("UserSignupFragment", t.toString());
                //   UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
            }
        });


    }

    private void setInProgressJobsData(ArrayList<TechInProgressEnt> result) {

     /*   userCollection.add(new InProgressEnt("02","23-3-17","Al Musa","Plumbing","AED 55.00","Dubai Marina,NearMarina"));
        userCollection.add(new InProgressEnt("02","23-3-17","Al Musa","Plumbing","AED 55.00","Dubai Marina,NearMarina"));
        userCollection.add(new InProgressEnt("02","23-3-17","Al Musa","Plumbing","AED 55.00","Dubai Marina,NearMarina"));
        userCollection.add(new InProgressEnt("02","23-3-17","Al Musa","Plumbing","AED 55.00","Dubai Marina,NearMarina"));

        bindData(userCollection);*/

        collectionGroup = new ArrayList<>();
        collectionChild = new ArrayList<>();

        txtNoresult.setText(R.string.no_inprogress_job);
        if(result.size()>0){
            elvInprogress.setVisibility(View.VISIBLE);
            txtNoresult.setVisibility(View.GONE);
        }else{
            elvInprogress.setVisibility(View.GONE);
            txtNoresult.setVisibility(View.VISIBLE);
        }

        listDataChild = new HashMap<>();
        for (TechInProgressEnt item : result
                ) {
            collectionGroup.add(item.getRequest_detail());
            if (item.getRequest_detail().getSubRequest().size() > 0) {
                for (RequestDetail childItem : item.getRequest_detail().getSubRequest()
                        ) {
                    childItem.setUser_detail(item.getRequest_detail().getUser_detail());
                    childItem.setTotal_amount(item.getRequest_detail().getTotal_amount());
                    collectionChild.add(childItem);

                }
                //  collectionChild.addAll(item.getRequest_detail().getSubRequest());}
                listDataChild.put(item.getRequest_detail(), collectionChild);
                collectionChild = new ArrayList<>();
            } else {
                listDataChild.put(item.getRequest_detail(), new ArrayList<RequestDetail>());
            }

        }
          /*  collectionGroup.addAll(result);

        //collectionGroup.add(new InProgressParentEnt("re","23-3-17","Al Musa","Plumbing","AED 55.00","Dubai Marina,NearMarina"));
        for(ServiceDetail)
        collectionChild.add(new InProgressChildEnt("Your order is successfully made "+"\n"+"and the details have been stored.","AED 110","AED 200"));
        collectionChild.add(new InProgressChildEnt("Your order is successfully made "+"\n"+"and the details have been stored.","AED 110","AED 200"));
            listDataChild.put(collectionGroup.get(0), collectionChild);
            listDataChild.put(collectionGroup.get(1), collectionChild);
             listDataChild.put(collectionGroup.get(2), collectionChild);
          listDataChild.put(collectionGroup.get(3), collectionChild);*/
        orderCounts.setInprogressCount(collectionGroup.size());
        adapter = new ArrayListExpandableAdapter<>(getDockActivity(), collectionGroup, listDataChild, new InprogressExpandBinder(getDockActivity(), this, this, prefHelper), elvInprogress);
        bindData();
    }

    private void bindData() {


        if (elvInprogress != null)
            elvInprogress.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void markAsComplete(final int position, final String RequestID) {
        String assignID = "", price = "";
        String userID = "";
        if (userCollection.size() > position) {
            assignID = String.valueOf(userCollection.get(position).getId());
            price = userCollection.get(position).getRequest_detail().getTotal_amount() + "";
            userID = userCollection.get(position).getRequest_detail().getUser_id();
        }
        if (!price.equals("0") && !price.isEmpty() && !price.equals("")) {
            final DialogHelper helper = new DialogHelper(getDockActivity());
            final String finalAssignID = assignID;
            final String finalUserID = userID;
            helper.initMarkCompleteDialog(price, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callMarkAsCompleteAPI(RequestID, finalAssignID, finalUserID, Math.round(helper.getRating(R.id.rbAddRating)));
                    helper.hideDialog();
                }
            });
            helper.showDialog();
        } else {
            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.please_entet_amount));
        }


    }

    private void callMarkAsCompleteAPI(final String RequestID, String assignID, final String userID, final int rating) {

        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            getDockActivity().onLoadingStarted();
            Call<ResponseWrapper<JobRequestEnt>> call = webService.markComplete(
                    assignID, prefHelper.getUserId(), RequestID, AppConstants.TECH_MARK_COMPLETE);

            call.enqueue(new Callback<ResponseWrapper<JobRequestEnt>>() {
                @Override
                public void onResponse(Call<ResponseWrapper<JobRequestEnt>> call, Response<ResponseWrapper<JobRequestEnt>> response) {

                    getDockActivity().onLoadingFinished();
                    if (response.body().getResponse().equals("2000")) {
                        // UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                        callRatingAPI(RequestID, userID, rating);
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
                    //  UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
                }
            });
        }
    }

    private void callRatingAPI(String RequestID, String userID, int rating) {

        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            getDockActivity().onLoadingStarted();
            Call<ResponseWrapper> call = webService.sendUserFeedback(userID, RequestID, prefHelper.getUserId(), rating);

            call.enqueue(new Callback<ResponseWrapper>() {
                @Override
                public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {

                    getDockActivity().onLoadingFinished();
                    if (response.body().getResponse().equals("2000")) {
                        // UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                        getDockActivity().popBackStackTillEntry(0);
                        getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                    } else {
                        UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                    }

                }

                @Override
                public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                    getDockActivity().onLoadingFinished();
                    Log.e("EntryCodeFragment", t.toString());
                    //  UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
                }
            });
        }
    }

    @Override
    public void CallOnUserNumber(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }
}
