package com.ingic.saeedni.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hbb20.CountryCodePicker;
import com.ingic.saeedni.R;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.WebServiceConstants;
import com.ingic.saeedni.ui.views.AnyEditTextView;
import com.ingic.saeedni.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UserChangePhoneFragment extends BaseFragment {
    @BindView(R.id.edtpassword)
    AnyEditTextView edtpassword;
    @BindView(R.id.oldCountrypicker)
    CountryCodePicker oldCountrypicker;
    @BindView(R.id.edtOldNumber)
    AnyEditTextView edtOldNumber;
    @BindView(R.id.Countrypicker)
    CountryCodePicker Countrypicker;
    @BindView(R.id.edtnumber)
    AnyEditTextView edtnumber;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    Unbinder unbinder;
    @BindView(R.id.parent)
    LinearLayout parent;
    int oldSelectedCountryCode = 0;
    int newSelectedCountryCode = 0;
    @BindView(R.id.containerOldPhone)
    RelativeLayout containerOldPhone;
    @BindView(R.id.containerNewPhone)
    RelativeLayout containerNewPhone;

    private boolean shouldEnterNewPhoneNumber = false;

    public static UserChangePhoneFragment newInstance(boolean shouldEnterNewPhoneNumber) {
        Bundle args = new Bundle();

        UserChangePhoneFragment fragment = new UserChangePhoneFragment();
        fragment.setArguments(args);
        fragment.shouldEnterNewPhoneNumber = shouldEnterNewPhoneNumber;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_change_phone;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parent.setLayoutDirection(prefHelper.isLanguageArabic() ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        oldCountrypicker.registerCarrierNumberEditText(edtOldNumber);
        Countrypicker.registerCarrierNumberEditText(edtnumber);
        oldCountrypicker.setOnCountryChangeListener(() -> {
            oldSelectedCountryCode = oldCountrypicker.getSelectedCountryCodeAsInt();
        });
        Countrypicker.setOnCountryChangeListener(() -> {
            newSelectedCountryCode = Countrypicker.getSelectedCountryCodeAsInt();
        });
        oldCountrypicker.setCountryForPhoneCode(oldSelectedCountryCode);
        Countrypicker.setCountryForPhoneCode(newSelectedCountryCode);

        if (shouldEnterNewPhoneNumber) {
            containerOldPhone.setVisibility(View.GONE);
            containerNewPhone.setVisibility(View.VISIBLE);
        } else {
            containerOldPhone.setVisibility(View.VISIBLE);
            containerNewPhone.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    private boolean isValidated() {
       /* if (edtpassword.getText() == null || (edtpassword.getText().toString().isEmpty()) || edtpassword.getText().toString().length() < 6) {
            edtpassword.setError(getDockActivity().getResources().getString(R.string.valid_password));
            return false;
        } else*/
        if (!shouldEnterNewPhoneNumber && !oldCountrypicker.isValidFullNumber()) {
            edtOldNumber.setError(getDockActivity().getResources().getString(R.string.enter_valid_number_error));
            return false;
        } else if (shouldEnterNewPhoneNumber && !Countrypicker.isValidFullNumber()) {
            edtnumber.setError(getDockActivity().getResources().getString(R.string.enter_valid_number_error));
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.changePhoneNumber));
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.CHANGE_PHONE_NUMBER:
                getDockActivity().replaceDockableFragment(PhoneVerificationFragment.newInstance(shouldEnterNewPhoneNumber), PhoneVerificationFragment.class.getSimpleName());
                break;
        }
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if (isValidated()) {
            if (shouldEnterNewPhoneNumber) {
                serviceHelper.enqueueCall(webService.checkNewPhoneNumber(prefHelper.getUserId(), Countrypicker.getFullNumberWithPlus()),
                        WebServiceConstants.CHANGE_PHONE_NUMBER);
            } else {
                serviceHelper.enqueueCall(webService.checkOldPhoneNumber(prefHelper.getUserId(), oldCountrypicker.getFullNumberWithPlus()),
                        WebServiceConstants.CHANGE_PHONE_NUMBER);
            }
           /* serviceHelper.enqueueCall(webService.changePhoneNumber(prefHelper.getUserId(),
                    edtpassword.getText().toString(),
                    oldCountrypicker.getFullNumberWithPlus(),
                    Countrypicker.getFullNumberWithPlus()),
                    WebServiceConstants.CHANGE_PHONE_NUMBER);*/
        }
    }
}