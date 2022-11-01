# Google One Tap Validation with Ktor

## YouTube Videos

[Google One Tap with Jetpack Compose and Ktor - Playlist](https://www.youtube.com/playlist?list=PLzxawGXQRFswx9iqiCCnrDtYJw1zwGLkd)

[Google One tap for Android with Ktor JWT Validation: Overview and Cloud Setup](https://youtu.be/WsnNiQje1o8)

[Validate Google One Tap JWT and Nonce with Ktor](https://youtu.be/Q7PgQdXfETU)

[Google One Tap with Jetpack Compose, Ktor HttpClient, ActivityResultRegistry](https://youtu.be/O_SBoS8aH7w)

## Overview

This branch validates a Google JWT token that's returned from the Google One Tap API.
When the validation is successful for a new user, it inserts  basic user data (email, name)
into the Users SQLite table. It also validates existing users and returns  user data from the
database from the JWT token email address.

The validation routes throw a custom Google validation exception, and also support validating
a Nonce claim.

Links:

* GitHub Source Code: https://github.com/santansarah/ktor-city-api/tree/google-one-tap
* Ktor JWT: https://ktor.io/docs/jwt.html
* Google One Tap: https://developers.google.com/identity/one-tap/android/idtoken-auth

## Authenticate a User

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

## Get Existing User

Once a userId is created and saved to my Android app, this route uses my app’s API key to return a user.

```
curl --location --request GET 'http://127.0.0.1:8080/users/20' \
--header 'x-api-key: Pr67HTHS4VIP1eN'
```