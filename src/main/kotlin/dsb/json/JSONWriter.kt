package dsb.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dsb.model.RepresentationPlan
import java.io.File

class JSONWriter(val file: File, val plans: Array<RepresentationPlan>) {

    fun write() {
        val mapper = ObjectMapper().registerKotlinModule()
        if(!file.exists())file.createNewFile()
        mapper.writeValue(file, plans)
    }
}