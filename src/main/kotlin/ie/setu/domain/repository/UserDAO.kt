package ie.setu.domain.repository

import ie.setu.domain.UserDTO
import ie.setu.domain.db.Users
import ie.setu.utils.mapToUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserDAO {

    fun getAll(): ArrayList<UserDTO> {
        val userDTOList: ArrayList<UserDTO> = arrayListOf()
        transaction {
            Users.selectAll().map {
                userDTOList.add(mapToUser(it)) }
        }
        return userDTOList
    }

    fun findById(id: Int): UserDTO?{
        return transaction {
            Users.select() {
                Users.id eq id}
                .map{mapToUser(it)}
                .firstOrNull()
        }
    }

    fun save(userDTO: UserDTO) : Int?{
        return transaction {
            Users.insert {
                it[name] = userDTO.name
                it[email] = userDTO.email
            } get Users.id
        }
    }

    fun findByEmail(email: String) :UserDTO?{
        return transaction {
            Users.select() {
                Users.email eq email}
                .map{mapToUser(it)}
                .firstOrNull()
        }
    }

    fun delete(id: Int):Int{
        return transaction{
            Users.deleteWhere{
                Users.id eq id
            }
        }
    }

    fun update(id: Int, userDTO: UserDTO): Int{
        return transaction {
            Users.update ({
                Users.id eq id}) {
                it[name] = userDTO.name
                it[email] = userDTO.email
            }
        }
    }
}