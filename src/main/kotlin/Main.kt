import java.io.*
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest
import java.util.stream.Collectors


var runPath: Path = Paths.get("").toAbsolutePath()

fun main(args: Array<String>) {
    println("Running Path: $runPath")
    val file = runPath.toFile() //获取其file对象
    val fs = file.listFiles() //遍历path下的文件和目录，放在File数组中
    for (f in fs!!) {                    //遍历File[]数组
        if (!f.isDirectory && (f.name.endsWith(".srg")) || f.name.endsWith(".dll")) {
            println("Found a SRGMap File: $f")
            val srgMap = readFile(f)
            val hashMode = HashMode.MD5
            val hash = getHashCode(srgMap, hashMode)
            if (hash != null) {
                println("${hashMode.modeName}: $hash")
                writeFile(file.resolve("${f.name}.${hashMode.name.lowercase()}"), hash)
            }
//            val hashOnline = getHashCode(getFileString("https://srgmaps.vercel.app/${f.name}"), hashMode)
//            println(hashOnline)
        }
    }
}

fun getFileString(url: String): String {
    val connection = createConnection(url)
    val reader = BufferedReader(InputStreamReader(connection.inputStream))
    return reader.lines().collect(Collectors.joining(System.lineSeparator()))
}

private fun createConnection(url: String): HttpURLConnection {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.connectTimeout = 5 * 1000;
    connection.setRequestProperty(
        "User-Agent",
        "\tMozilla/5.0 (Windows NT 10.0; Win64; x64; WebView/3.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 Edge/18.22000"
    )
    return connection
}

fun getHashCode(input: String, mode: HashMode): String? {
    var output: String? = null
    try {
        val digest = MessageDigest.getInstance(mode.modeName)
        digest.reset()
        digest.update(input.toByteArray(charset("utf8")))
        output = BigInteger(1, digest.digest()).toString(digest.digestLength)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return output
}

enum class HashMode(val modeName: String) {
    MD5("MD5"), SHA1("SHA-1"), SHA256("SHA-256"), SHA512("SHA-512")
}

@Throws(IOException::class)
fun readFile(file: File): String {
    val stream = file.inputStream()
    val reader = BufferedReader(InputStreamReader(stream))
    return reader.lines().collect(Collectors.joining(System.lineSeparator()))
}

fun writeFile(file: File, string: String) {
    val writer: FileWriter
    try {
        writer = FileWriter(file)
        writer.write(string)
        writer.flush()
        writer.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}