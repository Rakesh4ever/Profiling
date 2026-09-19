# Spring Boot Profiling

<p align="center">
  <img src="docs/images/hero-command-center.jpg" alt="Three environment portals in a night-time command center" width="100%">
</p>

<p align="center">
  <strong>One application. Three environments. One endpoint.</strong><br>
  Switch <code>dev</code>, <code>test</code>, or <code>prod</code> and <code>GET /rest/profile</code> answers with that world's message.
</p>

<p align="center">
  <a href="#pick-an-environment">Pick an environment</a> ·
  <a href="#hit-the-endpoint">Hit the endpoint</a> ·
  <a href="#run-it">Run it</a> ·
  <a href="#how-profiles-load">How profiles load</a>
</p>

---

Spring profiles let you run the same Spring Boot app with environment-specific configuration. This demo keeps **port 9001** for default, prod, and test. `dev` uses port **8085**.

`GET /rest/profile` returns `spring.message` from the **active** profile file.

## Pick an environment

Click a world, then run that profile. IntelliJ **Active profiles** takes the **name only** (`prod`, not `spring.profiles.active=prod`).

<table>
  <tr>
    <td align="center" width="33%">
      <a href="#dev">
        <img src="docs/images/env-dev.jpg" alt="Development workstation at night">
      </a><br>
      <strong><a href="#dev">dev</a></strong><br>
      <code>application-dev.yml</code><br>
      port <code>8085</code>
    </td>
    <td align="center" width="33%">
      <a href="#test">
        <img src="docs/images/env-test.jpg" alt="QA laboratory with amber light">
      </a><br>
      <strong><a href="#test">test</a></strong><br>
      <code>application-test.properties</code><br>
      port <code>9001</code>
    </td>
    <td align="center" width="33%">
      <a href="#prod">
        <img src="docs/images/env-prod.jpg" alt="Production data center aisle">
      </a><br>
      <strong><a href="#prod">prod</a></strong><br>
      <code>application-prod.properties</code><br>
      port <code>9001</code>
    </td>
  </tr>
</table>

| Profile | File | `GET /rest/profile` |
| --- | --- | --- |
| default | `application.properties` | `hello from (default) properties file` |
| [dev](#dev) | `application-dev.yml` | `Hello, from dev environment` |
| [test](#test) | `application-test.properties` | `hello from (test) properties file` |
| [prod](#prod) | `application-prod.properties` | `hello from (prod) properties file` |

## Hit the endpoint

<p align="center">
  <img src="docs/images/api-request.jpg" alt="A glowing request traveling through a network tunnel" width="100%">
</p>

With **prod** (or test / default) running:

```http
GET http://localhost:9001/rest/profile
```

With **dev** running:

```http
GET http://localhost:8085/rest/profile
```

The body is plain text from `spring.message`. No query params.

## Run it

Open a setup. Each one activates a **profile name**.

<details>
<summary><strong>IntelliJ — Active profiles field</strong></summary>

1. Open **Run → Edit Configurations**.
2. Select **PROD Profile**, **TEST Profile**, or **DEV Profile** (or the generated `SpringBootProfilesApplication` config).
3. Set **Active profiles** to the name only:

   | You want | Type this |
   | --- | --- |
   | Production | `prod` |
   | Test | `test` |
   | Development | `dev` |

4. Do **not** type `spring.profiles.active=prod`. Spring treats that whole string as the profile name and never loads `application-prod.properties`.
5. Run, then call the URL for that profile.

![IntelliJ run configuration for a named profile](https://user-images.githubusercontent.com/48691043/58821800-7c759900-8653-11e9-8ec2-4a0f11a32ec5.JPG)

</details>

<details>
<summary><strong>Maven</strong></summary>

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
mvn spring-boot:run -Dspring-boot.run.profiles=test
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Omit `-Dspring-boot.run.profiles=...` to stay on the default file (`application.properties`, port `9001`).

</details>

<details>
<summary><strong>curl the message</strong></summary>

```bash
curl http://localhost:9001/rest/profile
# hello from (prod) properties file   ← when prod is active
```

</details>

<details>
<summary><strong>Tests</strong></summary>

```bash
mvn test
```

`ProdProfileControllerTests` uses `@ActiveProfiles("prod")`.  
`TestProfileControllerTests` uses `@ActiveProfiles("test")`.  
`ProfileControllerTests` uses the default profile.

</details>

## How profiles load

Spring Boot always loads `application.properties` (or `.yml`). If a profile is active, it then overlays `application-{profile}.properties` (or `.yml`). Later files win on the same key.

```
application.properties          →  server.port=9001, default message
        └─ application-prod.properties   →  spring.message (prod)
        └─ application-test.properties   →  spring.message (test)
        └─ application-dev.yml           →  spring.message (dev) + port 8085
```

The controller does not branch on the profile name. It injects `${spring.message}`:

```java
public ProfileController(@Value("${spring.message}") String message) {
    this.message = message;
}
```

## Profile notes

### dev

<img src="docs/images/env-dev.jpg" alt="Development workstation" width="280" align="right">

Local inner-loop profile. YAML on purpose, matching the original demo.

- File: `src/main/resources/application-dev.yml`
- Activate: `dev`
- Port: **8085**
- Message: `Hello, from dev environment`

<br clear="all">

### test

<img src="docs/images/env-test.jpg" alt="QA laboratory" width="280" align="right">

Automated / QA profile. Properties file, same port as default so `http://localhost:9001/rest/profile` still works.

- File: `src/main/resources/application-test.properties`
- Activate: `test`
- Port: **9001**
- Message: `hello from (test) properties file`

<br clear="all">

### prod

<img src="docs/images/env-prod.jpg" alt="Production data center" width="280" align="right">

Production overlay. Properties file, port stays 9001.

- File: `src/main/resources/application-prod.properties`
- Activate: `prod`
- Port: **9001**
- Message: `hello from (prod) properties file`

After a successful prod start, the console should read:

```
The following profiles are active: prod
```

<br clear="all">

---

<p align="center"><sub>Illustrations generated for this repo. They are mood pieces, not screenshots of the running app.</sub></p>
