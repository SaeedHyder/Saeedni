package com.ingic.saeedni.fragments.abstracts;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.andreabaccega.formedittextvalidator.Validator;
import com.ingic.saeedni.R;
import com.ingic.saeedni.activities.DockActivity;
import com.ingic.saeedni.activities.MainActivity;
import com.ingic.saeedni.global.WebServiceConstants;
import com.ingic.saeedni.helpers.BasePreferenceHelper;
import com.ingic.saeedni.helpers.GPSTracker;
import com.ingic.saeedni.helpers.KeyboardHide;
import com.ingic.saeedni.helpers.ServiceHelper;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.interfaces.LoadingListener;
import com.ingic.saeedni.interfaces.webServiceResponseLisener;
import com.ingic.saeedni.retrofit.WebService;
import com.ingic.saeedni.retrofit.WebServiceFactory;
import com.ingic.saeedni.ui.views.AnyEditTextView;
import com.ingic.saeedni.ui.views.TitleBar;


public abstract class BaseFragment extends Fragment implements webServiceResponseLisener {

    protected WebService webService;
    protected DockActivity myDockActivity;
    protected Handler handler = new Handler();
    protected BasePreferenceHelper prefHelper;
    protected GPSTracker mGpsTracker;
    /**
     * Trigger when receives broadcasts from device to check wifi connectivity
     * using connectivity manager
     * <p>
     * Usage : registerBroadcastReceiver() on resume of activity to receive
     * notifications where needed and unregisterBroadcastReceiver() when not
     * needed.
     *
     * @return The connectivity of wifi/mobile carrier connectivity.
     */

    protected BroadcastReceiver mConnectionReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            boolean isWifiConnected = false;
            boolean isMobileConnected = false;

            ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (networkInfo != null)
                isWifiConnected = networkInfo.isConnected();

            networkInfo = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (networkInfo != null)
                isMobileConnected = networkInfo.isConnected();

            Log.d("NETWORK STATUS", "wifi==" + isWifiConnected + " & mobile=="
                    + isMobileConnected);
        }
    };
    public ServiceHelper serviceHelper;
    private boolean isLoading;
    private DockActivity activity;

    @Override
    public void ResponseSuccess(Object result, String Tag) {

    }

    @Override
    public void ResponseFailure(String tag) {

    }

    protected abstract int getLayout();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (DockActivity) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefHelper = new BasePreferenceHelper(getContext());
        getDockActivity().lockDrawer();

        mGpsTracker = new GPSTracker(getDockActivity());

        if (webService == null) {
            webService = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(getDockActivity(), WebServiceConstants.SERVICE_URL);
        }
        if (serviceHelper == null) {
            serviceHelper = new ServiceHelper(this, getDockActivity());
        }
        myDockActivity = getDockActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(getLayout(), container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (prefHelper.getUserType().equals("technician")) {
            getDockActivity().lockDrawer();
        }
        if (getMainActivity() != null && getMainActivity().getWindow().getDecorView() != null) {
            KeyboardHide.hideSoftKeyboard(getMainActivity(), getMainActivity().getWindow().getDecorView());
        }

        //setTitleBar(((MainActivity) getDockActivity()).titleBar);
    }

    @Override
    public void onPause() {
        if (getMainActivity() != null && getMainActivity().getWindow().getDecorView() != null) {
            KeyboardHide.hideSoftKeyboard(getMainActivity(), getMainActivity().getWindow().getDecorView());
        }
        super.onPause();

        if (getDockActivity().getWindow() != null)
            if (getDockActivity().getWindow().getDecorView() != null)
                UIHelper.hideSoftKeyboard(getDockActivity(), getDockActivity()
                        .getWindow().getDecorView());

    }

    protected void createClient() {
        // webService = WebServiceFactory.getInstanceWithBasicGsonConversion();

    }

    public void fragmentResume() {
        setTitleBar((getMainActivity()).titleBar);

    }

    protected void loadingStarted() {

        if (getParentFragment() != null)
            ((LoadingListener) getParentFragment()).onLoadingStarted();
        else
            getDockActivity().onLoadingStarted();

        isLoading = true;
    }

    protected void loadingFinished() {

        if (getParentFragment() != null)
            ((LoadingListener) getParentFragment()).onLoadingFinished();
        else if (getDockActivity() != null)
            getDockActivity().onLoadingFinished();

        isLoading = false;
        // else
        // ( (LoadingListener) super.getParentFragment() ).onLoadingFinished();
    }

    protected DockActivity getDockActivity() {

       /* DockActivity activity = (DockActivity) getActivity();
        while (activity == null) {
            activity = (DockActivity) getActivity();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        return activity;

    }


    protected MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    protected TitleBar getTitleBar() {
        return getMainActivity().titleBar;
    }

    /**
     * This is called in the end to modify titlebar. after all changes.
     *
     * @param
     */
    public void setTitleBar(TitleBar titleBar) {
        titleBar.showTitleBar();
        // titleBar.refreshListener();
    }

    public String getTitleName() {
        return "";
    }

    /**
     * Gets the preferred height for each item in the ListView, in pixels, after
     * accounting for screen density. ImageLoader uses this value to resize
     * thumbnail images to match the ListView item height.
     *
     * @return The preferred height in pixels, based on the current theme.
     */
    protected int getListPreferredItemHeight() {
        final TypedValue typedValue = new TypedValue();

        // Resolve list item preferred height theme attribute into typedValue
        getActivity().getTheme().resolveAttribute(
                android.R.attr.listPreferredItemHeight, typedValue, true);

        // Create a new DisplayMetrics object
        final DisplayMetrics metrics = new android.util.DisplayMetrics();

        // Populate the DisplayMetrics
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);

        // Return theme value based on DisplayMetrics
        return (int) typedValue.getDimension(metrics);
    }

    protected String getStringTrimed(AnyEditTextView edtView) {
        return edtView.getText().toString().trim();
    }

    /**
     * This generic method to add validator to a text view should be used
     * FormEditText
     * <p>
     * Usage : Takes Array of AnyEditTextView ;
     *
     * @return void
     */
    protected void addEmptyStringValidator(AnyEditTextView... allFields) {

        for (AnyEditTextView field : allFields) {
            field.addValidator(new EmptyStringValidator());
        }

    }

    protected void notImplemented() {
        UIHelper.showLongToastInCenter(getActivity(), "Coming Soon");
    }

    protected void serverNotFound() {
        UIHelper.showLongToastInCenter(getActivity(),
                "Unable to connect to the server, "
                        + "are you connected to the internet?");
    }

    protected void finishLoading() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                loadingFinished();
            }
        });
    }

    protected boolean checkLoading() {
        if (isLoading) {
            UIHelper.showLongToastInCenter(getActivity(),
                    R.string.message_wait);
            return false;
        } else {
            return true;
        }

    }

    /**
     * This generic null string validator to be used FormEditText
     * <p>
     * Usage : formEditText.addValicator(new EmptyStringValidator);
     *
     * @return Boolean and setError on respective field.
     */
    protected class EmptyStringValidator extends Validator {

        public EmptyStringValidator() {
            super("The field must not be empty");
        }

        @Override
        public boolean isValid(EditText et) {
            return et.getText().toString().trim().length() >= 1;
        }

    }

}
