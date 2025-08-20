package com.google.firebase.example.ailogic.kotlin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.type.FunctionResponsePart
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.FunctionDeclaration
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

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
    val chat =
      generativeModel.startChat(
        history =
          listOf(
            content(role = "user") { text("Hello, I have 2 dogs in my house.") },
            content(role = "model") {
              text("Great to meet you. What would you like to know?")
            }
          )
      )

    val response = chat.sendMessage("How many paws are in my house?")
    print(response.text)
    // [END chat_non_streaming]
  }

  suspend fun chatStreaming() {
    // [START chat_streaming]
    // Initialize the chat
    val chat =
      generativeModel.startChat(
        history =
          listOf(
            content(role = "user") { text("Hello, I have 2 dogs in my house.") },
            content(role = "model") {
              text("Great to meet you. What would you like to know?")
            }
          )
      )

    chat.sendMessageStream("How many paws are in my house?").collect { chunk -> print(chunk.text) }
    // [END chat_streaming]
  }

  suspend fun functionCalling() {
    // [START function_calling_create_function_declaration]
    val fetchWeatherTool =
      FunctionDeclaration(
        "fetchWeather",
        "Get the weather conditions for a specific city on a specific date.",
        mapOf(
          "location" to
            Schema.obj(
              mapOf(
                "city" to Schema.string("The city of the location."),
                "state" to Schema.string("The US state of the location."),
              ),
              description =
                "The name of the city and its state for which " +
                  "to get the weather. Only cities in the " +
                  "USA are supported."
            ),
          "date" to
            Schema.string(
              "The date for which to get the weather." +
                " Date must be in the format: YYYY-MM-DD."
            ),
        ),
      )
    // [END function_calling_create_function_declaration]

    // [START function_calling_generate_function_call]
    val prompt = "What was the weather in Boston on October 17, 2024?"
    val chat = generativeModel.startChat()
    // Send the user's question (the prompt) to the model using multi-turn chat.
    val result = chat.sendMessage(prompt)

    val functionCalls = result.functionCalls
    // When the model responds with one or more function calls, invoke the function(s).
    val fetchWeatherCall = functionCalls.find { it.name == "fetchWeather" }

    // Forward the structured input data prepared by the model
    // to the hypothetical external API.
    val functionResponse =
      fetchWeatherCall?.let {
        // Alternatively, if your `Location` class is marked as @Serializable, you can use
        // val location = Json.decodeFromJsonElement<Location>(it.args["location"]!!)
        val location =
          Location(
            it.args["location"]!!.jsonObject["city"]!!.jsonPrimitive.content,
            it.args["location"]!!.jsonObject["state"]!!.jsonPrimitive.content
          )
        val date = it.args["date"]!!.jsonPrimitive.content
        fetchWeather(location, date)
      }
    // [END function_calling_generate_function_call]

    // [START function_calling_pass_back_function_response]
    // Send the response(s) from the function back to the model
    // so that the model can use it to generate its final response.
    val finalResponse =
      chat.sendMessage(
        content("function") { part(FunctionResponsePart("fetchWeather", functionResponse!!)) }
      )

    // Log the text response.
    println(finalResponse.text ?: "No text in response")
    // [END function_calling_pass_back_function_response]
  }

  // [START function_calling_write_function]
  // This function calls a hypothetical external API that returns
  // a collection of weather information for a given location on a given date.
  // `location` is an object of the form { city: string, state: string }
  data class Location(val city: String, val state: String)

  suspend fun fetchWeather(location: Location, date: String): JsonObject {

    // TODO(developer): Write a standard function that would call to an external weather API.

    // For demo purposes, this hypothetical response is hardcoded here in the expected format.
    return JsonObject(
      mapOf(
        "temperature" to JsonPrimitive(38),
        "chancePrecipitation" to JsonPrimitive("56%"),
        "cloudConditions" to JsonPrimitive("partlyCloudy")
      )
    )
  }
  // [END function_calling_write_function]
}
