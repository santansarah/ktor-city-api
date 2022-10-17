package com.santansarah.plugins

import com.auth0.jwk.JwkProviderBuilder
import com.santansarah.utils.GoogleException
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import java.net.URL
import java.util.concurrent.TimeUnit

fun AuthenticationConfig.configureGoogleJWT(config: ApplicationConfig) {

    // here's the guide from Google:
    // https://developers.google.com/identity/one-tap/android/idtoken-auth
    // use these values in resources/application.conf
    // save your secrets in environment variables

    // not using realm for now.
    val jwtIssuer = config.property("jwt.google.issuer").getString()
    val jwtAudience = config.property("jwt.google.audience").getString()
    val jwtRealm = config.property("jwt.google.realm").getString()

    val jwkProvider = JwkProviderBuilder(URL("https://www.googleapis.com/oauth2/v3/certs"))
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()

    // google one-tap verification
    // name this, in case we end up with more than one
    // jwt check.
    jwt("google") {
        verifier(jwkProvider) {
            withIssuer(jwtIssuer)
            withAudience(jwtAudience)
        }
        validate { credentials ->

            println("credentials: ${credentials.payload.claims.values}")

            // check the JWT Google criteria
            with(credentials.payload) {
                if (!audience.contains(jwtAudience) || !issuer.contains(issuer))
                    return@validate null
            }

            // make sure the Nonce is the same from header + JWT
            val nonceFromHeader = this.request.headers["x-nonce"] ?: ""
            val nonceFromJWT = credentials.payload.getClaim("nonce").asString()
            println("nonce from validate: $nonceFromHeader ; $nonceFromJWT")

            if (nonceFromHeader != nonceFromJWT)
               return@validate null

            // if we've made it here, we're all good
            JWTPrincipal(credentials.payload)

        }
        challenge { defaultScheme, realm ->
            throw GoogleException()
        }

    }
}