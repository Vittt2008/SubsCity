package com.markus.subscity.api.client.converters

import com.markus.subscity.api.annotations.OnlyDate
import org.joda.time.DateTime
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import kotlin.reflect.KClass

/**
 * @author Vitaliy Markus
 */
class StringConverterFactory : Converter.Factory() {

    private val onlyDateConverter = OnlyDateConverter()

    override fun stringConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<*, String>? {
        return if (annotations.hasAnnotation(OnlyDate::class)) onlyDateConverter else null
    }

    private fun Array<out Annotation>?.hasAnnotation(kClass: KClass<*>): Boolean {
        return this?.any { it.annotationClass == kClass } ?: false
    }

    class OnlyDateConverter : Converter<DateTime, String> {

        override fun convert(value: DateTime): String {
            return value.toString("yyyy-MM-dd")
        }
    }

}