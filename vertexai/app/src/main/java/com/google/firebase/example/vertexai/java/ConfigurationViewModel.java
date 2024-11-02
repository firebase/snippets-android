package com.google.firebase.example.vertexai.java;

import androidx.lifecycle.ViewModel;

import com.google.firebase.vertexai.FirebaseVertexAI;
import com.google.firebase.vertexai.GenerativeModel;
import com.google.firebase.vertexai.java.GenerativeModelFutures;
import com.google.firebase.vertexai.type.BlockThreshold;
import com.google.firebase.vertexai.type.GenerationConfig;
import com.google.firebase.vertexai.type.HarmCategory;
import com.google.firebase.vertexai.type.RequestOptions;
import com.google.firebase.vertexai.type.SafetySetting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConfigurationViewModel extends ViewModel {

    void configModelParams() {
        // [START configure_model]
        GenerationConfig.Builder configBuilder = new GenerationConfig.Builder();
        configBuilder.temperature = 0.9f;
        configBuilder.topK = 16;
        configBuilder.topP = 0.1f;
        configBuilder.maxOutputTokens = 200;
        configBuilder.stopSequences = List.of("red");

        GenerationConfig generationConfig = configBuilder.build();

        GenerativeModel gm = FirebaseVertexAI.Companion.getInstance().generativeModel(
                "gemini-1.5-flash",
                generationConfig
        );

        GenerativeModelFutures model = GenerativeModelFutures.from(gm);
        // [END configure_model]
    }

    void configSafetySettings() {
        // [START safety_settings]
        SafetySetting harassmentSafety = new SafetySetting(HarmCategory.HARASSMENT,
                BlockThreshold.ONLY_HIGH);

        GenerativeModel gm = FirebaseVertexAI.Companion.getInstance().generativeModel(
                "gemini-1.5-flash",
                /* generationConfig is optional */ null,
                Collections.singletonList(harassmentSafety)
        );

        GenerativeModelFutures model = GenerativeModelFutures.from(gm);
        // [END safety_settings]
    }

    void configMultiSafetySettings() {
        // [START multi_safety_settings]
        SafetySetting harassmentSafety = new SafetySetting(HarmCategory.HARASSMENT,
                BlockThreshold.ONLY_HIGH);

        SafetySetting hateSpeechSafety = new SafetySetting(HarmCategory.HATE_SPEECH,
                BlockThreshold.MEDIUM_AND_ABOVE);

        GenerativeModel gm = FirebaseVertexAI.Companion.getInstance().generativeModel(
                "gemini-1.5-flash",
                /* generationConfig is optional */ null,
                List.of(harassmentSafety, hateSpeechSafety)
        );

        GenerativeModelFutures model = GenerativeModelFutures.from(gm);
        // [END multi_safety_settings]
    }
}
