<h1 align="center" style="text-align:center;">
  KHTTP
</h1>
<p align="center">
	<strong>基于jdk内部的httpserver封装的轻量级http服务端</strong>
</p>

<p align="center">
    <a target="_blank" href="https://www.apache.org/licenses/LICENSE-2.0.txt">
		<img src="https://img.shields.io/:license-Apache2-blue.svg" alt="Apache 2" />
	</a>
    <a target="_blank" href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">
		<img src="https://img.shields.io/badge/JDK-8+-green.svg" alt="jdk-8+" />
	</a>
    <br />
</p>

<br/>

<hr />

### 特点

* 非常轻量，代码简单，大小只有12k
* api友好，用的很爽
* 支持8+以上的所有版本，支持native构建

### 使用方式

Maven

```xml

<dependency>
    <groupId>io.github.kongweiguang</groupId>
    <artifactId>kHTTP</artifactId>
    <version>0.0.3</version>
</dependency>
```

Gradle

```xml
implementation 'io.github.kongweiguang:KHTTP:0.0.3'

```

Gradle-Kotlin

```xml
implementation("io.github.kongweiguang:kHTTP:0.0.3")
```

### 例子

```java
public class Test1 {
    public static void main(String[] args) {
        KHTTP.of()
                .executor(Executors.newCachedThreadPool())
                .web("/Users/kongweiguang/Desktop/hegui/xm/gs")
                .get("/get", (req, res) -> {
                    final MultiValueMap<String, String> params = req.params();
                    res.send("hello").send("world");
                })
                .post("/post", ((req, res) -> {
                    final String str = req.str();
                    System.out.println("str = " + str);

                    res.send("{\"key\":\"i am post res\"}");
                }))
                .post("/upload", (req, res) -> {

                    final MultiValueMap<String, String> params = req.params();

                    final Map<String, List<UpFile>> files = req.fileMap();
                })
                .get("/xz", new Handler() {
                    @Override
                    public void doHandler(final Req req, final Res res) throws IOException {
                        res.file("k.txt", Files.readAllBytes(Paths.get("D:\\k\\k.txt")));
                    }
                })
                .ok(8080);

    }
}
```