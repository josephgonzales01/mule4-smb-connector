# SMB Custom Connector for Mule 4

> Custom Connector for Reading, Writing, Updating, Deleting and moving of files via Microsoft SMB Protocol

## Contents
- [Installation](#installation)
- [Requirements](#requirements)
- [Support](#support)
- [Screenshots](#screenshots)

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

## Screenshots

### Global config
<img width="624" alt="smb_global_config" src="https://user-images.githubusercontent.com/15100839/52002182-49cc6000-24fc-11e9-866d-665b52fdc956.png">

### Write
<img width="633" alt="smb write" src="https://user-images.githubusercontent.com/15100839/52002213-5c469980-24fc-11e9-9c5d-f4d1467e9f86.png">

### Read
<img width="631" alt="smb_read" src="https://user-images.githubusercontent.com/15100839/52002236-6cf70f80-24fc-11e9-839f-eba4a2f052c2.png">

### Move and copy
<img width="631" alt="smb_move_copy" src="https://user-images.githubusercontent.com/15100839/52002246-77190e00-24fc-11e9-9c8d-39def7c9d227.png">

### Delete
<img width="635" alt="smb_delete" src="https://user-images.githubusercontent.com/15100839/52002276-8e57fb80-24fc-11e9-9ecf-88eebb832bdd.png">


## Support

Please log here https://github.com/josephgonzales01/mule4-smb-connector/issues for support.






