package com.ingic.saeedni.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.ingic.saeedni.R;
import com.ingic.saeedni.activities.MainActivity;
import com.ingic.saeedni.entities.CitiesEnt;
import com.ingic.saeedni.entities.LocationModel;
import com.ingic.saeedni.entities.RegistrationResultEnt;
import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.WebServiceConstants;
import com.ingic.saeedni.helpers.CameraHelper;
import com.ingic.saeedni.helpers.InternetHelper;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.ui.views.AnyEditTextView;
import com.ingic.saeedni.ui.views.AnySpinner;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.ingic.saeedni.ui.views.TitleBar;
import com.ingic.saeedni.ui.views.Util;
import com.jota.autocompletelocation.AutoCompleteLocation;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

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
 * Created on 5/24/2017.
 */

public class UserProfileFragment extends BaseFragment implements View.OnClickListener, AutoCompleteLocation.AutoCompleteLocationListener, MainActivity.ImageSetter {
    public File profilePic;
    @BindView(R.id.CircularImageSharePop)
    CircleImageView CircularImageSharePop;
    @BindView(R.id.edtname)
    AnyEditTextView edtname;
    @BindView(R.id.edtnumber)
    AnyEditTextView edtemail;
    @BindView(R.id.edt_locationgps)
    AutoCompleteLocation edtLocationgps;
    @BindView(R.id.img_gps)
    ImageView imgGps;
    @BindView(R.id.edt_locationspecific)
    AnyTextView edtLocationspecific;
    @BindView(R.id.btn_editcard)
    Button btnEditcard;
    @BindView(R.id.btn_submit)
    Button btnsubmit;
    @BindView(R.id.edtPhoneNo)
    AnyTextView edtPhoneNo;
    @BindView(R.id.spnCity)
    AnySpinner spnCity;
    @BindView(R.id.spnCountry)
    AnySpinner spnCountry;
    ArrayList<CitiesEnt> allCities = new ArrayList<>();
    ArrayList<CitiesEnt> allCountries = new ArrayList<>();
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    @BindView(R.id.parent)
    LinearLayout parent;
    private PhoneNumberUtil phoneUtil;

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_user_profile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        getDockActivity().lockDrawer();
        titleBar.showBackButton();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.edit_profile));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parent.setLayoutDirection(prefHelper.isLanguageArabic() ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        setlistener();
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            getUserProfile();
        }
        getallCountries();
        phoneUtil = PhoneNumberUtil.getInstance();
        getMainActivity().setImageSetter(this);
        edtemail.setInputType(InputType.TYPE_NULL);
        edtPhoneNo.setInputType(InputType.TYPE_NULL);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_ALL_CITIES:
                allCities = new ArrayList<>();
                CitiesEnt citiesEnt = new CitiesEnt();
                citiesEnt.setLocation("Select City");
                citiesEnt.setAr_address("اختر مدينة");
                citiesEnt.setId(0);
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

    private void setCitiesSpinner(ArrayList<CitiesEnt> citiesEnts) {

        final ArrayList<String> citiesCollection = new ArrayList<String>();


        int selectedPosition = 1;
        for (int i = 0; i < citiesEnts.size(); i++) {
            CitiesEnt ent = citiesEnts.get(i);
            if (prefHelper.getRegistrationResult().getCity_id() == ent.getId()) {
                selectedPosition = i;
            }
            if (prefHelper.isLanguageArabic()){
                citiesCollection.add(ent.getAr_location());

            }else {
                citiesCollection.add(ent.getLocation());
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
        if (citiesEnts.size() > selectedPosition) {
            spnCity.setSelection(selectedPosition);
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

    private void setCountriesSpinner(ArrayList<CitiesEnt> citiesEnts) {

        final ArrayList<String> citiesCollection = new ArrayList<String>();
        int selectedPosition = 0;
        for (int i = 0; i < citiesEnts.size(); i++) {
            CitiesEnt ent = citiesEnts.get(i);
            if (prefHelper.getRegistrationResult().getCountry_id() == ent.getId()) {
                selectedPosition = i;
            }
            if (prefHelper.isLanguageArabic()){
                citiesCollection.add(ent.getAr_location());

            }else {
                citiesCollection.add(ent.getLocation());
            }
        }

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getDockActivity(), R.layout.row_item_spinner, citiesCollection);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCountry.setAdapter(categoryAdapter);
        if (citiesEnts.size() > selectedPosition) {
            getAllCities(allCountries.get(selectedPosition).getId());
            spnCountry.setSelection(selectedPosition);
        }
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

    private void getUserProfile() {
        Call<ResponseWrapper<RegistrationResultEnt>> call = webService.getUserProfile(prefHelper.getUserId());
        call.enqueue(new Callback<ResponseWrapper<RegistrationResultEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<RegistrationResultEnt>> call, Response<ResponseWrapper<RegistrationResultEnt>> response) {
                if (response.body() == null) {
                    UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.server_error));
                    return;
                }
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
        Picasso.with(getDockActivity()).load(result.getProfileImage()).
                placeholder(R.drawable.camerawithcircle).into(CircularImageSharePop);
        edtname.setText(result.getFullName());
        edtemail.setText(result.getEmail());
        edtPhoneNo.setText(String.format(Locale.ENGLISH,"%s", result.getPhoneNo()));
        edtLocationgps.setText(result.getAddress());
        edtLocationspecific.setText(result.getAddress());

        if (getMainActivity() != null) {
            getMainActivity().refreshSideMenuWithnewFragment();
            getMainActivity().refreshSideMenu();
        }
    }

    private void setlistener() {
        btnEditcard.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);
        CircularImageSharePop.setOnClickListener(this);
        imgGps.setOnClickListener(this);
        edtLocationgps.setAutoCompleteTextListener(this);
        edtLocationspecific.setOnClickListener(this);
    }

    private void getLocation(AutoCompleteTextView textView) {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION)
                .onGranted(data -> {
                    if (getMainActivity().statusCheck()) {
                        LocationModel locationModel = getMainActivity().getMyCurrentLocation();
                        if (locationModel != null)
                            textView.setText(locationModel.getAddress());
                        else {
                            getLocation(edtLocationgps);
                        }
                    }
                })
                .onDenied(permissions -> {
                    UIHelper.showShortToastInCenter(getMainActivity(), getString(R.string.storage_permission));
                })
                .start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CircularImageSharePop:
                AndPermission.with(getMainActivity())
                        .runtime()
                        .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA)
                        .onGranted(data ->
                                CameraHelper.uploadMedia(getMainActivity()))
                        .onDenied(permissions -> {
                            UIHelper.showShortToastInCenter(getMainActivity(), getString(R.string.storage_permission));
                        })
                        .start();

                break;
            case R.id.img_gps:
                getLocation(edtLocationgps);
                break;
            case R.id.edt_locationspecific:
                openLocationSelector();
                break;
            case R.id.btn_editcard:
                UIHelper.showShortToastInCenter(getDockActivity(), "Will be Implemented Later");
                //getDockActivity().replaceDockableFragment(CreditCardFragment.newInstance(), "CreditCardFargment");
                break;
            case R.id.btn_submit:
                if (validate()) {
                    if (isPhoneNumberValid())
                        if (Util.doubleClickCheck()) {
                            if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                                loadingStarted();
                                try {
                                    updateProfile(getNationalPhoneNumber(edtPhoneNo.getText().toString(),
                                            PhoneNumberUtil.PhoneNumberFormat.E164));
                                } catch (Exception e) {
                                    updateProfile("");
                                }

                            }
                        }
                }
                break;
        }
    }

    private void updateProfile(String format) {
        MultipartBody.Part filePart;
        if (profilePic != null) {
            filePart = MultipartBody.Part.createFormData("profile_picture",
                    profilePic.getName(), RequestBody.create(MediaType.parse("image/*"), profilePic));
        } else {
            filePart = MultipartBody.Part.createFormData("profile_picture", "",
                    RequestBody.create(MediaType.parse("*/*"), ""));
        }
        Call<ResponseWrapper<RegistrationResultEnt>> call = webService.updateProfile(
                RequestBody.create(MediaType.parse("text/plain"), prefHelper.getUserId()),
                RequestBody.create(MediaType.parse("text/plain"), edtname.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtemail.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtLocationspecific.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtLocationspecific.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), format),
                RequestBody.create(MediaType.parse("text/plain"), allCities.get(spnCity.getSelectedItemPosition()).getId() + ""),
                RequestBody.create(MediaType.parse("text/plain"), allCountries.get(spnCountry.getSelectedItemPosition()).getId() + ""),
                filePart);
        call.enqueue(new Callback<ResponseWrapper<RegistrationResultEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<RegistrationResultEnt>> call, Response<ResponseWrapper<RegistrationResultEnt>> response) {
                loadingFinished();
                if (response.body().getResponse().equals("2000")) {
                    prefHelper.putRegistrationResult(response.body().getResult());
                    if (getMainActivity() != null)
                        getMainActivity().refreshSideMenu();

                    UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.profileUpdated));
                    getDockActivity().popBackStackTillEntry(0);
                    getDockActivity().replaceDockableFragment(UserHomeFragment.newInstance(), "UserHomeFragment");
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<RegistrationResultEnt>> call, Throwable t) {
                loadingFinished();
                Log.e("UserProfileDFragment", t.toString());
                // UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
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
                    edtLocationspecific.setText(place.getAddress().toString());
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

    private boolean validate() {
        if (edtemail.getText().toString().isEmpty()) {
            edtemail.setError(getDockActivity().getResources().getString(R.string.empty_email_error));
            return false;
        } else {
            return true;
        }
    }

    private boolean isPhoneNumberValid() {

        return true;
       /* try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(edtPhoneNo.getText().toString(), getDockActivity().getResources().getString(R.string.uae_country_code));
            if (phoneUtil.isValidNumber(number)) {
                return true;
            } else {
                edtPhoneNo.setError(getDockActivity().getResources().getString(R.string.enter_valid_number_error));
                return false;
            }
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
            edtPhoneNo.setError(getDockActivity().getResources().getString(R.string.enter_valid_number_error));
            return false;

        }*/
    }

    private String getNationalPhoneNumber(String Phonenumber, PhoneNumberUtil.PhoneNumberFormat numberFormat) {


        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(Phonenumber, getDockActivity().getResources().getString(R.string.uae_country_code));
            if (phoneUtil.isValidNumber(number)) {
                return phoneUtil.format(number,
                        numberFormat).replaceAll("\\s", "");
            } else {
                //edtPhoneNo.setError(getString(R.string.enter_valid_number_error));
                return "";
            }
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
            //edtPhoneNo.setError(getString(R.string.enter_valid_number_error));
            return "";

        }
    }

    @Override
    public void onTextClear() {

    }

    @Override
    public void onItemSelected(Place selectedPlace) {

    }

    @Override
    public void setImage(String imagePath) {
        if (imagePath != null) {
            profilePic = new File(imagePath);
            ImageLoader.getInstance().displayImage(
                    "file:///" + imagePath, CircularImageSharePop);
        }
    }

    @Override
    public void setFilePath(String filePath) {

    }

    @Override
    public void setVideo(String videoPath) {

    }


}
