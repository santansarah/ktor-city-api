package com.santansarah.plugins

import com.auth0.jwk.JwkProviderBuilder
import com.santansarah.utils.GoogleException
import io.ktor.http.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import java.net.URL
import java.util.concurrent.TimeUnit

/**
 * Here's the guide from Google:
 * https://developers.google.com/identity/one-tap/android/idtoken-auth
 * - use these values in resources/application.conf
 * - save your secrets in environment variables
 *
 * For my Google JWT configuration, I pass the [ApplicationConfig]
 * from the top level [Authentication] plugin.
 */
fun AuthenticationConfig.configureGoogleJWT(config: ApplicationConfig) {

    /**
     * Next, I use the [ApplicationConfig] to get the issuer and the audience values
     * from my application.config file.
     * - The issuer is: https://accounts.google.com
     * - and the audience is: the Web Client id that you created in Google Cloud.
     */
    val jwtIssuer = config.property("jwt.google.issuer").getString()
    val jwtAudience = config.property("jwt.google.audience").getString()

    /**
     * To verify the token's signature, I load Google's public keys from
     * their JWT oauth URL.
     */
    val jwkProvider = JwkProviderBuilder(URL("https://www.googleapis.com/oauth2/v3/certs"))
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()

    /**
     * Now it's time to create the JWT Authentication provider.
     * I give this instance a unique name of 'google', in case I decide
     * to implement my own JWT tokens later on.
     */
    jwt("google") {

        /**
         * Next, I use Google's public keys that are stored in the jwkProvider
         * to verify the token's signature. I also make sure that the token
         * contains the Google issuer URL and my Web client id.
         */
        verifier(jwkProvider) {
            withIssuer(jwtIssuer)
            withAudience(jwtAudience)
        }
        validate { jwtCredential ->

            println("credentials: ${jwtCredential.payload.claims.values}")

            /**
             * With validate, I can access my request header. I pass
             * an Nonce value from my Android app using Ktor HttpClient.
             * If the x-nonce header value is empty, I return null, and the
             * validate function fails.
             */
            val nonceFromHeader = this.request.headers["x-nonce"]
            if (nonceFromHeader.isNullOrBlank())
                return@validate null

            /**
             * Now that I know the nonce from the header isn't null,
             * I get the nonce claim from the token, and make sure that
             * it matches what was passed in the header.
             */
            val nonceFromJWT = jwtCredential.payload.getClaim("nonce").asString()
            println("nonce from validate: $nonceFromHeader ; $nonceFromJWT")

            if (nonceFromHeader != nonceFromJWT)
               return@validate null

            /**
             * Finally, if validation is successful, I return the payload.
             */
            JWTPrincipal(jwtCredential.payload)

        }
        /**
         * For the challenge, I created a custom Google exception.
         * This exception is caught in my [configureStatusExceptions]
         * module, and responds with
         * [com.santansarah.utils.ErrorCode.INVALID_GOOGLE_CREDENTIALS].
         */
        challenge { defaultScheme, realm ->
            throw GoogleException()
        }
    }
}