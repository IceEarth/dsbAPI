package dsb.request

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.net.URL
import java.nio.charset.Charset
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import kotlin.collections.ArrayList

internal class ServerHelloRequest (username: String, password: String) {

    private val currentTime: String = "${Date().toInstant()}Z"
    private val gson = Gson()
    private val url = URL("https://app.dsbcontrol.de/JsonHandler.ashx/GetData")
    private val params = mapOf(
        "UserId" to username,
        "UserPw" to password,
        "AppVersion" to "2.5.9",
        "Language" to "de",
        "OsVersion" to "28 8.0",
        "AppId" to UUID.randomUUID().toString(),
        "Device" to "iphone14",
        "BundleId" to "de.heinekingmedia.dsbmobile",
        "Date" to currentTime,
        "LastUpdate" to currentTime
    )
    /**params zu byte-code*/
    private val paramsByteString = gson.toJson(params).toByteArray(Charset.defaultCharset())
    /**params komprimieren und enkodieren*/
    private val compressedParams = compressAndEncode(paramsByteString)
    /**params in ein JSON-Object eintragen */
    private val requestData = "{'req': {'Data': '$compressedParams', 'DataType': 1}}"


    var urls : Array<URL>? = null
        private set


    init {
        refresh()
        println("${this.urls?.get(0)}  ${this.urls?.get(1)} " )
    }

    /**[refresh] sendet eine neue ServerHelloRequest um diese zu eventuell aktualisieren (Falls Änderungen passiert sind.)*/
    fun refresh(){

        /**Request senden...*/
        val response = request(this.requestData)

        /**Response zu einem JSON-Object machen*/
        val jsonOutput = gson.fromJson(response, JsonObject::class.java)

        /**Der gelesene Output wird dekomprimiert und dekodiert.*/
        val fileJsonObject = gson.fromJson(decompressAndDecode(jsonOutput.get("d").asString), JsonObject::class.java)



        val result = ArrayList<URL>()

        /**Heraussuchen der URLs in der returned JSON-File*/
        for(el in fileJsonObject.getAsJsonArray("ResultMenuItems")[0].asJsonObject.getAsJsonArray("Childs")[0].asJsonObject.getAsJsonObject("Root").getAsJsonArray("Childs")){
            result.add(
                URL(
                el.asJsonObject.getAsJsonArray("Childs").get(0).asJsonObject.get("Detail").asString
                )
            )
        }

        this.urls = result.toTypedArray()


    }

    private fun request(jsonData: String): String {
        val client = OkHttpClient()

        /**Erstellen des Anfrage-Body*/
        val requestBody = jsonData.toRequestBody("application/json".toMediaType())

        /**Erstellen der Anfrage*/
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        /** Ausführen der Anfrage>*/
        val response = client.newCall(request).execute()

        /** Lesen der Antwort*/
        val timetableData = response.body?.string()

        /** Schließen der Verbindung*/
        response.close()

        return timetableData.orEmpty()

    }

    private fun compressAndEncode(params: ByteArray): String {
        val compressedBytes = ByteArrayOutputStream().use { byteArrayOutputStream ->
            GZIPOutputStream(byteArrayOutputStream).use { gzipOutputStream ->
                gzipOutputStream.write(params)
            }
            byteArrayOutputStream.toByteArray()
        }

        val encodedString = Base64.getEncoder().encodeToString(compressedBytes)
        val decodedString = String(encodedString.toByteArray(), Charsets.UTF_8)

        return decodedString
    }

    private fun decompressAndDecode(dataCompressed: String): String {
        val decodedBytes = Base64.getDecoder().decode(dataCompressed)
        val decompressedBytes = GZIPInputStream(decodedBytes.inputStream()).readBytes()

        val decodedString = String(decompressedBytes, Charsets.UTF_8)

        return decodedString
    }



}
/*/*// Öffnen der Verbindung
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"

        // Einstellen des Content-Type-Headers
        connection.setRequestProperty("Content-Type", "application/json")
        //connection.setRequestProperty("Custom-Header", jsonData)

        // Aktivieren des Ausgabemodus für die Anfrage
        connection.doOutput = true

        // Schreiben der Daten in den Anfrage-Body
        val outputStream = BufferedOutputStream(connection.outputStream)
        outputStream.write(jsonData.toByteArray(Charsets.UTF_8))
        outputStream.flush()
        outputStream.close()

        // Lesen der Antwort
        val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
        val response = StringBuilder()
        var inputLine: String?
        while (inputStream.readLine().also { inputLine = it } != null) {
            response.append(inputLine)
        }
        inputStream.close()

        // Verbindung schließen
        connection.disconnect()

        // Die Antwort als String erhalten
        val timetableData = response.toString()

        return timetableData*/*/