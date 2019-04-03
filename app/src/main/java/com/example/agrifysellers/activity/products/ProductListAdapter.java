package com.example.agrifysellers.activity.products;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.agrifysellers.R;
import com.example.agrifysellers.activity.adapter.FirestoreAdapter;
import com.example.agrifysellers.databinding.ProductListItemBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;


public class ProductListAdapter extends FirestoreAdapter<ProductListViewHolder> {
    Activity activity;
    String TAG;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    private OnProductSelectedListner listener;
    FirebaseStorage storageReference;



    public interface OnProductSelectedListner {

        void onProductSelected(DocumentSnapshot store, View SharedView);

    }
    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
    public ProductListAdapter(Query query, Activity activity, String TAG, OnProductSelectedListner listner) {
        super(query);
        this.activity = activity;
        this.TAG = TAG;
        this.listener = listner;
        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        storageReference=FirebaseStorage.getInstance();

    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductListItemBinding productListItemBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.product_list_item,parent,false);

        return new ProductListViewHolder(productListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        holder.bind(getSnapshot(position), listener, activity, TAG);

    }
    String productName;
    public void delete(int pos)
    {
        DocumentReference id=getSnapshot(pos).getDocumentReference("storeProductRef");
        int imageCount=getSnapshot(pos).getDouble("imageCount").intValue();
        String counterId =getSnapshot(pos).getId();
        firebaseFirestore.collection("store").document(getSnapshot(pos).getReference().getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                productName=documentSnapshot.getString("name");
            }
        });

        getSnapshot(pos).getReference().delete().addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                FirebaseFunctions mFunctions;
// ...
                HashMap<String, Object> storeHash = new HashMap<>();

                DocumentReference ref = id;
                storeHash.put("path", ref.getPath());

                mFunctions = FirebaseFunctions.getInstance();
                try {
                    mFunctions.getHttpsCallable("recursiveDelete").call(storeHash).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
                        @Override
                        public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                            if (task.isSuccessful()) {
                                Toasty.info(activity, task.getResult().toString(), Toasty.LENGTH_SHORT).show();
                                updateProductCounter(counterId);
                                deleteSellerProductImage(productName, imageCount, firebaseUser.getUid());

                            } else {
                                Toasty.error(activity, task.getException().getLocalizedMessage(), Toasty.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                Toasty.error(activity,task.getException().getLocalizedMessage(),Toasty.LENGTH_SHORT).show();
            }
        });



    }

    private void updateProductCounter(String id) {
      firebaseFirestore.runTransaction(new Transaction.Function<Integer>() {

       DocumentReference reference=firebaseFirestore.collection("store").document(id);
          @Nullable
          @Override

          public Integer apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
              DocumentSnapshot doc=transaction.get(reference);

              int count=doc.getDouble("sellerCount").intValue();
              count=count-1;


                  transaction.update(reference, "sellerCount",count);

              return count;
          }



      }).addOnSuccessListener(new OnSuccessListener<Integer>() {
          @Override
          public void onSuccess(Integer integer) {
              Log.i("counter",integer.toString());

          }
      });

    }
    private void deleteSellerProductImage(String name, int imageCount,String seller_id) {
        for(int count=0;count<=imageCount;count++) {
            storageReference.getReference().child("storeProductImage").child(name).child(seller_id).child(name).child(name + count).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toasty.info(activity,name+"deleted imagest,Toasty.LENGTH_SHORT").show();
                }
            });
        }
    }
    public void edit(int pos)
    {

        Intent intent=new Intent(activity,productActivity.class);
        DocumentReference reference=getSnapshot(pos).getReference();
        activity.startActivity(intent);

    }
}
