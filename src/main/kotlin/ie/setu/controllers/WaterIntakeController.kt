package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.BMIDTO
import ie.setu.domain.WaterIntake
import ie.setu.domain.repository.WaterIntakeDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*

object WaterIntakeController {
    private val waterIntakeDAO = WaterIntakeDAO()

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
        summary = "Get water intake by UserDTO ID",
        operationId = "getWaterIntakeByUser",
        tags = ["WaterIntake"],
        path = "/api/waterintake/{user-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(BMIDTO::class)])]
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
}
