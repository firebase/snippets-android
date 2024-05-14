package com.google.firebase.example.vertexai.kotlin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.vertexai.GenerativeModel
import com.google.firebase.vertexai.type.FunctionResponsePart
import com.google.firebase.vertexai.type.InvalidStateException
import com.google.firebase.vertexai.type.Schema
import com.google.firebase.vertexai.type.Tool
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.type.defineFunction
import com.google.firebase.vertexai.vertexAI
import kotlinx.coroutines.launch
import org.json.JSONObject

class FunctionCallViewModel : ViewModel() {

    // [START vertexai_fc_create_function]
    suspend fun makeApiRequest(
        currencyFrom: String,
        currencyTo: String
    ): JSONObject {
        // This hypothetical API returns a JSON such as:
        // {"base":"USD","rates":{"SEK": 10.99}}
        return JSONObject().apply {
            put("base", currencyFrom)
            put("rates", hashMapOf(currencyTo to 10.99))
        }
    }
    // [END vertexai_fc_create_function]

    // [START vertexai_fc_func_declaration]
    val getExchangeRate = defineFunction(
        name = "getExchangeRate",
        description = "Get the exchange rate for currencies between countries",
        Schema.str("currencyFrom", "The currency to convert from."),
        Schema.str("currencyTo", "The currency to convert to.")
    ) { from, to ->
        // Call the function that you declared above
        makeApiRequest(from, to)
    }
    // [END vertexai_fc_func_declaration]

    // [START vertexai_fc_init]
    // Initialize the Vertex AI service and the generative model
    // Use a model that supports function calling, like Gemini 1.0 Pro.
    val generativeModel = Firebase.vertexAI.generativeModel(
        modelName = "gemini-1.0-pro",
        // Specify the function declaration.
        tools = listOf(Tool(listOf(getExchangeRate)))
    )
    // [END vertexai_fc_init]

    // [START vertexai_fc_generate]
    fun generateFunctionCall() {
        viewModelScope.launch {
            val chat = generativeModel.startChat()

            val prompt = "How much is 50 US dollars worth in Swedish krona?"

            // Send the message to the generative model
            var response = chat.sendMessage(prompt)

            // Check if the model responded with a function call
            response.functionCalls.firstOrNull()?.let { functionCall ->
                // Try to retrieve the stored lambda from the model's tools and
                // throw an exception if the returned function was not declared
                val matchedFunction = generativeModel.tools?.flatMap { it.functionDeclarations }
                    ?.first { it.name == functionCall.name }
                    ?: throw InvalidStateException("Function not found: ${functionCall.name}")

                // Call the lambda retrieved above
                val apiResponse: JSONObject = matchedFunction.execute(functionCall)

                // Send the API response back to the generative model
                // so that it generates a text response that can be displayed to the user
                response = chat.sendMessage(
                    content(role = "function") {
                        part(FunctionResponsePart(functionCall.name, apiResponse))
                    }
                )
            }

            // Whenever the model responds with text, show it in the UI
            response.text?.let { modelResponse ->
                println(modelResponse)
            }
        }
    }
    // [END vertexai_fc_generate]
}