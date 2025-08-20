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
import com.google.firebase.ai.type.GenerateContentResponse;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class CommonSnippets extends ViewModel {
  private Executor executor;
  private GenerativeModel ai = FirebaseAI.getInstance()
    .generativeModel("gemini-2.5-flash");
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
    Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
      @Override
      public void onSuccess(GenerateContentResponse result) {
        String resultText = result.getText();
        System.out.println(resultText);
      }

      @Override
      public void onFailure(Throwable t) {
        t.printStackTrace();
      }
    }, executor);
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
    Publisher<GenerateContentResponse> streamingResponse =
      chat.sendMessageStream(message);

    final String[] fullResponse = {""};

    streamingResponse.subscribe(new Subscriber<GenerateContentResponse>() {

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
}