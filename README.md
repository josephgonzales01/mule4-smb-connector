# Project Name

> SMB Custom Connector for Mule 4

## Contents
This is optional but If you have lenghty README contents, this will be a great addition.
- [Installation](#installation)
- [Requirements](#requirements)
- [Support](#support)

## Requirements
- Mule runtime 4.x.x
- Anypoint studio 7.0 and up
- Maven 3.5.x
- JDK 8.x.x 

## Installation

### 1. Clone and Build this project
```
mvn clean install -Dmaven.test.skip=true
```
### 2. Add this dependency to your application pom.xml

```
<dependency>
   <groupId>com.wsl.connectors</groupId>
   <artifactId>smbConnector</artifactId>
   <version>0.9.0</version>
   <classifier>mule-plugin</classifier>
</dependency>
```
## Support

Please log here https://github.com/josephgonzales01/mule4-smb-connector/issues for support.






