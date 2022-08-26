import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors


fun main(args: Array<String>) {
    //println(System.lineSeparator()[2].code)
    println("Running Path: $runPath")
    val file = runPath.toFile() //获取其file对象
    val fs = file.listFiles() //遍历path下的文件和目录，放在File数组中
    for (f in fs!!) {                    //遍历File[]数组
        if (!f.isDirectory) {
                if (f.name.endsWith(".srg")) {
                    println("Found a File: ${f.name}")
                    val stream = f.inputStream()
                    val buffer = ByteArray(stream.available())
                    stream.read(buffer)
                    String(buffer).split(10.toChar())[0].forEach { println("it: $it code: ${it.code}") }
                    return
//                    val reader = BufferedReader(InputStreamReader(stream))
//                    val lines = reader.lines().collect(Collectors.joining(System.lineSeparator()))
//                    val stringBuilder  = StringBuilder()
//                    for (s in lines.split(System.lineSeparator()).dropLastWhile { it.isEmpty() }) {
//                        stringBuilder.append(s.dropLastWhile { it.code == 13 }).append(System.lineSeparator())
//                    }
//                    writeFileString(file.resolve("${f.name}.test"), stringBuilder.toString())
                }
        }
    }
}