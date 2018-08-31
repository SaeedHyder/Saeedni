package com.ingic.saeedni.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.RegistrationResultEnt;
import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.entities.TechProfileEnt;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.AppConstants;
import com.ingic.saeedni.helpers.DateHelper;
import com.ingic.saeedni.helpers.InternetHelper;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.ingic.saeedni.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends BaseFragment {

    @BindView(R.id.CircularImageSharePop)
    CircleImageView CircularImageSharePop;
    @BindView(R.id.edtFirstName)
    AnyTextView edtFirstName;
    @BindView(R.id.edtLastName)
    AnyTextView edtLastName;
    @BindView(R.id.edtEmailId)
    AnyTextView edtEmailId;
    @BindView(R.id.edtPhoneNo)
    AnyTextView edtPhoneNo;
    @BindView(R.id.edtRegistrationType)
    AnyTextView edtRegistrationType;
    @BindView(R.id.edtRegDate)
    AnyTextView edtRegDate;
    @BindView(R.id.ll_profileItems)
    LinearLayout llProfileItems;
    @BindView(R.id.ll_profileDetail)
    LinearLayout llProfileDetail;

    @BindView(R.id.mainFrame)
    LinearLayout mainFrame;

    ImageLoader imageLoader;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_technicianprofile, container, false);
      ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageLoader=ImageLoader.getInstance();
        mainFrame.setVisibility(View.GONE);
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            getTechProfile();
        }
    }

    private void getTechProfile() {
        getDockActivity().onLoadingStarted();
        Call<ResponseWrapper<RegistrationResultEnt>> call = webService.techProfile(Integer.parseInt(prefHelper.getUserId()));

        call.enqueue(new Callback<ResponseWrapper<RegistrationResultEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<RegistrationResultEnt>> call, Response<ResponseWrapper<RegistrationResultEnt>> response) {
                getDockActivity().onLoadingFinished();
                mainFrame.setVisibility(View.VISIBLE);
                if (response.body().getResponse().equals("2000")) {
                    setProfileData(response.body().getResult());
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<RegistrationResultEnt>> call, Throwable t) {
                Log.e("EntryCodeFragment", t.toString());
               // UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
            }
        });

    }

    private void setProfileData(RegistrationResultEnt result) {
        prefHelper.putRegistrationResult(result);
        imageLoader.displayImage(prefHelper.getRegistrationResult().getProfileImage(),CircularImageSharePop);
        edtFirstName.setText(result.getFirstName());
        edtLastName.setText(result.getLastName());
        edtEmailId.setText(result.getEmail());
        edtPhoneNo.setText(result.getPhoneNo());
        edtRegistrationType.setText(result.getRegistrationType());
        if (!prefHelper.isLanguageArabic()) {
            edtRegDate.setText(DateHelper.dateFormat(result.getRegistrationDate(), AppConstants.DateFormat_DMY, AppConstants.DateFormat_YMD) + "");
        } else {
            edtRegDate.setText(result.getRegistrationDate() + "");
        }

        if (getMainActivity()!=null) {
            getMainActivity().refreshSideMenuWithnewFragment();
            getMainActivity().refreshSideMenu();
        }
       // edtRegDate.setText(result.getRegistration_date());
       /* imageLoader.displayImage(prefHelper.getRegistrationResult().getProfileImage(),CircularImageSharePop);
        edtFirstName.setText(prefHelper.getRegistrationResult().getFirstName());
        edtLastName.setText(prefHelper.getRegistrationResult().getLastName());
        edtEmailId.setText(prefHelper.getRegistrationResult().getEmail());
        edtPhoneNo.setText(prefHelper.getRegistrationResult().getPhoneNo());
        edtRegistrationType.setText(prefHelper.getRegistrationResult().getRegistrationType());
        edtRegDate.setText(prefHelper.getRegistrationResult().getRegistrationDate());*/

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_technicianprofile;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        getDockActivity().lockDrawer();
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.my_profile));

    }


}
