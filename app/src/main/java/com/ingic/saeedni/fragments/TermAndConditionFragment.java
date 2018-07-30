package com.ingic.saeedni.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ingic.saeedni.R;
import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.entities.StaticPageEnt;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.helpers.InternetHelper;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 5/23/2017.
 */

public class TermAndConditionFragment extends BaseFragment {
    @BindView(R.id.txt_term_condition)
    TextView txtTermCondition;
    @BindView(R.id.txt_line)
    View txtLine;
    @BindView(R.id.chk_read)
    CheckBox chkRead;


    public static TermAndConditionFragment newInstance() {
        return new TermAndConditionFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            bindTextview();
        }
        chkRead.setChecked(prefHelper.istermsCheck());
        chkRead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefHelper.setTermsCheck(isChecked);
            }
        });

    }

    private void bindTextview() {
        Call<ResponseWrapper<StaticPageEnt>> call = webService.getTermandAbout(prefHelper.getUserId(), "term");
        call.enqueue(new Callback<ResponseWrapper<StaticPageEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<StaticPageEnt>> call, Response<ResponseWrapper<StaticPageEnt>> response) {
                if (response.body().getResponse().equals("2000")) {
                    settitle(response.body().getResult().getBody());
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<StaticPageEnt>> call, Throwable t) {
                Log.e("TermAndCondition", t.toString());
                //  UIHelper.showShortToastInCenter(getDockActivity(), t.toString());
            }
        });

    }

    private void settitle(String response) {
        if (getMainActivity() != null && getMainActivity().titleBar != null) {
            getMainActivity().titleBar.setSubHeading(getString(R.string.terms_conditons));
            getMainActivity().titleBar.invalidate();
        }
        txtTermCondition.setText(response);
        txtTermCondition.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_term_condition;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.terms_conditons));
    }


}
