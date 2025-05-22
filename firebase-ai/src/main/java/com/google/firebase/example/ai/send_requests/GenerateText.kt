package com.google.firebase.example.ai.send_requests

import com.google.firebase.ai.GenerativeModel

class GenerateText(
  private val model: GenerativeModel
) {

  private suspend fun textOnlyNonStreaming() {
    // [START text_only_non_streaming]
    // Provide a prompt that contains text
    val prompt = "Write a story about a magic backpack."

    // To generate text output, call generateContent with the text input
    val response = model.generateContent(prompt)
    print(response.text)
    // [END text_only_non_streaming]
  }

  private suspend fun textOnlyStreaming() {
    // [START text_only_streaming]
    // Provide a prompt that includes only text
    val prompt = "Write a story about a magic backpack."

    // To stream generated text output, call generateContentStream and pass in the prompt
    var response = ""
    model.generateContentStream(prompt).collect { chunk ->
      print(chunk.text)
      response += chunk.text
    }
    // [END text_only_streaming]
  }
}