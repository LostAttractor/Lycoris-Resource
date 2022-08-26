import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.URL
import java.nio.channels.Channels
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest


var runPath: Path = Paths.get("").toAbsolutePath()

fun main(args: Array<String>) {
    println("Running Path: $runPath")
    val file = runPath.toFile() //获取其file对象
    val fs = file.listFiles() //遍历path下的文件和目录，放在File数组中
    for (f in fs!!) {                    //遍历File[]数组
        if (!f.isDirectory) {
            for (arg in args) {
                if (f.name.endsWith(".$arg")) {
                    println("Found a File: ${f.name}")
                    val hashAlgorithm = HashAlgorithm.MD5
                    val stream = f.inputStream()
                    val buffer = ByteArray(stream.available())
                    stream.read(buffer)
                    stream.close()
                    val hash = getHashCode(buffer, hashAlgorithm)
                    println("${hashAlgorithm.modeName}: $hash, ${buffer.size}")
                    writeFileString(file.resolve("${f.name}.${hashAlgorithm.name.lowercase()}"), hash)
                }
            }
        }
    }
//    val hashAlgorithm = HashAlgorithm.MD5
//    val stream = BufferedInputStream(URL("https://github.com/LostAttractor/SRGMaps/blob/main/VANILLA189.srg?raw=true").openStream())
//   // val connection = createConnection("https://github.com/LostAttractor/SRGMaps/blob/main/VANILLA189.srg?raw=true")
//    //val stream = connection.inputStream
//    val bufferStream = ByteArrayOutputStream()
//    val buffer = ByteArray(1024)
//    var count: Int
//    while (stream.read(buffer, 0, 1024).also { count = it } != -1) {
//        bufferStream.write(buffer, 0, count)
//    }
//    stream.close();
//    println(bufferStream.toByteArray().size)
//    val hash = getHashCode(buffer, hashAlgorithm)
//    println("${hashAlgorithm.modeName}: $hash")
    downloadUsingNIO("https://github.com/LostAttractor/SRGMaps/blob/main/VANILLA189.srg?raw=true", "VANILLA189.srg.test")
}

@Throws(IOException::class)
private fun downloadUsingNIO(urlStr: String, file: String) {
    val url = URL(urlStr)
    val rbc = Channels.newChannel(url.openStream())
    val fos = FileOutputStream(file)
    fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
    fos.close()
    rbc.close()
}

//fun getFileString(url: String): String {
//    val connection = createConnection(url)
//    val reader = BufferedReader(InputStreamReader(connection.inputStream))
//    return reader.lines().collect(Collectors.joining(System.lineSeparator()))
//}

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

fun getHashCode(input: ByteArray, mode: HashAlgorithm): String {
    val digest = MessageDigest.getInstance(mode.modeName)
    digest.update(input)
    return BigInteger(1, digest.digest()).toString(digest.digestLength)
}

enum class HashAlgorithm(val modeName: String) {
    MD5("MD5"), SHA1("SHA-1"), SHA256("SHA-256"), SHA512("SHA-512")
}

fun writeFileString(file: File, string: String) {
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