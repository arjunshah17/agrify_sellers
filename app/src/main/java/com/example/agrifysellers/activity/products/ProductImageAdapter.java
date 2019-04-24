package com.example.agrifysellers.activity.products;

import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrifysellers.R;

import java.util.ArrayList;

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.ViewHolder> {
    private ArrayList<Uri> urlList;
    ImageView productImage;

    ProductImageAdapter(ArrayList<Uri> urlList)
    {
        this.urlList=urlList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_detail_image_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uri=urlList.get(position);
        productImage.setImageURI(uri);
    }







    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=(ImageView) itemView.findViewById(R.id.product_imageView);

            itemView.setOnCreateContextMenuListener(this);



        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(),1,1,"remove");
        }
    }
    @Override
    public int getItemCount() {
        return urlList.size();
    }

    void removeImage(int pos)
    {

    }

}
