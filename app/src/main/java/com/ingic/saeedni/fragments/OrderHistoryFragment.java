package com.ingic.saeedni.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ingic.saeedni.R;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.interfaces.SetOrderCounts;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.ingic.saeedni.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.ingic.saeedni.activities.DockActivity.KEY_FRAG_FIRST;


public class OrderHistoryFragment extends BaseFragment implements View.OnClickListener, SetOrderCounts {

    @BindView(R.id.ll_listView)
    LinearLayout llListView;

    @BindView(R.id.ll_CompletedJobs)
    LinearLayout llCompletedJobs;
    @BindView(R.id.ll_InProgess)
    LinearLayout llInProgess;
    @BindView(R.id.selectedArrowCompletedJobs)
    ImageView selectedArrowCompletedJobs;
    @BindView(R.id.selectedArrowInProgress)
    ImageView selectedArrowInProgress;
    @BindView(R.id.txt_jobCount)
    AnyTextView txtJobCount;
    @BindView(R.id.txt_InProgressCount)
    AnyTextView txtInProgressCount;
    @BindView(R.id.mainFrame)
    LinearLayout mainFrame;
    @BindView(R.id.CircularImageSharePop)
    CircleImageView mCircularImageSharePop;
    @BindView(R.id.txt_userName)
    AnyTextView mTxtUserName;
    @BindView(R.id.txt_userProfession)
    AnyTextView mTxtUserProfession;
    @BindView(R.id.header)
    LinearLayout mHeader;
    @BindView(R.id.txt_Completedjob)
    AnyTextView mTxtCompletedjob;
    @BindView(R.id.txt_InProgress)
    AnyTextView mTxtInProgress;
    @BindView(R.id.ll_buttons)
    LinearLayout mLlButtons;
    ImageLoader imageloader;
    private boolean canShowCompleteJobs = false;

    public static OrderHistoryFragment newInstance(boolean canShowCompleteJobs) {
        OrderHistoryFragment fragment = new OrderHistoryFragment();
        fragment.setCanShowCompleteJobs(canShowCompleteJobs);
        return fragment;
    }

    public static OrderHistoryFragment newInstance() {
        return new OrderHistoryFragment();
    }

    public void setCanShowCompleteJobs(boolean canShowCompleteJobs) {
        this.canShowCompleteJobs = canShowCompleteJobs;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orderhistory, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_orderhistory;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageloader = ImageLoader.getInstance();
        setListners();
        ReplaceListView2Fragment(CompletedJobsFragment.newInstance());
        ReplaceListViewFragment(InProgressExpendFragment.newInstance());

        if (canShowCompleteJobs) {
            showCompleteJobsFragment();
        } else {
            showInprogressJobsFragment();
        }
        getTechData();


    }

    private void getTechData() {
        imageloader.displayImage(prefHelper.getRegistrationResult().getProfileImage(), mCircularImageSharePop);
        mTxtUserName.setText(prefHelper.getRegistrationResult().getFullName());
        mTxtUserProfession.setText(prefHelper.getRegistrationResult().getRegistrationType());


    }

    private void setListners() {
        llInProgess.setOnClickListener(this);
        llCompletedJobs.setOnClickListener(this);
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        getDockActivity().lockDrawer();
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.jobs));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_CompletedJobs:
                showCompleteJobsFragment();
                break;

            case R.id.ll_InProgess:
                showInprogressJobsFragment();
                break;
        }

    }

    private void showInprogressJobsFragment() {
        selectedArrowCompletedJobs.setVisibility(View.VISIBLE);
        selectedArrowInProgress.setVisibility(View.GONE);
        llCompletedJobs.setBackgroundColor(getResources().getColor(R.color.white));
        txtJobCount.setTextColor(ContextCompat.getColor(getDockActivity(), R.color.app_blue));
        mTxtCompletedjob.setTextColor(ContextCompat.getColor(getDockActivity(), R.color.app_blue));
        llInProgess.setBackgroundColor(getResources().getColor(R.color.app_blue));
        txtInProgressCount.setTextColor(ContextCompat.getColor(getDockActivity(), R.color.white));
        mTxtInProgress.setTextColor(ContextCompat.getColor(getDockActivity(), R.color.white));
        ReplaceListViewFragment(InProgressExpendFragment.newInstance());
    }

    private void showCompleteJobsFragment() {
        selectedArrowCompletedJobs.setVisibility(View.GONE);
        selectedArrowInProgress.setVisibility(View.VISIBLE);
        llCompletedJobs.setBackgroundColor(getResources().getColor(R.color.app_blue));
        txtJobCount.setTextColor(ContextCompat.getColor(getDockActivity(), R.color.white));
        mTxtCompletedjob.setTextColor(ContextCompat.getColor(getDockActivity(), R.color.white));
        llInProgess.setBackgroundColor(getResources().getColor(R.color.white));
        txtInProgressCount.setTextColor(ContextCompat.getColor(getDockActivity(), R.color.app_blue));
        mTxtInProgress.setTextColor(ContextCompat.getColor(getDockActivity(), R.color.app_blue));
        ReplaceListViewFragment(CompletedJobsFragment.newInstance());
    }

    private void ReplaceListViewFragment(CompletedJobsFragment frag) {

        frag.setOrderCounts(this);
        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.ll_listView, frag);
        transaction
                .addToBackStack(
                        getChildFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();

    }

    private void ReplaceListView2Fragment(CompletedJobsFragment frag) {

        frag.setOrderCounts(this);
        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.ll_listView2, frag);
        transaction
                .addToBackStack(
                        getChildFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();

    }


    private void ReplaceListViewFragment(InProgressExpendFragment frag) {

        frag.setOrderCounts(this);
        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.ll_listView, frag);
        transaction
                .addToBackStack(
                        getChildFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();

    }


    @Override
    public void setcompleteCount(int count) {
        txtJobCount.setText(String.valueOf(count));

    }

    @Override
    public void setInprogressCount(int count) {

        txtInProgressCount.setText(String.valueOf(count));

    }
}
