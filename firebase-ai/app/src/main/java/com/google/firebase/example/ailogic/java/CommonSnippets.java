package com.google.firebase.example.ailogic.java;

import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.ai.FirebaseAI;
import com.google.firebase.ai.GenerativeModel;
import com.google.firebase.ai.java.ChatFutures;
import com.google.firebase.ai.java.GenerativeModelFutures;
import com.google.firebase.ai.type.Content;
import com.google.firebase.ai.type.FunctionCallPart;
import com.google.firebase.ai.type.FunctionResponsePart;
import com.google.firebase.ai.type.GenerateContentResponse;
import com.google.firebase.ai.type.Schema;
import com.google.firebase.ai.type.TextPart;
import com.google.firebase.ai.type.Tool;
import com.google.firebase.ai.type.FunctionDeclaration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonElementKt;
import kotlinx.serialization.json.JsonObject;
import kotlinx.serialization.json.JsonPrimitive;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class CommonSnippets extends ViewModel {
  private Executor executor;
  private GenerativeModel ai = FirebaseAI.getInstance().generativeModel("gemini-2.5-flash");
  private GenerativeModelFutures model = GenerativeModelFutures.from(ai);

  public void chat() {
    chatNonStreaming();
    chatStreaming();
  }

  void chatNonStreaming() {
    // [START chat_non_streaming]
    // (optional) Create previous chat history for context
    Content.Builder userContentBuilder = new Content.Builder();
    userContentBuilder.setRole("user");
    userContentBuilder.addText("Hello, I have 2 dogs in my house.");
    Content userContent = userContentBuilder.build();

    Content.Builder modelContentBuilder = new Content.Builder();
    modelContentBuilder.setRole("model");
    modelContentBuilder.addText("Great to meet you. What would you like to know?");
    Content modelContent = userContentBuilder.build();

    List<Content> history = Arrays.asList(userContent, modelContent);

    // Initialize the chat
    ChatFutures chat = model.startChat(history);

    // Create a new user message
    Content.Builder messageBuilder = new Content.Builder();
    messageBuilder.setRole("user");
    messageBuilder.addText("How many paws are in my house?");

    Content message = messageBuilder.build();

    // Send the message
    ListenableFuture<GenerateContentResponse> response = chat.sendMessage(message);
    Futures.addCallback(
      response,
      new FutureCallback<GenerateContentResponse>() {
        @Override
        public void onSuccess(GenerateContentResponse result) {
          String resultText = result.getText();
          System.out.println(resultText);
        }

        @Override
        public void onFailure(Throwable t) {
          t.printStackTrace();
        }
      },
      executor);
    // [END chat_non_streaming]
  }

  void chatStreaming() {
    // [START chat_streaming]
    // (optional) Create previous chat history for context
    Content.Builder userContentBuilder = new Content.Builder();
    userContentBuilder.setRole("user");
    userContentBuilder.addText("Hello, I have 2 dogs in my house.");
    Content userContent = userContentBuilder.build();

    Content.Builder modelContentBuilder = new Content.Builder();
    modelContentBuilder.setRole("model");
    modelContentBuilder.addText("Great to meet you. What would you like to know?");
    Content modelContent = userContentBuilder.build();

    List<Content> history = Arrays.asList(userContent, modelContent);

    // Initialize the chat
    ChatFutures chat = model.startChat(history);

    // Create a new user message
    Content.Builder messageBuilder = new Content.Builder();
    messageBuilder.setRole("user");
    messageBuilder.addText("How many paws are in my house?");

    Content message = messageBuilder.build();

    // Send the message
    Publisher<GenerateContentResponse> streamingResponse = chat.sendMessageStream(message);

    final String[] fullResponse = {""};

    streamingResponse.subscribe(
      new Subscriber<GenerateContentResponse>() {

        @Override
        public void onNext(GenerateContentResponse generateContentResponse) {
          String chunk = generateContentResponse.getText();
          fullResponse[0] += chunk;
        }

        @Override
        public void onComplete() {
          System.out.println(fullResponse[0]);
        }

        // ... other methods omitted for brevity

        // [START_EXCLUDE]
        @Override
        public void onSubscribe(Subscription s) {
        }

        @Override
        public void onError(Throwable t) {
        }

        // [END_EXCLUDE]
      });
    // [END chat_streaming]
  }

  public void functionCalling() {
    // [START function_calling_create_function_declaration]
    FunctionDeclaration fetchWeatherTool =
      new FunctionDeclaration(
        "fetchWeather",
        "Get the weather conditions for a specific city on a specific date.",
        Map.of(
          "location",
          Schema.obj(
            Map.of(
              "city", Schema.str("The city of the location."),
              "state", Schema.str("The US state of the location."))),
          "date",
          Schema.str(
            "The date for which to get the weather. "
              + "Date must be in the format: YYYY-MM-DD.")),
        Collections.emptyList());
    // [END function_calling_create_function_declaration]

    // [START function_calling_generate_function_call]
    String prompt = "What was the weather in Boston on October 17, 2024?";
    ChatFutures chatFutures = model.startChat();
    // Send the user's question (the prompt) to the model using multi-turn chat.
    ListenableFuture<GenerateContentResponse> response =
      chatFutures.sendMessage(new Content("user", List.of(new TextPart(prompt))));

    ListenableFuture<JsonObject> handleFunctionCallFuture =
      Futures.transform(
        response,
        result -> {
          for (FunctionCallPart functionCall : result.getFunctionCalls()) {
            if (functionCall.getName().equals("fetchWeather")) {
              Map<String, JsonElement> args = functionCall.getArgs();
              JsonObject locationJsonObject = JsonElementKt.getJsonObject(args.get("location"));
              String city =
                JsonElementKt.getContentOrNull(
                  JsonElementKt.getJsonPrimitive(locationJsonObject.get("city")));
              String state =
                JsonElementKt.getContentOrNull(
                  JsonElementKt.getJsonPrimitive(locationJsonObject.get("state")));
              Location location = new Location(city, state);

              String date =
                JsonElementKt.getContentOrNull(
                  JsonElementKt.getJsonPrimitive(args.get("date")));
              return fetchWeather(location, date);
            }
          }
          return null;
        },
        Executors.newSingleThreadExecutor());
    // [END function_calling_generate_function_call]

    // [START function_calling_pass_back_function_response]
    ListenableFuture<GenerateContentResponse> modelResponseFuture =
      Futures.transformAsync(
        handleFunctionCallFuture,
        // Send the response(s) from the function back to the model
        // so that the model can use it to generate its final response.
        functionCallResult ->
          chatFutures.sendMessage(
            new Content(
              "function",
              List.of(new FunctionResponsePart("fetchWeather", functionCallResult)))),
        Executors.newSingleThreadExecutor());

    Futures.addCallback(
      modelResponseFuture,
      new FutureCallback<GenerateContentResponse>() {
        @Override
        public void onSuccess(GenerateContentResponse result) {
          if (result.getText() != null) {
            // Log the text response.
            System.out.println(result.getText());
          }
        }

        @Override
        public void onFailure(Throwable t) {
          // handle error
        }
      },
      Executors.newSingleThreadExecutor());
    // [END function_calling_pass_back_function_response]
  }

  // [START function_calling_write_function]
  // This function calls a hypothetical external API that returns
  // a collection of weather information for a given location on a given date.
  // `location` is an object of the form { city: string, state: string }
  public JsonObject fetchWeather(Location location, String date) {

    // TODO(developer): Write a standard function that would call to an external weather API.

    // For demo purposes, this hypothetical response is hardcoded here in the expected format.
    return new JsonObject(
      Map.of(
        "temperature",
        JsonElementKt.JsonPrimitive(38),
        "chancePrecipitation",
        JsonElementKt.JsonPrimitive("56%"),
        "cloudConditions",
        JsonElementKt.JsonPrimitive("partlyCloudy")
      )
    );
  }
  // [END function_calling_write_function]
}

class Location {
  public String city;
  public String state;

  public Location(String city, String state) {
    this.city = city;
    this.state = state;
  }
}