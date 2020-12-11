package com.example.techytech;

import com.parse.Parse;
import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Xq5Ptq73mDpIXUHeB7DhHLSRjHOTvKHqzEKWZc0k")
                // if defined
                .clientKey("FpJ7j2p1hLkUTWjNzKby0vyKg4lb7s2XlQGvlKqU")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}