package com.google.firebase.example.vertexai.kotlin

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.vertexai.type.BlockThreshold
import com.google.firebase.vertexai.type.HarmCategory
import com.google.firebase.vertexai.type.SafetySetting
import com.google.firebase.vertexai.type.generationConfig
import com.google.firebase.vertexai.vertexAI

class ConfigurationViewModel : ViewModel() {

    fun configModelParams() {
        // [START vertexai_model_params]
        val config = generationConfig {
            temperature = 0.9f
            topK = 16
            topP = 0.1f
            maxOutputTokens = 200
            stopSequences = listOf("red")
        }
        val generativeModel = Firebase.vertexAI.generativeModel(
            modelName = "gemini-1.5-pro-preview-0409",
            generationConfig = config
        )
        // [END vertexai_model_params]
    }

    fun configSafetySettings() {
        val generativeModel1 = Firebase.vertexAI.generativeModel(
            modelName = "MODEL_NAME",
            safetySettings = listOf(
                SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH)
            )
        )

        // [START vertexai_safety_settings]
        val harassmentSafety = SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH)
        val hateSpeechSafety = SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE)

        val generativeModel = Firebase.vertexAI.generativeModel(
            modelName = "MODEL_NAME",
            safetySettings = listOf(harassmentSafety, hateSpeechSafety)
        )
        // [END vertexai_safety_settings]
    }
}