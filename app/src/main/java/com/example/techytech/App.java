package com.example.techytech;

import com.parse.Parse;
import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("pTzTz9ddlrHW1HhcgkItEnmZM12diPEnGJxp6Gng")
                // if defined
                .clientKey("Lx1sitXUINGNVEETbOfOfpNnNxukkyaHhKkQnw7A")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}