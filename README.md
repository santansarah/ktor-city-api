# Get Cities by Prefix and Zip Code

This branch adds the routes to get city data.

* Android Source: https://github.com/santansarah/city-api-client/tree/ktor-client-app-scoped
* Android Source: https://github.com/santansarah/city-api-client/tree/ktor-client-closable

## YouTube Video

https://youtu.be/gyOdfRgNs2k

## Get City by Prefix

```
curl --location --request GET 'http://127.0.0.1:8080/cities?name=pho' \
--header 'x-api-key: Pr67HTHS4VIP1eN'
```

## Get City by Zip

```
curl --location --request GET 'http://127.0.0.1:8080/cities/zip/90210' \
--header 'x-api-key: Pr67HTHS4VIP1eN'
```