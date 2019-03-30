package com.example.agrifysellers.activity.address;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.agrifysellers.R;
import com.example.agrifysellers.databinding.ActivityAddressBinding;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.button.MaterialButton;

public class addressActivity extends AppCompatActivity {

    int PLACE_PICKER_REQUEST = 1;

    ActivityAddressBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_address);
        bind = DataBindingUtil.setContentView(this, R.layout.activity_address);
        bind.addressButton.setOnClickListener(v -> {
            try {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                startActivityForResult(builder.build(addressActivity.this), PLACE_PICKER_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String Name = String.format("%s", place.getName());
                bind.Placename.setText(Name);
                String Address = String.format("%s", place.getAddress(), "\n" + place.getLatLng().latitude, "\n" + place.getLatLng().longitude);
                bind.PlaceAdress.setText(Address);
            }
        }
    }
}
