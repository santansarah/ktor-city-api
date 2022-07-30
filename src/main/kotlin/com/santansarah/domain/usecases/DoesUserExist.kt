package com.santansarah.domain.usecases

import com.santansarah.data.User
import com.santansarah.data.UserDao
import com.santansarah.domain.UserResponse
import com.santansarah.utils.ServiceResult

class DoesUserExist(
    private val userDao: UserDao
) {
    suspend operator fun invoke(user: User): ServiceResult<User> {
        return userDao.getUser(user)
    }
}