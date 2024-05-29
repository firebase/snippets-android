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
        // [START configure_model]
        val config = generationConfig {
            temperature = 0.9f
            topK = 16
            topP = 0.1f
            maxOutputTokens = 200
            stopSequences = listOf("red")
        }
        val generativeModel = Firebase.vertexAI.generativeModel(
            modelName = "{{generic_model_name_initialization}}",
            generationConfig = config
        )
        // [END configure_model]
    }

    fun configSafetySetting() {
        // [START safety_settings]
        val harassmentSafety = SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH)
        val hateSpeechSafety = SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE)

        val generativeModel = Firebase.vertexAI.generativeModel(
            modelName = "{{generic_model_name_initialization}}",
            safetySettings = listOf(
                SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH)
            )
        )
        // [END safety_settings]
    }

    fun configMultiSafetySettings() {
        // [START multi_safety_settings]
        val harassmentSafety = SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH)
        val hateSpeechSafety = SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE)

        val generativeModel = Firebase.vertexAI.generativeModel(
            modelName = "{{generic_model_name_initialization}}",
            safetySettings = listOf(harassmentSafety, hateSpeechSafety)
        )
        // [END multi_safety_settings]
    }
}