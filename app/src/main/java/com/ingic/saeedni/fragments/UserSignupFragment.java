package com.ingic.saeedni.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.ingic.saeedni.R;
import com.ingic.saeedni.activities.MainActivity;
import com.ingic.saeedni.entities.CitiesEnt;
import com.ingic.saeedni.entities.RegistrationResultEnt;
import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.AppConstants;
import com.ingic.saeedni.global.WebServiceConstants;
import com.ingic.saeedni.helpers.CameraHelper;
import com.ingic.saeedni.helpers.DialogHelper;
import com.ingic.saeedni.helpers.InternetHelper;
import com.ingic.saeedni.helpers.TokenUpdater;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.ui.views.AnyEditTextView;
import com.ingic.saeedni.ui.views.AnySpinner;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.ingic.saeedni.ui.views.TitleBar;
import com.jota.autocompletelocation.AutoCompleteLocation;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 5/23/2017.
 */

public class UserSignupFragment extends BaseFragment implements View.OnClickListener {
    public File profilePic;
    public String UserImagePath;
    @BindView(R.id.edtname)
    AnyEditTextView edtname;
    @BindView(R.id.edtnumber)
    AnyEditTextView edtnumber;
    @BindView(R.id.edtEmail)
    AnyEditTextView edtEmail;
    @BindView(R.id.btn_signup)
    Button btnsignup;
    PhoneNumberUtil phoneUtil;
    @BindView(R.id.btn_image)
    CircleImageView btnImage;
    @BindView(R.id.edtpassword)
    AnyEditTextView edtpassword;

    @BindView(R.id.btn_tnc)
    AnyTextView btnTnc;
    @BindView(R.id.img_profile)
    ImageView imgProfile;
    @BindView(R.id.img_email)
    ImageView imgEmail;
    @BindView(R.id.img_password)
    ImageView imgPassword;
    @BindView(R.id.img_phone)
    ImageView imgPhone;
    @BindView(R.id.img_address)
    ImageView imgAddress;
    @BindView(R.id.spnCity)
    AnySpinner spnCity;
    @BindView(R.id.ll_textFields)
    LinearLayout llTextFields;
    ArrayList<CitiesEnt> allCities = new ArrayList<>();
    @BindView(R.id.edtadress)
    AutoCompleteLocation edtadress;
    @BindView(R.id.Countrypicker)
    CountryCodePicker Countrypicker;
    private MainActivity.ImageSetter imageSetter = new MainActivity.ImageSetter() {

        @Override
        public void setImage(String imagePath) {
            if (imagePath != null) {
                profilePic = new File(imagePath);
                ImageLoader.getInstance().displayImage(
                        "file:///" + imagePath, btnImage);
                UserImagePath = imagePath;
            }
        }

        @Override
        public void setFilePath(String filePath) {

        }

        @Override
        public void setVideo(String videoPath) {

        }
    };

    public static UserSignupFragment newInstance() {
        return new UserSignupFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (prefHelper.isLanguageArabic()) {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            // edtnumber.setGravity(Gravity.END);
        } else {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            //   edtnumber.setGravity(Gravity.START);
        }
        getMainActivity().setImageSetter(imageSetter);
        setlistener();
        phoneUtil = PhoneNumberUtil.getInstance();
        getAllCities();
        Countrypicker.registerCarrierNumberEditText(edtnumber);
    }

    private void getAllCities() {
        if (allCities.isEmpty())
            serviceHelper.enqueueCall(webService.getAllCities(), WebServiceConstants.GET_ALL_CITIES);
        else setCitiesSpinner(allCities);
    }

