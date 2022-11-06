package ie.setu.helpers

import ie.setu.domain.Activity
import ie.setu.domain.BMI
import ie.setu.domain.User
import ie.setu.domain.WaterIntake
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Bmi
import ie.setu.domain.db.Users
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.BmiDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.domain.repository.WaterIntakeDAO
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"
val updatedName = "Updated Name"
val updatedEmail = "Updated Email"

val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4)
)

val activities = arrayListOf<Activity>(
    Activity(id = 1, description = "Running", duration = 22.0, calories = 230, started = DateTime.now(), userId = 1),
    Activity(id = 2, description = "Hopping", duration = 10.5, calories = 80, started = DateTime.now(), userId = 1),
    Activity(id = 3, description = "Walking", duration = 12.0, calories = 120, started = DateTime.now(), userId = 2)
)

var waterintakelist = arrayListOf<WaterIntake>(
    WaterIntake(user_id = 1, "Water intake information", cups = 3, target = 8),
    WaterIntake(2,"Water Intake Information",4,7),
    WaterIntake(3,"Water Intake Information",5,6)
)

val bmi = arrayListOf<BMI>(
    BMI(1,"My BMI 1",6.0,67.0,18),
    BMI(2,"My BMI 2",5.7,71.0,20),
    BMI(3,"My MBI 3",6.6,76.0,28)
)

fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.save(users.get(0))
    userDAO.save(users.get(1))
    userDAO.save(users.get(2))
    return userDAO
}
fun populateActivityTable(): ActivityDAO {
    SchemaUtils.create(Activities)
    val activityDAO = ActivityDAO()
    activityDAO.save(activities.get(0))
    activityDAO.save(activities.get(1))
    activityDAO.save(activities.get(2))
    return activityDAO
}

fun PopulateBMITable() : BmiDAO{
    SchemaUtils.create(Bmi)
    val bmiDAO = BmiDAO()
    bmiDAO.save(bmi.get(0))
    bmiDAO.save(bmi.get(1))
    bmiDAO.save(bmi.get(2))
    return bmiDAO
}

fun PopulateWaterIntake() : WaterIntakeDAO{
    SchemaUtils.create(ie.setu.domain.db.WaterIntake)
    val waterintakeDAO = WaterIntakeDAO()
    waterintakeDAO.save(waterintakelist.get(0))
    waterintakeDAO.save(waterintakelist.get(1))
    waterintakeDAO.save((waterintakelist.get(2)))
    return  waterintakeDAO
}