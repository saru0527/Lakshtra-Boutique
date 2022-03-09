package com.example.e_commerce_1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce_1.R;
import com.example.e_commerce_1.models.MyCartModel;
import com.example.e_commerce_1.models.MyOrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    Context context;
    List<MyOrderModel> orderModelList;
    int totalPrice = 0;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    public MyOrderAdapter(Context context, List<MyOrderModel> orderModelList){
        this.context = context;
        this.orderModelList = orderModelList;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(orderModelList.get(position).getProductName());
        holder.price.setText(orderModelList.get(position).getProductPrice());
        holder.date.setText(orderModelList.get(position).getCurrentDate());
        holder.time.setText(orderModelList.get(position).getCurrentTime());
        holder.quantity.setText(orderModelList.get(position).getTotalQuantity());
        holder.totalPrice.setText(String.valueOf(orderModelList.get(position).getTotalPrice()));


    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, price, date, time, quantity, totalPrice;
        ImageView deleteItem;
        public ViewHolder(@NonNull View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            date = itemView.findViewById(R.id.current_date);
            time = itemView.findViewById(R.id.current_time);
            quantity = itemView.findViewById(R.id.total_quantity);
            totalPrice = itemView.findViewById(R.id.total_price);

        }
    }
}
