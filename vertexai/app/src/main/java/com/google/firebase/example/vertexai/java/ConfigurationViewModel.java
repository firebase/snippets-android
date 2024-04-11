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
        // [START vertexai_model_params]
        GenerationConfig.Builder configBuilder = new GenerationConfig.Builder();
        configBuilder.temperature = 0.9f;
        configBuilder.topK = 16;
        configBuilder.topP = 0.1f;
        configBuilder.maxOutputTokens = 200;
        configBuilder.stopSequences = List.of("red");

        GenerationConfig generationConfig = configBuilder.build();

        GenerativeModel gm = FirebaseVertexAI.Companion.getInstance().generativeModel(
                "MODEL_NAME",
                generationConfig
        );

        GenerativeModelFutures model = GenerativeModelFutures.from(gm);
        // [END vertexai_model_params]
    }

    void configSafetySettings() {
        SafetySetting harassmentSafety1 = new SafetySetting(HarmCategory.HARASSMENT,
                BlockThreshold.ONLY_HIGH);

        GenerativeModel gm1 = FirebaseVertexAI.Companion.getInstance().generativeModel(
                "MODEL_NAME",
                /* generationConfig is optional */ null,
                Collections.singletonList(harassmentSafety1)
        );

        GenerativeModelFutures model1 = GenerativeModelFutures.from(gm1);

        // [START vertexai_safety_settings]
        SafetySetting harassmentSafety = new SafetySetting(HarmCategory.HARASSMENT,
                BlockThreshold.ONLY_HIGH);

        SafetySetting hateSpeechSafety = new SafetySetting(HarmCategory.HATE_SPEECH,
                BlockThreshold.MEDIUM_AND_ABOVE);

        GenerativeModel gm = FirebaseVertexAI.Companion.getInstance().generativeModel(
                "MODEL_NAME",
                /* generationConfig is optional */ null,
                List.of(harassmentSafety, hateSpeechSafety)
        );

        GenerativeModelFutures model = GenerativeModelFutures.from(gm);
        // [END vertexai_safety_settings]
    }
}
