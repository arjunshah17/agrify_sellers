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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
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
    String productId;
    public void delete(int pos)
    {     ArrayList<String > url=new ArrayList<>();
        DocumentReference id=getSnapshot(pos).getDocumentReference("storeProductRef");
        int imageCount=getSnapshot(pos).getDouble("imageCount").intValue();
        String counterId =getSnapshot(pos).getId();
      productId=getSnapshot(pos).getId();
firebaseFirestore.collection("store").document(productId).collection("sellerList").document(auth.getCurrentUser().getUid()).collection("productImage").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                url.add(document.getString("imagePath"));
            }
            deleteSellerProductImage(productId,url,auth.getCurrentUser().getUid());
            getSnapshot(pos).getReference().delete().addOnCompleteListener(task1 -> {
                if(task1.isSuccessful())
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
                                if (task1.isSuccessful()) {
                                    Toasty.info(activity, task.getResult().toString(), Toasty.LENGTH_SHORT).show();
                                    updateProductCounter(counterId);

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

        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
        }
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
    private void deleteSellerProductImage(String productId,ArrayList<String>  imagePath,String seller_id) {

        for(String imageUrl:imagePath) {

            storageReference.getReference().child("storeProductImage").child(productId).child(seller_id).child(productId).child(imageUrl).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toasty.info(activity,   "deleted image,Toasty.LENGTH_SHORT").show();
                }
            });
        }

    }
    public void edit(int pos)
    {

        Intent intent=new Intent(activity,productActivity.class);

        String path=getSnapshot(pos).getReference().getPath();
        intent.putExtra("path",path);
        activity.startActivity(intent);

    }
    public void manage (int pos)
    {
        Intent intent=new Intent(activity,productManage.class);
        String sellerId=getSnapshot(pos).getString("sellerId");
        intent.putExtra("seller_id",sellerId);
        intent.putExtra("product_id",getSnapshot(pos).getString("productId"));
        activity.startActivity(intent);
    }
}
