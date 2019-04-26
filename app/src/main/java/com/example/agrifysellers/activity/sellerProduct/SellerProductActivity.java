package com.example.agrifysellers.activity.sellerProduct;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.agrifysellers.R;
import com.example.agrifysellers.activity.GlideApp;
import com.example.agrifysellers.activity.Utils.RatingUtils;
import com.example.agrifysellers.activity.model.Seller;
import com.example.agrifysellers.activity.model.Store;
import com.example.agrifysellers.activity.order.adapter.RatingAdapter;
import com.example.agrifysellers.activity.order.model.Rating;
import com.example.agrifysellers.databinding.ActivitySellerProductBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.vincent.blurdialog.BlurDialog;

import java.util.ArrayList;

import javax.annotation.Nullable;


public class SellerProductActivity extends AppCompatActivity implements EventListener<DocumentSnapshot> {
    String seller_id;
    String product_id;
    String TAG = "SellerProductActivity";
    FirebaseFirestore firebaseFirestore;
    ActivitySellerProductBinding binding;
    RatingAdapter adapter;
    Query query;
    WriteBatch cartBatch;
    WriteBatch clearBatch;
    FirebaseAuth auth;
    BlurDialog blurDialog;
    Seller seller;

    boolean isOutOfStock = false;
    String unit, name;
    Store store;
    private DocumentReference mSellerProductRef;
    private ListenerRegistration sellerProductListener;
    private ListenerRegistration productListener;
    private DocumentReference productRef;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_seller_product);
        if (getIntent().getStringExtra("seller_id") != null && getIntent().getStringExtra("product_id") != null) {
            product_id = getIntent().getStringExtra("product_id");
            seller_id = getIntent().getStringExtra("seller_id");
            Log.i("product_id", product_id);
            Log.i("seller_id", seller_id);
        }

        store = new Store();

        auth = FirebaseAuth.getInstance();

        seller = new Seller();
        setSupportActionBar(binding.appBar);

        firebaseFirestore = FirebaseFirestore.getInstance();
        cartBatch = firebaseFirestore.batch();
        clearBatch = firebaseFirestore.batch();

        loadProductData(product_id, seller_id);
        initSlider(product_id, seller_id);
        //   binding.imageSlider.setIndicatorAnimation(SliderLayout.A); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.imageSlider.setScrollTimeInSec(5);

        binding.appBar.setNavigationOnClickListener(v -> {
            onBackPressed();

        });
        initRatingRecycleView();



    }



    void loadProductData(String product_id, String seller_id) {
        productListener = null;
        sellerProductListener = null;

        mSellerProductRef = firebaseFirestore.collection("Sellers").document(seller_id).collection("productList").document(product_id);
        productRef = firebaseFirestore.collection("store").document(product_id);
        productLoading(true);
        productListener = productRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                productLoader(snapshot);

            }
        });
        sellerProductListener = mSellerProductRef.addSnapshotListener(this);
    }

    private void initRatingRecycleView() {
        query = firebaseFirestore.collection("Sellers").document(seller_id).collection("productList").document(product_id).collection("ratingList").orderBy("timestamp", Query.Direction.DESCENDING);
        adapter = new RatingAdapter(query, this) {

            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                super.onEvent(documentSnapshots, e);
                ArrayList<Rating> ratings = new ArrayList<Rating>();
                for (int i = 0; i < getItemCount(); i++) {
                    ratings.add(adapter.getRating(i));

                }
                binding.rattingCard.Rating.setRating((float) RatingUtils.getAverageRating(ratings));
                binding.rattingCard.NumRatings.setText("(" + String.valueOf(getItemCount()) + ")");

            }
        };
        binding.rattingCard.ratingListRv.setLayoutManager(new LinearLayoutManager(this));
        binding.rattingCard.ratingListRv.setAdapter(adapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }

    }

    private void initSlider(String product_id, String seller_id) {
        binding.imageSlider.clearSliderViews();
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

    @Override
    protected void onStart() {
        super.onStart();

        if (adapter != null) {
            adapter.startListening();
        }


    }

    private void productLoader(DocumentSnapshot snapshot) {
        store = snapshot.toObject(Store.class);
        binding.productName.setText(store.getName());
        unit = store.getUnit();
        name = store.getName();

    }

    @Override
    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            return;
        }
        productLoading(false);

        sellerProductLoader(snapshot);


    }

    private void sellerProductLoader(DocumentSnapshot snapshot) {
        try {


            seller = snapshot.toObject(Seller.class);

            binding.minQuantity.setText("min quantity:" + seller.getMinQuantity() + "/" + unit);
            binding.maxQuantity.setText("mxn quantity:" + seller.getMaxQuantity() + "/" + unit);
            binding.textInfo.setText(seller.getInfo());
            binding.appBar.setTitle(seller.getName());
            binding.sellerPrice.setText(seller.getPrice() + "/" + unit);

            initSellerDetails();

        } catch (Exception ex) {
            ex.printStackTrace();
            onBackPressed();
        }


    }








    void productLoading(boolean state) {
        if (state) {
            binding.progressBarLayout.progressBarLoader.setVisibility(View.VISIBLE);
            binding.mainLayout.setVisibility(View.GONE);
        } else {
            binding.progressBarLayout.progressBarLoader.setVisibility(View.GONE);
            binding.mainLayout.setVisibility(View.VISIBLE);
        }
    }



    void initSellerDetails() {
        binding.rattingCard.userName.setText(seller.getName());

        try {


            GlideApp.with(this)
                    .load(seller.getProfilePhotoUrl())
                    .into(binding.rattingCard.profilePhoto);
        } catch (Exception ex) {

        }
        binding.rattingCard.location.setOnClickListener(v -> {
            SellerAddressFragment sellerAddressFragment = new SellerAddressFragment(seller.getAddressRef());
            sellerAddressFragment.show(getSupportFragmentManager(), "SellerProductActivity");
        });
        binding.rattingCard.call.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + seller.getPhone()));
            try {


                startActivity(callIntent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });
        binding.rattingCard.email.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + seller.getEmail()));
            try {


                startActivity(emailIntent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });
    }
}
