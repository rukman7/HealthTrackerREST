package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

//singleton
object WaterIntake : Table("water_intake") {
    val user_id = integer("user_id").primaryKey()
    val description = varchar("description", 100)
    val cups = integer("cups")
    val target = integer("target")
}