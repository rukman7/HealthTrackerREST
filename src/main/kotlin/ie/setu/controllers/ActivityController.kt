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

object ActivityController {

    private val userDao = UserDAO()
    private val activityDAO = ActivityDAO()

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
        if (activities.size != 0) {
            ctx.status(200);
        } else {
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
        responses = [OpenApiResponse("200", [OpenApiContent(User::class)])]
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
            } else {
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
        responses = [OpenApiResponse("200")]
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
        responses = [OpenApiResponse("200", [OpenApiContent(Activity::class)])]
    )
    fun getActivitiesByActivityId(ctx: Context) {
        val activity = activityDAO.findByActivityId((ctx.pathParam("activity-id").toInt()))
        if (activity != null) {
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
        responses = [OpenApiResponse("204")]
    )
    fun deleteActivityByActivityId(ctx: Context) {
        if (activityDAO.deleteByActivityId(ctx.pathParam("activity-id").toInt()) != 0) {
            ctx.status(204);
        } else {
            ctx.status(204);
        }
    }

    fun deleteActivityByUserId(ctx: Context) {
        activityDAO.deleteByUserId(ctx.pathParam("user-id").toInt())
    }

    @OpenApi(
        summary = "Update activity by ID",
        operationId = "updateActivityById",
        tags = ["Activity"],
        path = "/api/activities/{activity-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("activity-id", Int::class, "The activity ID")],
        responses = [OpenApiResponse("204")]
    )
    fun updateActivity(ctx: Context) {
        val activity: Activity = jsonToObject(ctx.body())
        if ((activityDAO.updateByActivityId(
                activityId = ctx.pathParam("activity-id").toInt(),
                activityDTO = activity
            )) != 0
        ) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

}