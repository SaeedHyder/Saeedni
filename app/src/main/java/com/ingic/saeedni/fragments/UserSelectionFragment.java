package com.ingic.saeedni.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.ingic.saeedni.R;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.ui.views.TitleBar;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 5/22/2017.
 */


public class UserSelectionFragment extends BaseFragment implements View.OnClickListener{
    @BindView(R.id.usercontainer)
    LinearLayout usercontainer;
    @BindView(R.id.techniciancontainer)
    LinearLayout techniciancontainer;
    @BindView(R.id.cb_english)
    RadioButton cb_english;
    @BindView(R.id.cb_arabic)
    RadioButton cb_arabic;



    public static UserSelectionFragment newInstance() {
        return new UserSelectionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_userselection;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(prefHelper.isLanguageArabic())
        {
            cb_arabic.setChecked(true);
        }
        else
        {
            cb_english.setChecked(true);
        }
        setlistener();
    }

    private void setlistener() {
        usercontainer.setOnClickListener(this);
        techniciancontainer.setOnClickListener(this);

       cb_english.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               prefHelper.putLang(getDockActivity(),"en");
           }
       });
      cb_arabic.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              prefHelper.putLang(getDockActivity(),"ar");
          }
      });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
     ButterKnife.bind(this, Objects.requireNonNull(rootView));
        return rootView;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.usercontainer:
                prefHelper.setUserType("user");
                getDockActivity().replaceDockableFragment(UserSignupFragment.newInstance(),"UserSignUp Fragment");
                break;
            case R.id.techniciancontainer:
                prefHelper.setUserType("technician");
                getDockActivity().replaceDockableFragment(LoginFragment.newInstance(),"Login Fragment");
                break;
        }
    }
}
