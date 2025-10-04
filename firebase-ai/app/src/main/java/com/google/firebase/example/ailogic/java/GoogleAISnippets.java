package com.google.firebase.example.ailogic.java;

import androidx.annotation.OptIn;
import androidx.lifecycle.ViewModel;

import com.google.firebase.ai.FirebaseAI;
import com.google.firebase.ai.GenerativeModel;
import com.google.firebase.ai.LiveGenerativeModel;
import com.google.firebase.ai.java.GenerativeModelFutures;
import com.google.firebase.ai.java.ImagenModelFutures;
import com.google.firebase.ai.java.LiveModelFutures;
import com.google.firebase.ai.type.Content;
import com.google.firebase.ai.type.FunctionDeclaration;
import com.google.firebase.ai.type.GenerationConfig;
import com.google.firebase.ai.type.GenerativeBackend;
import com.google.firebase.ai.type.HarmBlockThreshold;
import com.google.firebase.ai.type.HarmCategory;
import com.google.firebase.ai.type.ImagenAspectRatio;
import com.google.firebase.ai.type.ImagenGenerationConfig;
import com.google.firebase.ai.type.ImagenImageFormat;
import com.google.firebase.ai.type.LiveGenerationConfig;
import com.google.firebase.ai.type.PublicPreviewAPI;
import com.google.firebase.ai.type.ResponseModality;
import com.google.firebase.ai.type.SafetySetting;
import com.google.firebase.ai.type.SpeechConfig;
import com.google.firebase.ai.type.Tool;
import com.google.firebase.ai.type.Voice;

import java.util.Collections;
import java.util.List;

public class GoogleAISnippets extends ViewModel {
  void functionCalling(FunctionDeclaration fetchWeatherTool) {
    // [START function_calling_specify_declaration_during_init]
    // Initialize the Gemini Developer API backend service
    // Create a `GenerativeModel` instance with a model that supports your use case
    GenerativeModelFutures model =
      GenerativeModelFutures.from(
        FirebaseAI.getInstance(GenerativeBackend.googleAI())
          .generativeModel(
            /* modelName */ "gemini-2.5-flash",
            /* generationConfig (optional) */ null,
            /* safetySettings (optional) */ null,
            // Provide the function declaration to the model.
            List.of(Tool.functionDeclarations(List.of(fetchWeatherTool)))));
    // [END function_calling_specify_declaration_during_init]
  }

  public void modelConfiguration_model_parameters_general() {
    // [START model_parameters_general]
    // ...

    // Set parameter values in a `GenerationConfig` (example values shown here)
    GenerationConfig.Builder configBuilder = new GenerationConfig.Builder();
    configBuilder.maxOutputTokens = 200;
    configBuilder.stopSequences = List.of("red");
    configBuilder.temperature = 0.9f;
    configBuilder.topK = 16;
    configBuilder.topP = 0.1f;

    GenerationConfig config = configBuilder.build();

    // Specify the config as part of creating the `GenerativeModel` instance
    GenerativeModelFutures model =
      GenerativeModelFutures.from(
        FirebaseAI.getInstance(GenerativeBackend.googleAI())
          .generativeModel(
            /* modelName */ "gemini-2.5-flash",
            /* generationConfig (optional) */ config));

    // ...
    // [END model_parameters_general]
  }

  @OptIn(markerClass = PublicPreviewAPI.class)
  public void modelConfiguration_model_parameters_imagen() {
    // [START model_parameters_imagen]
    // ...

    // Set parameter values in a `ImagenGenerationConfig` (example values shown here)
    ImagenGenerationConfig config =
      new ImagenGenerationConfig.Builder()
        .setNegativePrompt("frogs")
        .setNumberOfImages(2)
        .setAspectRatio(ImagenAspectRatio.LANDSCAPE_16x9)
        .setImageFormat(ImagenImageFormat.jpeg(100))
        .setAddWatermark(false)
        .build();

    // Specify the config as part of creating the `ImagenModel` instance
    ImagenModelFutures model =
      ImagenModelFutures.from(
        FirebaseAI.getInstance(GenerativeBackend.googleAI())
          .imagenModel(
            /* modelName */ "imagen-4.0-generate-001",
            /* imagenGenerationConfig (optional) */ config));

    // ...
    // [END model_parameters_imagen]
  }

