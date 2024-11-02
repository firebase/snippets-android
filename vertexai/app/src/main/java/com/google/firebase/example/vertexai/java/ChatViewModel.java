package com.google.firebase.example.vertexai.java;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.vertexai.Chat;
import com.google.firebase.vertexai.FirebaseVertexAI;
import com.google.firebase.vertexai.GenerativeModel;
import com.google.firebase.vertexai.java.ChatFutures;
import com.google.firebase.vertexai.java.GenerativeModelFutures;
import com.google.firebase.vertexai.type.Content;
import com.google.firebase.vertexai.type.CountTokensResponse;
import com.google.firebase.vertexai.type.GenerateContentResponse;
import com.google.firebase.vertexai.type.RequestOptions;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class ChatViewModel extends ViewModel {
    private GenerativeModelFutures model;

    void startChatSendMessageStream() {
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

    void startChatSendMessage(Executor executor) {
        // [START chat]
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
            public void onFailure(@NonNull Throwable t) {
                t.printStackTrace();
            }
        }, executor);
        // [END chat]
    }

    void countTokensChat(Executor executor) {
        ChatFutures chat = model.startChat();
        // [START count_tokens_chat]
        List<Content> history = chat.getChat().getHistory();

        Content messageContent = new Content.Builder()
                .addText("This is the message I intend to send")
                .build();

        Collections.addAll(history, messageContent);

        ListenableFuture<CountTokensResponse> countTokensResponse =
                model.countTokens(history.toArray(new Content[0]));
        Futures.addCallback(countTokensResponse, new FutureCallback<>() {
            @Override
            public void onSuccess(CountTokensResponse result) {
                int totalTokens = result.getTotalTokens();
                int totalBillableTokens = result.getTotalBillableCharacters();
                System.out.println("totalTokens = " + totalTokens +
                        "totalBillableTokens = " + totalBillableTokens);
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                t.printStackTrace();
            }
        }, executor);
        // [END count_tokens_chat]
    }

    void systemInstructionsText() {
        // [START system_instructions_text]
        // Initialize the Vertex AI service and the generative model
        // Specify a model that supports system instructions, like a Gemini 1.5 model
        Content systemInstruction = new Content.Builder()
                .addText("You are a cat. Your name is Neko.")
                .build();
        GenerativeModel model = FirebaseVertexAI.getInstance()
                .generativeModel(
                /* modelName */ "gemini-1.5-flash",
                /* generationConfig (optional) */ null,
                /* safetySettings (optional) */ null,
                /* requestOptions (optional) */ new RequestOptions(),
                /* tools (optional) */ null,
                /* toolsConfig (optional) */ null,
                /* systemInstruction (optional) */ systemInstruction
        );
        // [END system_instructions_text]
    }
}
