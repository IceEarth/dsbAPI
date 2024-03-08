import io.ktor.utils.io.jvm.javaio.*
import java.util.*
import java.util.zip.GZIPOutputStream
import java.nio.charset.StandardCharsets
import java.net.URLEncoder
import java.net.HttpURLConnection
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.DataOutputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL
import java.util.zip.GZIPInputStream
import java.util.zip.Inflater
import java.util.zip.InflaterInputStream

fun main() {
    val url = "https://app.dsbcontrol.de/JsonHandler.ashx/GetData"

    val currentTime = "${Date().toInstant()}Z"

    val params = mapOf(
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

    val paramsBytestring = params.entries.joinToString(
        separator = ",",
        prefix = "{",
        postfix = "}"
    ) { "\"${it.key}\":\"${it.value}\"" }.toByteArray(Charsets.UTF_8)
    println(paramsBytestring.contentToString())

    val byteArrayOutputStream = ByteArrayOutputStream()
    val gzipOutputStream = GZIPOutputStream(byteArrayOutputStream)
    gzipOutputStream.write(paramsBytestring)
    gzipOutputStream.close()
    val paramsCompressed = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray())
    println(paramsCompressed)

    val jsonData = "{\"req\":{\"Data\":\"$paramsCompressed\",\"DataType\":1}}"

    println(jsonData)

    val urlConnection = URL(url).openConnection() as HttpURLConnection
    urlConnection.requestMethod = "POST"
    urlConnection.doOutput = true
    urlConnection.setRequestProperty("Content-Type", "application/json")

    val dataOutputStream = DataOutputStream(urlConnection.outputStream)
    dataOutputStream.write(jsonData.toByteArray(Charsets.UTF_8))

    println("URL: ${getFullRequestUrl(urlConnection, dataOutputStream)}")

    dataOutputStream.flush()
    dataOutputStream.close()



    val responseCode = urlConnection.responseCode
    val responseMessage = urlConnection.responseMessage
    println("$responseCode: $responseMessage")

    if (responseCode == 200) {
        val inputStream = urlConnection.inputStream


        //val responseJson = decompressResponse(inputStream)


        inputStream.close()


        // Hier kannst du die weiteren Schritte zur Verarbeitung der JSON-Antwort durchführen
        println("kein Fehler")
    } else {
        println("error11!!!1")
    }
}

fun decompressAndDecodeBase64(encodedData: String): String {
    val decodedBytes = Base64.getDecoder().decode(encodedData)
    val decompressedBytes = decompressResponse(decodedBytes)
    return decompressedBytes.toString(Charsets.UTF_8)
}

fun decompressResponse(data: ByteArray): ByteArray {
    val byteArrayOutputStream = ByteArrayOutputStream()
    val buffer = ByteArray(1024)
    val inflater = Inflater(true)

    inflater.setInput(data)
    while (!inflater.finished()) {
        val count = inflater.inflate(buffer)
        byteArrayOutputStream.write(buffer, 0, count)
    }

    inflater.end()
    byteArrayOutputStream.close()

    return byteArrayOutputStream.toByteArray()
}

fun isGzipped(data: ByteArray): Boolean {
    return data.size >= 2 && data[0].toInt() and 0xFF == 0x1f && data[1].toInt() and 0xFF == 0x8b
}

fun getFullRequestUrl(urlConnection: HttpURLConnection, dataOutputStream: DataOutputStream): String {
    val requestMethod = urlConnection.requestMethod
    val url = urlConnection.url
    val queryParams = url.query
    val bodyData = dataOutputStream.toString()

    val fullRequestUrl = StringBuilder()
    fullRequestUrl.append(requestMethod).append(" ").append(url.toString())

    if (queryParams != null) {
        fullRequestUrl.append("?").append(queryParams)
    }

    if (bodyData.isNotEmpty()) {
        fullRequestUrl.append("\n\n").append(bodyData)
    }

    return fullRequestUrl.toString()
}