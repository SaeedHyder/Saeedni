package com.ingic.saeedni.fragments;

import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.ingic.saeedni.activities.DockActivity.KEY_FRAG_FIRST;

/**
 * Created on 5/24/2017.
 */

public class UserJobsFragment extends BaseFragment implements View.OnClickListener, SetOrderCounts {
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

    @BindView(R.id.CircularImageSharePop)
    CircleImageView CircularImageSharePop;
    @BindView(R.id.txt_userName)
    AnyTextView txtUserName;
    @BindView(R.id.mainFrame)
    LinearLayout mainFrame;
    @BindView(R.id.txt_userProfession)
    AnyTextView txtUserProfession;
    @BindView(R.id.header)
    LinearLayout header;
    @BindView(R.id.txt_InProgress)
    AnyTextView mTxtInProgress;
    @BindView(R.id.txt_Completedjob)
    AnyTextView mTxtCompletedjob;
    @BindView(R.id.ll_buttons)
    LinearLayout llButtons;
    @BindView(R.id.ll_listView2)
    LinearLayout llListView2;

    public static UserJobsFragment newInstance() {
        return new UserJobsFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_user_jobs;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListners();

        mainFrame.setVisibility(View.GONE);
        ReplaceListView2Fragment(UserCompleteJobsFragment.newInstance());
        ReplaceListViewFragment(UserInProgressFragment.newInstance());
        setProfileData();
        setInprogressCount(0);
        setcompleteCount(0);
    }

    private void setProfileData() {
        Picasso.with(getDockActivity()).load(prefHelper.getRegistrationResult().getProfileImage()).into(CircularImageSharePop);
        txtUserName.setText(prefHelper.getRegistrationResult().getFullName());
    }


    private void setListners() {
        llInProgess.setOnClickListener(this);
        llCompletedJobs.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    private void ReplaceListViewFragment(UserCompleteJobsFragment frag) {
        frag.setOrderCounts(this);
        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.ll_listView, frag);
        transaction
                .addToBackStack(
                        getChildFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();

    }

    private void ReplaceListView2Fragment(UserCompleteJobsFragment frag) {
        frag.setOrderCounts(this);
        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.ll_listView2, frag);
        transaction
                .addToBackStack(
                        getChildFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();

    }

    private void ReplaceListViewFragment(UserInProgressFragment frag) {
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
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        getDockActivity().lockDrawer();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.jobs));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_CompletedJobs:
                selectedArrowCompletedJobs.setVisibility(View.GONE);
                selectedArrowInProgress.setVisibility(View.VISIBLE);
                llCompletedJobs.setBackgroundColor(getResources().getColor(R.color.app_blue));
                txtJobCount.setTextColor(ContextCompat.getColor(getDockActivity(), R.color.white));
                mTxtCompletedjob.setTextColor(ContextCompat.getColor(getDockActivity(), R.color.white));
                llInProgess.setBackgroundColor(getResources().getColor(R.color.white));
                txtInProgressCount.setTextColor(ContextCompat.getColor(getDockActivity(), R.color.app_blue));
                mTxtInProgress.setTextColor(ContextCompat.getColor(getDockActivity(), R.color.app_blue));
                ReplaceListViewFragment(UserCompleteJobsFragment.newInstance());
                break;

            case R.id.ll_InProgess:


                selectedArrowCompletedJobs.setVisibility(View.VISIBLE);
                selectedArrowInProgress.setVisibility(View.GONE);
                llCompletedJobs.setBackgroundColor(getResources().getColor(R.color.white));
                txtJobCount.setTextColor(ContextCompat.getColor(getDockActivity(), R.color.app_blue));
                mTxtCompletedjob.setTextColor(ContextCompat.getColor(getDockActivity(), R.color.app_blue));
                llInProgess.setBackgroundColor(getResources().getColor(R.color.app_blue));
                txtInProgressCount.setTextColor(ContextCompat.getColor(getDockActivity(), R.color.white));
                mTxtInProgress.setTextColor(ContextCompat.getColor(getDockActivity(), R.color.white));
                ReplaceListViewFragment(UserInProgressFragment.newInstance());
                break;
        }
    }

    @Override
    public void setcompleteCount(int count) {
        mainFrame.setVisibility(View.VISIBLE);
        txtJobCount.setText(String.valueOf(count));
    }

    @Override
    public void setInprogressCount(int count) {
        txtInProgressCount.setText(String.valueOf(count));
    }


}
