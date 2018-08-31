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

import com.google.firebase.iid.FirebaseInstanceId;
import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.RegistrationResultEnt;
import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.AppConstants;
import com.ingic.saeedni.helpers.InternetHelper;
import com.ingic.saeedni.helpers.TokenUpdater;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.ui.views.AnyEditTextView;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.ingic.saeedni.ui.views.TitleBar;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 4/25/18.
 */
public class UserloginFragment extends BaseFragment {
    public static final String TAG = "UserloginFragment";
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.txt_login)
    AnyTextView txtLogin;
    @BindView(R.id.iv_emailIcon)
    ImageView ivEmailIcon;
    @BindView(R.id.edtEmail)
    AnyEditTextView edtEmail;
    @BindView(R.id.ll_email)
    LinearLayout llEmail;
    @BindView(R.id.iv_passwordIcon)
    ImageView ivPasswordIcon;
    @BindView(R.id.edtPassword)
    AnyEditTextView edtPassword;
    @BindView(R.id.ll_password)
    LinearLayout llPassword;
    @BindView(R.id.txtForgotPass)
    AnyTextView txtForgotPass;
    @BindView(R.id.txt_reset)
    AnyTextView txtReset;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btnBack)
    ImageView btnBack;

    public static UserloginFragment newInstance() {
        Bundle args = new Bundle();

        UserloginFragment fragment = new UserloginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_user_login;
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
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().popFragment();
            }
        });

    }

    private boolean isvalidate() {

        if (edtEmail.getText() == null || (edtEmail.getText().toString().isEmpty()) ||
                (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches())) {
            edtEmail.setError(getDockActivity().getResources().getString(R.string.valid_email));
            return false;
        } else if (edtPassword.getText() == null || (edtPassword.getText().toString().isEmpty()) || edtPassword.getText().toString().length() < 6) {
            edtPassword.setError(getDockActivity().getResources().getString(R.string.valid_password));
            return false;
        } else
            return true;

    }

    @OnClick({R.id.txt_reset, R.id.btn_login, R.id.btn_singup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_reset:
                getDockActivity().replaceDockableFragment(ForgotPasswordFragment.newInstance(), ForgotPasswordFragment.TAG);
                break;
            case R.id.btn_login:
                if (isvalidate()) {
                    if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                        loadingStarted();
                        loginUser();
                    }
                }
                break;
            case R.id.btn_singup:
                getDockActivity().replaceDockableFragment(UserSignupFragment.newInstance(), "UserSignupFragment");
                break;
        }
    }

    private void loginUser() {

        Call<ResponseWrapper<RegistrationResultEnt>> call = webService.loginUser(edtEmail.getText().toString(), edtPassword.getText().toString(), FirebaseInstanceId.getInstance().getToken()
                , AppConstants.Device_Type);
        call.enqueue(new Callback<ResponseWrapper<RegistrationResultEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<RegistrationResultEnt>> call, Response<ResponseWrapper<RegistrationResultEnt>> response) {
                loadingFinished();
                if (response.body().getResponse().equals("2000")) {
                    prefHelper.putRegistrationResult(response.body().getResult());
                    prefHelper.setUserType("user");
                    prefHelper.setUsrId(String.valueOf(response.body().getResult().getId()));
                    prefHelper.setUsrName(response.body().getResult().getFullName());
                    prefHelper.setPhonenumber(response.body().getResult().getPhoneNo());
                    TokenUpdater.getInstance().UpdateToken(getDockActivity(),
                            prefHelper.getUserId(),
                            AppConstants.Device_Type,
                            prefHelper.getFirebase_TOKEN());
                    if (response.body().getResult().getCode_verify().equalsIgnoreCase("0")) {
                        getDockActivity().replaceDockableFragment(EntryCodeFragment.newInstance(), "EntryCodeFragment");

                    } else {
                        prefHelper.setLoginStatus(true);
//                    getDockActivity().popBackStackTillEntry(0);
                        getMainActivity().refreshSideMenuWithnewFragment();
                        getDockActivity().popBackStackTillEntry(0);
                        getDockActivity().replaceDockableFragment(UserHomeFragment.newInstance(), "User Home Fragment");
                    }
                    //showSignupDialog();

                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<RegistrationResultEnt>> call, Throwable t) {
                loadingFinished();
                Log.e("UserSignupFragment", t.toString());

            }
        });
    }
}