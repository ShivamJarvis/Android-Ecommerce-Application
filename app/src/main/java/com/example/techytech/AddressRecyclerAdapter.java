package com.example.techytech;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AddressRecyclerAdapter extends RecyclerView.Adapter<AddressViewHolder> {
    private ArrayList<String> adressesList,stateCityList,zipCodeList,addressId;
    interface AddressIsClickedInterface{
        void AdressIsClicked(String addressId);
    }

    AddressIsClickedInterface addressIsClickedInterface;

    public AddressRecyclerAdapter(ArrayList<String> adressesList, ArrayList<String> stateCityList, ArrayList<String> zipCodeList, ArrayList<String> addressId, AddressIsClickedInterface addressIsClickedInterface) {
        this.adressesList = adressesList;
        this.stateCityList = stateCityList;
        this.zipCodeList = zipCodeList;
        this.addressId = addressId;
        this.addressIsClickedInterface = addressIsClickedInterface;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.adress_view_holder,parent,false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        holder.getAddressTextView().setText(adressesList.get(position));
        holder.getStateCityTextView().setText(stateCityList.get(position));
        holder.getZipCodeTextView().setText(zipCodeList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressIsClickedInterface.AdressIsClicked(addressId.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressId.size();
    }
}
