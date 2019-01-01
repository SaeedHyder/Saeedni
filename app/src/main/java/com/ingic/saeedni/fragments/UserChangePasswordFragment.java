package com.ingic.saeedni.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ingic.saeedni.R;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.WebServiceConstants;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.ui.views.AnyEditTextView;
import com.ingic.saeedni.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UserChangePasswordFragment extends BaseFragment {
    @BindView(R.id.img_password)
    ImageView imgPassword;
    @BindView(R.id.edtpassword)
    AnyEditTextView edtpassword;
    @BindView(R.id.img_newpassword)
    ImageView imgNewpassword;
    @BindView(R.id.edtNewPassword)
    AnyEditTextView edtNewPassword;
    @BindView(R.id.img_Confirmpassword)
    ImageView imgConfirmpassword;
    @BindView(R.id.edtConfirmPassword)
    AnyEditTextView edtConfirmPassword;
    @BindView(R.id.ll_textFields)
    LinearLayout llTextFields;
    @BindView(R.id.btn_editcard)
    Button btnEditcard;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    Unbinder unbinder;
    @BindView(R.id.parent)
    LinearLayout parent;

    public static UserChangePasswordFragment newInstance() {
        Bundle args = new Bundle();

        UserChangePasswordFragment fragment = new UserChangePasswordFragment();
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
        return R.layout.fragment_change_password;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parent.setLayoutDirection(prefHelper.isLanguageArabic() ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    public boolean isvalidated() {

        if (edtpassword.getText().toString().length() >= 6) {
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
        } else {
            edtpassword.setError(getString(R.string.password_length));
        }

        return false;
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
        titleBar.setSubHeading(getString(R.string.change_password));
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.CHANGE_PASSWORD:
                UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.passwordSuccessMessage));
                if (prefHelper.getUserType().equals("technician")) {
                    getDockActivity().popBackStackTillEntry(0);
                    getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "UserHomeFragment");
                } else {
                    getDockActivity().popBackStackTillEntry(0);
                    getDockActivity().replaceDockableFragment(UserHomeFragment.newInstance(), "UserHomeFragment");
                }
                break;
        }
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if (isvalidated()) {
            serviceHelper.enqueueCall(webService.changePassword(prefHelper.getUserId(), edtpassword.getText().toString(), edtNewPassword.getText().toString(), edtConfirmPassword.getText().toString()), WebServiceConstants.CHANGE_PASSWORD);
        }
    }

}