package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.BMIDTO
import ie.setu.domain.repository.BmiDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*

object BMIController {

    private val bmiDAO = BmiDAO()

    //--------------------------------------------------------------
    // BmiDAO specifics
    //--------------------------------------------------------------

    @OpenApi(
        summary = "Add BMIDTO Data",
        operationId = "addBmiData",
        tags = ["BMIDTO"],
        path = "/api/bmi",
        method = HttpMethod.POST,
        responses  = [OpenApiResponse("200")]
    )
    fun addBmiData(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val BMIDTOData = mapper.readValue<BMIDTO>(ctx.body())
        val bmiVal = calculateBmi(BMIDTOData.height, BMIDTOData.weight)
        BMIDTOData.bmi = bmiVal
        val userId = bmiDAO.save(BMIDTOData)
        if (userId != null){
            ctx.json(BMIDTOData)
            ctx.status(201)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Get all BMIDTO information",
        operationId = "getAllBmiInfo",
        tags = ["BMIDTO"],
        path = "/api/bmi",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<BMIDTO>::class)])]
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
        summary = "Get BMIDTO info by UserDTO ID",
        operationId = "getBmiInfoByUser",
        tags = ["BMIDTO"],
        path = "/api/bmi/{user-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(BMIDTO::class)])]
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
        summary = "Update BMIDTO data by user ID",
        operationId = "updateBmiData",
        tags = ["BMIDTO"],
        path = "/api/bmi/{user-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateBmiData(ctx: Context){
        val bmidata : BMIDTO = jsonToObject(ctx.body())
        bmidata.bmi = calculateBmi(bmidata.height, bmidata.weight)
        if ((bmiDAO.update(id = ctx.pathParam("user-id").toInt(), BMIDTOData=bmidata)) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


        @OpenApi(
        summary = "Delete BMIDTO data by ID",
        operationId = "deleteBmiDataByUserId",
        tags = ["BMIDTO"],
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
