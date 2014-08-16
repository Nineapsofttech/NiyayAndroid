package com.niyay.reader;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by APPLE on 2/8/14.
 */

public class Category extends Spinner {
    OnItemSelectedListener listener;

    public Category(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (listener != null)
            if (Config.getInstance().category != position)
                listener.onItemSelected(null, null, position, 0);
    }

    public void setOnItemSelectedEvenIfUnchangedListener(
            OnItemSelectedListener listener) {
        this.listener = listener;
    }
}