#!/bin/bash
cd metadata-manager && mvn clean install -Dmaven.test.skip=true && cp /Users/pprusty05/.m2/repository/codos/metadata-manager/1.0-SNAPSHOT/metadata-manager-1.0-SNAPSHOT-jar-with-dependencies.jar /Users/pprusty05/google_drive/OS_F2018/osf18repo/jar_folder/metadata-manager.jar && cd ..
cd remote-delete-manager && mvn clean install -Dmaven.test.skip=true && cd ..
cd recentfileManager && mvn clean install -Dmaven.test.skip=true && cd ..
cd drive-listener && mvn clean install -Dmaven.test.skip=true && cd ..
cd Remotedrivelistener && mvn clean install -Dmaven.test.skip=true && cd ..
cd download-manager && mvn clean install -Dmaven.test.skip=true && cp /Users/pprusty05/.m2/repository/codos/download-manager/1.0-SNAPSHOT/download-manager-1.0-SNAPSHOT-jar-with-dependencies.jar /Users/pprusty05/google_drive/OS_F2018/osf18repo/jar_folder/download-manager.jar && cd ..
cd upload-manager && mvn clean install -Dmaven.test.skip=true && cp /Users/pprusty05/.m2/repository/codos/upload-manager/1.0-SNAPSHOT/upload-manager-1.0-SNAPSHOT-jar-with-dependencies.jar /Users/pprusty05/google_drive/OS_F2018/osf18repo/jar_folder/upload-manager.jar && cd ..
cd cronjobmanager && mvn clean install -Dmaven.test.skip=true && cp /Users/pprusty05/.m2/repository/codos/cron-job-manager/1.0-SNAPSHOT/cron-job-manager-1.0-SNAPSHOT-jar-with-dependencies.jar /Users/pprusty05/google_drive/OS_F2018/osf18repo/jar_folder/cronjob-manager.jar && cd ..

