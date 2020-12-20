package com.example.techytech;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsRecyclerAdapter extends RecyclerView.Adapter<OrderDetailsViewHolder> {
    private String productId;
    private String orderStatus;

    public OrderDetailsRecyclerAdapter(String productId) {
        this.productId = productId;
    }

    @NonNull
    @Override
    public OrderDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.order_detail_product_viewholder,parent,false);
        return new OrderDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsViewHolder holder, int position) {

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Product");
        parseQuery.whereEqualTo("objectId",productId);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects.size()==1){
                    ParseObject object = objects.get(0);
                    holder.getProductName().setText(object.getString("product_name"));
                    ParseFile parseFile = object.getParseFile("product_image");
                    parseFile.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if(e==null && data!=null){
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                holder.getProductImage().setImageBitmap(bitmap);
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
