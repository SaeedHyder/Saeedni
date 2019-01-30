package com.ingic.saeedni.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ingic.saeedni.R;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.WebServiceConstants;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.ui.views.PinEntryEditText;
import com.ingic.saeedni.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ForgotPasswordVerifcationFragment extends BaseFragment {
    @BindView(R.id.txt_pin_entry)
    PinEntryEditText txtPinEntry;
    @BindView(R.id.btn_submit_code)
    Button btnSubmitCode;
    Unbinder unbinder;

    public static ForgotPasswordVerifcationFragment newInstance() {
        Bundle args = new Bundle();

        ForgotPasswordVerifcationFragment fragment = new ForgotPasswordVerifcationFragment();
        fragment.setArguments(args);
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
        return R.layout.fragment_entry_code;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.verification));
    }
    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.VERIFY_FORGOT_PASSWORD:
                getDockActivity().replaceDockableFragment(ForgotNewPasswordFragment.newInstance(), ForgotNewPasswordFragment.class.getSimpleName());
                break;
        }
    }

    @OnClick(R.id.btn_submit_code)
    public void onViewClicked() {
        if (!(txtPinEntry.getText().toString().equals("") && txtPinEntry.getText().toString().length() < 4)) {
            serviceHelper.enqueueCall(webService.forgotVerifyCode(prefHelper.getUserId(), txtPinEntry.getText().toString()), WebServiceConstants.VERIFY_FORGOT_PASSWORD);
        } else {
            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.valid_code_error));
        }
    }
}