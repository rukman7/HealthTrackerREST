package ie.setu.utils

import ie.setu.domain.*
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Bmi
import ie.setu.domain.db.Foods
import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.ResultRow

fun mapToUser(it: ResultRow) = UserDTO(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
)

fun mapToActivity(it: ResultRow) = ActivityDTO(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)

fun mapToWaterIntake(it: ResultRow) = WaterIntake(
    userId = it[ie.setu.domain.db.WaterIntake.user_id],
    description = it[ie.setu.domain.db.WaterIntake.description],
    cups = it[ie.setu.domain.db.WaterIntake.cups],
    target = it[ie.setu.domain.db.WaterIntake.target]
)

fun mapToBmi(it: ResultRow) = BMIDTO(
    userId = it[Bmi.userId],
    description = it[Bmi.description],
    height = it[Bmi.height],
    weight = it[Bmi.weight],
    bmi = it[Bmi.bmi],
    id = it[Bmi.id],
    timestamp = it[Bmi.timestamp]
)

fun mapToFoodDTO(it: ResultRow) = FoodDTO(
    foodId = it[Foods.foodId],
    mealname = it[Foods.mealname],
    foodname = it[Foods.foodname],
    foodtime = it[Foods.foodtime],
    calories = it[Foods.calories],
    userId = it[Foods.userId]
)
