package com.example.eti2menf.callstop.bases;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.example.eti2menf.callstop.R;

import butterknife.ButterKnife;

/**
 * Created by ETI2MENF on 17/10/2017.
 */

public class BaseFragment extends Fragment {
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public Snackbar snackbar(View view, String mensaje) {
        Snackbar snackbar = Snackbar
                .make(view, mensaje, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getActivity().getResources().getDimension(R.dimen.snackbar_textsize));
        textView.setTextColor(Color.WHITE);
        textView.setMaxLines(3);
        return snackbar;
    }
}
