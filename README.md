# Ktor City API with Koin, Exposed, and SQLite

## Video - Part 1

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

## Database Setup

1. Scrub the data. I deleted some unwanted columns, and set the population to
   `0` where it was null.

   ![image](sorted_data.png)






















City info provided by: https://simplemaps.com/data/us-zips