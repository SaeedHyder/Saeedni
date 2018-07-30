package com.ingic.saeedni.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.LocationModel;
import com.ingic.saeedni.fragments.HomeFragment;
import com.ingic.saeedni.fragments.SideMenuFragment;
import com.ingic.saeedni.fragments.UserHomeFragment;
import com.ingic.saeedni.fragments.UserloginFragment;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.helpers.ScreenHelper;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.ui.views.TitleBar;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenImages;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends DockActivity implements OnClickListener, ImageChooserListener {
    private final static String TAG = "ICA";
    public TitleBar titleBar;
    public boolean isNotification;
    @BindView(R.id.header_main)
    TitleBar header_main;
    @BindView(R.id.mainFrameLayout)
    FrameLayout mainFrameLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    String string = "";
    private MainActivity mContext;
    private boolean loading;
    private ImageChooserManager imageChooserManager;
    private String filePath;
    private int chooserType;
    private boolean isActivityResultOver = false;
    private String originalFilePath;
    private String thumbnailFilePath;
    private String thumbnailSmallFilePath;
    private float lastTranslate = 0.0f;
    private ImageSetter imageSetter;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean isSystemLanguageArabic() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale= getApplicationContext().getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            locale= getApplicationContext().getResources().getConfiguration().locale;
        }
        return locale.getLanguage().equalsIgnoreCase("ar");
    }

    private void setCurrentLocale() {
        if (prefHelper.isLanguageArabic() || isSystemLanguageArabic()) {
            Resources resources = getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Configuration conf = resources.getConfiguration();
            conf.locale = new Locale("ar");
            prefHelper.putLang("ar");
            resources.updateConfiguration(conf, dm);
        } else {
            Resources resources = getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Configuration conf = resources.getConfiguration();
            conf.locale = new Locale("en");
            prefHelper.putLang("en");
            resources.updateConfiguration(conf, dm);
        }
    }

    /* @Override
     public void onConfigurationChanged(Configuration newConfig) {
         // refresh your views here
         super.onConfigurationChanged(newConfig);
     }*/
    public void notificationIntent() {

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                isNotification = getIntent().getExtras().getBoolean("tapped");

            }
        }
    }

    private void askPermission() {

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = new String[0];

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            PERMISSIONS = new String[]{
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.VIBRATE,
                    Manifest.permission.CAMERA,
                    Manifest.permission_group.STORAGE,
                    Manifest.permission_group.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };

        }

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public void addTitleBarPadding() {
        mainFrameLayout.setPadding(0, Math.round(getResources().getDimension(R.dimen.x50)), 0, 0);
    }

    public void removeTitleBarPadding() {
        mainFrameLayout.setPadding(0, 0, 0, 0);

    }

    public View getDrawerView() {
        return getLayoutInflater().inflate(getSideMenuFrameLayoutId(), null);
    }

    public void settingSideMenu() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        sideMenuFragment = SideMenuFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(getSideMenuFrameLayoutId(), sideMenuFragment).commit();

        drawerLayout.closeDrawers();
    }

    public void refreshFirstTimeSideMenu() {
        sideMenuFragment = SideMenuFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.remove(sideMenuFragment).commit();
        settingSideMenu();
    }

    public void refreshSideMenu() {
       /* sideMenuFragment = SideMenuFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.remove(sideMenuFragment).commit();
        settingSideMenu();*/
        if (sideMenuFragment != null) {
            sideMenuFragment.refreshMenuOption();
        }
    }

    public void refreshSideMenuWithnewFragment() {
        sideMenuFragment = SideMenuFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.remove(sideMenuFragment).commit();
        settingSideMenu();
       /* if (sideMenuFragment != null) {
            sideMenuFragment.refreshMenuOption();
        }*/
    }

    private int getSideMenuFrameLayoutId() {
        return R.id.sideMneuFragmentContainer;

    }


    public void initFragment() {

        //prefHelper.putLang(this, prefHelper.getLang());
        getSupportFragmentManager().addOnBackStackChangedListener(getListener());

        if (prefHelper.isLogin()) {
            if (prefHelper.getUserType().equals("user")) {
                replaceDockableFragment(UserHomeFragment.newInstance(), "HomeFragment");
            } else {
                replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
            }

        } else {
            replaceDockableFragment(UserloginFragment.newInstance(), "UserloginFragment");
        }
    }



    public void restartActivity() {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private FragmentManager.OnBackStackChangedListener getListener() {

        return new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null) {
                    BaseFragment currFrag = (BaseFragment) manager.findFragmentById(getDockFrameLayoutId());
                    if (currFrag != null) {
                        currFrag.fragmentResume();
                    }
                }
            }
        };
    }


    @Override
    public void onLoadingStarted() {

        if (mainFrameLayout != null) {
            mainFrameLayout.setVisibility(View.VISIBLE);
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
            loading = true;
        }
    }

    @Override
    public void onLoadingFinished() {
        mainFrameLayout.setVisibility(View.VISIBLE);

        if (progressBar != null) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
        loading = false;

    }

    @Override
    public void onProgressUpdated(int percentLoaded) {

    }

    @Override
    public int getDockFrameLayoutId() {
        return R.id.mainFrameLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dock);
        ButterKnife.bind(this);
        titleBar = header_main;
        titleBar.setMainActivityContext(this);
        // setBehindContentView(R.layout.fragment_frame);
        mContext = this;
        Log.i("Screen Density", ScreenHelper.getDensity(this) + "");
        settingSideMenu();
        setCurrentLocale();
        //  prefHelper.putLang(this,prefHelper.getLang());

        if (getIntent().getExtras() != null) {
            string = getIntent().getExtras().getString("mystring");
        }

       /* if(string.equals("tapped")){
            isNotification=true;
        }*/
        notificationIntent();

        titleBar.setMenuButtonListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);


            }
        });

        titleBar.setBackButtonListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (loading) {
                    UIHelper.showLongToastInCenter(getApplicationContext(),
                            R.string.message_wait);
                } else {

                    popFragment();
                    UIHelper.hideSoftKeyboard(getApplicationContext(),
                            titleBar);
                }
            }
        });

        //
            initFragment();

    }

    @Override
    public void onBackPressed() {
        if (loading) {
            UIHelper.showLongToastInCenter(getApplicationContext(),
                    R.string.message_wait);
        } else
            super.onBackPressed();

    }

    @Override
    public void onMenuItemActionCalled(int actionId, String data) {

    }

    @Override
    public void setSubHeading(String subHeadText) {

    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public void hideHeaderButtons(boolean leftBtn, boolean rightBtn) {
    }

    private void notImplemented() {
        UIHelper.showLongToastInCenter(this, "Coming Soon");
    }    @Override
    public void onClick(View view) {

    }

    public void chooseImage() {
        //askPermission();
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_PICK_PICTURE, true);
        Bundle bundle = new Bundle();
        // bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.clearOldFiles();
        try {
            //pbar.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void takePicture() {
        //askPermission();
        chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_CAPTURE_PICTURE, true);
        imageChooserManager.setImageChooserListener(this);
        try {
            //pbar.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "OnActivityResult");
        Log.i(TAG, "File Path : " + filePath);
        Log.i(TAG, "Chooser Type: " + chooserType);
        setCurrentLocale();
        if (resultCode == RESULT_OK
                && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
        } else {
            // progressBar.setVisibility(View.GONE);
        }


        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(getDockFrameLayoutId());

        if (fragment != null) {
            try {
                fragment.onActivityResult(requestCode, resultCode, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType, true);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }

    @Override
    public void onImageChosen(final ChosenImage image) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.i(TAG, "Chosen Image: O - " + image.getFilePathOriginal());
                Log.i(TAG, "Chosen Image: T - " + image.getFileThumbnail());
                Log.i(TAG, "Chosen Image: Ts - " + image.getFileThumbnailSmall());
                isActivityResultOver = true;
                originalFilePath = image.getFilePathOriginal();
                thumbnailFilePath = image.getFileThumbnail();
                thumbnailSmallFilePath = image.getFileThumbnailSmall();
                //pbar.setVisibility(View.GONE);
                if (image != null) {
                    Log.i(TAG, "Chosen Image: Is not null");

                    // Toast.makeText(getApplication(),thumbnailFilePath,Toast.LENGTH_LONG).show();
                    imageSetter.setImage(thumbnailFilePath);

                    //loadImage(imageViewThumbnail, image.getFileThumbnail());
                } else {
                    Log.i(TAG, "Chosen Image: Is null");
                }

            }
        });

    }

    @Override
    public void onError(final String reason) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.i(TAG, "OnError: " + reason);
                // pbar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, reason,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onImagesChosen(final ChosenImages images) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "On Images Chosen: " + images.size());
                onImageChosen(images.getImage(0));
            }
        });
    }

    public void setImageSetter(ImageSetter imageSetter) {
        this.imageSetter = imageSetter;
    }

    public LocationModel getMyCurrentLocation() {

        LocationModel locationObj = null;
        String address = "";

// instantiate the location manager, note you will need to request permissions in your manifest
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

// get the last know location from your location manager.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
// now get the lat/lon from the location and do something with it.
        Location gpslocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location networklocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location passivelocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        Location locationchangelocation = locationManager.getLastKnownLocation(LocationManager.KEY_LOCATION_CHANGED);


        if (gpslocation != null) {

            Log.d("Location", "GPS::" + gpslocation.getLatitude() + "," + gpslocation.getLongitude());
            address = getCurrentAddress(gpslocation.getLatitude(), gpslocation.getLongitude());
            locationObj = new LocationModel(address, gpslocation.getLatitude(), gpslocation.getLongitude());

            return locationObj;

        } else if (networklocation != null) {

            Log.d("Location", "NETWORK::" + networklocation.getLatitude() + "," + networklocation.getLongitude());
            address = getCurrentAddress(networklocation.getLatitude(), networklocation.getLongitude());
            locationObj = new LocationModel(address, networklocation.getLatitude(), networklocation.getLongitude());

            return locationObj;
        } else if (passivelocation != null) {

            Log.d("Location", "PASSIVE::" + passivelocation.getLatitude() + "," + passivelocation.getLongitude());
            address = getCurrentAddress(passivelocation.getLatitude(), passivelocation.getLongitude());
            locationObj = new LocationModel(address, passivelocation.getLatitude(), passivelocation.getLongitude());

            return locationObj;
        } else if (locationchangelocation != null) {

            Log.d("Location", "CHAGELOCATION::" + locationchangelocation.getLatitude() + "," + locationchangelocation.getLongitude());
            address = getCurrentAddress(locationchangelocation.getLatitude(), locationchangelocation.getLongitude());
            locationObj = new LocationModel(address, locationchangelocation.getLatitude(), locationchangelocation.getLongitude());

            return locationObj;
        }

        return locationObj;


    }

    public boolean statusCheck() {
        if (isNetworkAvailable()) {
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
                return false;
            } else {
                return true;
            }
        } else {
            UIHelper.showShortToastInCenter(this, getString(R.string.internet_not_connected));
            return false;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.gps_question))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.gps_yes), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.gps_no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private String getCurrentAddress(double lat, double lng) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
// String city = addresses.get(0).getLocality();
// String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
// String postalCode = addresses.get(0).getPostalCode();
// String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            return address + ", " + country;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public interface ImageSetter {

        public void setImage(String imagePath);

        public void setFilePath(String filePath);

        public void setVideo(String videoPath);

    }




}
