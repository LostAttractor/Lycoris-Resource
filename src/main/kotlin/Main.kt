import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.math.BigInteger
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest


var runPath: Path = Paths.get("").toAbsolutePath()

fun main(args: Array<String>) {
    println("Running Path: $runPath")
    val file = runPath.toFile() //获取其file对象
    val fs = file.listFiles() //遍历path下的文件和目录，放在File数组中
    for (f in fs!!) {                    //遍历File[]数组
        if (!f.isDirectory && f.name.endsWith(".srg")) {
            println("Found a SRGMap File: $f")
            val srgMap = readFile(f)
            val hashMode = HashMode.MD5
            val hash = getHashCode(srgMap, hashMode)
            if (hash != null) {
                println("${hashMode.modeName}: $hash")
                writeFile(file.resolve("${f.name}.${hashMode.name.lowercase()}"), hash)
            }
        }
    }
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
    val bos = ByteArrayOutputStream()
    val buffer = ByteArray(512000)
    var length: Int
    while (stream.read(buffer).also { length = it } != -1) {
        bos.write(buffer, 0, length)
    }
    return bos.toString()
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