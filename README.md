# ms-reqresp-lib
Base request and response types to be used by microservices.

## Display Java Tools Installed

```shell
./gradlew -q javaToolchains
```
### Update the gradlew wrapper version

```shell
./gradlew wrapper --gradle-version=9.1.0 --distribution-type=bin
```

## Clean, Build, Test, Assemble, Publish, Release

```shell
./gradlew --info clean
```

```shell
./gradlew :lib:spotlessApply
```

```shell
./gradlew --info clean build
```

```shell
# --info is required for Grdle to display logs from tests
./gradlew --info clean test
```

```shell
./gradlew --info jar
```

```shell
./gradlew --info assemble
```

```shell
git commit -m "fixes and refactorings" -a
git push
```

```shell
# only Rubens can release
./gradlew --info release
```

```shell
git checkout release
git pull
./gradlew --info publish
git checkout main
```

---
Author:  [Rubens Gomes](https://rubensgomes.com/)


