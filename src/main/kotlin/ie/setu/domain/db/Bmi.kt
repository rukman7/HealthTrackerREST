package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

//singleton
object Bmi : Table("bmi") {
    val user_id = integer("user_id").primaryKey()
    val description = varchar("description", 100)
    val height = double("height")
    val weight = double("weight")
    val bmi = integer("bmi")
}