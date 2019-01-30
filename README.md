# Dynamic Permissions Update

 - Simple App demonstrate how to add new client permissions to an already running
Hazelcast IMDG cluster. (See https://docs.hazelcast.org/docs/latest/manual/html-single/index.html#permissions)
 - Since [Security](https://docs.hazelcast.org/docs/3.11.1/manual/html-single/index.html#security) feature is available in
 Hazelcast IMDG Enterprise Edition, an Hazelcast Enterprise license key needed to run the app.

## Build

 - `mvn clean package -DskipTests`, package the application without running tests

 - If you want to run the tests & verify, open [hazelcast.xml](src/test/resources/hazelcast.xml)
 and [hazelcast-updated.xml](src/test/resources/hazelcast-updates.xml) files
 and replace `YOUR_LICENSE_KEY` text with a valid Hazelcast Enterprise license key
 and run ``mvn clean package`.

## Usage

 - After build, run `java -jar target/dynamic-security-update-1.0.jar
 [configFileLocation]` from the project directory. Replace `[configFileLocation]`
  with a string pointing to a Hazelcast config file that contains the new client permissions
  and other necessary configs for a `Lite Member` to connect to the running cluster.

