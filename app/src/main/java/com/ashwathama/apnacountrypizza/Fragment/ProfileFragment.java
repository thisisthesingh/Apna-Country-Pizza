package com.ashwathama.apnacountrypizza.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.ashwathama.apnacountrypizza.AddAddress;
import com.ashwathama.apnacountrypizza.AddressActivity;
import com.ashwathama.apnacountrypizza.ChangeName;
import com.ashwathama.apnacountrypizza.ChangePasswordKnown;
import com.ashwathama.apnacountrypizza.ChangePhone;
import com.ashwathama.apnacountrypizza.LoginActivity;
import com.ashwathama.apnacountrypizza.R;
import com.ashwathama.apnacountrypizza.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import java.util.HashMap;

public class ProfileFragment extends Fragment {

    String name, phone;
    TextView pro_name, pro_phone, change_pass, change_name, change_phone, changeAddress, logout_btn;
    ImageView change_p, change_name_icon, change_phone_icon, change_address_icon, logout_icon;
    LinearLayout logout, change;

    public static final int MANAGE_ADDRESS = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_profile, container,false);
        pro_name = (TextView) v.findViewById(R.id.profile_name);
        pro_phone = (TextView) v.findViewById(R.id.profile_phone);
        logout = (LinearLayout) v.findViewById(R.id.logout_layout);
        logout_btn = (TextView) v.findViewById(R.id.logout_text);
        logout_icon = (ImageView) v.findViewById(R.id.logout_icon);
        change = (LinearLayout) v.findViewById(R.id.change_layout);
        change_pass = (TextView) v.findViewById(R.id.change_btn);
        change_p = (ImageView) v.findViewById(R.id.change_image);
        change_name = (TextView) v.findViewById(R.id.change_name);
        change_phone = (TextView) v.findViewById(R.id.change_phone);
        change_name_icon = (ImageView) v.findViewById(R.id.change_name_icon);
        change_phone_icon = (ImageView) v.findViewById(R.id.change_phone_icon);
        changeAddress = (TextView) v.findViewById(R.id.add_address_btn);
        change_address_icon = (ImageView) v.findViewById(R.id.add_address_icon);
        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
        name = userDetails.get(SessionManager.KEY_FULLNAME);
        phone = userDetails.get(SessionManager.KEY_PHONE);
        pro_name.setText(name);
        pro_phone.setText(phone);

        //Change Name
        change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), ChangeName.class));
                getActivity().finish();
            }
        });

        change_name_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), ChangeName.class));
                getActivity().finish();
            }
        });

        //Change Phone
        change_phone_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), ChangePhone.class));
                getActivity().finish();
            }
        });

        change_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), ChangePhone.class));
                getActivity().finish();
            }
        });

        //Logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
                sessionManager.logoutUserFromSessiom();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
                getActivity().finish();

            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
                sessionManager.logoutUserFromSessiom();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
                getActivity().finish();
            }
        });

        logout_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
                sessionManager.logoutUserFromSessiom();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
                getActivity().finish();
            }
        });

        //Change Password
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), ChangePasswordKnown.class));
                getActivity().finish();
            }
        });

        change_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), ChangePasswordKnown.class));
                getActivity().finish();
            }
        });

        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), ChangePasswordKnown.class));
                getActivity().finish();
            }
        });

        //Manage Address
        changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), AddressActivity.class));
                getActivity().finish();
            }
        });

        //Add Address
        change_address_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), AddAddress.class));
                getActivity().finish();
            }
        });
        return v;

    }

}