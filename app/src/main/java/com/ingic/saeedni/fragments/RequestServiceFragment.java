package com.ingic.saeedni.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.ingic.saeedni.R;
import com.ingic.saeedni.activities.MainActivity;
import com.ingic.saeedni.entities.CitiesEnt;
import com.ingic.saeedni.entities.ImageDetailEnt;
import com.ingic.saeedni.entities.LocationModel;
import com.ingic.saeedni.entities.RegistrationResultEnt;
import com.ingic.saeedni.entities.RequestEnt;
import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.entities.ServiceChildEnt;
import com.ingic.saeedni.entities.ServiceEnt;
import com.ingic.saeedni.entities.UserInProgressEnt;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.AppConstants;
import com.ingic.saeedni.global.WebServiceConstants;
import com.ingic.saeedni.helpers.CameraHelper;
import com.ingic.saeedni.helpers.DateHelper;
import com.ingic.saeedni.helpers.DatePickerHelper;
import com.ingic.saeedni.helpers.DialogHelper;
import com.ingic.saeedni.helpers.InternetHelper;
import com.ingic.saeedni.helpers.TimePickerHelper;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.interfaces.onDeleteImage;
import com.ingic.saeedni.ui.adapters.ArrayListAdapter;
import com.ingic.saeedni.ui.adapters.RecyclerViewAdapterImages;
import com.ingic.saeedni.ui.viewbinder.SelectedJobBinder;
import com.ingic.saeedni.ui.views.AnyEditTextView;
import com.ingic.saeedni.ui.views.AnySpinner;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.ingic.saeedni.ui.views.TitleBar;
import com.jota.autocompletelocation.AutoCompleteLocation;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.ingic.saeedni.R.id.add;
import static com.ingic.saeedni.R.id.edt_addtional_job;


public class RequestServiceFragment extends BaseFragment implements View.OnClickListener, MainActivity.ImageSetter, onDeleteImage, AutoCompleteLocation.AutoCompleteLocationListener {
    public static String TYPE = "TYPE";
    public static String SCREENFROM = "SCREENFROM";
    @BindView(R.id.spn_jobtype)
    AnySpinner spnJobtype;
    @BindView(R.id.spn_jobdescription)
    AnySpinner spnJobdescription;
    @BindView(R.id.listView_jobselected)
    ListView listViewJobselected;
    @BindView(R.id.edt_locationgps)
    AutoCompleteLocation edtLocationgps;

    @BindView(R.id.btn_preferreddate)
    AnyTextView btnPreferreddate;
    @BindView(R.id.btn_preferredtime)
    AnyTextView btnPreferredtime;
    @BindView(R.id.btn_addimage)
    LinearLayout btnAddimage;
    @BindView(R.id.addimages)
    RecyclerView addimages;
    @BindView(R.id.btn_cc)
    Button btnCc;
    @BindView(R.id.btn_cod)
    Button btnCod;
    @BindView(R.id.btn_request)
    Button btnRequest;
    @BindView(R.id.img_gps)
    ImageView imgGps;
    @BindView(R.id.img_cc_check)
    ImageView imgCcCheck;
    @BindView(R.id.img_cod_check)
    ImageView imgCodCheck;

