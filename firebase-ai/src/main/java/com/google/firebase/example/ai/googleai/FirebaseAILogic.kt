package com.google.firebase.example.ai.googleai

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend

class FirebaseAILogic {

  private fun initialization() {
    // [START initialize_googleai_and_model]
    // Initialize the Gemini Developer API backend service
    // Create a `GenerativeModel` instance with a model that supports your use case
    val model = Firebase.ai(backend = GenerativeBackend.googleAI())
      .generativeModel("gemini-2.0-flash")
    // [END initialize_googleai_and_model]
  }


}
