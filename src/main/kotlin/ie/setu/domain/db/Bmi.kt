package ie.setu.domain.db


import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

//singleton
object Bmi : Table("bmi") {
    val id = integer("bmi_id").autoIncrement().primaryKey()
    val userId = integer("userid").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val description = varchar("description", 100)
    val height = double("height")
    val weight = double("weight")
    val bmi = integer("bmi")
    val timestamp = datetime("timestamp")
}