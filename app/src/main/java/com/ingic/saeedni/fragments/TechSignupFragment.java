package com.ingic.saeedni.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.ingic.saeedni.R;
import com.ingic.saeedni.activities.MainActivity;
import com.ingic.saeedni.entities.AllServicesEnt;
import com.ingic.saeedni.entities.CitiesEnt;
import com.ingic.saeedni.entities.RegistrationResultEnt;
import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.AppConstants;
import com.ingic.saeedni.global.WebServiceConstants;
import com.ingic.saeedni.helpers.CameraHelper;
import com.ingic.saeedni.helpers.DatePickerHelper;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class TechSignupFragment extends BaseFragment {


    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public File profilePic;
    public String UserImagePath;
    public File file;
    public String fileAddress;
    PhoneNumberUtil phoneUtil;
    ArrayList<CitiesEnt> allCities = new ArrayList<>();
    ArrayList<AllServicesEnt> allServices = new ArrayList<>();
    @BindView(R.id.btn_image)
    CircleImageView btnImage;
    @BindView(R.id.img_company_name)
    ImageView imgCompanyName;
    @BindView(R.id.edtCompanyName)
    AnyEditTextView edtCompanyName;
    @BindView(R.id.img_first_name)
    ImageView imgFirstName;
    @BindView(R.id.edtfirsname)
    AnyEditTextView edtfirsname;
    @BindView(R.id.img_last_name)
    ImageView imgLastName;
    @BindView(R.id.edtlastname)
    AnyEditTextView edtlastname;
    @BindView(R.id.img_phone)
    ImageView imgPhone;
    @BindView(R.id.edtnumber)
    AnyEditTextView edtnumber;
    @BindView(R.id.img_email)
    ImageView imgEmail;
    @BindView(R.id.edtEmail)
    AnyEditTextView edtEmail;
    @BindView(R.id.img_password)
    ImageView imgPassword;
    @BindView(R.id.edtpassword)
    AnyEditTextView edtpassword;
    @BindView(R.id.img_speciality)
    ImageView imgSpeciality;
    @BindView(R.id.spnSpeciality)
    AnySpinner spnSpeciality;
    @BindView(R.id.img_address)
    ImageView imgAddress;
    @BindView(R.id.edtadress)
    AnyTextView edtadress;
    @BindView(R.id.img_address2)
    ImageView imgAddress2;
    @BindView(R.id.spnCity)
    AnySpinner spnCity;
    @BindView(R.id.img_license_attach)
    ImageView imgLicenseAttach;
    @BindView(R.id.edtLicenseAttach)
    AnyTextView edtLicenseAttach;
    @BindView(R.id.img_license_expiray)
    ImageView imgLicenseExpiray;
    @BindView(R.id.edtLicenseExpiry)
    AnyTextView edtLicenseExpiry;
    @BindView(R.id.btn_signup)
    Button btnSignup;
    @BindView(R.id.btn_tnc)
    AnyTextView btnTnc;
    @BindView(R.id.ll_textFields)
    LinearLayout llTextFields;
    Unbinder unbinder;
    @BindView(R.id.img_registration_type)
    ImageView imgRegistrationType;
    @BindView(R.id.edtregistrationtype)
    AnyEditTextView edtregistrationtype;
    @BindView(R.id.edtRegistration_date)
    AnyTextView edtRegistrationDate;
    @BindView(R.id.Countrypicker)
    CountryCodePicker Countrypicker;
    @BindView(R.id.spnCountry)
    AnySpinner spnCountry;
    ArrayList<CitiesEnt> allCountries = new ArrayList<>();
    @BindView(R.id.containerPassword)
    RelativeLayout containerPassword;
    private boolean isSocialLogin = false;
    private String selectedDate = "";
    private FacebookLoginHelper.FacebookLoginEnt facebookLoginEnt;
    private GoogleHelper.GoogleLoginEnt googleLoginEnt;
    private LatLng latLng;

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
            if (filePath != null) {
                file = new File(filePath);
                fileAddress = filePath;
                edtLicenseAttach.setText(filePath);

            }

        }

        @Override
        public void setVideo(String videoPath) {

        }
    };

    public static TechSignupFragment newInstance() {
        TechSignupFragment fragment = new TechSignupFragment();
        fragment.isSocialLogin = false;
        return fragment;
    }

    public static TechSignupFragment newInstance(FacebookLoginHelper.FacebookLoginEnt loginEnt) {
        TechSignupFragment fragment = new TechSignupFragment();
        fragment.facebookLoginEnt = null;
        fragment.googleLoginEnt = null;
        fragment.isSocialLogin = true;
        fragment.setFacebookLoginEnt(loginEnt);
        return fragment;
    }

    public static TechSignupFragment newInstance(GoogleHelper.GoogleLoginEnt loginEnt) {
        TechSignupFragment fragment = new TechSignupFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (prefHelper.isLanguageArabic()) {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        getMainActivity().setImageSetter(imageSetter);
        phoneUtil = PhoneNumberUtil.getInstance();
        getallCountries();
        getAllServices();
        Countrypicker.registerCarrierNumberEditText(edtnumber);
        checkAndBindSocialMedia();
        if (isSocialLogin) {
            containerPassword.setVisibility(View.GONE);
        } else {
            containerPassword.setVisibility(View.VISIBLE);
        }
    }

    private void checkAndBindSocialMedia() {
        if (googleLoginEnt != null) {
            edtfirsname.setText(googleLoginEnt.getGoogleFirstName() == null ? "" : googleLoginEnt.getGoogleFirstName());
            edtlastname.setText(googleLoginEnt.getGoogleLastName() == null ? "" : googleLoginEnt.getGoogleLastName());
            edtEmail.setText(googleLoginEnt.getGoogleEmail() == null ? "" : googleLoginEnt.getGoogleEmail());
        } else if (facebookLoginEnt != null) {
            edtfirsname.setText(facebookLoginEnt.getFacebookFirstName() == null ? "" : facebookLoginEnt.getFacebookFirstName());
            edtlastname.setText(facebookLoginEnt.getFacebookLastName() == null ? "" : facebookLoginEnt.getFacebookLastName());
            edtEmail.setText(facebookLoginEnt.getFacebookEmail() == null ? "" : facebookLoginEnt.getFacebookEmail());
        }
    }

    private void getAllServices() {

        if (allServices.isEmpty())
            serviceHelper.enqueueCall(webService.getAllServices(), WebServiceConstants.GET_ALL_SERVICES);
        else setServicesSpinner(allServices);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_tech_signup;
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

    private boolean isPhoneNumberValid() {

        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(edtnumber.getText().toString(), Countrypicker.getSelectedCountryNameCode());
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

            case WebServiceConstants.GET_ALL_SERVICES:
                AllServicesEnt allServicesEnt = new AllServicesEnt();
                allServicesEnt.setTitle(getDockActivity().getResources().getString(R.string.select_speciality));
                allServicesEnt.setArTitle(getDockActivity().getResources().getString(R.string.select_speciality));
                allServices.add(allServicesEnt);
                allServices.addAll((ArrayList<AllServicesEnt>) result);
                setServicesSpinner(allServices);
                break;


        }
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
        if (citiesEnts.size() > 0) getAllCities(allCountries.get(0).getId());
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getDockActivity(), R.layout.row_item_spinner, citiesCollection);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCountry.setAdapter(categoryAdapter);
        spnCountry.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getAllCities(allCountries.get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                    latLng = place.getLatLng();
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

    private void registerUser(String number) {
        MultipartBody.Part filePart;
        MultipartBody.Part attachedFile;
        if (profilePic != null) {
            filePart = MultipartBody.Part.createFormData("profile_picture",
                    profilePic.getName(), RequestBody.create(MediaType.parse("image/*"), profilePic));
        } else {
            filePart = MultipartBody.Part.createFormData("profile_picture", "",
                    RequestBody.create(MediaType.parse("*/*"), ""));
        }

        if (fileAddress != null && file != null) {
            attachedFile = MultipartBody.Part.createFormData("resume", fileAddress,
                    RequestBody.create(MediaType.parse("*/*"), file));

        } else {
            attachedFile = MultipartBody.Part.createFormData("resume", "",
                    RequestBody.create(MediaType.parse("*/*"), ""));
        }

        String currentDate = new SimpleDateFormat(AppConstants.DateFormat_YMD, Locale.ENGLISH).format(new Date());
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
        String latitude = latLng != null ? latLng.latitude + "" : "";
        String longitude = latLng != null ? latLng.longitude + "" : "";
        Call<ResponseWrapper<RegistrationResultEnt>> call = webService.registerTechnician(
                RequestBody.create(MediaType.parse("text/plain"), edtCompanyName.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtfirsname.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtlastname.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), number),
                RequestBody.create(MediaType.parse("text/plain"), edtEmail.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), allServices.get(spnSpeciality.getSelectedItemPosition()).getId() + ""),
                RequestBody.create(MediaType.parse("text/plain"), edtpassword.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtpassword.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtregistrationtype.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtRegistrationDate.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtadress.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), latitude),
                RequestBody.create(MediaType.parse("text/plain"), longitude),
                RequestBody.create(MediaType.parse("text/plain"), allCountries.get(spnCountry.getSelectedItemPosition()).getId() + ""),
                RequestBody.create(MediaType.parse("text/plain"), allCities.get(spnCity.getSelectedItemPosition()).getId() + ""),
                RequestBody.create(MediaType.parse("text/plain"), edtLicenseExpiry.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), prefHelper.getFirebase_TOKEN()),
                RequestBody.create(MediaType.parse("text/plain"), AppConstants.Device_Type),
                RequestBody.create(MediaType.parse("text/plain"), prefHelper.getLang()),
                RequestBody.create(MediaType.parse("text/plain"), socialMediaID),
                RequestBody.create(MediaType.parse("text/plain"), socialMediaPlatform),
                filePart, attachedFile);
        call.enqueue(new Callback<ResponseWrapper<RegistrationResultEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<RegistrationResultEnt>> call, Response<ResponseWrapper<RegistrationResultEnt>> response) {
                loadingFinished();
                if (response.body().getResponse().equals("2000")) {
                    prefHelper.putRegistrationResult(response.body().getResult());
                    prefHelper.setUserType("technician");
                    prefHelper.setUsrId(String.valueOf(response.body().getResult().getId()));
                    prefHelper.setUsrName(response.body().getResult().getFullName());
                    prefHelper.setPhonenumber(response.body().getResult().getPhoneNo());
                    TokenUpdater.getInstance().UpdateToken(getDockActivity(),
                            prefHelper.getUserId(),
                            AppConstants.Device_Type,
                            prefHelper.getFirebase_TOKEN());
                    prefHelper.setLoginStatus(false);

                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                    getDockActivity().popBackStackTillEntry(1);
                    getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");

                    // prefHelper.setLoginStatus(true);
                    // showSignupDialog();

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
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragmnet");
            }
        });
        signupDialog.setCancelable(false);
        signupDialog.showDialog();
    }

    private void setServicesSpinner(ArrayList<AllServicesEnt> allServices) {
        final ArrayList<String> servicesCollection = new ArrayList<String>();

        for (AllServicesEnt item : allServices
                ) {
            if (prefHelper.isLanguageArabic()) {
                servicesCollection.add(item.getArTitle());
            } else {
                servicesCollection.add(item.getTitle());
            }
        }
        //  ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getDockActivity(), android.R.layout.simple_spinner_item, servicesCollection);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getDockActivity()
                , R.layout.row_item_spinner, servicesCollection) {
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
        spnSpeciality.setAdapter(categoryAdapter);


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

    }

    private boolean isvalidated() {

        if (edtCompanyName.getText().toString().isEmpty()) {
            edtCompanyName.setError(getDockActivity().getResources().getString(R.string.enter_company_name));
            return false;
        } else if (edtfirsname.getText().toString().isEmpty()) {
            edtfirsname.setError(getDockActivity().getResources().getString(R.string.enter_first_name));
            return false;
        } else if (edtlastname.getText().toString().isEmpty()) {
            edtlastname.setError(getDockActivity().getResources().getString(R.string.enter_last_name));
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
        } else if (spnSpeciality.getSelectedItemPosition() == 0) {
            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.select_speciality));
            return false;
        } else if (edtregistrationtype.getText().toString().isEmpty()) {
            edtregistrationtype.setError(getDockActivity().getResources().getString(R.string.enter_registration_type));
            return false;
        } else if (edtRegistrationDate.getText().toString().isEmpty()) {
            //  edtLicenseExpiry.setError(getString(R.string.select_expiry_date));
            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.enter_registration_date));
            return false;
        } else if (edtadress.getText().toString().isEmpty()) {
            edtadress.setError(getDockActivity().getResources().getString(R.string.address_empty_error));
            return false;
        } else if (spnCity.getSelectedItemPosition() == 0) {
            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.select_city));
            return false;
        } else if (edtLicenseAttach.getText().toString().isEmpty()) {
            //  edtLicenseAttach.setError(getString(R.string.attach_file));
            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.attach_file));
            return false;
        } else if (edtLicenseExpiry.getText().toString().isEmpty()) {
            //  edtLicenseExpiry.setError(getString(R.string.select_expiry_date));
            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.select_expiry_date));
            return false;
        } else if (profilePic == null) {
            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.select_profile_picture));
            return false;
        } else {
            return true;
        }

    }

    @OnClick({R.id.btn_image, R.id.edtLicenseExpiry, R.id.btn_signup, R.id.edtLicenseAttach, R.id.edtRegistration_date, R.id.edtadress})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
            case R.id.edtLicenseExpiry:
                initDatePicker(edtLicenseExpiry);
                break;
            case R.id.edtadress:
                openLocationSelector();
                break;
            case R.id.edtRegistration_date:
                initDatePicker(edtRegistrationDate);
                break;
            case R.id.edtLicenseAttach:
                AndPermission.with(this)
                        .runtime()
                        .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                CameraHelper.uploadFile(getMainActivity());
                            }
                        })
                        .onDenied(permissions -> {
                            UIHelper.showShortToastInCenter(getMainActivity(), getString(R.string.storage_permission));
                        })
                        .start();

                break;
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
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.signup));
    }

    private void initDatePicker(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        final DatePickerHelper datePickerHelper = new DatePickerHelper();
        datePickerHelper.initDateDialog(
                getDockActivity(),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
                , "PreferredDate");
        datePickerHelper.setListener(new DatePickerHelper.OnDateSelectedListener() {
            @Override
            public void onDatePicked(Calendar date) {
                selectedDate = new SimpleDateFormat(AppConstants.SERVER_DATE_FORMAT, Locale.ENGLISH)
                        .format(date.getTime());
                textView.setText(new SimpleDateFormat(AppConstants.DateFormat_YMD, Locale.ENGLISH)
                        .format(date.getTime()));
            }
        });
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.YEAR, c.get(Calendar.YEAR));
        datePickerHelper.setminDate(c.getTime().getTime());
        datePickerHelper.showDate();
    }


}


