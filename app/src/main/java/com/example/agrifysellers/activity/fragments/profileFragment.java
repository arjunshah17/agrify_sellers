package com.example.agrifysellers.activity.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;


import com.example.agrifysellers.R;

import com.example.agrifysellers.activity.adapter.StoreAdapter;
import com.example.agrifysellers.activity.editProfile;
import com.example.agrifysellers.activity.model.User;
import com.example.agrifysellers.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class profileFragment extends Fragment implements StoreAdapter.OnStoreSelectedListener {
    String TAG = "profileFragment";
    private FirebaseAuth firebaseAuth;
    private Query mQuery;
    private StoreAdapter mAdapter;

    User user;

    private FirebaseFirestore firebaseFirestore;

    private FirebaseUser firebaseUser;
    public profileFragment() {
        // Required empty public constructor
    }

    private FragmentProfileBinding bind;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_profile,
                container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseFirestore = FirebaseFirestore.getInstance();

        stateLoading(true);
        firebaseFirestore.disableNetwork().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadData(); //lo
                firebaseFirestore.enableNetwork().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadData();
                    }
                });
            }
        });


        bind.EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), editProfile.class);
                startActivity(intent);
            }
        });
        initFirestore();
        initRecyclerView();
        return bind.getRoot();
    }

    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }
        mAdapter = new StoreAdapter(mQuery, this, getActivity(), TAG) {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                super.onEvent(documentSnapshots, e);


                if (getItemCount() == 0) {
                    noProductFound(true);
                    productLoadingState(false);
                } else {
                    noProductFound(false);
                    productLoadingState(false);
                }
            }

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    noProductFound(true);

                } else {
                    noProductFound(false);
                }

            }


        };

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        bind.storeRecycleView.setHasFixedSize(true);
        bind.storeRecycleView.setLayoutManager(gridLayoutManager);


        bind.storeRecycleView.setAdapter(mAdapter);
    }

    private void initFirestore() {

                mQuery = firebaseFirestore.collection("Sellers").document(firebaseUser.getUid()).collection("productList");



    }

    private void loadData() {
        user = new User();

        firebaseFirestore.collection("Sellers").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                user.setName(firebaseUser.getDisplayName());
                user.setEmail(firebaseUser.getEmail());
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        user.setPhone(task.getResult().getString("phone"));


                    }
                    if (firebaseUser.getPhotoUrl() != null) {
                        if (getActivity() != null) {
                         Picasso.get().load(firebaseUser.getPhotoUrl()).placeholder(R.drawable.add_photo).into(bind.userProfilePhoto);
                        }
                    }

                    bind.setUser(user);


                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getActivity(), "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                }
                stateLoading(false);


            }
        });


    }

    private void stateLoading(boolean stateStatus) {
        if (stateStatus) {
            bind.ProfileLayout.setVisibility(View.GONE);
            bind.progressLoading.setVisibility(View.VISIBLE);
        } else {
            bind.ProfileLayout.setVisibility(View.VISIBLE);
            bind.progressLoading.setVisibility(View.GONE);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Start sign in if necessary

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    void productLoadingState(boolean state) {
        if (state) {
            bind.storeRecycleView.setVisibility(View.GONE);
            bind.shimmerRecyclerView.showShimmerAdapter();


        } else {
            bind.storeRecycleView.setVisibility(View.VISIBLE);
            bind.shimmerRecyclerView.hideShimmerAdapter();
            // TODO stop shrimmer effect

        }
    }

    void noProductFound(boolean state) {
        if (state) {
            bind.storeRecycleView.setVisibility(View.GONE);


            bind.animationView.playAnimation();
            bind.animationLayout.setVisibility(View.VISIBLE);
        } else {
            bind.animationLayout.setVisibility(View.GONE);
            bind.storeRecycleView.setVisibility(View.VISIBLE);
            bind.animationView.cancelAnimation();
        }
    }

    @Override
    public void onStoreSelected(DocumentSnapshot store, View SharedView) {

        Toasty.info(getContext(), "selected" + store.getData().toString(), Toasty.LENGTH_SHORT).show();
    }
}


