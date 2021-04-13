package com.example.recipegenerator;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        ParseObject.registerSubclass(Post.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("mUzY4aXYUABG0HaubXfV2pjZ56VOekmAuD9GKbDF")
                .clientKey("EgOzssZCm4eET6gvl0QASxdh1n9mH0WrismHCBeG")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
