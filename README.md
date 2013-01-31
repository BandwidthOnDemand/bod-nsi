# BoD NSI
This project contains the (wsimport/jaxws) generated java classes for the NSI wsdl files.

## Building the project
Because the `pom.xml` has a reference to the bod-parent-pom you should add the public-releases repo to your maven settings (`~/.m2/settings.xml`) file.

    <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">

      <profiles>
        <profile>
          <activation>
            <activeByDefault>true</activeByDefault>
          </activation>
          <repositories>
            <repository>
              <id>surfnet-public-releases</id>
              <name>SURFnet Public Releases</name>
              <url>http://atlas.dlp.surfnet.nl/nexus/content/repositories/public-releases</url>
              <layout>default</layout>
            </repository>
          </repositories>
        </profile>
      </profiles>
    </settings>
