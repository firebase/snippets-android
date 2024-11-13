package com.google.firebase.example.vertexai.kotlin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.vertexai.type.FunctionDeclaration
import com.google.firebase.vertexai.type.Schema
import com.google.firebase.vertexai.type.Tool
import com.google.firebase.vertexai.vertexAI
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class FunctionCallViewModel : ViewModel() {

    // [START vertexai_fc_create_function]
    // This function calls a hypothetical external API that returns
    // a collection of weather information for a given location on a given date.
    // `location` is an object of the form { city: string, state: string }
    data class Location(val city: String, val state: String)

    suspend fun fetchWeather(location: Location, date: String): JsonObject {

        // TODO(developer): Write a standard function that would call to an external weather API.

        // For demo purposes, this hypothetical response is hardcoded here in the expected format.
        return JsonObject(mapOf(
            "temperature" to JsonPrimitive(38),
            "chancePrecipitation" to JsonPrimitive("56%"),
            "cloudConditions" to JsonPrimitive("partlyCloudy")
        ))
    }
    // [END vertexai_fc_create_function]

    // [START vertexai_fc_func_declaration]
    val fetchWeatherTool = FunctionDeclaration(
        "fetchWeather",
        "Get the weather conditions for a specific city on a specific date.",
        mapOf(
            "location" to Schema.obj(
                mapOf(
                    "city" to Schema.string("The city of the location."),
                    "state" to Schema.string("The US state of the location."),
                ),
                description = "The name of the city and its state for which " +
                        "to get the weather. Only cities in the " +
                        "USA are supported."
            ),
            "date" to Schema.string("The date for which to get the weather." +
                    " Date must be in the format: YYYY-MM-DD."
            ),
        ),
    )
    // [END vertexai_fc_func_declaration]

    // [START vertexai_fc_init]
    // Initialize the Vertex AI service and the generative model
    // Use a model that supports function calling, like a Gemini 1.5 model
    val model = Firebase.vertexAI.generativeModel(
        modelName = "gemini-1.5-flash",
        // Provide the function declaration to the model.
        tools = listOf(Tool.functionDeclarations(listOf(fetchWeatherTool)))
    )
    // [END vertexai_fc_init]

    // [START vertexai_fc_generate]
    fun generateFunctionCall() {
        viewModelScope.launch {
            val prompt = "What was the weather in Boston on October 17, 2024?"
            val chat = model.startChat()
            // Send the user's question (the prompt) to the model using multi-turn chat.
            val result = chat.sendMessage(prompt)

            val functionCalls = result.functionCalls
            // When the model responds with one or more function calls, invoke the function(s).
            val fetchWeatherCall = functionCalls.find { it.name == "fetchWeather" }

            // Forward the structured input data prepared by the model
            // to the hypothetical external API.
            val functionResponse = fetchWeatherCall?.let {
                // Alternatively, if your `Location` class is marked as @Serializable, you can use
                // val location = Json.decodeFromJsonElement<Location>(it.args["location"]!!)
                val location = Location(
                    it.args["location"]!!.jsonObject["city"]!!.jsonPrimitive.content,
                    it.args["location"]!!.jsonObject["state"]!!.jsonPrimitive.content
                )
                val date = it.args["date"]!!.jsonPrimitive.content
                fetchWeather(location, date)
            }
        }
    }
    // [END vertexai_fc_generate]
}
