# reqresp-lib

This project uses the [JSON Schema Specs](https://json-schema.org/) to define
JSON schemas for BaseRequest, BaseResponse and Error types. These
types are converted from the corresponding JSON Schema to Java types.
And those Java types may be used as base types to be used by microservices
request and response, where the Error type is used to return errors.

## Display Java Tools Installed

```shell
./gradlew -q javaToolchains
```
### Update the gradlew wrapper version

```shell
./gradlew wrapper --gradle-version=9.0.0 --distribution-type=bin
```

## Clean, Build, Test, Assemble, Publish, Release

```shell
./gradlew --info clean
```

```shell
./gradlew :lib:spotlessApply
```

```shell
./gradlew --info build
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
git commit -m "updated gradle-catalog" -a
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


