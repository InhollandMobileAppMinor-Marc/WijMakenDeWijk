package nl.woonwaard.wij_maken_de_wijk.domain.services

import nl.woonwaard.wij_maken_de_wijk.domain.models.User

interface UsersRepository {
    suspend fun getAllUsers(): Set<User>

    suspend fun getUserById(id: String): User?

    suspend fun deleteUser(user: User): Boolean

    suspend fun deleteUser(id: String): Boolean
}
