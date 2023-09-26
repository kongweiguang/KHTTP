<h1 align="center" style="text-align:center;">
  KHTTP
</h1>
<p align="center">
	<strong>基于jdk内部的httpserver封装的轻量级http服务端端</strong>
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


参考hutool-http，jlhttp按照自己的编码习惯封装的建议轻量http服务端工具

### 特点

* 非常轻量，代码简单，大小只有12k
* api友好，用的很爽
* 链式编程

### 使用方式

Maven

```xml

<dependency>
    <groupId>io.github.kongweiguang</groupId>
    <artifactId>kHTTP</artifactId>
    <version>0.1</version>
</dependency>
```

Gradle

```xml
implementation group: 'io.github.kongweiguang', name: 'kHTTP', version: '0.1'
```

Gradle-Kotlin

```xml
implementation("io.github.kongweiguang:kHTTP:0.1")
```

### 例子
