package ie.setu.utils

import ie.setu.domain.User
import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.ResultRow
import ie.setu.domain.Activity
import ie.setu.domain.BMI
import ie.setu.domain.WaterIntake
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Bmi

fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
)

fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)

fun mapToWaterIntake(it: ResultRow) = WaterIntake(
    user_id = it[ie.setu.domain.db.WaterIntake.user_id],
    description = it[ie.setu.domain.db.WaterIntake.description],
    cups = it[ie.setu.domain.db.WaterIntake.cups],
    target = it[ie.setu.domain.db.WaterIntake.target]
)

fun mapToBmi(it: ResultRow) = BMI(
    user_id = it[Bmi.user_id],
    description = it[Bmi.description],
    height = it[Bmi.height],
    weight = it[Bmi.weight],
    bmi = it[Bmi.bmi]
)
