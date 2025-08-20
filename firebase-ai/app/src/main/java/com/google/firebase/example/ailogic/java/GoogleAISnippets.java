package com.google.firebase.example.ailogic.java;

import androidx.lifecycle.ViewModel;
import com.google.firebase.ai.FirebaseAI;
import com.google.firebase.ai.java.GenerativeModelFutures;
import com.google.firebase.ai.type.GenerativeBackend;
import com.google.firebase.ai.type.Tool;
import com.google.firebase.ai.type.FunctionDeclaration;
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
            "gemini-2.5-flash",
            null,
            null,
            // Provide the function declaration to the model.
            List.of(Tool.functionDeclarations(List.of(fetchWeatherTool)))));
    // [END function_calling_specify_declaration_during_init]
  }
}