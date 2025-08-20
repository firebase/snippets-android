package com.google.firebase.example.ailogic.kotlin;

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.FunctionDeclaration
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.Tool

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
          tools = listOf(Tool.functionDeclarations(listOf(fetchWeatherTool)))
        )
    // [END function_calling_specify_declaration_during_init]
  }
}