package dsb.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dsb.model.RepresentationPlan
import java.io.File

class JSONReader(val file: File) {

    fun read(): Array<RepresentationPlan>? {
        val mapper = ObjectMapper().registerKotlinModule()
        return try {
            mapper.readValue(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}