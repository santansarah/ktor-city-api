# Ktor City API with Koin, Exposed, and SQLite

## Video - Part 1
https://youtu.be/iX4ZIRjmpN4

Links:
* GitHub Source Code: https://github.com/santansarah/ktor-city-api
* Video Branch: https://github.com/santansarah/ktor-city-api/tree/user-implementation
* Ktor Status Pages: https://ktor.io/docs/status-pages.html
* Exposed Wiki: https://github.com/JetBrains/Exposed/wiki
* City info: https://simplemaps.com/data/us-zips

This Ktor REST API allows users to fetch city information by:
* City name prefixes
* Zip codes
* and more

In Part 1, I'll go over:
* The project background and basic concepts
* API Postman/Endpoints
* Exposed setup with SQLite
* Ktor data layer
* Creating tables and inserting data with Exposed
* Koin dependency injection
* Implementing Use Cases
* Ktor routes
* Ktor Status Pages (Routing Exceptions)

### Create a User

```
curl --location --request POST 'http://127.0.0.1:8080/users/create' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email":"tester@mail.com"
}
```

### Create User Response

```
{
    "user": {
        "userId": 1,
        "email": "tester@mail.com",
        "userCreateDate": "2022-07-30 01:17:45",
        "apps": []
    },
    "errors": []
}
```

## Video - Part 2
https://youtu.be/Ricf7e5cScg

Links:
* GitHub Source Code: https://github.com/santansarah/ktor-city-api
* Ktor API Key: https://github.com/LukasForst/ktor-api-key
* Ktor Status Pages: https://ktor.io/docs/status-pages.html
* Exposed Wiki: https://github.com/JetBrains/Exposed/wiki
* City info: https://simplemaps.com/data/us-zips

In Part 2, I'll go over:
* Generating an API key
* Exposed table joins
* Ktor API Key Authentication
* Ktor Dynamic Status Pages
* GET API city data

### Get an API Key

```
curl --location --request POST 'http://127.0.0.1:8080/apps/create' \
--header 'Content-Type: application/json' \
--data-raw '{
    "userId": 1,
    "email": "tester@mail.com",
    "appName": "Test App",
    "appType": "dev"
}
```

### API Key Response

```
{
    "userWithApp": {
        "userId": 1,
        "email": "tester@mail.com",
        "userCreateDate": "2022-07-23 02:20:41",
        "userAppId": 1,
        "appName": "Test App",
        "appType": "dev",
        "apiKey": "lPXYwavwODBdgd8",
        "appCreateDate": "2022-08-06 04:00:04"
    },
    "errors": []
}
```

### Use a Ktor API Key

```
curl --location --request GET 'http://127.0.0.1:8080/cities?name=maric' \
--header 'x-api-key: lPXYwavwODBdgd8'
```

### City Data Response

```
{
    "userWithApp": {
        "userId": 2,
        "email": "test1@mail.com",
        "userCreateDate": "2022-07-23 02:20:41",
        "userAppId": 1,
        "appName": "Test App",
        "appType": "dev",
        "apiKey": "Pr67HTHS4VIP1eN",
        "appCreateDate": "2022-08-06 03:59:50"
    },
    "cities": [
        {
            "zip": 85138,
            "lat": 33.0151,
            "lng": -111.98681,
            "city": "Maricopa",
            "state": "AZ",
            "population": 41266
        }
    ],
    "errors": []
}
```

## Database Setup

1. Scrub the data. I deleted some unwanted columns, and set the population to
   `0` where it was null.

   ![image](sorted_data.png)






















City info provided by: https://simplemaps.com/data/us-zips