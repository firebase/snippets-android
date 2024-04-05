package com.google.firebase.example.vertexai.kotlin

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.vertexai.GenerativeModel
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.vertexAI
import kotlinx.coroutines.launch

@Suppress("JoinDeclarationAndAssignment") // for the generativeModel var
class ChatViewModel : ViewModel() {
    private val TAG = "ChatViewModel"
    private var generativeModel: GenerativeModel

    init {
        generativeModel = Firebase.vertexAI.generativeModel("gemini-1.5-pro")
    }

    fun startChatSendMessageStream() {
        viewModelScope.launch {
            // [START vertexai_send_message_stream]
            val chat = generativeModel.startChat(
                history = listOf(
                    content(role = "user") { text("Hello, I have 2 dogs in my house.") },
                    content(role = "model") { text("Great to meet you. What would you like to know?") }
                )
            )

            chat.sendMessageStream("How many paws are in my house?").collect { chunk ->
                Log.d(TAG, chunk.text ?: "")
            }
            // [END vertexai_send_message_stream]
        }
    }

    fun startChatSendMessage() {
        viewModelScope.launch {
            // [START vertexai_send_message]
            val chat = generativeModel.startChat(
                history = listOf(
                    content(role = "user") { text("Hello, I have 2 dogs in my house.") },
                    content(role = "model") { text("Great to meet you. What would you like to know?") }
                )
            )

            val response = chat.sendMessage("How many paws are in my house?")
            Log.d(TAG, response.text ?: "")
            // [END vertexai_send_message]
        }
    }

    fun countTokensChat() {
        viewModelScope.launch {
            val chat = generativeModel.startChat()
            // [START vertexai_count_tokens_chat]
            // Count tokens for a chat prompt
            val history = chat.history
            val messageContent = content { text("This is the message I intend to send") }
            val (totalTokens) = generativeModel.countTokens(*history.toTypedArray(), messageContent)
            // [END vertexai_count_tokens_chat]
        }
    }
}