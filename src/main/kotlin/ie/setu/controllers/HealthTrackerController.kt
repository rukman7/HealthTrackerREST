package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ie.setu.domain.User
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Activity
import ie.setu.domain.BMI
import ie.setu.domain.WaterIntake
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.BmiDAO
import ie.setu.domain.repository.WaterIntakeDAO
import ie.setu.utils.jsonToObject
import io.javalin.plugin.openapi.annotations.*

object HealthTrackerController {

    private val userDao = UserDAO()
    private val activityDAO = ActivityDAO()
    private val waterIntakeDAO = WaterIntakeDAO()
    private val bmiDAO = BmiDAO()

    @OpenApi(
        summary = "Get all users",
        operationId = "getAllUsers",
        tags = ["User"],
        path = "/api/users",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<User>::class)])]
    )
    fun getAllUsers(ctx: Context) {
        val users = userDao.getAll()
        if (users.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(users)
    }

    @OpenApi(
        summary = "Get user by ID",
        operationId = "getUserById",
        tags = ["User"],
        path = "/api/users/{user-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(User::class)])]
    )
    fun getUserByUserId(ctx: Context) {
        val user = userDao.findById(ctx.pathParam("user-id").toInt())
        if (user != null) {
            ctx.json(user)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Get user by Email",
        operationId = "getUserByEmail",
        tags = ["User"],
        path = "/api/users/email/{email}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("email", Int::class, "The user email")],
        responses  = [OpenApiResponse("200", [OpenApiContent(User::class)])]
    )
    fun getUserByEmail(ctx: Context) {
        val user = userDao.findByEmail(ctx.pathParam("email"))
        if (user != null) {
            ctx.json(user)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Add User",
        operationId = "addUser",
        tags = ["User"],
        path = "/api/users",
        method = HttpMethod.POST,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200")]
    )
    fun addUser(ctx: Context) {
        val user : User = jsonToObject(ctx.body())
        val userId = userDao.save(user)
        if (userId != null) {
            user.id = userId
            ctx.json(user)
            ctx.status(201)
        }
    }

    @OpenApi(
        summary = "Delete user by ID",
        operationId = "deleteUserById",
        tags = ["User"],
        path = "/api/users/{user-id}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteUser(ctx: Context){
        if (userDao.delete(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Update user by ID",
        operationId = "updateUserById",
        tags = ["User"],
        path = "/api/users/{user-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateUser(ctx: Context){
        val foundUser : User = jsonToObject(ctx.body())
        if ((userDao.update(id = ctx.pathParam("user-id").toInt(), user=foundUser)) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    //--------------------------------------------------------------
    // activityDAOI specifics
    //-------------------------------------------------------------
    @OpenApi(
        summary = "Get all activities",
        operationId = "getAllActivities",
        tags = ["Activity"],
        path = "/api/activities",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<User>::class)])]
    )
    fun getAllActivities(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val activities = activityDAO.getAll();
        if(activities.size != 0){
            ctx.status(200);
        }
        else{
            ctx.status(404);
        }
        ctx.json(mapper.writeValueAsString(activities));
    }

    @OpenApi(
        summary = "Get activity by user ID",
        operationId = "getActivityByUserId",
        tags = ["Activity"],
        path = "/api/activities/{user-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(User::class)])]
    )
    fun getActivitiesByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val activities = activityDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (activities.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(activities))
                ctx.status(200)
            }
            else{
                ctx.status(404)
            }
        }
    }

    @OpenApi(
        summary = "Add Activity",
        operationId = "addActivity",
        tags = ["Activity"],
        path = "/api/activities",
        method = HttpMethod.POST,
        responses  = [OpenApiResponse("200")]
    )
    fun addActivity(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val activity = mapper.readValue<Activity>(ctx.body())
        activityDAO.save(activity)
        ctx.json(activity)
    }

    @OpenApi(
        summary = "Get activity by activity ID",
        operationId = "getActivityByActivityId",
        tags = ["Activity"],
        path = "/api/activities/{activity-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("activity-id", Int::class, "The activity ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(Activity::class)])]
    )
    fun getActivitiesByActivityId(ctx: Context) {
        val activity = activityDAO.findByActivityId((ctx.pathParam("activity-id").toInt()))
        if (activity != null){
            val mapper = jacksonObjectMapper()
                .registerModule(JodaModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            ctx.json(mapper.writeValueAsString(activity))
        }
    }

    @OpenApi(
        summary = "Delete Activity by Activity ID",
        operationId = "deleteActivityByActivityId",
        tags = ["Activity"],
        path = "/api/activities/{activity-id}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("activity-id", Int::class, "The activity ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteActivityByActivityId(ctx: Context){
        if (activityDAO.deleteByActivityId(ctx.pathParam("activity-id").toInt()) != 0){
                ctx.status(204);
            }
            else{
                ctx.status(204);
        }
    }

    fun deleteActivityByUserId(ctx: Context){
        activityDAO.deleteByUserId(ctx.pathParam("user-id").toInt())
    }

    @OpenApi(
        summary = "Update activity by ID",
        operationId = "updateActivityById",
        tags = ["Activity"],
        path = "/api/activities/{activity-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("activity-id", Int::class, "The activity ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateActivity(ctx: Context){
        val activity : Activity = jsonToObject(ctx.body())
        if ((activityDAO.updateByActivityId(activityId = ctx.pathParam("activity-id").toInt(), activityDTO=activity)) != 0){
            ctx.status(204)
        }
        else{
            ctx.status(404)
        }
    }

    //--------------------------------------------------------------
    // WaterIntakeDAO specifics
    //--------------------------------------------------------------

    @OpenApi(
        summary = "Add Water Intake",
        operationId = "addWaterIntake",
        tags = ["WaterIntake"],
        path = "/api/waterintake",
        method = HttpMethod.POST,
        responses  = [OpenApiResponse("200")]
    )
    fun addWaterIntake(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val waterIntake = mapper.readValue<WaterIntake>(ctx.body())
        val userId = waterIntakeDAO.save(waterIntake)
        if (userId != null){
            waterIntake.user_id = userId
            ctx.json(waterIntake)
            ctx.status(201)
        }
        else{
            ctx.status(400)
        }
    }

    @OpenApi(
        summary = "Get all water intake",
        operationId = "getAllWaterIntake",
        tags = ["WaterIntake"],
        path = "/api/waterintake",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<WaterIntake>::class)])]
    )
    fun getAllWaterIntake(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val waterintake = waterIntakeDAO.getAll()
        if (waterintake.size != 0){
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(mapper.writeValueAsString(waterIntakeDAO.getAll()))
    }

    @OpenApi(
        summary = "Update water intake by user ID",
        operationId = "updateWaterIntake",
        tags = ["WaterIntake"],
        path = "/api/waterintake/{user-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateWaterIntake(ctx: Context){
        val waterIntake : WaterIntake = jsonToObject(ctx.body())
        if ((waterIntakeDAO.update(id = ctx.pathParam("user-id").toInt(), waterIntake=waterIntake)) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Get water intake by User ID",
        operationId = "getWaterIntakeByUser",
        tags = ["WaterIntake"],
        path = "/api/waterintake/{user-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(BMI::class)])]
    )
    fun getWaterIntakeByUser(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val waterintake = waterIntakeDAO.getByUserId(ctx.pathParam("user-id").toInt())
        if (waterintake != null){
            ctx.json(mapper.writeValueAsString(waterintake))
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Delete water intake by user ID",
        operationId = "deleteWaterIntakeByUserId",
        tags = ["WaterIntake"],
        path = "/api/waterintake/{user-id}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteWaterIntakeByUserId(ctx: Context){
        waterIntakeDAO.deleteByUserId(ctx.pathParam("user-id").toInt())
    }


    //--------------------------------------------------------------
    // BmiDAO specifics
    //--------------------------------------------------------------

    @OpenApi(
        summary = "Add BMI Data",
        operationId = "addBmiData",
        tags = ["BMI"],
        path = "/api/bmi",
        method = HttpMethod.POST,
        responses  = [OpenApiResponse("200")]
    )
    fun addBmiData(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val bmiData = mapper.readValue<BMI>(ctx.body())
        val bmiVal = calculateBmi(bmiData.height, bmiData.weight)
        bmiData.bmi = bmiVal
        val userId = bmiDAO.save(bmiData)
        if (userId != null){
            ctx.json(bmiData)
            ctx.status(201)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Get all BMI information",
        operationId = "getAllBmiInfo",
        tags = ["BMI"],
        path = "/api/bmi",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<BMI>::class)])]
    )
    fun getAllBmiInfo(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val bmidata = bmiDAO.getAll()
        if(bmidata.size != 0){
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(mapper.writeValueAsString(bmidata))
    }

        @OpenApi(
        summary = "Get BMI info by User ID",
        operationId = "getBmiInfoByUser",
        tags = ["BMI"],
        path = "/api/bmi/{user-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(BMI::class)])]
    )
    fun getBmiInfoByUser(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val bmidata = bmiDAO.getByUserId(ctx.pathParam("user-id").toInt())
        if (bmidata != null){
            ctx.json(mapper.writeValueAsString(bmidata))
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Update BMI data by user ID",
        operationId = "updateBmiData",
        tags = ["BMI"],
        path = "/api/bmi/{user-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateBmiData(ctx: Context){
        val bmidata : BMI = jsonToObject(ctx.body())
        bmidata.bmi = calculateBmi(bmidata.height, bmidata.weight)
        if ((bmiDAO.update(id = ctx.pathParam("user-id").toInt(), bmiData=bmidata)) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


        @OpenApi(
        summary = "Delete BMI data by ID",
        operationId = "deleteBmiDataByUserId",
        tags = ["BMI"],
        path = "/api/bmi/{user-id}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteBmiDataByUserId(ctx: Context){
        if(bmiDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0){
            ctx.status(204)
        }
        else{
            ctx.status(404)
        }
    }

    private fun calculateBmi(height: Double, weight: Double) : Int{
        return ((weight / (height * height)).toInt())
    }
}