  @OptIn(markerClass = PublicPreviewAPI.class)
  public void modelConfiguration_model_parameters_live() {
    // [START model_parameters_live]
    // ...

    // Set parameter values in a `LiveGenerationConfig` (example values shown here)
    LiveGenerationConfig.Builder configBuilder = new LiveGenerationConfig.Builder();
    configBuilder.setMaxOutputTokens(200);
    configBuilder.setResponseModality(ResponseModality.AUDIO);

    configBuilder.setSpeechConfig(new SpeechConfig(new Voice("FENRIR")));
    configBuilder.setTemperature(0.9f);
    configBuilder.topK = 16;
    configBuilder.topP = 0.1f;

    LiveGenerationConfig config = configBuilder.build();

    // Initialize the Gemini Developer API backend service
    // Specify the config as part of creating the `LiveModel` instance
    LiveModelFutures model =
      LiveModelFutures.from(
        FirebaseAI.getInstance(GenerativeBackend.googleAI())
          .liveModel(
            /* modelName */ "gemini-2.0-flash-live-preview-04-09",
            /* liveGenerationConfig (optional) */ config));

    // ...
    // [END model_parameters_live]
  }

  @OptIn(markerClass = PublicPreviewAPI.class)
  public void modelConfiguration_safety_settings_imagen() {
    // [START safety_settings_imagen]
    // Specify the safety settings as part of creating the `ImagenModel` instance
    ImagenModelFutures model =
      ImagenModelFutures.from(
        FirebaseAI.getInstance(GenerativeBackend.googleAI())
          .imagenModel(
            /* modelName */ "imagen-4.0-generate-001",
            /* imageGenerationConfig (optional) */ null));

    // ...
    // [END safety_settings_imagen]
  }

  public void modelConfiguration_safety_settings_multiple() {
    // [START safety_settings_multiple]
    SafetySetting harassmentSafety =
      new SafetySetting(HarmCategory.HARASSMENT, HarmBlockThreshold.ONLY_HIGH, null);

    SafetySetting hateSpeechSafety =
      new SafetySetting(HarmCategory.HATE_SPEECH, HarmBlockThreshold.MEDIUM_AND_ABOVE, null);

    // Specify the safety settings as part of creating the `GenerativeModel` instance
    GenerativeModelFutures model =
      GenerativeModelFutures.from(
        FirebaseAI.getInstance(GenerativeBackend.googleAI())
          .generativeModel(
            /* modelName */ "gemini-2.5-flash",
            /* generationConfig is optional */ null,
            List.of(harassmentSafety, hateSpeechSafety)));

    // ...
    // [END safety_settings_multiple]
  }

  public void modelConfiguration_safety_settings_single() {
    // [START safety_settings_single]
    SafetySetting harassmentSafety =
      new SafetySetting(HarmCategory.HARASSMENT, HarmBlockThreshold.ONLY_HIGH, null);

    // Specify the safety settings as part of creating the `GenerativeModel` instance
    GenerativeModelFutures model =
      GenerativeModelFutures.from(
        FirebaseAI.getInstance(GenerativeBackend.googleAI())
          .generativeModel(
            /* modelName */ "gemini-2.5-flash",
            /* generationConfig is optional */ null,
            Collections.singletonList(harassmentSafety)));

    // ...
    // [END safety_settings_single]
  }

  public void systemInstructions_general() {
    // [START system_instructions_general]
    // Specify the system instructions as part of creating the `GenerativeModel` instance
    GenerativeModel ai = FirebaseAI.getInstance(GenerativeBackend.googleAI())
      .generativeModel(
        /* modelName */ "gemini-2.5-flash",
        /* generationConfig (optional) */ null,
        /* safetySettings (optional) */ null,
        /* tools (optional) */ null,
        /* toolConfig (optional) */ null,
        /* systemInstructions (optional) */ new Content.Builder()
          .addText("You are a cat. Your name is Neko.")
          .build()
      );

    GenerativeModelFutures model = GenerativeModelFutures.from(ai);
    // [END system_instructions_general]
  }

  @OptIn(markerClass = PublicPreviewAPI.class)
  public void systemInstructions_live() {
    // [START system_instructions_live]
    // Specify the system instructions as part of creating the `LiveModel` instance
    LiveGenerativeModel ai = FirebaseAI.getInstance(GenerativeBackend.googleAI())
      .liveModel(
        /* modelName */ "gemini-2.0-flash-live-preview-04-09",
        /* liveGenerationConfig (optional) */ null,
        /* tools (optional) */ null,
        /* systemInstructions (optional) */ new Content.Builder()
          .addText("You are a cat. Your name is Neko.")
          .build()
      );

    LiveModelFutures model = LiveModelFutures.from(ai);
    // [END system_instructions_live]
  }
}