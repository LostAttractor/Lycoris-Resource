### 请将SRG文件转为使用LF
ASCII 10: 换行符 \n (LF)  
ASCII 13： LE 回车符 (CR)  
System.lineSeparator() (CRLF): ASCII 10 + ASCII 13 (两个字符)  

### Usage
```java -jar SRGMaps-1.0-SNAPSHOT-jar-with-dependencies.jar *Filename-extension*```
#### Example
```java -jar SRGMaps-1.0-SNAPSHOT-jar-with-dependencies.jar srg dll```

### Packaging
```mvn package assembly:single```