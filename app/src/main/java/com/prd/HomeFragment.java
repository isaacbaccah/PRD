package com.prd;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private EditText requestName;
    private EditText requestPhone;
    private EditText requestAddress;
    private EditText requestAmount;
    private Button requestBtn;
    private ProgressBar requestProgress;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private String namerequest, phonerequest, addressrequest, amountrequest;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        requestProgress = view.findViewById(R.id.request_progress);
        requestName = view.findViewById(R.id.request_name);
        requestPhone = view.findViewById(R.id.request_phone);
        requestAddress = view.findViewById(R.id.request_address);
        requestAmount = view.findViewById(R.id.request_amount);
        requestBtn = view.findViewById(R.id.request_btn);
        requestBtn.setOnClickListener(mClickListener);


        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String namerequest = requestName.getText().toString();
                String phonerequest = requestPhone.getText().toString();
                String addressrequest = requestAddress.getText().toString();
                String amountrequest = requestAmount.getText().toString();



                if (TextUtils.isEmpty(namerequest) && TextUtils.isEmpty(phonerequest) && TextUtils.isEmpty(addressrequest) && TextUtils.isEmpty(amountrequest)) {
                    Toast.makeText(getActivity(), "Please fill out all Input Fields", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(namerequest) && TextUtils.isEmpty(phonerequest) && TextUtils.isEmpty(addressrequest)) {
                    Toast.makeText(getActivity(), "Please fill out all Input Fields", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(namerequest) && TextUtils.isEmpty(phonerequest)) {
                    Toast.makeText(getActivity(), "Please fill out all Input Fields", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(namerequest)) {
                    Toast.makeText(getActivity(), "Please fill out all Input Fields", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(amountrequest)) {
                    Toast.makeText(getActivity(), "Please fill out all Input Fields", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(addressrequest)) {
                    Toast.makeText(getActivity(), "Please fill out all Input Fields", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(phonerequest)) {
                    Toast.makeText(getActivity(), "Please fill out all Input Fields", Toast.LENGTH_LONG).show();
                    return;
                }
                requestProgress.setVisibility(View.VISIBLE);

                Map<String, String> userMap = new HashMap<>();
                userMap.put("name", namerequest);
                userMap.put("phone", phonerequest);
                userMap.put("address", addressrequest);
                userMap.put("amount", amountrequest);



                firebaseFirestore.collection("requests").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Intent toastIntent = new Intent(getActivity(), OrderConfirmationActivity.class);
                        startActivity(toastIntent);

                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        String error = e.getMessage();

                        Toast.makeText(getActivity(), "Error : " + error, Toast.LENGTH_LONG).show();

                        requestProgress.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });


        return view;
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            namerequest = requestName.getText().toString();
            phonerequest = requestPhone.getText().toString();
            addressrequest = requestAddress.getText().toString();
            amountrequest = requestAmount.getText().toString();

            FragmentTransaction transection=getFragmentManager().beginTransaction();
            HistoryFragment hfragment = new HistoryFragment();

//using Bundle to send data
            Bundle bundle = new Bundle();
            bundle.putString("name", namerequest);
            bundle.putString("phone", phonerequest);
            bundle.putString("address", addressrequest);
            bundle.putString("amount", amountrequest);

            hfragment.setArguments(bundle); //data being send to SecondFragment
            transection.replace(R.id.main_container, hfragment);
            transection.commit();
        }
    };


}
