package com.example.agrifysellers.activity.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrifysellers.R;
import com.example.agrifysellers.activity.adapter.ProductListAdapter;
import com.example.agrifysellers.databinding.ProductbottomsheetfragmentBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class ProductbottomsheetFragment extends BottomSheetDialogFragment {
    ProductListAdapter mAdapter;
    ProductListAdapter.OnProductSelectedListener listener;
    ProductbottomsheetfragmentBinding binding;
    String cat;
    private FirebaseFirestore mFirestore;
    private Query mQuery;

    public ProductbottomsheetFragment(ProductListAdapter.OnProductSelectedListener listener) {
        this.listener = listener;
        // Required empty public constructor
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cat = getArguments().getString("category");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.productbottomsheetfragment, container,
                false);
        mFirestore = FirebaseFirestore.getInstance();
        // get the views and attach the listener
        initFirestore();
        initRecyclerView();
        return binding.getRoot();

    }

    private void initFirestore() {
        // TODO(developer): Implement
        mFirestore = FirebaseFirestore.getInstance();
        mQuery = mFirestore.collection("store").whereEqualTo("category", cat);

    }

    private void initRecyclerView() {


        mAdapter = new ProductListAdapter(mQuery, getActivity(), listener) {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                super.onEvent(documentSnapshots, e);


                if (getItemCount() == 0) {

                } else {

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

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.storeRecycleView.setHasFixedSize(true);
        binding.storeRecycleView.setLayoutManager(mLayoutManager);


        binding.storeRecycleView.setAdapter(mAdapter);
        binding.serachView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Query queryName;
                if (query != null) {
                    queryName = mFirestore.collection("store").orderBy("name").startAt(query.toLowerCase()).endAt(query.toLowerCase() + "\uf8ff");
                    mAdapter.setQuery(queryName);

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        binding.serachView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mAdapter.setQuery(mQuery);
                return false;
            }
        });


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

    void noProductFound(boolean state) {
        if (state) {
            binding.storeRecycleView.setVisibility(View.GONE);


            binding.animationView.playAnimation();
            binding.animationLayout.setVisibility(View.VISIBLE);
        } else {
            binding.animationLayout.setVisibility(View.GONE);
            binding.storeRecycleView.setVisibility(View.VISIBLE);
            binding.animationView.cancelAnimation();
        }
    }

}
