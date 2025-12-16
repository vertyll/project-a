package com.vertyll.projectabackend.user.service

import com.vertyll.projectabackend.user.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
        userRepository
            .findByEmailWithRoles(username)
            .orElseThrow { UsernameNotFoundException("User not found with email: $username") }
}
