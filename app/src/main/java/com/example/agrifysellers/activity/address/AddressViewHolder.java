package com.example.agrifysellers.activity.address;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrifysellers.activity.Utils.internetConnectionUtils;
import com.example.agrifysellers.activity.address.model.Address;
import com.example.agrifysellers.databinding.AddressItemBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vincent.blurdialog.BlurDialog;
import com.vincent.blurdialog.listener.OnPositiveClick;

import es.dmoral.toasty.Toasty;

public class AddressViewHolder extends RecyclerView.ViewHolder {
    AddressItemBinding binding;
    Activity activity;
    BlurDialog blurDialog;
    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    public AddressViewHolder( AddressItemBinding binding) {
        super(binding.getRoot());
        this.binding=binding;

    }

    public void bind(DocumentSnapshot snapshot, Activity activity, String TAG, AddressAdapter.OnAddressSelectedListener listener) {
        if(TAG=="AddressListFragment")
        {
            binding.buttonLayout.setVisibility(View.GONE);
            itemView.setOnClickListener(v->{
                listener.OnAddressSelected(snapshot);
            });
        }
        Address address=snapshot.toObject(Address.class);
        binding.setAddress(address);
        binding.editButton.setOnClickListener(v -> {
            if(internetConnectionUtils.isInternetConnected(activity)) {
                Intent intent = new Intent(activity, addressActivity.class);
                intent.putExtra("addressRef", snapshot.getReference().getPath());
                activity.startActivity(intent);
            }
        });
        blurDialog=new BlurDialog();
        BlurDialog.Builder builder = new BlurDialog.Builder()
                .isCancelable(true).radius(10)
                .isOutsideCancelable(true).message("are you sure you want to delete "+snapshot.getString("name")+" address ?")
                .positiveMsg("Yes")
                .negativeMsg("No").positiveClick(new OnPositiveClick() {
                    @Override
                    public void onClick() {
                        delete(snapshot);
                        blurDialog.dismiss();
                    }
                }) .negativeClick(() -> {
                    blurDialog.dismiss();

                }) .type(BlurDialog.TYPE_DELETE)
                .createBuilder(activity);
        blurDialog.setBuilder(builder);
        String comAdress=address.getHouseNum()+address.getLocation();
        binding.addressLocation.setText(comAdress);
        this.activity=activity;
        binding.deleteButton.setOnClickListener(v -> {
            if(internetConnectionUtils.isInternetConnected(activity)) {
                blurDialog.show();

            }

        });
    }

    private void delete(DocumentSnapshot snapshot) {
        firebaseFirestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        firebaseFirestore.collection("Sellers").document(auth.getUid()).collection("productList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                boolean isAddressUsed=false;
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot doc:task.getResult())
                    {try {


                        String ref = doc.getDocumentReference("addressRef").getPath();
                        if (ref.equals(snapshot.getReference().getPath())) {
                            isAddressUsed = true;
                            break;

                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    }
                    if(isAddressUsed)
                    {
                        Toasty.error(activity,"address is used by some of product,try to delete first then remove address",Toasty.LENGTH_SHORT).show();
                    }
                    else {
                        snapshot.getReference().delete().addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful())
                            {
                                Toasty.info(activity,"success",Toasty.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toasty.error(activity,task.getException().getLocalizedMessage(),Toasty.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });


    }

}
