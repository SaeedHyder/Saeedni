package com.ingic.saeedni.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ingic.saeedni.R;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.WebServiceConstants;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.ui.views.AnyEditTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ForgotNewPasswordFragment extends BaseFragment {
    @BindView(R.id.edtNewPassword)
    AnyEditTextView edtNewPassword;
    @BindView(R.id.edtConfirmPassword)
    AnyEditTextView edtConfirmPassword;
    @BindView(R.id.parent)
    LinearLayout parent;


    public static ForgotNewPasswordFragment newInstance() {
        Bundle args = new Bundle();

        ForgotNewPasswordFragment fragment = new ForgotNewPasswordFragment();
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
        return R.layout.fragment_forgot_new_password;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        parent.setLayoutDirection(prefHelper.isLanguageArabic() ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);

    }


    public boolean isvalidated() {


        if (edtNewPassword.getText().toString().length() >= 6) {
            if (edtConfirmPassword.getText().toString().length() >= 6) {
                if (edtNewPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
                    return true;
                } else {
                    UIHelper.showShortToastInCenter(getMainActivity(), getString(R.string.password_not_equal));
                }
            } else {
                edtConfirmPassword.setError(getString(R.string.password_length));
            }
        } else {
            edtNewPassword.setError(getString(R.string.password_length));
        }


        return false;
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.FORGOT_NEW_PASSWORD:
                prefHelper.setLoginStatus(true);
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(UserHomeFragment.newInstance(), "UserHomeFragment");
                break;
        }
    }


    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if (isvalidated()) {
            serviceHelper.enqueueCall(webService.forgotNewPassword(prefHelper.getUserId(),
                    edtNewPassword.getText().toString(), edtConfirmPassword.getText().toString()), WebServiceConstants.FORGOT_NEW_PASSWORD);
        }
    }


}