    @BindView(edt_addtional_job)
    AnyEditTextView edtAddtionalJob;
    @BindView(R.id.txt_job_posted)
    AnyTextView txtJobPosted;
    @BindView(R.id.ll_btnDate)
    LinearLayout llBtnDate;
    @BindView(R.id.ll_btnTime)
    LinearLayout llBtnTime;
    @BindView(R.id.spn_subjobtype)
    AnySpinner spnSubJobType;
    @BindView(R.id.edt_job_description)
    AnyEditTextView edtJobDescription;
    @BindView(R.id.txt_jobtype)
    AnyTextView txtJobtype;
    @BindView(R.id.txt_subjobtype)
    AnyTextView txtSubjobtype;
    @BindView(R.id.txt_jobdescription)
    AnyTextView txtJobdescription;
    @BindView(R.id.txt_jobselected)
    AnyTextView txtJobselected;
    @BindView(R.id.txt_jobadditional)
    AnyTextView txtJobadditional;
    /*    @BindView(R.id.edt_addtional_job)
        AnyEditTextView edtAddtionalJob;*/
    @BindView(R.id.txt_joblocation)
    AnyTextView txtJoblocation;
    @BindView(R.id.spnCity)
    AnySpinner spnCity;
    @BindView(R.id.img_location_autocomplete)
    ImageView imgLocationAutocomplete;
    @BindView(R.id.img_location_image)
    ImageView imgLocationImage;
    @BindView(R.id.txt_jobpreferedDate)
    AnyTextView txtJobpreferedDate;
    @BindView(R.id.txt_jobpreferedtime)
    AnyTextView txtJobpreferedtime;
    @BindView(R.id.txt_jobpaymentmethod)
    AnyTextView txtJobpaymentmethod;
    ArrayList<CitiesEnt> allCities = new ArrayList<>();
    ArrayList<CitiesEnt> allCountries = new ArrayList<>();
    ArrayList<RegistrationResultEnt> allTechnician = new ArrayList<>();
    @BindView(R.id.edt_locationspecific)
    AnyTextView edtLocationspecific;
    boolean isOnCall = false;
    @BindView(R.id.spnCountry)
    AnySpinner spnCountry;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    @BindView(R.id.spnTechnician)
    AnySpinner spnTechnician;
    @BindView(R.id.edtAddress)
    AnyEditTextView edtAddress;
    @BindView(R.id.txtaddImage)
    AnyTextView txtaddImage;
    private ArrayList<String> images = new ArrayList<>();
    private RecyclerViewAdapterImages mAdapter;
    private ArrayList<ServiceEnt> selectedJobs;
    private ArrayListAdapter<ServiceEnt> selectedJobsadapter;
    private ServiceEnt jobtype;
    private String paymentType = "COD";
    private ServiceEnt homeSelectedService;
    private ArrayList<ServiceEnt> jobcollection;
    private ArrayList<ServiceEnt> jobChildcollection;
    private ArrayList<ServiceEnt> subjobcollection;
    private String preferreddate = "preferreddate";
    private String preferredtime = "preferredtime";
    private String predate = "", preTime = "";
    private UserInProgressEnt previousRequestData;
    private Boolean isEdit = false;
    private ArrayList<String> deleteimages;
    private Date DateSelected;
    private String TAG = "Request";

    public static RequestServiceFragment newInstance() {
        return new RequestServiceFragment();
    }

    public static RequestServiceFragment newInstance(ServiceEnt type, UserInProgressEnt editData) {
        Bundle args = new Bundle();
        args.putString(TYPE, new Gson().toJson(type));
        args.putString(SCREENFROM, new Gson().toJson(editData));
        RequestServiceFragment fragment = new RequestServiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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
                    edtLocationspecific.setError(null);
                    edtLocationspecific.setText(place.getAddress().toString());
                    spnCity.setSelection(0);
                    spnCountry.setSelection(0);
                    spnCity.setAdapter(null);
                    String latitude = place.getLatLng() != null ? place.getLatLng().latitude + "" : "";
                    String longitude = place.getLatLng() != null ? place.getLatLng().longitude + "" : "";
                    serviceHelper.enqueueCall(webService.getTechniciansByCity(null, null, latitude, longitude), WebServiceConstants.GET_ALL_TECHNICIAN);
                    Log.i(TAG, "Place: " + place.getName());
                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getDockActivity(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }

    private void getAllCities(int countryID) {
        serviceHelper.enqueueCall(webService.getAllCities(countryID), WebServiceConstants.GET_ALL_CITIES);
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
                CitiesEnt countryEnt = new CitiesEnt();
                countryEnt.setLocation("Select Country");
                countryEnt.setAr_address("حدد الدولة");
                allCountries.add(0, countryEnt);
                allCountries.addAll((ArrayList<CitiesEnt>) result);
                setCountriesSpinner(allCountries);

                break;
            case WebServiceConstants.GET_ALL_TECHNICIAN:
                allTechnician = new ArrayList<>();
                allTechnician.addAll((ArrayList<RegistrationResultEnt>) result);
                initTechnicianSpinner(allTechnician);

                break;

        }
    }

