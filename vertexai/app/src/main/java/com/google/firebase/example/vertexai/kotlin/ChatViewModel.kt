package com.google.firebase.example.vertexai.kotlin

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.vertexai.GenerativeModel
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.vertexAI

@Suppress("JoinDeclarationAndAssignment") // for the generativeModel var
class ChatViewModel : ViewModel() {

    private var generativeModel: GenerativeModel

    init {
        generativeModel = Firebase.vertexAI.generativeModel("gemini-1.5-pro")
    }

    suspend fun startChatSendMessageStream() {
        // [START vertexai_send_message_stream]
        val chat = generativeModel.startChat(
            history = listOf(
                content(role = "user") { text("Hello, I have 2 dogs in my house.") },
                content(role = "model") { text("Great to meet you. What would you like to know?") }
            )
        )

        chat.sendMessageStream("How many paws are in my house?").collect { chunk ->
            print(chunk.text)
        }
        // [END vertexai_send_message_stream]
    }

    suspend fun startChatSendMessage(resources: Resources) {
        // [START vertexai_send_message]
        val chat = generativeModel.startChat(
            history = listOf(
                content(role = "user") { text("Hello, I have 2 dogs in my house.") },
                content(role = "model") { text("Great to meet you. What would you like to know?") }
            )
        )

        val response = chat.sendMessage("How many paws are in my house?")
        print(response.text)
        // [END vertexai_send_message]
    }
}