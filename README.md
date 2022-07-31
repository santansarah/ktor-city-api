# Ktor City API with Koin, Exposed, and SQLite

## Video - Part 1

GitHub Source Code: https://github.com/santansarah/ktor-city-api

City info: https://simplemaps.com/data/us-zips

This Ktor REST API allows users to fetch city information by:
* City name prefixes
* Zip codes
* and more

In Part 1, I'll go over:
* The project background and basic concepts
* Exposed setup with SQLite
* Creating tables and inserting data with Exposed
* Koin dependency injection
* Implementing Use Cases
* Ktor routes

## Database Setup

1. Scrub the data. I deleted some unwanted columns, and set the population to
   `0` where it was null.

   ![image](sorted_data.png)






















City info provided by: https://simplemaps.com/data/us-zips