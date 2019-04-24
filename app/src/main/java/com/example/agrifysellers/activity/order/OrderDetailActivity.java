package com.example.agrifysellers.activity.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.agrifysellers.R;
import com.example.agrifysellers.activity.GlideApp;
import com.example.agrifysellers.activity.model.Seller;
import com.example.agrifysellers.activity.model.User;
import com.example.agrifysellers.activity.order.adapter.OrderItemAdapter;
import com.example.agrifysellers.activity.order.model.Order;
import com.example.agrifysellers.activity.order.model.OrderItem;
import com.example.agrifysellers.databinding.ActivityOrderDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.annotation.Nullable;

public class OrderDetailActivity extends AppCompatActivity{
    Query query;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;

    String orderId;
    OrderItemAdapter orderItemAdapter;
    ActivityOrderDetailBinding binding;

    Order order;
    Seller seller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.orderStatus_spinner, android.R.layout.simple_spinner_item);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        binding.orderStatus.setAdapter(adapter);
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        binding.appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (getIntent().getStringExtra("orderId") != null) {
            orderId = getIntent().getStringExtra("orderId");
        }
        InitUi();
        initCartRecycleView();
        setSupportActionBar(binding.appBar);


    }

    private void initCartRecycleView() {
        query = firebaseFirestore.collection("Sellers").document(auth.getUid()).collection("orderList").document(orderId).collection("orderList");
        orderItemAdapter = new OrderItemAdapter(query, this) {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                super.onEvent(documentSnapshots, e);


                if (getItemCount() == 0) {
                    noProductFound(true);
                } else {
                    noProductFound(false);
                }


            }

            @Override
            protected void onDocumentModified(DocumentChange change) {
                super.onDocumentModified(change);
            }
        };


        binding.orderRecycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.orderRecycleView.setAdapter(orderItemAdapter);
    }

    private void DownloadInvoice() {


        firebaseFirestore.collection("Users").document(order.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                User user=snapshot.toObject(User.class);
                //objects are arlready created,no need to create new object
                //TODO use user,seller and order object to initilize invoice header
            }
        });
        for (int i = 0; i < orderItemAdapter.getItemCount(); i++) {
            OrderItem orderItem = orderItemAdapter.getOrderItem(i);
            //object for product information
            //TODO user order item for intilization of product

        }
    }

    private void InitUi() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy");
        firebaseFirestore.collection("Users").document(auth.getUid()).collection("orderList").document(orderId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                try {
                    {

                        order = snapshot.toObject(Order.class);
                        binding.orderId.setText(order.getOrderId());
                        binding.orderDate.setText(simpleDateFormat.format(order.getTimestamp().toDate()));
//                        binding.orderStatus.setText(order.getOrderStatus());
                        binding.addressNameTv.setText(order.getUserAddressname());
                        binding.addressLocation.setText(order.getUserHouseNum() + order.getUserLocation());
                        binding.totalAmount.setText("₹"+NumberFormat.getInstance().format(order.getTotalAmount()));
                        initSellerDetails();

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

    private void noProductFound(boolean b) {
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (orderItemAdapter != null) {
            orderItemAdapter.stopListening();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (orderItemAdapter != null) {
            orderItemAdapter.startListening();
        }
    }

    void initSellerDetails() {
       firebaseFirestore.collection("Sellers").document( order.getSellerId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
           @Override
           public void onSuccess(DocumentSnapshot snapshot) {
                seller=snapshot.toObject(Seller.class);
               binding.userName.setText(seller.getName());
               binding.downloadInvoice.setOnClickListener(v -> {
                   DownloadInvoice();
               });
               try {


                   GlideApp.with(getApplicationContext())
                           .load(seller.getProfilePhotoUrl())
                           .into(binding.profilePhoto);
               } catch (Exception ex) {

               }

               binding.call.setOnClickListener(v -> {
                   Intent callIntent = new Intent(Intent.ACTION_DIAL);
                   callIntent.setData(Uri.parse("tel:" + seller.getPhone()));
                   try {


                       startActivity(callIntent);
                   } catch (Exception ex) {
                       ex.printStackTrace();
                   }

               });
               binding.email.setOnClickListener(v -> {
                   Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                   emailIntent.setData(Uri.parse("mailto:" + seller.getEmail()));
                   try {


                       startActivity(emailIntent);
                   } catch (Exception ex) {
                       ex.printStackTrace();
                   }

               });
           }
       });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //add the function to perform here
        WriteBatch batch=firebaseFirestore.batch();
        DocumentReference sellerRef=firebaseFirestore.collection("Sellers").document(order.getSellerId()).collection("orderList").document(orderId);
        DocumentReference userRef=firebaseFirestore.collection("Users").document(order.getUserId()).collection("orderList").document(orderId);
        batch.update(sellerRef,"orderStatus",binding.orderStatus.getSelectedItem().toString());
        batch.update(userRef,"orderStatus",binding.orderStatus.getSelectedItem().toString());
        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onBackPressed();
            }
        });
      return true;

    }
}
