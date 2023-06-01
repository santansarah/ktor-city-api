# Ktor City API with Koin, Exposed, and SQLite

This Ktor REST API provides:

* User Validation with Google One Tap JWT
* API Key Generation
* CRUD Operations for API Keys Assigned to Apps
* City Name Lookups
* City Detail Lookups

Features and plugins include Exposed with SQLite, global error handling with Ktor Status Pages, and two forms of
Authentication, including by API key for the front-end client, and JWT authentication for Google One Tap.

## Important - Latest Branch - Do Not Use Main

> https://github.com/santansarah/ktor-city-api/tree/ktor-crud

## Database Setup

1. Get the data: https://simplemaps.com/data/us-zips.
2. Scrub the data. I deleted some unwanted columns, and set the population to
   `0` where it was null.

   ![image](sorted_data.png)
3. Import the data into an SQLite database.
4. Save it in your project root folder.

## Ktor API with Exposed

[![Ktor API with Exposed](https://img.youtube.com/vi/iX4ZIRjmpN4/0.jpg)](https://www.youtube.com/watch?v=iX4ZIRjmpN4)

Links:

* GitHub Source Code: https://github.com/santansarah/ktor-city-api
* Video Branch: https://github.com/santansarah/ktor-city-api/tree/user-implementation
* Ktor Status Pages: https://ktor.io/docs/status-pages.html
* Exposed Wiki: https://github.com/JetBrains/Exposed/wiki
* City info: https://simplemaps.com/data/us-zips

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

## Google One Tap Validation with Ktor

[![Google One Tap](https://img.youtube.com/vi/WsnNiQje1o8/0.jpg)](https://www.youtube.com/playlist?list=PLzxawGXQRFswx9iqiCCnrDtYJw1zwGLkd)

This branch validates a Google JWT token that's returned from the Google One Tap API.
When the validation is successful for a new user, it inserts basic user data (email, name)
into the Users SQLite table. It also validates existing users and returns user data from the
database from the JWT token email address.

The validation routes throw a custom Google validation exception, and also support validating
a Nonce claim.

Links:

* Android app: https://github.com/santansarah/city-api-client/tree/ktor-network-api
* Ktor JWT: https://ktor.io/docs/jwt.html
* Google One Tap: https://developers.google.com/identity/one-tap/android/idtoken-auth

### Authenticate a User

Sign up, Sign In: This route inserts or returns an authenticated user.

In my Android app, I send an Nonce with my sign in requests. Google sends back a JWT,
including a users basic account info and the Nonce.

My Ktor API expects the following request, which includes a custom `x-nonce` header field
and the Google `Bearer Authorization` JWT token.

```
curl --location --request GET 'http://127.0.0.1:8080/users/authenticate' \
--header 'x-nonce: XXXaaa000YYyy' \
--header 'Authorization: Bearer xxxxXXXX.yyyyYYYY.zzzzZZZZ'
```

### Get Existing User

Once a userId is created and saved to my Android app, this route uses my app’s API key to return a user.

```
curl --location --request GET 'http://127.0.0.1:8080/users/20' \
--header 'x-api-key: Pr67HTHS4VIP1eN'
```

## Get Cities by Prefix and Zip Code

[![Ktor Client](https://img.youtube.com/vi/gyOdfRgNs2k/0.jpg)](https://www.youtube.com/watch?v=gyOdfRgNs2k)

This branch adds the routes to get city data.

* Android Source: https://github.com/santansarah/city-api-client/tree/ktor-client-app-scoped
* Android Source: https://github.com/santansarah/city-api-client/tree/ktor-client-closable

### Get City by Prefix

```
curl --location --request GET 'http://127.0.0.1:8080/cities?name=pho' \
--header 'x-api-key: Pr67HTHS4VIP1eN'
```

### Get City by Zip

```
curl --location --request GET 'http://127.0.0.1:8080/cities/zip/90210' \
--header 'x-api-key: Pr67HTHS4VIP1eN'
```

# Ktor Create, Read, Patch (CRUD)

[![Ktor CRUD](https://img.youtube.com/vi/vT5q6dQ-em4/0.jpg)](https://www.youtube.com/watch?v=vT5q6dQ-em4)

This branch allows users to create, get, and patch apps. When an app is created,
the API auto-generates an API key that developers can use to query city data.

* Ktor API Source: https://github.com/santansarah/ktor-city-api/tree/ktor-crud
* Android Source: https://github.com/santansarah/city-api-client/tree/ktor-crud

### GET: Apps

```
curl --location --request GET 'http://127.0.0.1:8080/apps/24' \
--header 'x-api-key: Pr67HTHS4VIP1eN'
```

### POST: Create a New App

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

### GET: Get App Details

```
curl --location --request GET 'http://127.0.0.1:8080/app/4' \
--header 'x-api-key: Pr67HTHS4VIP1eN'
```

### PATCH: Update App Details

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

City info provided by: https://simplemaps.com/data/us-zips
