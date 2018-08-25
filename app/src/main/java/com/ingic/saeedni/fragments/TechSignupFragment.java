package com.ingic.saeedni.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.ingic.saeedni.helpers.InternetHelper;
import com.ingic.saeedni.helpers.TokenUpdater;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.ui.views.AnyEditTextView;
import com.ingic.saeedni.ui.views.AnySpinner;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.ingic.saeedni.ui.views.TitleBar;
import com.jota.autocompletelocation.AutoCompleteLocation;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

public class TechSignupFragment extends BaseFragment {


    public File profilePic;
    public String UserImagePath;
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
    AutoCompleteLocation edtadress;
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

    public File file;
    public String fileAddress;
    @BindView(R.id.img_registration_type)
    ImageView imgRegistrationType;
    @BindView(R.id.edtregistrationtype)
    AnyEditTextView edtregistrationtype;
    @BindView(R.id.edtRegistration_date)
    AnyTextView edtRegistrationDate;
    private String selectedDate = "";


    public static TechSignupFragment newInstance() {
        return new TechSignupFragment();
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
        getAllCities();
        getAllServices();
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


    private void getAllCities() {
        if (allCities.isEmpty())
            serviceHelper.enqueueCall(webService.getAllCities(), WebServiceConstants.GET_ALL_CITIES);
        else setCitiesSpinner(allCities);
    }


    private boolean isPhoneNumberValid() {

        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(edtnumber.getText().toString(), getDockActivity().getResources().getString(R.string.uae_country_code));
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
                CitiesEnt citiesEnt = new CitiesEnt();
                citiesEnt.setLocation(getDockActivity().getResources().getString(R.string.select_city));
                allCities.add(citiesEnt);
                allCities.addAll((ArrayList<CitiesEnt>) result);
                setCitiesSpinner(allCities);
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
                RequestBody.create(MediaType.parse("text/plain"), allCities.get(spnCity.getSelectedItemPosition()).getId() + ""),
                RequestBody.create(MediaType.parse("text/plain"), edtLicenseExpiry.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), prefHelper.getFirebase_TOKEN()),
                RequestBody.create(MediaType.parse("text/plain"), AppConstants.Device_Type),
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
                , android.R.layout.simple_spinner_item, servicesCollection) {
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
        } else if (edtnumber.getText().toString().length() < 9 || edtnumber.getText().toString().length() > 10) {
            edtnumber.setError(getDockActivity().getResources().getString(R.string.enter_valid_number_error));
            return false;
        } else if (edtEmail.getText() == null || (edtEmail.getText().toString().isEmpty()) ||
                (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches())) {
            edtEmail.setError(getDockActivity().getResources().getString(R.string.enter_email));
            return false;
        } else if (edtpassword.getText() == null || (edtpassword.getText().toString().isEmpty()) || edtpassword.getText().toString().length() < 6) {
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


    @OnClick({R.id.btn_image, R.id.edtLicenseExpiry, R.id.btn_signup, R.id.edtLicenseAttach, R.id.edtRegistration_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_image:
                CameraHelper.uploadMedia(getMainActivity());
                break;
            case R.id.edtLicenseExpiry:
                initDatePicker(edtLicenseExpiry);
                break;
            case R.id.edtRegistration_date:
                initDatePicker(edtRegistrationDate);
                break;
            case R.id.edtLicenseAttach:
                CameraHelper.uploadFile(getMainActivity());
                break;
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

