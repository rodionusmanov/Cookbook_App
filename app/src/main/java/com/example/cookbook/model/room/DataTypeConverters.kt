package com.example.cookbook.model.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.Collections

class DataTypeConverters {

    private val gson = Gson()
    private val listTypeListString: Type = object : TypeToken<List<String>>() {}.type

    @TypeConverter
    fun stringToList(data: String): List<String> {
        if (data == null) {
            return Collections.emptyList()
        }
        return gson.fromJson(data, listTypeListString)
    }

    @TypeConverter
    fun listToString(someObjects: List<String>): String {
        return gson.toJson(someObjects)
    }

    @TypeConverter
    fun stringToDouble(data: String): Double {
        if (data == null) {
            return 0.0
        }
        val listType: Type = object : TypeToken<Double>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun DoubleToString(someObjects: Double): String {
        return gson.toJson(someObjects)
    }
}