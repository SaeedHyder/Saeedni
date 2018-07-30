package com.ingic.saeedni.fragments;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.RegistrationResultEnt;
import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.AppConstants;
import com.ingic.saeedni.helpers.InternetHelper;
import com.ingic.saeedni.helpers.TokenUpdater;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.ui.views.AnyEditTextView;
import com.ingic.saeedni.ui.views.TitleBar;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 4/26/18.
 */
public class ForgotPasswordFragment extends BaseFragment {
    public static final String TAG = "ForgotPasswordFragment";
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.iv_emailIcon)
    ImageView ivEmailIcon;
    @BindView(R.id.edtEmail)
    AnyEditTextView edtEmail;
    @BindView(R.id.ll_email)
    LinearLayout llEmail;
    @BindView(R.id.btn_login)
    Button btnLogin;

    public static ForgotPasswordFragment newInstance() {
        Bundle args = new Bundle();

        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
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
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, Objects.requireNonNull(rootView));
        return rootView;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_user_forgotpassword;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.forgot_password_));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private boolean isvalidate() {

        if (edtEmail.getText() == null || (edtEmail.getText().toString().isEmpty()) ||
                (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches())) {
            edtEmail.setError(getString(R.string.valid_email));
            return false;
        } else
            return true;

    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        if (isvalidate()) {
            if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                loadingStarted();
                CallforgotPassword();
            }

        }
    }

    private void CallforgotPassword() {
        Call<ResponseWrapper> call = webService.forgotPassword(edtEmail.getText().toString());
        call.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                if (response.body().getResponse().equals("2000")) {
                    loadingFinished();
                    UIHelper.showShortToastInCenter(getDockActivity(),getDockActivity().getResources().getString(R.string.forgor_password_message));
                    getDockActivity().popBackStackTillEntry(0);
                    getDockActivity().replaceDockableFragment(UserloginFragment.newInstance(), "UserloginFragment");
                } else {
                    loadingFinished();
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                loadingFinished();
                Log.e("UserSignupFragment", t.toString());
                //  UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
            }
        });
    }
}