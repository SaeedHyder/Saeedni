package com.ingic.saeedni.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.RegistrationResultEnt;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.global.WebServiceConstants;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.ingic.saeedni.ui.views.TitleBar;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ContactUsFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.txtPhone)
    AnyTextView txtPhone;
    @BindView(R.id.txEmail)
    AnyTextView txEmail;
    @BindView(R.id.txtAddress)
    AnyTextView txtAddress;
    @BindView(R.id.parent)
    ConstraintLayout parent;
    Unbinder unbinder1;

    public static ContactUsFragment newInstance() {
        Bundle args = new Bundle();

        ContactUsFragment fragment = new ContactUsFragment();
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
        return R.layout.fragment_contact_us;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        parent.setLayoutDirection(prefHelper.isLanguageArabic() ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        serviceHelper.enqueueCall(webService.getAdminDetails(), WebServiceConstants.GET_ADMIN_DETAILS);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.contactUs));
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_ADMIN_DETAILS:
                RegistrationResultEnt mAdminModel = (RegistrationResultEnt) result;
                txtAddress.setText(mAdminModel.getAddress() + "");
                txtPhone.setText(mAdminModel.getPhoneNo() + "");
                txEmail.setText(mAdminModel.getEmail() + "");
                break;
        }
    }

    private void openEmail(String email) {
        try {
            final Intent emailIntent = new Intent(Intent.ACTION_SEND);

            /* Fill it with Data */
            emailIntent.setType("plain/text");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
            emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.write_your_comments));

            /* Send it off to the Activity-Chooser */
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (Exception e) {
            e.printStackTrace();
            UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.something_error));
        }
    }

    private void openPhone(String number) {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.CALL_PHONE)
                .onGranted(permissions -> {
                    try {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + number));
                        startActivity(callIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.something_error));
                    }
                    // CameraHelper.uploadPhotoDialog(getMainActivity());

                })
                .onDenied(permissions -> {
                    UIHelper.showShortToastInCenter(getDockActivity(), "Permission is required to access this feature");
                })
                .start();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.txtPhone, R.id.txEmail, R.id.txtAddress})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txtPhone:
                openPhone(txtPhone.getText().toString());
                break;
            case R.id.txEmail:
                openEmail(txEmail.getText().toString());
                break;
            case R.id.txtAddress:
                break;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }
}