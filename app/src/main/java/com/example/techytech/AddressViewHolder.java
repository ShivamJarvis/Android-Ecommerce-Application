package com.example.techytech;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddressViewHolder extends RecyclerView.ViewHolder {
    private TextView addressTextView, stateCityTextView, zipCodeTextView;
    public AddressViewHolder(@NonNull View itemView) {
        super(itemView);
        addressTextView = itemView.findViewById(R.id.address_viewholder);
        stateCityTextView = itemView.findViewById(R.id.state_city_viewholder);
        zipCodeTextView = itemView.findViewById(R.id.zip_code_viewholder);
    }

    public TextView getAddressTextView() {
        return addressTextView;
    }

    public TextView getStateCityTextView() {
        return stateCityTextView;
    }

    public TextView getZipCodeTextView() {
        return zipCodeTextView;
    }
}