    private void setlistener() {
        btnsignup.setOnClickListener(this);
        btnImage.setOnClickListener(this);
        btnTnc.setOnClickListener(this);
        edtnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
              /*  if(prefHelper.isLanguageArabic()){
                if (!edtnumber.getText().toString().isEmpty()) {
                    edtnumber.setGravity(Gravity.END);
                } else {
                    edtnumber.setGravity(Gravity.START);
                }}*/
               /* if (!edtnumber.getText().toString().isEmpty()) {
                    edtnumber.setGravity(Gravity.START);
                } else {
                    edtnumber.setGravity(Gravity.END);
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signup:
                if (isvalidated()) {
                    if (isPhoneNumberValid())
                        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                            try {
                                loadingStarted();
                                registerUser(phoneUtil.format(phoneUtil.parse(edtnumber.getText().toString(), getDockActivity().getResources().getString(R.string.uae_country_code)),
                                        PhoneNumberUtil.PhoneNumberFormat.E164));

                            } catch (Exception e) {

                            }


                        }

                }
                break;
            case R.id.btn_image:

                AndPermission.with(this)
                        .runtime()
                        .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                CameraHelper.uploadMedia(getMainActivity());
                            }
                        })
                        .onDenied(permissions -> {
                            UIHelper.showShortToastInCenter(getMainActivity(), getString(R.string.storage_permission));
                        })
                        .start();
                break;
            case R.id.btn_tnc:
                getDockActivity().replaceDockableFragment(TermAndConditionFragment.newInstance(), "Terms And conditon Fragment");
                break;
        }
    }

    private boolean isPhoneNumberValid() {


        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(edtnumber.getText().toString(),Countrypicker.getSelectedCountryNameCode());
            /*if (!Countrypicker.isValidFullNumber()) {
                return true; getDockActivity().getResources().getString(R.string.uae_country_code
            } else {
                edtnumber.setError(getDockActivity().getResources().getString(R.string.enter_valid_number_error));
                return false;
            } */if (phoneUtil.isValidNumber(number)) {
                return true;
            } else {
                edtnumber.setError(getDockActivity().getResources().getString(R.string.enter_valid_number_error));
                return false;
            }
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
            edtnumber.setError(getDockActivity().getResources().getString(R.string.enter_valid_number_error));
            return false;

        }

    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_ALL_CITIES:
                CitiesEnt citiesEnt = new CitiesEnt();
                citiesEnt.setLocation(getDockActivity().getResources().getString(R.string.select_city));
                allCities.add(citiesEnt);
                allCities.addAll((ArrayList<CitiesEnt>) result);
                setCitiesSpinner(allCities);
                break;
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_user_signup;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserImagePath != null) {
            if (!UserImagePath.isEmpty()) {
                if (profilePic == null)
                    profilePic = new File(UserImagePath);
                ImageLoader.getInstance().displayImage(
                        "file:///" + UserImagePath, btnImage);
            }
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.signup));
    }

    private void registerUser(String number) {
        MultipartBody.Part filePart;
        if (profilePic != null) {
            filePart = MultipartBody.Part.createFormData("profile_picture",
                    profilePic.getName(), RequestBody.create(MediaType.parse("image/*"), profilePic));
        } else {
            filePart = MultipartBody.Part.createFormData("profile_picture", "",
                    RequestBody.create(MediaType.parse("*/*"), ""));
        }
        Call<ResponseWrapper<RegistrationResultEnt>> call = webService.registerUser(
                RequestBody.create(MediaType.parse("text/plain"), edtname.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtEmail.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), allCities.get(spnCity.getSelectedItemPosition()).getId() + ""),
                RequestBody.create(MediaType.parse("text/plain"), number),
                RequestBody.create(MediaType.parse("text/plain"), edtadress.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtpassword.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtpassword.getText().toString()),
                filePart);
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
                    prefHelper.setLoginStatus(false);
                    getDockActivity().replaceDockableFragment(EntryCodeFragment.newInstance(), "EntryCodeFragment");
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

    private void showSignupDialog() {
        final DialogHelper signupDialog = new DialogHelper(getDockActivity());
        signupDialog.initSignUpDialog(R.layout.dialog_signup_alert, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupDialog.hideDialog();
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(UserHomeFragment.newInstance(), "UserHomeFragment");
            }
        });
        signupDialog.setCancelable(false);
        signupDialog.showDialog();
    }

    private void setCitiesSpinner(ArrayList<CitiesEnt> citiesEnts) {

        final ArrayList<String> citiesCollection = new ArrayList<String>();


        for (CitiesEnt item : citiesEnts
                ) {
            citiesCollection.add(item.getLocation());
        }
        //  ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getDockActivity(), android.R.layout.simple_spinner_item, citiesCollection);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getDockActivity()
                , android.R.layout.simple_spinner_item, citiesCollection) {
            @Override
            public boolean isEnabled(int position) {
                return !(position == 0);
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
                return view;
            }

        };

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCity.setAdapter(categoryAdapter);
    }

    private boolean isvalidated() {

        if (edtname.getText().toString().isEmpty()) {
            edtname.setError(getDockActivity().getResources().getString(R.string.enter_name));
            return false;
        } else if (edtnumber.getText().toString().equals("") && edtnumber.getText().toString().isEmpty()) {
            edtnumber.setError(getDockActivity().getResources().getString(R.string.enter_number));
            return false;
        } /*else if (edtnumber.getText().toString().length() < 9 || edtnumber.getText().toString().length() > 10) {
            edtnumber.setError(getDockActivity().getResources().getString(R.string.enter_valid_number_error));
            return false;
        }*/ else if (edtEmail.getText() == null || (edtEmail.getText().toString().isEmpty()) ||
                (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches())) {
            edtEmail.setError(getDockActivity().getResources().getString(R.string.enter_email));
            return false;
        } else if (edtpassword.getText() == null || (edtpassword.getText().toString().isEmpty()) || edtpassword.getText().toString().length() < 6) {
            edtpassword.setError(getDockActivity().getResources().getString(R.string.valid_password));
            return false;
        } else if (edtadress.getText().toString().isEmpty()) {
            edtadress.setError(getDockActivity().getResources().getString(R.string.address_empty_error));
            return false;
        } else if (spnCity.getSelectedItemPosition() == 0) {
            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.select_city));
            return false;
        } else if (!prefHelper.istermsCheck()) {
            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.terms_error_message));
            return false;
        } else {
            return true;
        }

    }


}
