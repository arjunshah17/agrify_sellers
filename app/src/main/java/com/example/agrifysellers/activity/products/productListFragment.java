package com.example.agrifysellers.activity.products;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.agrifysellers.R;
import com.example.agrifysellers.databinding.FragmentProductListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 */
public class productListFragment extends Fragment implements ProductListAdapter.OnProductSelectedListner {


    public productListFragment() {
        // Required empty public constructor
    }
FragmentProductListBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Query mQuery;
 private ProductListAdapter productListAdapter;
String TAG="productListFragment()";
    private FirebaseFirestore firebaseFirestore;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();

       getProducts();



        return binding.getRoot();
    }

    private void getProducts() {
        mQuery = firebaseFirestore.collection("Sellers").document(firebaseUser.getUid()).collection("productList");
        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }
        productListAdapter= new ProductListAdapter(mQuery,getActivity(), TAG, this)
        {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                super.onEvent(documentSnapshots, e);
            }

            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }
        };
        binding.productListRv.setLayoutManager(new LinearLayoutManager(getActivity()));



        binding.productListRv.setAdapter(productListAdapter);


    }

    @Override
    public void onProductSelected(DocumentSnapshot store, View SharedView) {

    }
    @Override
    public void onStop() {
        super.onStop();
        if (productListAdapter != null) {
            productListAdapter.stopListening();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Start sign in if necessary

        // Start listening for Firestore updates
        if (productListAdapter != null) {
            productListAdapter.startListening();
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 1:
               productListAdapter.edit(item.getGroupId());
                return true;
            case 2:

               productListAdapter.delete(item.getGroupId());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
