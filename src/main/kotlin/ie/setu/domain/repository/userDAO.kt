package ie.setu.domain.repository

import ie.setu.domain.User
import ie.setu.domain.db.Users
import ie.setu.utils.mapToUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserDAO {

    private val users = arrayListOf<User>(
        User(name = "Alice", email = "alice@wonderland.com", id = 0),
        User(name = "Bob", email = "bob@cat.ie", id = 1),
        User(name = "Mary", email = "mary@contrary.com", id = 2),
        User(name = "Carol", email = "carol@singer.com", id = 3)
    )

    fun getAll(): ArrayList<User> {
        val userList: ArrayList<User> = arrayListOf()
        transaction {
            Users.selectAll().map {
                userList.add(mapToUser(it)) }
        }
        return userList
    }

    fun findById(id: Int): User?{
        return transaction {
            Users.select() {
                Users.id eq id}
                .map{mapToUser(it)}
                .firstOrNull()
        }
    }

    fun save(user: User){
        transaction {
            Users.insert {
                it[name] = user.name
                it[email] = user.email
            }
        }
    }

    fun delete(id: Int):Int{
        return transaction{ Users.deleteWhere{
            Users.id eq id
        }
        }
    }

    fun update(id: Int, user: User){
        transaction {
            Users.update ({
                Users.id eq id}) {
                it[name] = user.name
                it[email] = user.email
            }
        }
    }
}