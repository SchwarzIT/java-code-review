# Coupon

Technical Test Spring Boot

## Code review

> All the changes made during the code review of this test are documented in [REVIEW_en.md](REVIEW_en.md).

This application implemented using:

#### Spring boot 2.2.5

#### java Open JDK 11

#### gradle

#### JPA

#### H2 database

#### lombok

#### mapstruct

#### Swagger (springfox)

#### Bean Validation

#### Mockito

#### Junit 5

#### Integration Test

## Java version

> java version used to compile is 11

## IDE

> IntelliJ IDEA

#### Testing

> Unit tests and Integration tests are both performed on 'gradlew test'

> Integration Test is located at
> 'review.src.test.java.schwarz.jobs.interview.coupon.web.controller.CouponResourceControllerIntegrationTest',
> it runs against an in-memory H2 database and covers the three endpoints end to end.

## To start the API

run ./gradlew bootRun

Or with Docker, no local JDK/Gradle needed:

```
docker build -t coupon-api .
docker run -p 8080:8080 coupon-api
```

## To access H2 DB console

> URL : http://localhost:8080/h2-console
>
> JDBC URL : jdbc:h2:mem:testdb
>
> user is sa
>
> password is password
>
> default data is loaded on startup from data.sql. Default coupons are:
>> TEST1 : 10% discount, minimum basket 50
>>
>> TEST2 : 15% discount, minimum basket 100
>>
>> TEST3 : 20% discount, minimum basket 200

## Swagger api

> URL : http://localhost:8080/swagger-ui/index.html

> URL : http://localhost:8080/v2/api-docs

#### Applies a coupon to a basket

> POST : http://localhost:8080/api/apply

```
{
  "code": "TEST1",
  "basket": {
    "value": 100.00
  }
}
```

#### Creates a new coupon

> POST : http://localhost:8080/api/create

```
{
  "code": "string",
  "discount": "number",
  "minBasketValue": "number"
}
```

#### Gets coupons by code

> GET : http://localhost:8080/api/coupons?codes=TEST1&codes=TEST2

```
Unknown codes are skipped instead of returning an error.
```
