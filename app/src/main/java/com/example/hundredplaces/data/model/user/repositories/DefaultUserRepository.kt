package com.example.hundredplaces.data.model.user.repositories

import android.util.Log
import com.example.hundredplaces.data.model.user.UpdatePasswordRequest
import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.user.datasources.UserDao
import com.example.hundredplaces.data.model.user.datasources.UserRestApi
import com.example.hundredplaces.util.NetworkConnection
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import java.net.SocketTimeoutException

const val USER_REPOSITORY_TAG = "User Repository"

class DefaultUserRepository(
    private val userLocalDataSource: UserDao,
    private val userRemoteDataSource: UserRestApi,
    private val networkConnection: NetworkConnection,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val refreshIntervalMs: Long = 60_000
) : UserRepository{

    private var _currentUser: User? = null
    private val _userId = MutableStateFlow<Long?>(null)
    override val userId: StateFlow<Long?> = _userId

    private fun getIsUserExistingById(userId: Long): Flow<Boolean> = flow {
        while (true) {
            if (networkConnection.isNetworkConnected) {
                try {
                    val isUserExisting = userRemoteDataSource.getIsUserExistingById(userId)
                    emit(isUserExisting)
                }
                catch (_: SocketTimeoutException) {
                    Log.e("User Remote Datasource", "Server is unreachable!")
                }
            }
            delay(refreshIntervalMs)
        }
    }.flowOn(dispatcher)

    override fun logOut() {
        _userId.value = null
    }

    override suspend fun pullUser(): Pair<String, String>? {
        return withContext(dispatcher) {
            var result: Pair<String, String>? = null
            if (networkConnection.isNetworkConnected) {
                try {
                    _currentUser?.let {
                        val user = userRemoteDataSource.getUserByEmailAndPassword(it.email, it.password)
                        if (user != null) {
                            if (it != user) {
                                _currentUser = user
                                userLocalDataSource.update(user)
                            }
                            result = Pair(user.name, user.email)
                        }
                    }
                }
                catch (_: SocketTimeoutException) {
                    Log.e("User Remote Datasource", "Server is unreachable!")
                }
            }
            return@withContext result
        }
    }

    override suspend fun getUserByEmailAndPassword(email: String, password: String){

        var user = userLocalDataSource.getUserByEmailAndPassword(email, password)

        if (networkConnection.isNetworkConnected && user == null) {
            try {
                user = userRemoteDataSource.getUserByEmailAndPassword(email, password)
                if (user != null) userLocalDataSource.insert(user)
            }
            catch (_: SerializationException) {
                Log.e(USER_REPOSITORY_TAG, "Serialization error occurred!")
            }
            catch (_: SocketTimeoutException) {
                Log.e(USER_REPOSITORY_TAG, "Server is unreachable!")
            }
        }

        if (user != null) {
            _currentUser = user
            _userId.value = user.id
            CoroutineScope(dispatcher).launch {
                pullUser()
                getIsUserExistingById(user.id)
                    .collect { isExisting ->
                        if (!isExisting) {
                            userLocalDataSource.delete(user)
                            _userId.value = null
                        }
                    }
            }
        }
    }

    override suspend fun getUserByEmail(email: String) {

        var user = userLocalDataSource.getUserByEmail(email)

        if (user != null) {
            _currentUser = user
            _userId.value = user.id
            CoroutineScope(dispatcher).launch {
                pullUser()
                getIsUserExistingById(user.id)
                    .collect { isExisting ->
                        if (!isExisting) {
                            userLocalDataSource.delete(user)
                            _userId.value = null
                        }
                    }
            }
        }
    }

    override suspend fun insertUser(user: User) : Boolean {
        var result = false
        if (networkConnection.isNetworkConnected) {
            try {
                userRemoteDataSource.insert(user)
                userLocalDataSource.insert(user)
                _userId.value = user.id
                result = true
            } catch (_: SocketTimeoutException) {
                Log.e(USER_REPOSITORY_TAG, "Server is unreachable. Insert in remote failed.")
            }
        }
        return result
    }

    override suspend fun deleteUser(user: User) : Boolean {
        var result = false
        withContext(dispatcher) {
            if (networkConnection.isNetworkConnected) {
                try {
                    userRemoteDataSource.delete(user)
                    userLocalDataSource.delete(user)
                    _userId.value = null
                    result = true
                } catch (_: SocketTimeoutException) {
                    Log.e(USER_REPOSITORY_TAG, "Server is unreachable. Delete in remote failed.")
                }
            }
        }
        return result
    }

    override suspend fun updateUserDetails(name: String, email: String) : Boolean {
        var result = false
        withContext(dispatcher) {
            if (networkConnection.isNetworkConnected) {
                pullUser()
                val newUser = _currentUser?.copy(
                    name = name,
                    email = email
                )
                if (newUser != null) {
                    try {
                        userRemoteDataSource.updateDetails(newUser)
                        userLocalDataSource.update(newUser)
                        result = true
                    } catch (_: SocketTimeoutException) {
                        Log.e(USER_REPOSITORY_TAG, "Server is unreachable. Update in remote failed.")
                    }
                }
            }
        }
        return result
    }

    override suspend fun updateUserPassword(oldPassword: String, newPassword: String): Boolean {
        var result = false
        if (_currentUser?.password == oldPassword) {
            withContext(dispatcher) {
                if (networkConnection.isNetworkConnected) {
                    pullUser()
                    val newUser = _currentUser?.copy(
                        password = newPassword
                    )
                    if (newUser != null) {
                        val updatePasswordRequest = UpdatePasswordRequest(
                            user = newUser,
                            oldPassword = _currentUser?.password.toString()
                        )
                        try {
                            userRemoteDataSource.updatePassword(updatePasswordRequest)
                            userLocalDataSource.update(newUser)
                            result = true
                        } catch (_: SocketTimeoutException) {
                            Log.e(USER_REPOSITORY_TAG, "Server is unreachable. Update in remote failed.")
                        }
                    }
                }
            }
        }
        return result
    }
}