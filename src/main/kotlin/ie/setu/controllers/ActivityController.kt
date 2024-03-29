package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.ActivityDTO
import ie.setu.domain.UserDTO
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*

private const val USER_ID = "user-id"

private const val ACTIVITY_ID = "activity-id"

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
        responses = [OpenApiResponse("200", [OpenApiContent(Array<UserDTO>::class)])]
    )
    fun getAllActivities(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val activities = activityDAO.getAll()
        if (activities.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(mapper.writeValueAsString(activities))
    }

    @OpenApi(
        summary = "Get activity by user ID",
        operationId = "getActivityByUserId",
        tags = ["Activity"],
        path = "/api/activities/{user-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam(USER_ID, Int::class, "The user ID")],
        responses = [OpenApiResponse("200", [OpenApiContent(UserDTO::class)])]
    )
    fun getActivitiesByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam(USER_ID).toInt()) != null) {
            val activities = activityDAO.findByUserId(ctx.pathParam(USER_ID).toInt())
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
        val activityDTO = mapper.readValue<ActivityDTO>(ctx.body())
        val activityId = activityDAO.save(activityDTO)
        if (activityId != null) {
            ctx.json(mapper.writeValueAsString(activityDTO))
            ctx.status(201)
        } else {
            ctx.status(400)
        }

    }

    @OpenApi(
        summary = "Get activity by activity ID",
        operationId = "getActivityByActivityId",
        tags = ["Activity"],
        path = "/api/activities/{activity-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam(ACTIVITY_ID, Int::class, "The activity ID")],
        responses = [OpenApiResponse("200", [OpenApiContent(ActivityDTO::class)])]
    )
    fun getActivitiesByActivityId(ctx: Context) {
        val activity = activityDAO.findByActivityId((ctx.pathParam(ACTIVITY_ID).toInt()))
        if (activity != null) {
            val mapper = jacksonObjectMapper()
                .registerModule(JodaModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            ctx.json(mapper.writeValueAsString(activity))
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Delete Activity by Activity ID",
        operationId = "deleteActivityByActivityId",
        tags = ["Activity"],
        path = "/api/activities/{activity-id}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam(ACTIVITY_ID, Int::class, "The activity ID")],
        responses = [OpenApiResponse("204")]
    )
    fun deleteActivityByActivityId(ctx: Context) {
        if (activityDAO.deleteByActivityId(ctx.pathParam(ACTIVITY_ID).toInt()) != 0) {
            ctx.status(204)
        }
        ctx.status(204)
    }

    fun deleteActivityByUserId(ctx: Context) {
        activityDAO.deleteByUserId(ctx.pathParam(USER_ID).toInt())
    }

    @OpenApi(
        summary = "Update activity by ID",
        operationId = "updateActivityById",
        tags = ["Activity"],
        path = "/api/activities/{activity-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam(ACTIVITY_ID, Int::class, "The activity ID")],
        responses = [OpenApiResponse("204")]
    )
    fun updateActivity(ctx: Context) {
        val activityDTO: ActivityDTO = jsonToObject(ctx.body())
        if ((activityDAO.updateByActivityId(
                activityId = ctx.pathParam(ACTIVITY_ID).toInt(),
                activityDTO = activityDTO
            )) != 0
        ) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

}