package com.source.subscity.api.deserializers

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.lang.reflect.ParameterizedType

/**
 * @author Vitaliy Markus
 */
object SubsCityTypeAdapterFactory : TypeAdapterFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        if (type.rawType == List::class.java && (type.type as ParameterizedType).actualTypeArguments[0] == String::class.java) {
            val delegate = gson.getDelegateAdapter(this, type)
            return ListStringTypeAdapter(delegate as TypeAdapter<Any>) as TypeAdapter<T>
        }
        return null
    }

    @Suppress("UNCHECKED_CAST")
    class ListStringTypeAdapter(private val delegate: TypeAdapter<Any>) : TypeAdapter<List<String>>() {

        override fun read(reader: JsonReader): List<String> {
            return if (reader.peek() === JsonToken.NULL) {
                reader.nextNull()
                emptyList()
            } else {
                delegate.read(reader) as List<String>
            }
        }

        override fun write(writer: JsonWriter, value: List<String>) {
            delegate.write(writer, value)
        }

    }
}