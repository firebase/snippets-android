package com.google.firebase.example.ailogic.kotlin;

import androidx.lifecycle.ViewModel
import com.google.firebase.ai.FirebaseAI
import com.google.firebase.ai.type.FunctionDeclaration
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.Tool

class VertexAISnippets : ViewModel() {
  fun functionCalling(fetchWeatherTool: FunctionDeclaration) {
    // [START function_calling_specify_declaration_during_init]
    // Initialize the Vertex AI Gemini API backend service
    // Optionally specify the location to access the model (`global` is recommended)
    // Create a `GenerativeModel` instance with a model that supports your use case
    val model =
      FirebaseAI.getInstance(backend = GenerativeBackend.vertexAI(location = "global"))
        .generativeModel(
          modelName = "gemini-2.5-flash",
          // Provide the function declaration to the model.
          tools = listOf(Tool.functionDeclarations(listOf(fetchWeatherTool)))
        )
    // [END function_calling_specify_declaration_during_init]
  }
}