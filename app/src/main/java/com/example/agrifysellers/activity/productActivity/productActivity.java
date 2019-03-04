package com.example.agrifysellers.activity.productActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.agrifysellers.R;
import com.example.agrifysellers.activity.MainActivity;
import com.example.agrifysellers.activity.adapter.ProductListAdapter;
import com.example.agrifysellers.activity.model.Seller;
import com.example.agrifysellers.activity.model.Store;
import com.example.agrifysellers.databinding.ActivityProductBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.WriteBatch;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;
import es.dmoral.toasty.Toasty;

public class productActivity extends AppCompatActivity implements StepperFormListener, ProductListAdapter.OnProductSelectedListener {
    public Product product;
    ActivityProductBinding bind;
    ProductListAdapter.OnProductSelectedListener listener;
    Category cat = new Category("select Category");
    ProductbottomsheetFragment productBottomSheetFragment;
    Price price = new Price("enter price ");
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth Auth;
    DocumentReference documentReference;
    DocumentReference userRef;
    WriteBatch batch;
    DocumentReference prodRef;
    String product_id;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INIT();

        Auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        batch = firebaseFirestore.batch();
        userRef = firebaseFirestore.collection("Sellers").document(Auth.getCurrentUser().getUid());

    }

    void INIT() {
        product = new Product("select product", getSupportFragmentManager());
        bind = DataBindingUtil.setContentView(this, R.layout.activity_product);
        listener = this;
        productBottomSheetFragment = new ProductbottomsheetFragment(listener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            bind.productLinearLayout.setBackground(this.getDrawable(R.drawable.store_item_background));//set curve background
        }

        bind.stepperForm.setup(this, cat, product, price).displayBottomNavigation(false)
                .lastStepNextButtonText("start selling").init();

        product.productButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("category", cat.getStepData());
                productBottomSheetFragment.setArguments(bundle);
                productBottomSheetFragment.show(getSupportFragmentManager(), "productlist");


            }
        });
    }
    Seller seller;
    @Override
    public void onCompletedForm() {
         seller = new Seller();
       DocumentReference ref= firebaseFirestore.collection("Sellers").document(Auth.getCurrentUser().getUid());
       ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               seller=task.getResult().toObject(Seller.class);
               seller.setPrice(price.getStepData());
               seller.setUserId(userRef);
               StoreData();
           }
       });





    }
    void StoreData()
    {
        prodRef = firebaseFirestore.collection("store").document(product_id).collection("sellerList").document(Auth.getCurrentUser().getUid());
        batch.set(prodRef, seller);
        final Map<String, DocumentReference> user_product_info = new HashMap<>();
        user_product_info.put("id", prodRef);
        userRef = firebaseFirestore.collection("Sellers").document(Auth.getCurrentUser().getUid()).collection("productList").document(product_id);
        batch.set(userRef, user_product_info);
        stateLoading(true);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                stateLoading(false);
                if (task.isSuccessful()) {
                    Toasty.success(getApplicationContext(), "product added", Toasty.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });
    }

    @Override
    public void onCancelledForm() {

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("name", product.getStepData());
        savedInstanceState.putString("category", cat.getStepData());
        savedInstanceState.putFloat("price", price.getStepData());

        // IMPORTANT: The call to the super method must be here at the end.
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey("name")) {
            String name = savedInstanceState.getString("name");
            product.restoreStepData(name);
        }

        if (savedInstanceState.containsKey("category")) {
            String category = savedInstanceState.getString("category");
            cat.restoreStepData(category);
        }

        if (savedInstanceState.containsKey("price")) {
            float pric = savedInstanceState.getFloat("price");
            price.restoreStepData(pric);
        }

        // IMPORTANT: The call to the super method must be here at the end.
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onProductSelected(DocumentSnapshot store) {
        Store mStore = store.toObject(Store.class);
        product_id = store.getId();
        firebaseFirestore.collection("Sellers").document(Auth.getCurrentUser().getUid()).collection("productList").document(product_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().exists()) {               //product is not there
                        productBottomSheetFragment.dismiss();
                        product.productButton.setText(mStore.getName());
                    } else {
                        Toasty.error(getApplicationContext(), store.getString("name") + " already you are selling", Toasty.LENGTH_SHORT).show();
                    }
                    product.markAsCompletedOrUncompleted(true);
                }
            }
        });


    }

    private void stateLoading(boolean stateStatus) {
        if (stateStatus) {
            bind.loaderLayout.setVisibility(View.GONE);
            bind.loaderProgressBar.setVisibility(View.VISIBLE);
        } else {
            bind.loaderLayout.setVisibility(View.VISIBLE);
            bind.loaderProgressBar.setVisibility(View.GONE);
        }

    }
}
