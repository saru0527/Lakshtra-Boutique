package com.example.e_commerce_1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce_1.activities.PlacedOrderActivity;
import com.example.e_commerce_1.adapters.MyCartAdapter;
import com.example.e_commerce_1.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MyCartsFragment extends Fragment {

    FirebaseFirestore db;
    FirebaseAuth auth;
    TextView overTotalAmount;
    RecyclerView recyclerView;
    MyCartAdapter cartAdapter;
    List<MyCartModel> cartModelList;
    Button buyNow;
    int totalBill;
    ProgressBar progressBar;
    double totalAmount = 0.0;
    public MyCartsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_carts, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);



        overTotalAmount = root.findViewById(R.id.overall_total_price);

        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setVisibility(View.GONE);
        buyNow = root.findViewById(R.id.buy_now);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(getActivity(),cartModelList);
        recyclerView.setAdapter(cartAdapter);

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){

                        String documentId = documentSnapshot.getId();

                        MyCartModel cartModel = documentSnapshot.toObject(MyCartModel.class);

                        cartModel.setDocumentId(documentId);
                        cartModelList.add(cartModel);
                        cartAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    calculateTotalAmount(cartModelList);

                }
            }
        });

        //****************************************************************************
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //deleteAddToCart();
                String S = Double.toString(totalAmount);
                Toast.makeText(getContext(), S, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), PlacedOrderActivity.class);
                intent.putExtra("itemList", (Serializable) cartModelList);
                startActivity(intent);
            }
        });

        return root;
    }

   /* private void deleteAddToCart() {
        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart")
                .document(cartModelList.get().getDocumentId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(getContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(getContext(), "Error"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    } */


    private void calculateTotalAmount(List<MyCartModel> cartModelList) {

        for(MyCartModel myCartModel : cartModelList){
            totalAmount += myCartModel.getTotalPrice();
        }
        overTotalAmount.setText("Total Amount :"+totalAmount);
    }

}