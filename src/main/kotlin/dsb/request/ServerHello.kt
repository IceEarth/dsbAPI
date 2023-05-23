package dsb.request

import com.google.gson.Gson
import com.google.gson.JsonObject
import io.ktor.network.sockets.*
import json.data.JsonData
import org.jsoup.Connection
import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.*
import java.util.zip.Deflater
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import kotlin.collections.ArrayList

class ServerHello(username: String, password: String) {

    private val currentTime: String = "${Date().toInstant()}Z"
    private val gson = Gson()
    private val params = mapOf(
        "UserId" to "148922",
        "UserPw" to "vPl4nS4g",
        "AppVersion" to "2.5.9",
        "Language" to "de",
        "OsVersion" to "28 8.0",
        "AppId" to UUID.randomUUID().toString(),
        "Device" to "SM-G930F",
        "BundleId" to "de.heinekingmedia.dsbmobile",
        "Date" to currentTime,
        "LastUpdate" to currentTime
    )
    private val url = URL("https://app.dsbcontrol.de/JsonHandler.ashx/GetData")

    var urls : Array<URL>? = null
        private set
    init {
        this.fetchURLs()
    }

    /*private*/ fun fetchURLs(){
        val paramsByteString = gson.toJson(params).toByteArray(Charset.defaultCharset())

        println(paramsByteString.toString())

        var compressedParams = compressAndEncode(paramsByteString)

        println(compressedParams.toString())

        compressedParams = "H4sIAEeVbGQC/4WOMW+DMBBG/0rEHFvmbMCwpapaVWrVSGkyZDPcQa2CQZikQ9X/XuMu2TLe0/ee7ic5eppfMKmSVOkSINlGsv8O5LrvlTuoLrDdNJ1o9nZ0gQPPeBngq3HdxXQUEFK43/3NRm80F/9mzGvToGoFMVEYZKrIG2bSvGA6F9DmZVarYm080tU2a/Hwxp5LKZ4Ce7g47ClWkPgnWUdf1nUDoTUcfT2Mte2jbJZVBQGSiYyB/EihAl2B5FJl5/iyX44T3tn9/gHfTonbFwEAAA=="

        val jsonData = "{'req': {'Data': '$compressedParams', 'DataType': 1}}"


        println(jsonData)

        val requestOutput = request(jsonData)

        val jsonOutput = gson.fromJson(requestOutput, JsonObject::class.java)

        println(decompressAndDecode(jsonOutput.get("d").asString))

        val fileJsonObject = gson.fromJson(decompressAndDecode(jsonOutput.get("d").asString), JsonObject::class.java)



        println(decompressAndDecode("H4sIAEeVbGQC/4WOMW+DMBBG/0rEHFvmbMCwpapaVWrVSGkyZDPcQa2CQZikQ9X/XuMu2TLe0/ee7ic5eppfMKmSVOkSINlGsv8O5LrvlTuoLrDdNJ1o9nZ0gQPPeBngq3HdxXQUEFK43/3NRm80F/9mzGvToGoFMVEYZKrIG2bSvGA6F9DmZVarYm080tU2a/Hwxp5LKZ4Ce7g47ClWkPgnWUdf1nUDoTUcfT2Mte2jbJZVBQGSiYyB/EihAl2B5FJl5/iyX44T3tn9/gHfTonbFwEAAA=="))

        val result = ArrayList<URL>()

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
        // Öffnen der Verbindung
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

        return timetableData

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