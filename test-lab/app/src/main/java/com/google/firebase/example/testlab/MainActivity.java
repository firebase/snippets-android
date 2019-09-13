package com.google.firebase.example.testlab;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void checkEnvironment() {
        // [START ftl_check_env]
        String testLabSetting = Settings.System.getString(getContentResolver(), "firebase.test.lab");
        if ("true".equals(testLabSetting)) {
            // Do something when running in Test Lab
            // ...
        }
        // [END ftl_check_env]
    }

    public void gameCheckIntent() {
        // [START ftl_game_check_intent]
        Intent launchIntent = getIntent();
        if(launchIntent.getAction().equals("com.google.intent.action.TEST_LOOP")) {
            int scenario = launchIntent.getIntExtra("scenario", 0);
            // Code to handle your game loop here
        }
        // [END ftl_game_check_intent]
    }

    public void gameFinish() {
        Activity yourActivity = this;
        // [START ftl_game_finish]
        yourActivity.finish();
        // [END ftl_game_finish]
    }

    public void gameOutputFile() {
        // [START ftl_game_output_file]
        Intent launchIntent = getIntent();
        Uri logFile = launchIntent.getData();
        if (logFile != null) {
            Log.i(TAG, "Log file " + logFile.getEncodedPath());
            // ...
        }
        // [END ftl_game_output_file]
    }

    public void gameOutputFileDescriptor() throws FileNotFoundException {
        // [START ftl_game_output_fd]
        Intent launchIntent = getIntent();
        Uri logFile = launchIntent.getData();
        int fd = -1;
        if (logFile != null) {
            Log.i(TAG, "Log file " + logFile.getEncodedPath());
            try {
                fd = getContentResolver()
                        .openAssetFileDescriptor(logFile, "w")
                        .getParcelFileDescriptor()
                        .getFd();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fd = -1;
            } catch (NullPointerException e) {
                e.printStackTrace();
                fd = -1;
            }
        }

        // C++ code invoked here.
        // native_function(fd);
        // [END ftl_game_output_fd]
    }

    public void gameScenario() {
        // [START ftl_game_scenario]
        Intent launchIntent = getIntent();
        int scenario = launchIntent.getIntExtra("scenario", 0);
        // [END ftl_game_scenario]
    }

    public void takeScreenshot() {
        // [START ftl_take_screenshot]
        ScreenShotter.takeScreenshot("main_screen_2", this /* activity */);
        // [END ftl_take_screenshot]
    }

}
