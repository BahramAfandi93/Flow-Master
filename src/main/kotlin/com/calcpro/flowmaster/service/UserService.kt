package com.calcpro.flowmaster.service

import com.calcpro.flowmaster.dao.entity.AppUser
import com.calcpro.flowmaster.dao.repository.UserRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.UUID
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    companion object {
        private val log = KotlinLogging.logger {}
    }

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = DefaultOAuth2UserService().loadUser(userRequest)

        val attributes = oAuth2User.attributes
        val email = attributes["email"] as String?
        val name = attributes["given_name"] as String? // First name
        val lastname = attributes["family_name"] as String? // Last name
//        val picture = attributes["picture"] as String? // Profile picture URL (optional)

        if (email.isNullOrBlank()) {
            throw OAuth2AuthenticationException("Email not found from Google account")
        }

        // Check if user already exists
        val user = findByEmail(email)?.apply {
            this.name = name
            this.lastname = lastname
        } ?: AppUser(
            id = UUID.randomUUID().toString(),
            email = email,
            name = name,
            lastname = lastname
        )

        userRepository.save(user)

        // Store user in SecurityContext for session tracking
        return DefaultOAuth2User(
            oAuth2User.authorities,
            attributes,
            "email"
        )
    }

    fun findByEmail(email: String): AppUser? {
        return userRepository.findByEmail(email)
    }

//    fun save(user: AppUser): AppUser {
//        return userRepository.save(user)
//    }

//    @Transactional
//    fun saveOrUpdateUser(oAuth2User: OAuth2User): AppUser {
//        val email = oAuth2User.getAttribute<String>("email")
//        val name = oAuth2User.getAttribute<String>("name")
//        val UUID = oAuth2User.getAttribute<String>("sub") // Google unique ID
//
//        val existingUser = userRepository.findById(id!!).orElse(null)
//
//        return if (existingUser == null) {
//            val newUser = AppUser(
//                UUID = UUID,
//                name = name,
//                email = email
//            )
//            userRepository.save(newUser).also {
//                println("New user registered: ${newUser.email}")
//            }
//        } else {
//            // Update existing user details if necessary
//            existingUser.name = name
//            existingUser.email = email
//            userRepository.save(existingUser).also {
//                println("Existing user updated: ${existingUser.email}")
//            }
//        }
//    }
}