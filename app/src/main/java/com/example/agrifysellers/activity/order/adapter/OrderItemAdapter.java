package com.example.agrifysellers.activity.order.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.agrifysellers.R;
import com.example.agrifysellers.activity.adapter.FirestoreAdapter;
import com.example.agrifysellers.activity.order.model.OrderItem;
import com.example.agrifysellers.activity.order.viewHolder.OrderItemHolder;
import com.example.agrifysellers.databinding.OrderItemBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class OrderItemAdapter extends FirestoreAdapter<OrderItemHolder> {
    Activity activity;
    OnClickRateListener listener;

    public OrderItemAdapter(Query query, Activity activity) {
        super(query);
        this.activity = activity;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        OrderItemBinding binding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()), R.layout.order_item, parent, false);


        return new OrderItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemHolder holder, int position) {
        holder.bind(getSnapshot(position), activity);

    }

    public OrderItem getOrderItem(int pos) {
        return getSnapshot(pos).toObject(OrderItem.class);
    }

    public interface OnClickRateListener {
        void onClikedRating(DocumentSnapshot snapshot, OrderItem orderItem);
    }
}
