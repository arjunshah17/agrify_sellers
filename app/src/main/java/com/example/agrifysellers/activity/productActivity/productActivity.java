package com.example.agrifysellers.activity.productActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrifysellers.R;
import com.example.agrifysellers.activity.MainActivity;
import com.example.agrifysellers.activity.adapter.ProductListAdapter;
import com.example.agrifysellers.activity.model.Seller;
import com.example.agrifysellers.activity.model.Store;
import com.example.agrifysellers.databinding.ActivityProductBinding;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.gabrielbb.cutout.CutOut;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;
import es.dmoral.toasty.Toasty;
import io.grpc.Context;

import static android.widget.GridLayout.HORIZONTAL;

public class productActivity extends AppCompatActivity implements StepperFormListener, ProductListAdapter.OnProductSelectedListener {
    public ProductName productName;
    ActivityProductBinding bind;
    Boolean imageUploaded=false;
private  ProductImageAdapter productImageAdapter;
    private static final int REQUEST_CODE = 123;
    ArrayList<Uri> imageUrl;
    StorageReference storageRef;

    ProductListAdapter.OnProductSelectedListener listener;
    Category cat = new Category("select Category");
    ProductbottomsheetFragment productBottomSheetFragment;
    ProductDetails productDetails = new ProductDetails("enter productDetails ");
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth Auth;
    DocumentReference documentReference;
    DocumentReference userRef;
    WriteBatch batch;
    DocumentReference prodRef;
    String product_id;
    Seller seller;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INIT();

        Auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        batch = firebaseFirestore.batch();
seller=new Seller();

    }

    void INIT() {
        productName = new ProductName("select productName", getSupportFragmentManager());
        bind = DataBindingUtil.setContentView(this, R.layout.activity_product);
        listener = this;
        imageUrl=new ArrayList<Uri>();
        productImageAdapter =new ProductImageAdapter(imageUrl);
        productBottomSheetFragment = new ProductbottomsheetFragment(listener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            bind.productLinearLayout.setBackground(this.getDrawable(R.drawable.store_item_background));//set curve background
        }

        bind.stepperForm.setup(this, cat, productName, productDetails).displayBottomNavigation(false)
                .lastStepNextButtonText("start selling").init();

        productName.productButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("category", cat.getStepData());
                productBottomSheetFragment.setArguments(bundle);
                productBottomSheetFragment.show(getSupportFragmentManager(), "productlist");


            }
        });
        storageRef= FirebaseStorage.getInstance().getReference();
      productDetails.binding.imageRecycleView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2,RecyclerView.HORIZONTAL,false));
      productDetails.binding.imageRecycleView.setAdapter(productImageAdapter);
        productDetails.binding.productImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             loadImages();
            }
        });
    }

    private void loadImages() {
        CutOut.activity().start(this);

    }

    @Override
    public void onCompletedForm() {

       DocumentReference ref= firebaseFirestore.collection("Sellers").document(Auth.getCurrentUser().getUid());
       ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task) {

               seller=task.getResult().toObject(Seller.class);
               seller.setPrice(Float.valueOf(productDetails.binding.priceEditText.getText().toString().trim()));
               seller.setStock(Integer.valueOf(productDetails.binding.stockEditText.getText().toString().trim()));
               seller.setMaxQuantity(Integer.valueOf(productDetails.binding.maxQuantityEditText.getText().toString().trim()));
               seller.setMinQuantity(Integer.valueOf(productDetails.binding.minQuantityEditText.getText().toString().trim()));
seller.setStock(Integer.valueOf(productDetails.binding.stockEditText.getText().toString().trim()));
               userRef = firebaseFirestore.collection("Sellers").document(Auth.getCurrentUser().getUid()).collection("productList").document(product_id);
               seller.setUserId(userRef);
               StoreData();
           }
       });





    }
    int sellerCount;
    Float Price;
    void StoreData()
    {      stateLoading(true);

        prodRef = firebaseFirestore.collection("store").document(product_id).collection("sellerList").document(Auth.getCurrentUser().getUid());

        batch.set(prodRef, seller);
        final Map<String, DocumentReference> user_product_info = new HashMap<>();
        user_product_info.put("id", prodRef);

        batch.set(userRef, user_product_info);

        DocumentReference storeProduct=firebaseFirestore.collection("store").document(product_id);
        storeProduct.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                sellerCount= documentSnapshot.getDouble("sellerCount").intValue();
                if(imageUploaded)
                {
                    uploadImage();
                }
                sellerCount=sellerCount+1;
                Price=documentSnapshot.getDouble("price").floatValue();
                if(seller.getPrice()<Price || Price==0)
                {
                    batch.update(storeProduct,"price",seller.getPrice());
                }
                batch.update(storeProduct,"sellerCount",sellerCount);
                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        stateLoading(false);
                        if (task.isSuccessful()) {
                            Toasty.success(getApplicationContext(), "productName added", Toasty.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onCancelledForm() {

    }





    @Override
    public void onProductSelected(DocumentSnapshot store) {
        Store mStore = store.toObject(Store.class);
        product_id = store.getId();
productDetails.binding.priceTextField.setHint("enter product ₹ for per "+mStore.getUnit());
productDetails.binding.stockTextField.setHint("how many "+mStore.getUnit()+" of "+mStore.getName()+" you want to sell ?");
productDetails.binding.minQuantityTextField.setHint("set minimum "+mStore.getUnit()+" of "+mStore.getName()+ " that user can buy");
        productDetails.binding.maxQuantityTextField.setHint("set maximum "+mStore.getUnit()+" of "+mStore.getName()+ " that user can buy");
        firebaseFirestore.collection("Sellers").document(Auth.getCurrentUser().getUid()).collection("productList").document(product_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().exists()) {               //productName is not there
                        productBottomSheetFragment.dismiss();
                        productName.productButton.setText(mStore.getName());
                    } else {
                        Toasty.error(getApplicationContext(), store.getString("name") + " already you are selling", Toasty.LENGTH_SHORT).show();
                    }
                    productName.markAsCompletedOrUncompleted(true);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CutOut.CUTOUT_ACTIVITY_REQUEST_CODE) {

            switch (resultCode) {
                case Activity.RESULT_OK:
                  imageUploaded=true;

                  imageUrl.add(CutOut.getUri(data));
                  productImageAdapter.notifyDataSetChanged();
                  imageUploaded=true;
             
                    // Save the image using the returned Uri here
                    break;
                case CutOut.CUTOUT_ACTIVITY_RESULT_ERROR_CODE:
                    Exception ex = CutOut.getError(data);
                    break;
                default:
                    System.out.print("User cancelled the CutOut screen");
            }
        }
    }

    private void uploadImage() {


int count=0;
for(Uri result:imageUrl)
{
count=count+1;
    final StorageReference ref=storageRef.child(Auth.getCurrentUser().getUid()).child("store").child(productName.getStepData()+count);
    UploadTask image_path = (UploadTask) ref.putFile(result);//uploaded image in cloud
    Task<Uri> urlTask=image_path.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
        @Override
        public Task<Uri> then(@io.reactivex.annotations.NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return ref.getDownloadUrl();

        }
    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
        @Override

        public void onComplete(@io.reactivex.annotations.NonNull Task<Uri> task) {
            if(task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                HashMap<String,String> imageHash=new HashMap<>();
                imageHash.put("url",downloadUri.toString());
              firebaseFirestore.collection("store").document(product_id).collection("sellerList").document(Auth.getCurrentUser().getUid()).collection("productImage").document().set(imageHash).addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      if(task.isSuccessful()) {
                          Toasty.info(getApplicationContext(), "image uploaded in server", Toasty.LENGTH_SHORT).show();
                      }
                      else
                      {
                          Toasty.error(getApplicationContext(), "error in uploading image", Toasty.LENGTH_SHORT).show();
                      }
                  }
              });


            }}});

}

    }
}
