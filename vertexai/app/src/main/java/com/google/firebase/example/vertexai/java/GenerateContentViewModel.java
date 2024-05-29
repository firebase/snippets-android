package com.google.firebase.example.vertexai.java;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.example.vertexai.R;
import com.google.firebase.vertexai.FirebaseVertexAI;
import com.google.firebase.vertexai.GenerativeModel;
import com.google.firebase.vertexai.java.GenerativeModelFutures;
import com.google.firebase.vertexai.type.Content;
import com.google.firebase.vertexai.type.CountTokensResponse;
import com.google.firebase.vertexai.type.GenerateContentResponse;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executor;

public class GenerateContentViewModel extends ViewModel {
    private GenerativeModelFutures model;

    // Only meant to separate the scope of the initialization snippet
    // so that it doesn't cause a naming clash with the top level declaration
    static class InitializationSnippet {
        // [START initialize_model]
        GenerativeModel gm = FirebaseVertexAI.getInstance()
                .generativeModel("gemini-1.5-pro-preview-0409");

        GenerativeModelFutures model = GenerativeModelFutures.from(gm);
        // [END initialize_model]
    }

    void generateContentStream() {
        // [START text_gen_text_only_prompt_streaming]
        Content prompt = new Content.Builder()
                .addText("Write a story about a magic backpack.")
                .build();

        Publisher<GenerateContentResponse> streamingResponse =
                model.generateContentStream(prompt);

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

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onSubscribe(Subscription s) {
            }
        });
        // [END text_gen_text_only_prompt_streaming]
    }

    void generateContent(Executor executor) {
        // [START text_gen_text_only_prompt]
        // Provide a prompt that contains text
        Content prompt = new Content.Builder()
                .addText("Write a story about a magic backpack.")
                .build();

        // To generate text output, call generateContent with the text input
        ListenableFuture<GenerateContentResponse> response = model.generateContent(prompt);
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
        // [END text_gen_text_only_prompt]
    }

    // Fake implementation to exemplify Activity.getResources()
    Resources getResources() {
        return null;
    }

    // Fake implementation to exemplify Activity.getApplicationContext()
    Context getApplicationContext() {
        return null;
    }

    void generateContentWithImageStream() {
        // [START text_gen_multimodal_one_image_prompt_streaming]
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sparky);

        Content prompt = new Content.Builder()
                .addImage(bitmap)
                .addText("Write a story about a magic backpack.")
                .build();

        Publisher<GenerateContentResponse> streamingResponse =
                model.generateContentStream(prompt);

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

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onSubscribe(Subscription s) {
            }
        });
        // [END text_gen_multimodal_one_image_prompt_streaming]
    }

    void generateContentWithImage(Executor executor) {
        // [START text_gen_multimodal_one_image_prompt]
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sparky);

        Content content = new Content.Builder()
                .addImage(bitmap)
                .addText("What developer tool is this mascot from?")
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
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
        // [END text_gen_multimodal_one_image_prompt]
    }

    void generateContentWithMultipleImagesStream() {
        // [START text_gen_multimodal_multi_image_prompt_streaming]
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.sparky);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.sparky_eats_pizza);

        // Provide a prompt that includes the images specifed above and text
        Content prompt = new Content.Builder()
                .addImage(bitmap1)
                .addImage(bitmap2)
                .addText("What's different between these pictures?")
                .build();

        Publisher<GenerateContentResponse> streamingResponse =
                model.generateContentStream(prompt);

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

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onSubscribe(Subscription s) {
            }
        });
        // [END text_gen_multimodal_multi_image_prompt_streaming]
    }

    void generateContentWithMultipleImages(Executor executor) {
        // [START text_gen_multimodal_multi_image_prompt]
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.sparky);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.sparky_eats_pizza);

        // Provide a prompt that includes the images specifed above and text
        Content prompt = new Content.Builder()
                .addImage(bitmap1)
                .addImage(bitmap2)
                .addText("What's different between these pictures?")
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(prompt);
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
        // [END text_gen_multimodal_multi_image_prompt]
    }

    void generateContentWithVideo(Executor executor, Uri videoUri) {
        // [START text_gen_multimodal_video_prompt]
        ContentResolver resolver = getApplicationContext().getContentResolver();
        try (InputStream stream = resolver.openInputStream(videoUri)) {
            File videoFile = new File(new URI(videoUri.toString()));
            int videoSize = (int) videoFile.length();
            byte[] videoBytes = new byte[videoSize];
            if (stream != null) {
                stream.read(videoBytes, 0, videoBytes.length);
                stream.close();

                Content prompt = new Content.Builder()
                        .addBlob("video/mp4", videoBytes)
                        .addText("What is in the video?")
                        .build();

                ListenableFuture<GenerateContentResponse> response = model.generateContent(prompt);
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // [END text_gen_multimodal_video_prompt]
    }

    void generateContentWithVideoStream(
            Uri videoUri
    ) {
        // [START text_gen_multimodal_video_prompt_streaming]
        ContentResolver resolver = getApplicationContext().getContentResolver();
        try (InputStream stream = resolver.openInputStream(videoUri)) {
            File videoFile = new File(new URI(videoUri.toString()));
            int videoSize = (int) videoFile.length();
            byte[] videoBytes = new byte[videoSize];
            if (stream != null) {
                stream.read(videoBytes, 0, videoBytes.length);
                stream.close();

                Content prompt = new Content.Builder()
                        .addBlob("video/mp4", videoBytes)
                        .addText("What is in the video?")
                        .build();

                Publisher<GenerateContentResponse> streamingResponse =
                        model.generateContentStream(prompt);

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

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onSubscribe(Subscription s) {
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // [END text_gen_multimodal_video_prompt_streaming]
    }

    void countTokensText(Executor executor) {
        // [START count_tokens_text]
        Content text = new Content.Builder()
                .addText("Write a story about a magic backpack.")
                .build();

        ListenableFuture<CountTokensResponse> countTokensResponse = model.countTokens(text);

        Futures.addCallback(countTokensResponse, new FutureCallback<CountTokensResponse>() {
            @Override
            public void onSuccess(CountTokensResponse result) {
                int totalTokens = result.getTotalTokens();
                int totalBillableTokens = result.getTotalBillableCharacters();
                System.out.println("totalTokens = " + totalTokens +
                        "totalBillableTokens = " + totalBillableTokens);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, executor);
        // [END count_tokens_text]
    }

    void countTokensMultimodal(Executor executor, Bitmap bitmap) {
        // [START count_tokens_text_image]
        Content text = new Content.Builder()
                .addImage(bitmap)
                .addText("Where can I buy this")
                .build();

        // For text-only input
        ListenableFuture<CountTokensResponse> countTokensResponse = model.countTokens(text);

        Futures.addCallback(countTokensResponse, new FutureCallback<CountTokensResponse>() {
            @Override
            public void onSuccess(CountTokensResponse result) {
                int totalTokens = result.getTotalTokens();
                int totalBillableTokens = result.getTotalBillableCharacters();
                System.out.println("totalTokens = " + totalTokens +
                        "totalBillableTokens = " + totalBillableTokens);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, executor);
        // [END count_tokens_text_image]
    }
}
