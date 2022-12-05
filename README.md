# Ktor Create, Read, Patch (CRUD)

This branch allows users to create, get, and patch apps. When an app is created,
the API auto-generates an API key that developers can use to query city data.

* Ktor API Source: https://github.com/santansarah/ktor-city-api/tree/ktor-crud
* Android Source: https://github.com/santansarah/city-api-client/tree/ktor-crud

## YouTube Video

https://youtu.be/vT5q6dQ-em4

## GET: Apps

```
curl --location --request GET 'http://127.0.0.1:8080/apps/24' \
--header 'x-api-key: Pr67HTHS4VIP1eN'
```

## POST: Create a New App

```
curl --location --request POST 'http://127.0.0.1:8080/apps/create' \
--header 'x-api-key: Pr67HTHS4VIP1eN' \
--header 'Content-Type: application/json' \
--data-raw '{
    "userId": 24,
    "email": "tester@mail.com",
    "appName": "Create App Demo",
    "appType": "dev"
}
'
```

## GET: Get App Details

```
curl --location --request GET 'http://127.0.0.1:8080/app/4' \
--header 'x-api-key: Pr67HTHS4VIP1eN'
```

## PATCH: Update App Details

```
curl --location --request PATCH 'http://127.0.0.1:8080/app/4' \
--header 'x-api-key: Pr67HTHS4VIP1eN' \
--header 'Content-Type: application/json' \
--data-raw '{
    "appName": "Update App",
    "appType": "dev"
}
'
```