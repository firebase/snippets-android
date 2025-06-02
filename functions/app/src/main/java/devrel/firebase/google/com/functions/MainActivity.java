/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package devrel.firebase.google.com.functions;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableReference;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.functions.StreamResponse;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // [START define_functions_instance]
    private FirebaseFunctions mFunctions;
    // [END define_functions_instance]


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // [START initialize_functions_instance]
        mFunctions = FirebaseFunctions.getInstance();
        // [END initialize_functions_instance]
    }

    public void emulatorSettings() {
        // [START functions_emulator_connect]
        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
        // the host computer from an Android emulator.
        FirebaseFunctions functions = FirebaseFunctions.getInstance();
        functions.useEmulator("10.0.2.2", 5001);
        // [END functions_emulator_connect]
    }

    // [START function_add_numbers]
    private Task<Integer> addNumbers(int a, int b) {
        // Create the arguments to the callable function, which are two integers
        Map<String, Object> data = new HashMap<>();
        data.put("firstNumber", a);
        data.put("secondNumber", b);

        // Call the function and extract the operation from the result
        return mFunctions
                .getHttpsCallable("addNumbers")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, Integer>() {
                    @Override
                    public Integer then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        Map<String, Object> result = (Map<String, Object>) task.getResult().getData();
                        return (Integer) result.get("operationResult");
                    }
                });
    }
    // [END function_add_numbers]

    // [START function_add_message]
    private Task<String> addMessage(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("push", true);

        return mFunctions
                .getHttpsCallable("addMessage")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });
    }
    // [END function_add_message]

    private void callAddNumbers(int firstNumber, int secondNumber) {
        // [START call_add_numbers]
        addNumbers(firstNumber, secondNumber)
                .addOnCompleteListener(new OnCompleteListener<Integer>() {
                    @Override
                    public void onComplete(@NonNull Task<Integer> task) {
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;

                                // Function error code, will be INTERNAL if the failure
                                // was not handled properly in the function call.
                                FirebaseFunctionsException.Code code = ffe.getCode();

                                // Arbitrary error details passed back from the function,
                                // usually a Map<String, Object>.
                                Object details = ffe.getDetails();
                            }
                        }
                    }
                });
        // [END call_add_numbers]
    }

  private void callAddMessage(String inputMessage) {
    // [START call_add_message]
    addMessage(inputMessage)
      .addOnCompleteListener(new OnCompleteListener<String>() {
        @Override
        public void onComplete(@NonNull Task<String> task) {
          if (!task.isSuccessful()) {
            Exception e = task.getException();
            if (e instanceof FirebaseFunctionsException) {
              FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
              FirebaseFunctionsException.Code code = ffe.getCode();
              Object details = ffe.getDetails();
            }
          }
        }
      });
    // [END call_add_message]
  }

  private void callStreamingFunctions() {
    List<devrel.firebase.google.com.functions.kotlin.MainActivity.Location> favoriteLocations = new ArrayList<>();
    favoriteLocations.add(new devrel.firebase.google.com.functions.kotlin.MainActivity.Location(
      "The Googleplex",
      37.4220199895279,
      -122.08531347325561));
    favoriteLocations.add(new devrel.firebase.google.com.functions.kotlin.MainActivity.Location(
      "Yosemite Valley",
      37.745192257741984,
      -119.5945133017153));
    favoriteLocations.add(new devrel.firebase.google.com.functions.kotlin.MainActivity.Location(
      "Old Faithful",
      44.46037818049411,
      -110.82802255265777));
    // [START stream_data_client]
    // Get the callable by passing an initialized functions SDK.
    HttpsCallableReference getForecast = mFunctions.getHttpsCallable("getForecast");
    getForecast.stream(
      new HashMap<String, Object>() {{
        put("locations", favoriteLocations);
      }}
    ).subscribe(new Subscriber<StreamResponse>() {
      @Override
      public void onSubscribe(Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
      }

      @Override
      public void onNext(StreamResponse streamResponse) {
        if (streamResponse instanceof StreamResponse.Message) {
          // The flow will emit a [StreamResponse.Message] value every time the
          // callable function calls `sendChunk()`.
          StreamResponse.Message response = (StreamResponse.Message) streamResponse;
          Map<String, Object> forecastDataChunk =
            (Map<String, Object>) response.getMessage().getData();
          // Update the UI every time a new chunk is received
          // from the callable function
          updateUI(
            (double) forecastDataChunk.get("latitude"),
            (double) forecastDataChunk.get("longitude"),
            (double) forecastDataChunk.get("forecast")
          );
        } else if(streamResponse instanceof StreamResponse.Result) {
          // The flow will emit a [StreamResponse.Result] value when the
          // callable function completes.
          StreamResponse.Result response = (StreamResponse.Result) streamResponse;
          List<Map<String, Object>> allWeatherForecasts =
            (List<Map<String, Object>>) response.getResult().getData();
          finalizeUI();
        }
      }

      @Override
      public void onError(Throwable throwable) {
        // an error occurred in the function
      }

      @Override
      public void onComplete() {

      }
    });
    // [END stream_data_client]
  }

  private void updateUI(
    double latitude,
    double longitude,
    double forecast
  ) {

  }

  private void finalizeUI() {

  }
}
