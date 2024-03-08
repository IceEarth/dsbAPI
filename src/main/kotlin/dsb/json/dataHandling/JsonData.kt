package dsb.json.dataHandling

import com.google.gson.Gson
import java.io.File
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Eine abstrakte Klasse zum Lesen und Schreiben von JSON-Daten in einer Datei.
 * Der generische Typ `T` definiert den Typ der Daten, die gelesen und geschrieben werden.
 *
 * @param file Die Datei, in der die JSON-Daten gespeichert werden.
 */
internal open class JsonData<T> (private val file: File) {
    private val gson = Gson()
    private val type: Type

    /**
     * Initialisiert die Klasse und bestimmt den generischen Typ `T` anhand der Vererbungshierarchie.
     * Dies ist erforderlich, um den korrekten Typ für die Deserialisierung der JSON-Daten festzulegen.
     */
    init {
        val superClass = javaClass.genericSuperclass
        val typeParams = (superClass as ParameterizedType).actualTypeArguments
        type = typeParams[0]
    }

    /**
     * Liest die JSON-Daten aus der Datei und gibt sie als Objekt vom Typ `T` zurück.
     *
     * @return Die gelesenen Daten oder `null`, wenn die Datei nicht existiert oder ein Fehler aufgetreten ist.
     */
    open fun getData(): T? {
        if (file.exists()) {
            val json = file.readText()
            return gson.fromJson(json, type)
        }
        return null
    }

    /**
     * Schreibt die Daten als JSON in die Datei.
     *
     * @param data Die zu speichernden Daten.
     */
    open fun saveData(data: T?) {
        if (!file.exists()) {
            file.createNewFile()
        }
        val json = gson.toJson(data)
        file.writeText(json)
    }
}