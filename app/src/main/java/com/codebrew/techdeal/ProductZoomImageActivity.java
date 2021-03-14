package com.codebrew.techdeal;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.List;

public class ProductZoomImageActivity extends AppCompatActivity {
    private String recievedProductId;
    private CarouselView carouselView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_zoom_image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        carouselView = findViewById(R.id.product_image_carousel_zoom);
        recievedProductId = getIntent().getStringExtra("productId");

        ParseQuery<ParseObject> parseQueryForImages = ParseQuery.getQuery("ProductImages");
        parseQueryForImages.whereEqualTo("product_id", recievedProductId);
        parseQueryForImages.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> imageObjects, ParseException e) {
                if (e == null) {
                    if (imageObjects.size() > 0) {
                        carouselView.setImageListener(new ImageListener() {
                            @Override
                            public void setImageForPosition(int position, ImageView imageView) {
                                ParseFile parseFile = (ParseFile) imageObjects.get(position).getParseFile("images");
                                parseFile.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        imageView.setScaleType(ImageView.ScaleType.CENTER);
                                        imageView.setFocusable(true);
                                        imageView.setImageBitmap(bitmap);

                                    }
                                });
                            }
                        });
                        carouselView.setPageCount(imageObjects.size());
                    }
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}