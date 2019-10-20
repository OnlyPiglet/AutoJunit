# AutoJunit
[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg?style=flat-square)](https://github.com/996icu/996.ICU/blob/master/LICENSE)[![GPL Licence](https://badges.frapsoft.com/os/gpl/gpl.svg?v=103)](https://opensource.org/licenses/GPL-3.0/)[![jdk>=1.8](https://img.shields.io/badge/jdk-%3E%3D1.8-yellow.svg)](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)[![codacy](https://api.codacy.com/project/badge/Grade/3e1b8a70248c46579b7b0d01d60c6377)](https://app.codacy.com/manual/OnlyPiglet/AutoJunit/dashboard?bid=14511850)[![AJ](https://img.shields.io/badge/AJ-AutoJunit-orange)](https://github.com/OnlyPiglet/ajunit)
![AJ](./img/AJ.png)

-------------------------------------------------------------------------------

## 简介

自动生成基于Maven工程的Junit的单元测试文件

## 快速入门

```xml
<dependency>
    <groupId>com.github.onlypiglet</groupId>
    <artifactId>autojunit-core</artifactId>
    <version>1.0.1-alpha</version>
</dependency>


<plugin>    
    <groupId>com.github.onlypiglet</groupId>
    <artifactId>autojunit</artifactId>
    <version>1.0.1-alpha</version>   
    <executions>        
        <execution>            
            <phase>generate-test-sources</phase>            
            <goals>                
                <goal>genjunit</goal>            
            </goals>        
        </execution>    
    </executions>
</plugin>
```

在Pom.xml中添加相应插件，并执行```mvn clean generate-test-sources```。
