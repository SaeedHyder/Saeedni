package com.ingic.saeedni.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.InProgressEnt;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.interfaces.CallUser;
import com.ingic.saeedni.ui.adapters.ArrayListAdapter;
import com.ingic.saeedni.ui.views.AnyTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by saeedhyder on 5/24/2017.
 */

public class InProgressFragment extends BaseFragment implements CallUser{

    @BindView(R.id.txt_noresult)
    AnyTextView txtNoresult;
    @BindView(R.id.InProgress_ListView)
    ListView InProgressListView;
    Unbinder unbinder;

    private ArrayListAdapter<InProgressEnt> adapter;

    private ArrayList<InProgressEnt> userCollection = new ArrayList<>();

    public static InProgressFragment newInstance() {
        return new InProgressFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_inprogress;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //adapter = new ArrayListAdapter<InProgressEnt>(getDockActivity(), new InProgressBinder());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setInProgressJobsData();
    }

    private void setInProgressJobsData() {

        userCollection.add(new InProgressEnt("02","23-3-17","Al Musa","Plumbing","AED 55.00","Dubai Marina,NearMarina"));
        userCollection.add(new InProgressEnt("02","23-3-17","Al Musa","Plumbing","AED 55.00","Dubai Marina,NearMarina"));
        userCollection.add(new InProgressEnt("02","23-3-17","Al Musa","Plumbing","AED 55.00","Dubai Marina,NearMarina"));
        userCollection.add(new InProgressEnt("02","23-3-17","Al Musa","Plumbing","AED 55.00","Dubai Marina,NearMarina"));

        bindData(userCollection);
    }

    private void bindData(ArrayList<InProgressEnt> userCollection) {

        adapter.clearList();
        InProgressListView.setAdapter(adapter);
        adapter.addAll(userCollection);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void CallOnUserNumber(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0123456789"));
        startActivity(intent);
    }


}

