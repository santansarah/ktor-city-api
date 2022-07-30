package com.santansarah.domain.usecases

import com.santansarah.data.User
import com.santansarah.data.UserApp
import com.santansarah.data.UserAppDao
import com.santansarah.utils.ServiceResult

class DoesAppExist(
    private val userAppDao: UserAppDao
) {
    suspend operator fun invoke(userApp: UserApp): ServiceResult<Boolean> {
        return userAppDao.checkForDupApp(userApp)
    }

}