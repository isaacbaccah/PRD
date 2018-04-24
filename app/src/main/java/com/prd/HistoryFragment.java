package com.prd;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private TextView request_Name, request_Phone, request_Address, request_Amount;


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        request_Name= view.findViewById(R.id.request_name);
        request_Phone= view.findViewById(R.id.request_phone);
        request_Address= view.findViewById(R.id.request_address);
        request_Amount = view.findViewById(R.id.request_amount);

        Bundle bundle = getArguments();
        request_Name.setText(String.valueOf(bundle.getString("name")));
        request_Phone.setText(String.valueOf(bundle.getString("phone")));
        request_Address.setText(String.valueOf(bundle.getString("address")));
        request_Amount.setText(String.valueOf(bundle.getString("amount")));



        return view;
    }

}
