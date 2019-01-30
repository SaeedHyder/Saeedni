package com.ingic.saeedni.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.RegistrationResultEnt;
import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.AppConstants;
import com.ingic.saeedni.global.WebServiceConstants;
import com.ingic.saeedni.helpers.FacebookLoginHelper;
import com.ingic.saeedni.helpers.GoogleHelper;
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
public class UserloginFragment extends BaseFragment implements FacebookLoginHelper.FacebookLoginListener, GoogleHelper.GoogleHelperInterfce {
    public static final String TAG = "UserloginFragment";
    private static final int RC_SIGN_IN = 7;
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
    @BindView(R.id.parent)
    ScrollView parent;
    private CallbackManager callbackManager;
    private FacebookLoginHelper facebookLoginHelper;
    private GoogleHelper googleHelper;
    private LoginManager loginManager;
    private FacebookLoginHelper.FacebookLoginEnt facebookLoginEnt;
    private GoogleHelper.GoogleLoginEnt googleLoginEnt;

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
        FacebookSdk.sdkInitialize(getMainActivity().getApplicationContext());
        ButterKnife.bind(this, Objects.requireNonNull(rootView));
        loginManager = LoginManager.getInstance();
        return rootView;
    }

    private void setupGoogleSignup() {
        googleHelper = GoogleHelper.getInstance();
        googleHelper.setGoogleHelperInterface(this);
        googleHelper.configGoogleApiClient(this);
    }

    private void setupFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();

        // btnfbLogin.setFragment(this);
        facebookLoginHelper = new FacebookLoginHelper(getDockActivity(), this, this);
        loginManager.registerCallback(callbackManager, facebookLoginHelper);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            googleHelper.handleGoogleResult(requestCode, resultCode, data);
        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parent.setLayoutDirection(prefHelper.isLanguageArabic() ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().popFragment();
            }
        });
        setupGoogleSignup();
        setupFacebookLogin();

    }

    @Override
    public void onStart() {
        super.onStart();
        googleHelper.ConnectGoogleAPi();
        // googleHelper.checkGoogleSeesion();
    }

    @Override
    public void onStop() {
        super.onStop();
        googleHelper.DisconnectGoogleApi();
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

    @OnClick({R.id.txt_reset, R.id.btn_login, R.id.btn_singup, R.id.btnGoogle, R.id.btnFacebook})
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
            case R.id.btnFacebook:
                if (AccessToken.getCurrentAccessToken() != null) {
                    loginManager.logOut();
                }
                loginManager.logInWithReadPermissions(getMainActivity(), facebookLoginHelper.getPermissionNeeds());
                break;
            case R.id.btnGoogle:
                if (googleHelper != null && googleHelper.isGoogleSign()) {
                    googleHelper.googleRevokeAccess();
                    googleHelper.googleSignOut();
                }
                assert googleHelper != null;
                googleHelper.intentGoogleSign();
                break;
        }
    }

    private void loginUser() {

        Call<ResponseWrapper<RegistrationResultEnt>> call = webService.loginUser(edtEmail.getText().toString(), edtPassword.getText().toString(), prefHelper.getLang(), FirebaseInstanceId.getInstance().getToken()
                , AppConstants.Device_Type);
        call.enqueue(new Callback<ResponseWrapper<RegistrationResultEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<RegistrationResultEnt>> call, Response<ResponseWrapper<RegistrationResultEnt>> response) {
                loadingFinished();
                if (response.body().getResponse().equals("2000")) {
                    proceedLogin(response.body().getResult());
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

    private void proceedLogin(RegistrationResultEnt response) {
        prefHelper.putRegistrationResult(response);
        prefHelper.setUserType("user");
        prefHelper.setUsrId(String.valueOf(response.getId()));
        prefHelper.setUsrName(response.getFullName());
        prefHelper.setPhonenumber(response.getPhoneNo());
        TokenUpdater.getInstance().UpdateToken(getDockActivity(),
                prefHelper.getUserId(),
                AppConstants.Device_Type,
                prefHelper.getFirebase_TOKEN());
        if (response.getCode_verify().equalsIgnoreCase("0")) {
            getDockActivity().replaceDockableFragment(EntryCodeFragment.newInstance(), "EntryCodeFragment");

        } else {
            prefHelper.setLoginStatus(true);
//                    getDockActivity().popBackStackTillEntry(0);
            getMainActivity().refreshSideMenuWithnewFragment();
            getDockActivity().popBackStackTillEntry(0);
            getDockActivity().replaceDockableFragment(UserHomeFragment.newInstance(), "User Home Fragment");
        }
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.LOGIN_SOCIAL:
                RegistrationResultEnt registrationResultEnt = (RegistrationResultEnt) result;
                if (registrationResultEnt.getId() == null) {
                    if (facebookLoginEnt == null) {
                        getDockActivity().replaceDockableFragment(UserSignupFragment.newInstance(googleLoginEnt), "UserSignupFragment");
                    } else {
                        getDockActivity().replaceDockableFragment(UserSignupFragment.newInstance(facebookLoginEnt), "UserSignupFragment");
                    }
                } else {
                    proceedLogin(registrationResultEnt);
                }
                break;


        }
    }

    @Override
    public void onSuccessfulFacebookLogin(FacebookLoginHelper.FacebookLoginEnt LoginEnt) {
        facebookLoginEnt = LoginEnt;
        googleLoginEnt = null;

        serviceHelper.enqueueCall(webService.userSocialLogin(facebookLoginEnt.getFacebookUID(),facebookLoginEnt.getFacebookEmail(),
                AppConstants.SOCIAL_MEDIA_TYPE_FACEBOOK, prefHelper.getLang()), WebServiceConstants.LOGIN_SOCIAL);
    }

    @Override
    public void onGoogleSignInResult(GoogleHelper.GoogleLoginEnt result) {
        if (googleHelper != null && googleHelper.isGoogleSign()) {
            googleHelper.googleRevokeAccess();
            googleHelper.googleSignOut();
        }
        googleLoginEnt = result;
        facebookLoginEnt = null;

        serviceHelper.enqueueCall(webService.userSocialLogin(googleLoginEnt.getGoogleUID(),result.getGoogleEmail(),
                AppConstants.SOCIAL_MEDIA_TYPE_GOOGLE, prefHelper.getLang()), WebServiceConstants.LOGIN_SOCIAL);
    }
}