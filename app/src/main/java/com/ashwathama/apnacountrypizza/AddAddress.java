package com.ashwathama.apnacountrypizza;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class AddAddress extends AppCompatActivity {

    TextInputLayout city, area, house_no, pincode, state, landmark, alternate_phone;
    String _city, _area, _house_no, _pincode, _state, _landmark, _alternate_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);


//
//        //Hooks
//        city = findViewById(R.id.city);
//        area = findViewById(R.id.area);
//        house_no = findViewById(R.id.house_no);
//        pincode = findViewById(R.id.pincode);
//        state = findViewById(R.id.state);
//        landmark = findViewById(R.id.landmark);
//        alternate_phone = findViewById(R.id.alternate_mobile);
//
//        //Strings
//        _city = city.getEditText().getText().toString().trim();
//        _area = area.getEditText().getText().toString().trim();
//        _house_no = house_no.getEditText().getText().toString().trim();
//        _pincode = pincode.getEditText().getText().toString().trim();
//        _state = state.getEditText().getText().toString().trim();
//        _landmark = landmark.getEditText().getText().toString().trim();
//        _alternate_phone = alternate_phone.getEditText().getText().toString().trim();

    }
}