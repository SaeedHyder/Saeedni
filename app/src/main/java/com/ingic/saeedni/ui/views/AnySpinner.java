package com.ingic.saeedni.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created on 6/14/2017.
 */

public class AnySpinner extends Spinner {
    OnItemSelectedListener listener;

    public AnySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnySpinner(Context context) {
        super(context);

    }


    public AnySpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (listener != null)
            listener.onItemSelected(this, getSelectedView(), position, getSelectedItemId());
    }

    public void setOnItemSelectedEvenIfUnchangedListener(
            OnItemSelectedListener listener) {
        this.listener = listener;
    }
}

