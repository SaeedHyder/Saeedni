package com.ingic.saeedni.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.RegistrationResultEnt;
import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.AppConstants;
import com.ingic.saeedni.helpers.DialogHelper;
import com.ingic.saeedni.helpers.InternetHelper;
import com.ingic.saeedni.helpers.TokenUpdater;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.ui.views.AnyEditTextView;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.ingic.saeedni.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends BaseFragment implements OnClickListener {


    Unbinder unbinder;
    @BindView(R.id.btn_login)
    Button loginButton;
    @BindView(R.id.btn_signup)
    Button signupButton;
    @BindView(R.id.btnBack)
    ImageView btnBack;

    @BindView(R.id.txt_reset)
    AnyTextView txtForgotPass;
    @BindView(R.id.edtEmail)
    AnyEditTextView edtEmail;
    @BindView(R.id.edtPassword)
    AnyEditTextView edtPassword;
    @BindView(R.id.scrollview_bigdaddy)
    LinearLayout scrollviewBigdaddy;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.txt_login)
    AnyTextView txtLogin;
    @BindView(R.id.btn_login1)
    RelativeLayout btnLogin1;
    @BindView(R.id.btn_singup)
    RelativeLayout btnSingup;
    @BindView(R.id.iv_emailIcon)
    ImageView ivEmailIcon;
    @BindView(R.id.ll_email)
    LinearLayout llEmail;
    @BindView(R.id.iv_passwordIcon)
    ImageView ivPasswordIcon;
    @BindView(R.id.ll_password)
    LinearLayout llPassword;
    @BindView(R.id.txtForgotPass)
    AnyTextView txtForgotPass1;

    private boolean isUserSelection = false;
    private static String USERSELECTIONKEY = "USERSELECTIONKEY";


    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public static LoginFragment newInstance(boolean userSelection) {
        Bundle args = new Bundle();
        args.putBoolean(USERSELECTIONKEY, userSelection);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isUserSelection = getArguments().getBoolean(USERSELECTIONKEY);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (prefHelper.isLanguageArabic()) {
            scrollviewBigdaddy.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            // edtPassword.setGravity(Gravity.END);
        } else {
            scrollviewBigdaddy.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            //  edtPassword.setGravity(Gravity.START);
        }
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*if (!edtPassword.getText().toString().isEmpty()) {
                    edtPassword.setGravity(Gravity.START);
                } else {
                    edtPassword.setGravity(Gravity.END);
                }*/
               /* if(prefHelper.isLanguageArabic()){
                    if (!edtPassword.getText().toString().isEmpty()) {
                        edtPassword.setGravity(Gravity.START);
                    } else {
                        edtPassword.setGravity(Gravity.END);
                    }}*/
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        setListeners();
       /* if(isUserSelection){
            btnBack.setVisibility(View.VISIBLE);
        }else{
            btnBack.setVisibility(View.GONE);
        }*/
        btnBack.setVisibility(View.VISIBLE);

    }

    private void setListeners() {
        loginButton.setOnClickListener(this);
        txtForgotPass.setOnClickListener(this);
        signupButton.setOnClickListener(this);
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().popFragment();
            }
        });
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();

    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_login;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_login:
                if (isvalidate()) {
                    if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                        loadingStarted();
                        loginTechnician();
                    }
                }
                break;

            case R.id.btn_signup:
          //      getDockActivity().replaceDockableFragment(TechSignupFragment.newInstance(), "TechSignupFragment");
                break;

            case R.id.txt_reset:
                final DialogHelper forgetDialog = new DialogHelper(getDockActivity());
                forgetDialog.initForgotPasswordDialog(R.layout.forgot_password_dialog, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        forgetDialog.hideDialog();
                    }
                });
                forgetDialog.setCancelable(false);
                forgetDialog.showDialog();
                break;

        }
    }

    private void loginTechnician() {
        Call<ResponseWrapper<RegistrationResultEnt>> call = webService.loginTechnician(edtEmail.getText().toString(),
                edtPassword.getText().toString());
        call.enqueue(new Callback<ResponseWrapper<RegistrationResultEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<RegistrationResultEnt>> call, Response<ResponseWrapper<RegistrationResultEnt>> response) {
                if (response.body().getResponse().equals("2000")) {
                    loadingFinished();
                    if (response.body().getResult().getId() != null) {
                        prefHelper.setUserType("technician");
                        prefHelper.setUsrId(String.valueOf(response.body().getResult().getId()));
                        prefHelper.setUsrName(response.body().getResult().getFullName());
                        prefHelper.setPhonenumber(response.body().getResult().getPhoneNo());
                        prefHelper.putRegistrationResult(response.body().getResult());
                        TokenUpdater.getInstance().UpdateToken(getDockActivity(),
                                prefHelper.getUserId(),
                                AppConstants.Device_Type,
                                prefHelper.getFirebase_TOKEN());
                        prefHelper.setLoginStatus(true);
                        getDockActivity().popBackStackTillEntry(0);
                        getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragmnet");
                    } else {
                        loadingFinished();
                        UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                    }
                } else {
                    loadingFinished();
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<RegistrationResultEnt>> call, Throwable t) {
                loadingFinished();
                Log.e("UserSignupFragment", t.toString());
                //  UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }




    @OnClick({R.id.btn_login1, R.id.btn_singup})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn_singup:
                getDockActivity().replaceDockableFragment(TechSignupFragment.newInstance(), "TechSignupFragment");
                break;
        }
    }
}
