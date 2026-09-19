# Profiling

Spring profiles run the application in an environment other than the default
(for example `dev`, `prod`, or `test`).

`GET /rest/profile` returns `spring.message` from the active profile.

| Profile | File | Response | Port |
| --- | --- | --- | --- |
| default | `application.properties` | `hello from (default) properties file` | 9001 |
| prod | `application-prod.properties` | `hello from (prod) properties file` | 9001 |
| test | `application-test.properties` | `hello from (test) properties file` | 9001 |
| dev | `application-dev.yml` | `Hello, from dev environment` | 8085 |

## Run

IntelliJ **Active profiles** takes the profile **name only**: `prod`, `test`, or `dev`.

Do not put `spring.profiles.active=prod` in that field. Spring treats the whole
string as the profile name and will not load `application-prod.properties`.

Command line:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

Then:

```
GET http://localhost:9001/rest/profile
```

![dev-profile-screenshot](https://user-images.githubusercontent.com/48691043/58821800-7c759900-8653-11e9-8ec2-4a0f11a32ec5.JPG)
