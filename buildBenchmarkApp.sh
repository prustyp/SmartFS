#!/bin/bash
cd jgrep &&  mvn clean install -Dmaven.test.skip=true && cp /Users/pprusty05/.m2/repository/codos-app/jgrep/1.0-SNAPSHOT/jgrep-1.0-SNAPSHOT-jar-with-dependencies.jar /Users/pprusty05/google_drive/OS_F2018/osf18repo/jar_folder/jgrep.jar && cd ..
cd jgzip &&  mvn clean install -Dmaven.test.skip=true && cp /Users/pprusty05/.m2/repository/codosApp/jgzip/1.0-SNAPSHOT/jgzip-1.0-SNAPSHOT-jar-with-dependencies.jar /Users/pprusty05/google_drive/OS_F2018/osf18repo/jar_folder/jgzip.jar && cd ..
