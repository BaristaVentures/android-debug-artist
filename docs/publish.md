## Publish

- You need to create `lib/bintray.properties` with the variables declared on `lib/bintray.properties.example`.

```
cp lib/gradle.properties.example lib/gradle.properties
```

- To Sync with Maven Central you need to get auth tokens from: https://oss.sonatype.org/
- Run `scripts/deploy.sh` to upload to BinTray (jcenter).