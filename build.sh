#! /bin/bash
echo "==================================================="
echo "Gradle 빌드를 실행합니다"
echo "==================================================="
./gradlew build -x test
echo "done."

echo "==================================================="
echo "Dockerfile을 build합니다."
echo "==================================================="
docker build --build-arg DEPENDENCY=build/dependency -t jamiehun/sendwish_back --platform linux/amd64 .
echo "done."

echo "==================================================="
echo "Dockerfile을 push합니다." 
echo "==================================================="
docker push jamiehun/sendwish_back
echo "done."
