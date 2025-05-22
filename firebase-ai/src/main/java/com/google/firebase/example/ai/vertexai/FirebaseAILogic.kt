package com.google.firebase.example.ai.vertexai

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend

class FirebaseAILogic {

  private fun initialization() {
    // [START initialize_vertexai_and_model]
    // Initialize the Vertex AI Gemini API backend service
    // Create a `GenerativeModel` instance with a model that supports your use case
    val model = Firebase.ai(backend = GenerativeBackend.vertexAI())
      .generativeModel("gemini-2.0-flash")
    // [END initialize_vertexai_and_model]
  }

}
