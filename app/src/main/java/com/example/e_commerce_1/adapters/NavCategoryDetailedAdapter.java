package com.example.e_commerce_1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commerce_1.activities.DetailedActivity;
import com.example.e_commerce_1.models.NavCategoryDetailedModel;
import com.example.e_commerce_1.R;
import com.example.e_commerce_1.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class NavCategoryDetailedAdapter extends RecyclerView.Adapter<NavCategoryDetailedAdapter.ViewHolder> {

    Context context;
    List<NavCategoryDetailedModel> list;

    public NavCategoryDetailedAdapter(Context context, List<NavCategoryDetailedModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_category_detailed_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(list.get(position).getName());
        holder.price.setText(list.get(position).getPrice());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, price;
        ImageView addItem, removeItem;
        Button addToCart;
        FirebaseFirestore firestore;
        FirebaseAuth auth;
        TextView quantity;
        int totalQuantity = 1;
        int totalPrice = 0;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.cat_nav_img);
            name = itemView.findViewById(R.id.nav_cat_name);
            price = itemView.findViewById(R.id.price);
            addItem = itemView.findViewById(R.id.add_item);
            removeItem = itemView.findViewById(R.id.remove_item);
            addToCart = itemView.findViewById(R.id.buynow);
            quantity = itemView.findViewById(R.id.quantity);

            firestore = FirebaseFirestore.getInstance();
            auth = FirebaseAuth.getInstance();



            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addedToCart(auth,firestore,name,price,totalQuantity,totalPrice);
                }
            });

            addItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(totalQuantity < 10){
                        totalQuantity++;
                        quantity.setText(String.valueOf(totalQuantity));
                        String pric = price.getText().toString();
                        int prices = Integer.parseInt(pric);
                        totalPrice = prices * totalQuantity;
                    }
                }
            });

            removeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(totalQuantity > 1){
                        totalQuantity--;
                        quantity.setText(String.valueOf(totalQuantity));
                        String pric = price.getText().toString();
                        int prices = Integer.parseInt(pric);
                        totalPrice = prices * totalQuantity;
                    }
                }
            });
        }
    }

    private void addedToCart(FirebaseAuth auth,FirebaseFirestore firestore, TextView name,TextView price,
                                int quantity, int totalPrice) {
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();

        cartMap.put("productName", name.getText().toString());
        cartMap.put("productPrice", price.getText().toString());
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("currentTime", saveCurrentTime);
        String quantities=Integer.toString(quantity);
        cartMap.put("totalQuantity", quantities);
        cartMap.put("totalPrice", totalPrice);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
