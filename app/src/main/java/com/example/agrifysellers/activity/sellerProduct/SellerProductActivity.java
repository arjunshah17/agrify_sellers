package com.example.agrifysellers.activity.sellerProduct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.agrifysellers.R;
import com.example.agrifysellers.databinding.ActivitySellerProductBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;



public class SellerProductActivity extends AppCompatActivity {
String seller_id;
String product_id;
FirebaseFirestore firebaseFirestore;
ActivitySellerProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_seller_product);
       if(getIntent().getStringExtra("seller_id")!=null && getIntent().getStringExtra("product_id")!=null)
       {
           product_id=getIntent().getStringExtra("product_id");
         seller_id=  getIntent().getStringExtra("seller_id");
Log.i("product_id",product_id);
Log.i("seller_id",seller_id);
       }
       firebaseFirestore=FirebaseFirestore.getInstance();
     //   binding.imageSlider.setIndicatorAnimation(SliderLayout.A); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.imageSlider.setScrollTimeInSec(5);
        initSlider();

    }

    private void initSlider() {

        firebaseFirestore.collection("store").document(product_id).collection("sellerList").document(seller_id).collection("productImage").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                DefaultSliderView sliderView = new DefaultSliderView(getApplicationContext());
                                sliderView.setImageUrl(document.getString("url"));
                                sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                binding.imageSlider.addSliderView(sliderView);
                            }

                        } else {

                        }

                    }
                });



    }
}
