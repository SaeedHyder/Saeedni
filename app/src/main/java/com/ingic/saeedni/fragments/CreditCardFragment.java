package com.ingic.saeedni.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.ingic.saeedni.R;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;
import com.ingic.saeedni.helpers.DatePickerHelper;
import com.ingic.saeedni.helpers.UIHelper;
import com.ingic.saeedni.ui.views.AnyEditTextView;
import com.ingic.saeedni.ui.views.AnyTextView;
import com.ingic.saeedni.ui.views.TitleBar;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 5/23/2017.
 */

public class CreditCardFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.edt_cc_number)
    AnyEditTextView edtCcNumber;
    @BindView(R.id.edt_cc_expiredate)
    AnyTextView edtCcExpiredate;
    @BindView(R.id.edt_cc_cvv)
    AnyEditTextView edtCcCvv;
    @BindView(R.id.edt_cc_name)
    AnyEditTextView edtCcName;
    @BindView(R.id.btn_update)
    Button btnupdate;



    public static CreditCardFragment newInstance() {
        return new CreditCardFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_credit_card;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListener();
    }

    private void setListener() {
        edtCcExpiredate.setOnClickListener(this);
        btnupdate.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
       ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_update:
                if (validate()){
                    getDockActivity().popFragment();
                }
                break;
            case R.id.edt_cc_expiredate:
                initDatePicker(edtCcExpiredate);
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.add_payment));
    }

    private void initDatePicker(final TextView textView){
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
                            UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.date_before_error));
                        } else {
                            textView.setText(datePickerHelper.getStringDate(year, month, dayOfMonth));
                        }
                    }
                },"PreferredDate");

        datePickerHelper.showDate();
    }
    private boolean validate() {
        if (edtCcNumber.getText().toString().isEmpty()){
            edtCcNumber.setError(getString(R.string.enter_cc_number));
        return false;
        }else if (edtCcNumber.getText().toString().length()<16){
            edtCcNumber.setError(getString(R.string.cc_valid_error));
            return false;
        }
        else if (edtCcExpiredate.getText().toString().isEmpty()){
            edtCcExpiredate.setError(getString(R.string.expiredate_error));
            return false;
        }
        else if (edtCcCvv.getText().toString().isEmpty()){
            edtCcCvv.setError(getString(R.string.cvv_error));
            return false;
        }
        else if (edtCcName.getText().toString().isEmpty()){
            edtCcName.setError(getString(R.string.cc_name_error));
            return false;
        }
        else {
            return true;
        }
    }
}
