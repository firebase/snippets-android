package com.google.firebase.example.ailogic.kotlin

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.FunctionDeclaration
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.HarmBlockThreshold
import com.google.firebase.ai.type.HarmCategory
import com.google.firebase.ai.type.ImagenAspectRatio
import com.google.firebase.ai.type.ImagenImageFormat
import com.google.firebase.ai.type.ImagenPersonFilterLevel
import com.google.firebase.ai.type.ImagenSafetyFilterLevel
import com.google.firebase.ai.type.ImagenSafetySettings
import com.google.firebase.ai.type.PublicPreviewAPI
import com.google.firebase.ai.type.ResponseModality
import com.google.firebase.ai.type.SafetySetting
import com.google.firebase.ai.type.SpeechConfig
import com.google.firebase.ai.type.Tool
import com.google.firebase.ai.type.Voice
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import com.google.firebase.ai.type.imagenGenerationConfig
import com.google.firebase.ai.type.liveGenerationConfig

class GoogleAISnippets : ViewModel() {
  fun functionCalling(fetchWeatherTool: FunctionDeclaration) {
    // [START function_calling_specify_declaration_during_init]
    // Initialize the Gemini Developer API backend service
    // Create a `GenerativeModel` instance with a model that supports your use case
    val model =
      Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(
          modelName = "gemini-2.5-flash",
          // Provide the function declaration to the model.
          tools = listOf(Tool.functionDeclarations(listOf(fetchWeatherTool))),
        )
    // [END function_calling_specify_declaration_during_init]
  }

  fun modelConfiguration_model_parameters_general() {
    // [START model_parameters_general]
    // ...

    // Set parameter values in a `GenerationConfig` (example values shown here)
    val config = generationConfig {
      maxOutputTokens = 200
      stopSequences = listOf("red")
      temperature = 0.9f
      topK = 16
      topP = 0.1f
    }

    // Initialize the Gemini Developer API backend service
    // Specify the config as part of creating the `GenerativeModel` instance
    val model =
      Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(
          modelName = "gemini-2.5-flash",
          generationConfig = config)

    // ...
    // [END model_parameters_general]
  }

  @OptIn(PublicPreviewAPI::class)
  fun modelConfiguration_model_parameters_imagen() {
    // [START model_parameters_imagen]
    // ...

    // Set parameter values in a `ImagenGenerationConfig` (example values shown here)
    val config = imagenGenerationConfig {
      negativePrompt = "frogs"
      numberOfImages = 2
      aspectRatio = ImagenAspectRatio.LANDSCAPE_16x9
      imageFormat = ImagenImageFormat.jpeg(compressionQuality = 100)
      addWatermark = false
    }

    // Initialize the Gemini Developer API backend service
    // Specify the config as part of creating the `GenerativeModel` instance
    val model =
      Firebase.ai(backend = GenerativeBackend.vertexAI())
        .imagenModel(
          modelName = "imagen-4.0-generate-001",
          generationConfig = config)

    // ...
    // [END model_parameters_imagen]
  }

  @OptIn(PublicPreviewAPI::class)
  fun modelConfiguration_model_parameters_live() {
    // [START model_parameters_live]
    // ...

    // Set parameter values in a `LiveGenerationConfig` (example values shown here)
    val config = liveGenerationConfig {
      maxOutputTokens = 200
      responseModality = ResponseModality.AUDIO
      speechConfig = SpeechConfig(Voice("FENRIR"))
      temperature = 0.9f
      topK = 16
      topP = 0.1f
    }

    // Initialize the Gemini Developer API backend service
    // Specify the config as part of creating the `LiveModel` instance
    val model =
      Firebase.ai(backend = GenerativeBackend.googleAI())
        .liveModel(
          modelName = "gemini-2.5-flash",
          generationConfig = config)

    // ...
    // [END model_parameters_live]
  }

  @OptIn(PublicPreviewAPI::class)
  fun modelConfiguration_safety_settings_imagen() {
    // [START safety_settings_imagen]
    // Specify the safety settings as part of creating the `ImagenModel` instance
    val model =
      Firebase.ai(backend = GenerativeBackend.googleAI())
        .imagenModel(
          modelName = "imagen-4.0-generate-001",
          safetySettings =
            ImagenSafetySettings(
              safetyFilterLevel = ImagenSafetyFilterLevel.BLOCK_LOW_AND_ABOVE,
              personFilterLevel = ImagenPersonFilterLevel.BLOCK_ALL,
            ),
        )

    // ...
    // [END safety_settings_imagen]
  }

  fun modelConfiguration_safety_settings_multiple() {
    // [START safety_settings_multiple]
    val harassmentSafety = SafetySetting(HarmCategory.HARASSMENT, HarmBlockThreshold.ONLY_HIGH)
    val hateSpeechSafety =
      SafetySetting(HarmCategory.HATE_SPEECH, HarmBlockThreshold.MEDIUM_AND_ABOVE)

    // Specify the safety settings as part of creating the `GenerativeModel` instance
    val model =
      Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(
          modelName = "gemini-2.5-flash",
          safetySettings = listOf(harassmentSafety, hateSpeechSafety),
        )

    // ...
    // [END safety_settings_multiple]
  }

  fun modelConfiguration_safety_settings_single() {
    // [START safety_settings_single]
    // Specify the safety settings as part of creating the `GenerativeModel` instance
    val model =
      Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(
          modelName = "gemini-2.5-flash",
          safetySettings =
            listOf(SafetySetting(HarmCategory.HARASSMENT, HarmBlockThreshold.ONLY_HIGH)),
        )

    // ...
    // [END safety_settings_single]
  }

  fun systemInstructions_general() {
    // [START system_instructions_general]
    // Specify the system instructions as part of creating the `GenerativeModel` instance
    val model = Firebase.ai(backend = GenerativeBackend.googleAI())
      .generativeModel(
        modelName = "gemini-2.5-flash",
        systemInstruction = content { text("You are a cat. Your name is Neko.") }
      )
    // [END system_instructions_general]
  }

  @OptIn(PublicPreviewAPI::class)
  fun systemInstructions_live() {
    // [START system_instructions_live]
    // Specify the system instructions as part of creating the `LiveModel` instance
    val model = Firebase.ai(backend = GenerativeBackend.googleAI())
      .liveModel(
        modelName = "gemini-2.5-flash",
        systemInstruction = content { text("You are a cat. Your name is Neko.") }
      )
    // [END system_instructions_live]
  }
}