    private void initTechnicianSpinner(ArrayList<RegistrationResultEnt> result) {
        final ArrayList<String> citiesCollection = new ArrayList<String>();


        for (RegistrationResultEnt ent : result
                ) {
            citiesCollection.add(ent.getFullName());
        }
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getDockActivity(), R.layout.row_item_spinner, citiesCollection);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTechnician.setAdapter(categoryAdapter);
        spnTechnician.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setCountriesSpinner(ArrayList<CitiesEnt> citiesEnts) {

        final ArrayList<String> citiesCollection = new ArrayList<String>();


        for (CitiesEnt ent : citiesEnts
                ) {
            if (prefHelper.isLanguageArabic()) {
                citiesCollection.add(ent.getAr_location());
            } else {
                citiesCollection.add(ent.getLocation());
            }
        }
        //if (citiesEnts.size() > 0) getAllCities(allCountries.get(0).getId());
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

        spnCountry.setAdapter(categoryAdapter);
        spnCountry.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) return;
                getAllCities(allCountries.get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_request_service;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            TYPE = getArguments().getString(TYPE);
            SCREENFROM = getArguments().getString(SCREENFROM);
            if (TYPE != null)
                homeSelectedService = new Gson().fromJson(TYPE, ServiceEnt.class);
            if (SCREENFROM != null)
                previousRequestData = new Gson().fromJson(SCREENFROM, UserInProgressEnt.class);

        }
        selectedJobsadapter = new ArrayListAdapter<ServiceEnt>(getDockActivity(), new SelectedJobBinder(this, prefHelper));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_request_service, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        getDockActivity().lockDrawer();
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.request_setvice));
    }

    private void setCitiesSpinner(ArrayList<CitiesEnt> citiesEnts) {
        spnTechnician.setAdapter(null);
        final ArrayList<String> citiesCollection = new ArrayList<String>();
        int selectedPosition = 0;
        RegistrationResultEnt userItems = prefHelper.getRegistrationResult();
        for (int i = 0; i < citiesEnts.size(); i++) {
          /*  if (userItems.getCity_id() != null) {
                if (Objects.equals(userItems.getCity_id(), citiesEnts.get(i).getId())) {
                    selectedPosition = i;
                }
            }*/
            if (prefHelper.isLanguageArabic()) {
                citiesCollection.add(citiesEnts.get(i).getAr_location());

            } else {
                citiesCollection.add(citiesEnts.get(i).getLocation());
            }

        }
        // ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getDockActivity(), android.R.layout.simple_spinner_item, citiesCollection);
        // categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
        //spnCity.setSelection(selectedPosition);
        spnCity.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    spnTechnician.setAdapter(null);
                    return;
                }
                edtLocationspecific.setText("");
                int cityID = allCities.get(position).getId();
                int countryID = allCountries.get(spnCountry.getSelectedItemPosition()).getId();
                serviceHelper.enqueueCall(webService.getTechniciansByCity(countryID, cityID, null, null), WebServiceConstants.GET_ALL_TECHNICIAN);
                // bindSelectedJobview(selectedJobs);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
              /*  if (!selectedJobs.contains(jobChildcollection.get(0)))
                    selectedJobs.add(jobChildcollection.get(0));

                refreshListview();*/
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectedJobs = new ArrayList<>();
        deleteimages = new ArrayList<>();
        getAllCountries();
        if (prefHelper.isLanguageArabic()) {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        setListener();
        if (previousRequestData != null) {
            isEdit = true;
            editCurrentService();
        } else {
            setDataInAdapter(images);
            if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                initJobTypeSpinner(TYPE);
            }
            txtJobPosted.setText(getDockActivity().getResources().getString(R.string.job_posted_label) +
                    new SimpleDateFormat("dd-MM-yy").format(Calendar.getInstance().getTime()));

        }

        setListners();


    }

    private void getAllCountries() {
        serviceHelper.enqueueCall(webService.getAllCountries(), WebServiceConstants.GET_ALL_COUNTRIES);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            btnPreferredtime.setText(savedInstanceState.getString(preferredtime));
            btnPreferreddate.setText(savedInstanceState.getString(preferreddate));
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(preferreddate, btnPreferreddate.getText().toString());
        outState.putString(preferredtime, btnPreferredtime.getText().toString());

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListners() {

        edtAddtionalJob.setOnTouchListener((v, event) -> {

            v.getParent().requestDisallowInterceptTouchEvent(true);
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
            return false;
        });
    }


    private void setListener() {
        btnPreferreddate.setOnClickListener(this);
        btnPreferredtime.setOnClickListener(this);
        btnAddimage.setOnClickListener(this);
        btnCc.setOnClickListener(this);
        btnCod.setOnClickListener(this);
        btnRequest.setOnClickListener(this);
        getMainActivity().setImageSetter(this);
        edtLocationgps.setAutoCompleteTextListener(this);
        imgGps.setOnClickListener(this);
        btnPreferreddate.setOnClickListener(this);
        btnPreferredtime.setOnClickListener(this);
        edtLocationspecific.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_preferreddate:
                initDatePicker(btnPreferreddate);
                break;
            case R.id.btn_preferredtime:
                initTimePicker(btnPreferredtime);
                break;
            case R.id.btn_addimage:
                if (images.size() > 4) {
                    UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.imagelimit_error));
                } else {
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

                }

                break;
            case R.id.edt_locationspecific:

                openLocationSelector();
                break;

            case R.id.btn_cod:
                paymentType = "cash";
                setCODCheck();
                break;
            case R.id.btn_request:

                if (!isOnCall) {
                    if (validate()) {
                        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                            loadingStarted();
                            isOnCall = true;
                            CreateRequest();
                        }
                    }
                }

                break;
            case R.id.img_gps:
               /* if (getMainActivity().statusCheck()) {
                    LocationModel locationModel = getMainActivity().getMyCurrentLocation();
                    if (locationModel != null)
                        edtLocationgps.setText(locationModel.getAddress());
                }*/
                getLocation(edtLocationgps);

                break;
        }
    }

    private void initJobTypeSpinner(String type) {
        jobcollection = new ArrayList<>();
        Call<ResponseWrapper<ArrayList<ServiceEnt>>> call = webService.getHomeServices();
        call.enqueue(new Callback<ResponseWrapper<ArrayList<ServiceEnt>>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<ArrayList<ServiceEnt>>> call, Response<ResponseWrapper<ArrayList<ServiceEnt>>> response) {
                if (response.body().getResponse().equals("2000")) {
                    jobcollection.clear();
                    jobcollection.addAll(response.body().getResult());
                    setJobtypeSpinner();

                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<ArrayList<ServiceEnt>>> call, Throwable t) {
                Log.e("TermAndCondition", t.toString());
                // UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
            }
        });


    }

    private void setJobtypeSpinner() {
        final ArrayList<String> jobtypearraylist = new ArrayList<String>();
        for (ServiceEnt item : jobcollection
                ) {
            if (!prefHelper.isLanguageArabic()) {
                jobtypearraylist.add(item.getTitle());
            } else {
                jobtypearraylist.add(item.getArTitle());
            }
        }


        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getDockActivity(), R.layout.row_item_spinner, jobtypearraylist);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnJobtype.setAdapter(categoryAdapter);
        spnJobtype.setEnabled(false);
        if (homeSelectedService != null && jobcollection.size() > 0) {
            if (jobtypearraylist.contains(homeSelectedService.getTitle())) {
                spnJobtype.setSelection(jobtypearraylist.indexOf(homeSelectedService.getTitle()));
                jobtype = jobcollection.get(jobtypearraylist.indexOf(homeSelectedService.getTitle()));
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    // initJobDescriptionSpinner(jobtype);
                    initSubJobDescriptionSpinner(jobtype);
                }
            } else if (jobtypearraylist.contains(homeSelectedService.getArTitle())) {
                spnJobtype.setSelection(jobtypearraylist.indexOf(homeSelectedService.getArTitle()));
                jobtype = jobcollection.get(jobtypearraylist.indexOf(homeSelectedService.getArTitle()));
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    //  initJobDescriptionSpinner(jobtype);
                    initSubJobDescriptionSpinner(jobtype);
                }

            } else {
                spnJobtype.setSelection(0);
                jobtype = jobcollection.get(0);
                spnJobtype.setEnabled(true);
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    // initJobDescriptionSpinner(jobtype);
                    initSubJobDescriptionSpinner(jobtype);
                }
            }
        }

        spnJobtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jobtype = jobcollection.get(position);
                //initJobDescriptionSpinner(jobtype);
                initSubJobDescriptionSpinner(jobtype);
                selectedJobs.clear();
                refreshListview();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //   initJobDescriptionSpinner(jobtype);
            }
        });
    }

    private void initSubJobDescriptionSpinner(ServiceEnt homeSelectedService) {
        if (homeSelectedService != null) {

            subjobcollection = new ArrayList<>();
            Call<ResponseWrapper<ArrayList<ServiceEnt>>> call = webService.getSubServices(String.valueOf(homeSelectedService.getId()));
            call.enqueue(new Callback<ResponseWrapper<ArrayList<ServiceEnt>>>() {
                @Override
                public void onResponse(Call<ResponseWrapper<ArrayList<ServiceEnt>>> call, Response<ResponseWrapper<ArrayList<ServiceEnt>>> response) {
                    if (response.body().getResponse().equals("2000")) {
                        subjobcollection.clear();
                        subjobcollection.addAll(response.body().getResult());
                        setsubJobDescriptionSpinner();

                    } else {
                        UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseWrapper<ArrayList<ServiceEnt>>> call, Throwable t) {
                    Log.e("TermAndCondition", t.toString());

                }
            });

        }
    }

    private void setsubJobDescriptionSpinner() {
        int SelectedIndex = 0;
        int count = -1;
        final ArrayList<String> jobdescriptionarraylist = new ArrayList<String>();
        for (ServiceEnt item : subjobcollection
                ) {
            count++;
            if (previousRequestData != null && item.getId() == previousRequestData.getCategoryID()) {
                SelectedIndex = count;
            }
            if (!prefHelper.isLanguageArabic()) {
                jobdescriptionarraylist.add(item.getTitle());
            } else {
                jobdescriptionarraylist.add(item.getArTitle());
            }
        }
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getDockActivity(), R.layout.row_item_spinner, jobdescriptionarraylist);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSubJobType.setAdapter(categoryAdapter);
        spnSubJobType.setSelection(SelectedIndex);
        spnSubJobType.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < subjobcollection.size())
                    initJobDescriptionSpinner(subjobcollection.get(position));

                // bindSelectedJobview(selectedJobs);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
              /*  if (!selectedJobs.contains(jobChildcollection.get(0)))
                    selectedJobs.add(jobChildcollection.get(0));

                refreshListview();*/
            }
        });
    }

    private void initJobDescriptionSpinner(ServiceEnt selectedService) {
        if (selectedService != null) {

            jobChildcollection = new ArrayList<>();
            Call<ResponseWrapper<ArrayList<ServiceEnt>>> call = webService.getchildServices(String.valueOf(selectedService.getId()));
            call.enqueue(new Callback<ResponseWrapper<ArrayList<ServiceEnt>>>() {
                @Override
                public void onResponse(Call<ResponseWrapper<ArrayList<ServiceEnt>>> call, Response<ResponseWrapper<ArrayList<ServiceEnt>>> response) {
                    if (response.body().getResponse().equals("2000")) {
                        jobChildcollection.clear();
                        ServiceEnt serviceEnt = new ServiceEnt();
                        serviceEnt.setTitle(getDockActivity().getResources().getString(R.string.select_job_description));
                        serviceEnt.setArTitle(getDockActivity().getResources().getString(R.string.select_job_description));

                        jobChildcollection.add(serviceEnt);
                        jobChildcollection.addAll(response.body().getResult());
                        setJobDescriptionSpinner();

                    } else {
                        UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseWrapper<ArrayList<ServiceEnt>>> call, Throwable t) {
                    Log.e("TermAndCondition", t.toString());

                }
            });

        }
    }

    private void editCurrentService() {
        List<ImageDetailEnt> imagesDetail = previousRequestData.getImageDetail();
        for (ImageDetailEnt item : imagesDetail
                ) {
            images.add(item.getFileLink());
        }
        setDataInAdapter(images);
        for (ServiceChildEnt item : previousRequestData.getServicsList()
                ) {
            selectedJobs.add(item.getServiceEnt());
        }

        refreshListview();
        if (previousRequestData.getServiceDetail() != null)
            homeSelectedService = previousRequestData.getServiceDetail();
        edtLocationgps.setText(previousRequestData.getAddress());
        edtLocationspecific.setText(previousRequestData.getFullAddress());
        edtAddtionalJob.setText(previousRequestData.getDiscription());
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            initJobTypeSpinner(TYPE);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
          /*  Date d  = sdf.parse(previousRequestData.getDate());
            Date d1 = sdf.parse(previousRequestData.getTime());*/
            Date d2 = sdf.parse(previousRequestData.getCreatedAt());
            predate = previousRequestData.getDate();
            preTime = previousRequestData.getTime();
          /*  btnPreferreddate.setText(new SimpleDateFormat("yyyy MMM dd", Locale.ENGLISH).format(d));
            btnPreferredtime.setText( new SimpleDateFormat("h:mm a").format(d1));*/
            btnPreferreddate.setText(previousRequestData.getDate());
            btnPreferredtime.setText(previousRequestData.getTime());
            txtJobPosted.setText(getDockActivity().getResources().getString(R.string.job_posted_label) + new SimpleDateFormat("dd-MM-yy").format(d2));
        } catch (Exception ex) {
            Logger.getLogger(RequestServiceFragment.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (previousRequestData.getPaymentType().equals("credit")) {
            paymentType = "credit";
            setCCCheck();
        } else {
            paymentType = "cash";
            setCODCheck();
        }


    }


    private void setJobDescriptionSpinner() {
        final ArrayList<String> jobdescriptionarraylist = new ArrayList<String>();
        for (ServiceEnt item : jobChildcollection
                ) {
            if (!prefHelper.isLanguageArabic()) {
                jobdescriptionarraylist.add(item.getTitle());
            } else {
                jobdescriptionarraylist.add(item.getArTitle());
            }
        }

        // ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getDockActivity(), android.R.layout.simple_spinner_item, jobdescriptionarraylist);
        //   categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getDockActivity()
                , R.layout.row_item_spinner, jobdescriptionarraylist) {
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
        spnJobdescription.setAdapter(categoryAdapter);

        spnJobdescription.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!selectedJobs.contains(jobChildcollection.get(position))) {
                    if (position != 0)
                        selectedJobs.add(jobChildcollection.get(position));
                }


                refreshListview();
                // bindSelectedJobview(selectedJobs);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
              /*  if (!selectedJobs.contains(jobChildcollection.get(0)))
                    selectedJobs.add(jobChildcollection.get(0));

                refreshListview();*/
            }
        });
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
                            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.location_error));
                            //getLocation(edtLocationgps);
                        }
                    }
                })
                .onDenied(permissions -> {
                    UIHelper.showShortToastInCenter(getMainActivity(), getString(R.string.storage_permission));
                })
                .start();

    }

    private void CreateRequest() {
        String serviceID = String.valueOf(jobcollection.get(spnJobtype.getSelectedItemPosition()).getId());
        String subServiceID = "-1";
        if (!subjobcollection.isEmpty())
            subServiceID = String.valueOf(subjobcollection.get(spnSubJobType.getSelectedItemPosition()).getId());
        StringBuilder sb = new StringBuilder(selectedJobs.size());
        ArrayList<Integer> selectedIDS = new ArrayList<>();
        for (ServiceEnt item : selectedJobs
                ) {
            selectedIDS.add(item.getId());
        }
        String serviceIDS = StringUtils.join(selectedIDS, ",");
        ArrayList<MultipartBody.Part> files = new ArrayList<>();

        for (String item : images
                ) {
            if (!item.contains("https")) {

                File file = new File(item);
                files.add(MultipartBody.Part.createFormData("images[]",
                        file.getName(), RequestBody.create(MediaType.parse("image/*"), file)));

            }

        }
        //MultipartBody.Part[] part = files.toArray();
        Call<ResponseWrapper<RequestEnt>> call;
        if (!isEdit) {
            String address = edtLocationspecific.getText().toString().isEmpty() ? edtAddress.getText().toString() : edtLocationspecific.getText().toString();
            call = webService.createRequest(
                    RequestBody.create(MediaType.parse("text/plain"), prefHelper.getUserId()),
                    RequestBody.create(MediaType.parse("text/plain"), serviceID),
                    RequestBody.create(MediaType.parse("text/plain"), subServiceID),
                    RequestBody.create(MediaType.parse("text/plain"), allTechnician.get(spnTechnician.getSelectedItemPosition()).getId() + ""),
                    RequestBody.create(MediaType.parse("text/plain"), (spnCountry.getSelectedItemPosition() == -1 || spnCountry.getSelectedItemPosition() == 0) ? "" : allCountries.get(spnCountry.getSelectedItemPosition()).getId() + ""),
                    RequestBody.create(MediaType.parse("text/plain"), (spnCity.getSelectedItemPosition() == -1 || spnCity.getSelectedItemPosition() == 0) ? "" : allCities.get(spnCity.getSelectedItemPosition()).getId() + ""),
                    RequestBody.create(MediaType.parse("text/plain"), serviceIDS),
                    RequestBody.create(MediaType.parse("text/plain"), edtAddtionalJob.getText().toString()),
                    RequestBody.create(MediaType.parse("text/plain"), address),
                    RequestBody.create(MediaType.parse("text/plain"), address),
                    RequestBody.create(MediaType.parse("text/plain"), predate),
                    RequestBody.create(MediaType.parse("text/plain"), preTime),
                    RequestBody.create(MediaType.parse("text/plain"), paymentType),
                    RequestBody.create(MediaType.parse("text/plain"), String.valueOf(AppConstants.ACCEPT_QUOTE)), files);
        } else {
            ArrayList<String> deleteImagesIDs = new ArrayList<>();
            if (previousRequestData != null) {
                int index = 0;

                for (int i = 0; i < deleteimages.size(); i++) {
                    for (int j = 0; j < previousRequestData.getImageDetail().size(); j++) {
                        if (previousRequestData.getImageDetail().get(j).getFileLink().equals(deleteimages.get(i))) {
                            deleteImagesIDs.add(String.valueOf(previousRequestData.getImageDetail()
                                    .get(j).getId()));
                            break;
                        }
                    }
                }
            }
            String deleteimagesIDS = StringUtils.join(deleteImagesIDs, ",");
            call = webService.editUserRequest(
                    RequestBody.create(MediaType.parse("text/plain"), prefHelper.getUserId()),
                    RequestBody.create(MediaType.parse("text/plain"), String.valueOf(previousRequestData.getId())),
                    RequestBody.create(MediaType.parse("text/plain"), serviceID),
                    RequestBody.create(MediaType.parse("text/plain"), subServiceID),
                    RequestBody.create(MediaType.parse("text/plain"), serviceIDS),
                    RequestBody.create(MediaType.parse("text/plain"), edtAddtionalJob.getText().toString()),
                    RequestBody.create(MediaType.parse("text/plain"), edtLocationspecific.getText().toString()),
                    RequestBody.create(MediaType.parse("text/plain"), edtLocationspecific.getText().toString()),
                    RequestBody.create(MediaType.parse("text/plain"), predate),
                    RequestBody.create(MediaType.parse("text/plain"), preTime),
                    RequestBody.create(MediaType.parse("text/plain"), paymentType),
                    RequestBody.create(MediaType.parse("text/plain"), String.valueOf(AppConstants.CREATE_REQUEST)), files,
                    RequestBody.create(MediaType.parse("text/plain"), deleteimagesIDS));
        }
        call.enqueue(new Callback<ResponseWrapper<RequestEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<RequestEnt>> call, Response<ResponseWrapper<RequestEnt>> response) {
                isOnCall = false;
                loadingFinished();
                if (response.body().getResponse().equals("2000")) {
                    final DialogHelper RequestSend = new DialogHelper(getDockActivity());
                    RequestSend.initRequestSendDialog(R.layout.request_send_dialog, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RequestSend.hideDialog();
                            getDockActivity().popBackStackTillEntry(0);
                            getDockActivity().replaceDockableFragment(UserHomeFragment.newInstance(), "UserHomeFragment");
                        }
                    });
                    RequestSend.setCancelable(false);
                    RequestSend.showDialog();
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<RequestEnt>> call, Throwable t) {
                loadingFinished();
                isOnCall = false;
                Log.e("EntryCodeFragment", t.toString());
            }
        });
    }

    private boolean validate() {
        /*if (edtLocationspecific.getText().toString().isEmpty()) {
            edtLocationspecific.setError(getDockActivity().getResources().getString(R.string.enter_location));
            return false;
        } else*/
        if (btnPreferreddate.getText().toString().isEmpty()) {
            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.select_date));
            return false;
        } else if (btnPreferredtime.getText().toString().isEmpty()) {
            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.select_time));
            return false;
        } else if (paymentType.isEmpty()) {
            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.payment_type));
            return false;
        } else if (selectedJobs.isEmpty()) {
            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.selectjob));
            return false;
        } else if (spnCity.getSelectedItemPosition() == 0 && edtLocationspecific.getText().toString().isEmpty()) {
            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.select_city));
            return false;
        } else if (edtAddress.getText().toString().trim().equals("") && edtLocationspecific.getText().toString().isEmpty()) {
            edtAddress.setError(getDockActivity().getResources().getString(R.string.enter_location));
            return false;
        } else if (spnCountry.getSelectedItemPosition() == 0 && edtLocationspecific.getText().toString().isEmpty()) {
            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.select_country));
            return false;
        } else if (spnTechnician.getSelectedItemPosition() == -1) {
            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.selectTechnician));
            return false;
        } else {
            return true;
        }
    }

    private void setCCCheck() {

        imgCcCheck.invalidate();
        imgCcCheck.setVisibility(View.VISIBLE);
        imgCodCheck.setVisibility(View.GONE);
    }

    private void setCODCheck() {
        imgCcCheck.setVisibility(View.GONE);
        imgCodCheck.setVisibility(View.VISIBLE);
    }

    private void initDatePicker(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        final DatePickerHelper datePickerHelper = new DatePickerHelper();
        datePickerHelper.initDateDialog(
                getDockActivity(),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
                , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Date date = new Date();
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

// and get that as a Date
                        Date dateSpecified = c.getTime();
                        if (dateSpecified.before(date)) {
                            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.date_before_error));
                        } else {
                            DateSelected = dateSpecified;
                            if (prefHelper.isLanguageArabic())
                                textView.setText(new SimpleDateFormat("yyyy-MM-dd", new Locale("ar")).format(c.getTime()));
                            else
                                textView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(c.getTime()));
                            predate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(c.getTime());

                        }

                    }
                }, "PreferredDate");

        datePickerHelper.showDate();
    }

    private void initTimePicker(final TextView textView) {
        if (DateSelected != null) {
            Calendar calendar = Calendar.getInstance();
            final TimePickerHelper timePicker = new TimePickerHelper();

            timePicker.initTimeDialog(getDockActivity(), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Date date = new Date();
                    if (DateHelper.isSameDay(DateSelected, date) && !DateHelper.isTimeAfter(date.getHours(), date.getMinutes(), hourOfDay, minute)) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.less_time_error));
                    } else {
                        Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        c.set(year, month, day, hourOfDay, minute);
                        preTime = new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(c.getTime());
                        if (prefHelper.isLanguageArabic())
                            textView.setText(new SimpleDateFormat("HH:mm", new Locale("ar")).format(c.getTime()));
                        else
                            textView.setText(new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(c.getTime()));
                    }

                }
            }, true);

            timePicker.showTime();
        } else {
            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.select_date_error));
        }
    }

    private void bindSelectedJobview(ArrayList<ServiceEnt> jobs) {
        selectedJobsadapter.clearList();
        listViewJobselected.setAdapter(selectedJobsadapter);
        selectedJobsadapter.addAll(jobs);
        selectedJobsadapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(listViewJobselected);
    }

    private void setDataInAdapter(ArrayList<String> ImageArray) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.HORIZONTAL, false);
        addimages.setLayoutManager(layoutManager);
        mAdapter = new RecyclerViewAdapterImages(ImageArray, getDockActivity(), this);
        /* RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());*/
        addimages.setLayoutManager(layoutManager);
        addimages.setItemAnimator(new DefaultItemAnimator());
        addimages.setAdapter(mAdapter);

    }

    private void refreshListview() {
        selectedJobsadapter.clearList();
        listViewJobselected.setAdapter(selectedJobsadapter);
        selectedJobsadapter.addAll(selectedJobs);
        selectedJobsadapter.notifyDataSetChanged();

        setListViewHeightBasedOnChildren(listViewJobselected);
    }

    @Override
    public void setImage(String imagePath) {
        if (imagePath != null) {
            images.add(imagePath);
            mAdapter.notifyItemInserted(images.size() - 1);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setFilePath(String filePath) {

    }

    @Override
    public void setVideo(String videoPath) {

    }

    @Override
    public void onDelete(int position) {
        if (images.get(position).contains("https")) {
            deleteimages.add(images.get(position));
        }
        images.remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnDeleteJobs(int position) {
        if (selectedJobs.size() > position)
            selectedJobs.remove(position);

        if (selectedJobs.size() <= 0) {
            spnJobdescription.setSelection(0);
        }

        refreshListview();

    }

    @Override
    public void onTextClear() {

    }

    @Override
    public void onItemSelected(Place selectedPlace) {
        System.out.println(selectedPlace);
    }


}
