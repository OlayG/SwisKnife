package com.example.admin.swisknife;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlertDialogTasksFragment extends Fragment {


    @BindView(R.id.btnBasic)
    Button btnBasic;
    @BindView(R.id.btnCustom)
    Button btnCustom;
    Unbinder unbinder;
    @BindView(R.id.tvBodyParts)
    TextView tvBodyParts;

    CheckBox cbHead, cbShoulders, cbKnees, cbToes;

    public AlertDialogTasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alert_dialog_tasks, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogFragment alert = new AlertDialogFragment();
                alert.show(getFragmentManager(), "Alert Dialog Fragment");
            }
        });

        btnCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomFragment(v);
            }
        });
    }

    private void showCustomFragment(View v) {
        final Dialog cusDialog = new Dialog(v.getContext());
        cusDialog.setTitle("Select Body Parts");
        cusDialog.setContentView(R.layout.custom_dialog);
        cbHead = (CheckBox) cusDialog.findViewById(R.id.cbHead);
        cbShoulders = (CheckBox) cusDialog.findViewById(R.id.cbShoulders);
        cbKnees = (CheckBox) cusDialog.findViewById(R.id.cbKnees);
        cbToes = (CheckBox) cusDialog.findViewById(R.id.cbToes);

        Button btnDisplay = (Button) cusDialog.findViewById(R.id.btnDisplay);
        btnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer result = new StringBuffer();
                result.append("Head check : ").append(cbHead.isChecked());
                result.append("\nShoulder check : ").append(cbShoulders.isChecked());
                result.append("\nKnees check :").append(cbKnees.isChecked());
                result.append("\nToes check :").append(cbToes.isChecked());

                tvBodyParts.setText(result);
                cusDialog.dismiss();
            }
        });
        cusDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

