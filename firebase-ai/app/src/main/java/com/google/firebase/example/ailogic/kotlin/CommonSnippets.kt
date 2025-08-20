package com.google.firebase.example.ailogic.kotlin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.type.content
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CommonSnippets(
  private val generativeModel: GenerativeModel
) : ViewModel() {

  fun chat() {
    viewModelScope.launch {
      chatNonStreaming()
      chatStreaming()
    }
  }

  suspend fun chatNonStreaming() {
    // [START chat_non_streaming]
    // Initialize the chat
    val chat = generativeModel.startChat(
      history = listOf(
        content(role = "user") { text("Hello, I have 2 dogs in my house.") },
        content(role = "model") { text("Great to meet you. What would you like to know?") }
      )
    )

    val response = chat.sendMessage("How many paws are in my house?")
    print(response.text)
    // [END chat_non_streaming]
  }

  suspend fun chatStreaming() {
    // [START chat_streaming]
    // Initialize the chat
    val chat = generativeModel.startChat(
      history = listOf(
        content(role = "user") { text("Hello, I have 2 dogs in my house.") },
        content(role = "model") { text("Great to meet you. What would you like to know?") }
      )
    )

    chat.sendMessageStream("How many paws are in my house?").collect { chunk ->
      print(chunk.text)
    }
    // [END chat_streaming]
  }
}