package com.ingic.saeedni.fragments;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
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
import com.ingic.saeedni.helpers.FacebookLoginHelper;
import com.ingic.saeedni.helpers.GoogleHelper;
import com.ingic.saeedni.helpers.InternetHelper;
import com.ingic.saeedni.helpers.TokenUpdater;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.ui.views.AnyEditTextView;
import com.ingic.saeedni.ui.views.AnySpinner;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.ingic.saeedni.ui.views.TitleBar;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created on 5/23/2017.
 */

public class UserSignupFragment extends BaseFragment implements View.OnClickListener {
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
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
    @BindView(R.id.spnCountry)
    AnySpinner spnCountry;
    @BindView(R.id.ll_textFields)
    LinearLayout llTextFields;
    ArrayList<CitiesEnt> allCities = new ArrayList<>();
    ArrayList<CitiesEnt> allCountries = new ArrayList<>();
    @BindView(R.id.edtadress)
    AnyTextView edtadress;
    @BindView(R.id.Countrypicker)
    CountryCodePicker Countrypicker;
    @BindView(R.id.containerPassword)
    RelativeLayout containerPassword;
    int selectCountryIndex = 0;
    int selectCityIndex = 0;
    int newSelectedCountryCode = 0;
    @BindView(R.id.chk_read)
    CheckBox chkRead;
    String address = "";
    private boolean isSocialLogin = false;
    private FacebookLoginHelper.FacebookLoginEnt facebookLoginEnt;
    private GoogleHelper.GoogleLoginEnt googleLoginEnt;
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
        UserSignupFragment fragment = new UserSignupFragment();
        fragment.isSocialLogin = false;
        return fragment;
    }

    public static UserSignupFragment newInstance(FacebookLoginHelper.FacebookLoginEnt loginEnt) {
        UserSignupFragment fragment = new UserSignupFragment();
        fragment.facebookLoginEnt = null;
        fragment.googleLoginEnt = null;
        fragment.isSocialLogin = true;
        fragment.setFacebookLoginEnt(loginEnt);
        return fragment;
    }

    public static UserSignupFragment newInstance(GoogleHelper.GoogleLoginEnt loginEnt) {
        UserSignupFragment fragment = new UserSignupFragment();
        fragment.facebookLoginEnt = null;
        fragment.googleLoginEnt = null;
        fragment.isSocialLogin = true;
        fragment.setGoogleLoginEnt(loginEnt);
        return fragment;
    }

    public void setFacebookLoginEnt(FacebookLoginHelper.FacebookLoginEnt facebookLoginEnt) {
        this.facebookLoginEnt = facebookLoginEnt;
    }

    public void setGoogleLoginEnt(GoogleHelper.GoogleLoginEnt googleLoginEnt) {
        this.googleLoginEnt = googleLoginEnt;
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
        chkRead.setChecked(prefHelper.istermsCheck());
        chkRead.setOnCheckedChangeListener((buttonView, isChecked) -> prefHelper.setTermsCheck(isChecked));
        getMainActivity().setImageSetter(imageSetter);
        setlistener();
        phoneUtil = PhoneNumberUtil.getInstance();
        getallCountries();

        Countrypicker.registerCarrierNumberEditText(edtnumber);
        Countrypicker.setOnCountryChangeListener(() -> {
            newSelectedCountryCode = Countrypicker.getSelectedCountryCodeAsInt();
        });
        Countrypicker.setCountryForPhoneCode(newSelectedCountryCode);

        checkAndBindSocialMedia();
        if (isSocialLogin) {
            containerPassword.setVisibility(View.GONE);
        } else {
            containerPassword.setVisibility(View.VISIBLE);
        }
        edtadress.setText(address + "");
    }

    private void checkAndBindSocialMedia() {
        if (googleLoginEnt != null) {
            edtname.setText(googleLoginEnt.getGoogleFullName() == null ? "" : googleLoginEnt.getGoogleFullName());
            edtEmail.setText(googleLoginEnt.getGoogleEmail() == null ? "" : googleLoginEnt.getGoogleEmail());
        } else if (facebookLoginEnt != null) {
            edtname.setText(facebookLoginEnt.getFacebookFullName() == null ? "" : facebookLoginEnt.getFacebookFullName());
            edtEmail.setText(facebookLoginEnt.getFacebookEmail() == null ? "" : facebookLoginEnt.getFacebookEmail());
        }
    }

    private void openLocationSelector() {

        try {
           /* Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(getDockActivity());*/
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            getDockActivity().startActivityForResult(builder.build(getDockActivity()), PLACE_AUTOCOMPLETE_REQUEST_CODE);
            //this.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(getDockActivity(), data);
                if (place != null) {
                    edtadress.setText(place.getAddress().toString());
                    address = (place.getAddress().toString());
                    Log.i("Profile", "Place: " + place.getName());
                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getDockActivity(), data);
                // TODO: Handle the error.
                Log.i("Profile", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }

    private void getallCountries() {
        if (allCountries.isEmpty()) {
            serviceHelper.enqueueCall(webService.getAllCountries(), WebServiceConstants.GET_ALL_COUNTRIES);
        } else {
            setCountriesSpinner(allCountries);
        }
    }

    private void getAllCities(int countryID) {
        serviceHelper.enqueueCall(webService.getAllCities(countryID), WebServiceConstants.GET_ALL_CITIES);
    }

    private void setlistener() {
        btnsignup.setOnClickListener(this);
        btnImage.setOnClickListener(this);
        btnTnc.setOnClickListener(this);
        edtadress.setOnClickListener(this);
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
                                /*registerUser(phoneUtil.format(phoneUtil.parse(edtnumber.getText().toString(), getDockActivity().getResources().getString(R.string.uae_country_code)),
                                        PhoneNumberUtil.PhoneNumberFormat.E164));*/
                                registerUser(Countrypicker.getFullNumberWithPlus());

                            } catch (Exception e) {

                            }


                        }

                }
                break;
            case R.id.edtadress:
                openLocationSelector();
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
            Phonenumber.PhoneNumber number = phoneUtil.parse(edtnumber.getText().toString(), Countrypicker.getSelectedCountryNameCode());
            /*if (!Countrypicker.isValidFullNumber()) {
                return true; getDockActivity().getResources().getString(R.string.uae_country_code
            } else {
                edtnumber.setError(getDockActivity().getResources().getString(R.string.enter_valid_number_error));
                return false;
            } */
            if (phoneUtil.isValidNumber(number)) {
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
                allCities = new ArrayList<>();
                CitiesEnt citiesEnt = new CitiesEnt();
                citiesEnt.setLocation("Select City");
                citiesEnt.setAr_address("اختر مدينة");
                allCities.add(citiesEnt);
                allCities.addAll((ArrayList<CitiesEnt>) result);
                setCitiesSpinner(allCities);
                break;
            case WebServiceConstants.GET_ALL_COUNTRIES:
                allCountries.addAll((ArrayList<CitiesEnt>) result);
                setCountriesSpinner(allCountries);

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
        edtadress.setText(address + "");
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
        String socialMediaID;
        String socialMediaPlatform;
        if (googleLoginEnt != null) {
            socialMediaPlatform = AppConstants.SOCIAL_MEDIA_TYPE_GOOGLE;
            socialMediaID = googleLoginEnt.getGoogleUID();
        } else if (facebookLoginEnt != null) {
            socialMediaID = facebookLoginEnt.getFacebookUID();
            socialMediaPlatform = AppConstants.SOCIAL_MEDIA_TYPE_FACEBOOK;
        } else {
            socialMediaID = "";
            socialMediaPlatform = "";
        }
        Call<ResponseWrapper<RegistrationResultEnt>> call = webService.registerUser(
                RequestBody.create(MediaType.parse("text/plain"), edtname.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtEmail.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), allCities.get(spnCity.getSelectedItemPosition()).getId() + ""),
                RequestBody.create(MediaType.parse("text/plain"), allCountries.get(spnCountry.getSelectedItemPosition()).getId() + ""),
                RequestBody.create(MediaType.parse("text/plain"), number),
                RequestBody.create(MediaType.parse("text/plain"), edtadress.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtpassword.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtpassword.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), prefHelper.getLang()),
                RequestBody.create(MediaType.parse("text/plain"), socialMediaID),
                RequestBody.create(MediaType.parse("text/plain"), socialMediaPlatform),
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
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
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

            if (prefHelper.isLanguageArabic()) {
                citiesCollection.add(item.getAr_location());

            } else {
                citiesCollection.add(item.getLocation());
            }
        }
        //  ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getDockActivity(), android.R.layout.simple_spinner_item, citiesCollection);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getDockActivity()
                , R.layout.row_item_spinner, citiesCollection) {
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
        if (selectCityIndex < citiesCollection.size())
            spnCity.setSelection(selectCityIndex);
        spnCity.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectCityIndex = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setCountriesSpinner(ArrayList<CitiesEnt> citiesEnts) {

        final ArrayList<String> citiesCollection = new ArrayList<String>();


        for (CitiesEnt item : citiesEnts
                ) {
            if (prefHelper.isLanguageArabic()) {
                citiesCollection.add(item.getAr_location());

            } else {
                citiesCollection.add(item.getLocation());
            }
        }
        if (citiesEnts.size() > 0) getAllCities(allCountries.get(selectCountryIndex).getId());
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getDockActivity(), R.layout.row_item_spinner, citiesCollection);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCountry.setAdapter(categoryAdapter);
        spnCountry.setOnItemSelectedEvenIfUnchangedListener(null);
        spnCountry.setSelection(selectCountryIndex);
        spnCountry.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getAllCities(allCountries.get(i).getId());
                selectCountryIndex = i;
                selectCityIndex = 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
        } else if ((edtpassword.getText() == null || (edtpassword.getText().toString().isEmpty()) || edtpassword.getText().toString().length() < 6) && !isSocialLogin) {
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
