# buildAndReload task, running in background, watches for source changes
(sleep 60; ./gradlew buildAndReload --continuous -PskipDownload=true -x Test)&
./gradlew bootRun -PskipDownload=true --args='--spring.profiles.active=dev'
