package ie.setu.controllers


import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ie.setu.domain.BMIDTO
import io.javalin.http.Context
import ie.setu.domain.FoodDTO
import ie.setu.domain.repository.FoodDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.plugin.openapi.annotations.*

object FoodTrackerController {
    private val userDao = UserDAO()
    private val foodDAO = FoodDAO()
    //--------------------------------------------------------------
    // FoodDAO specifics
    //-------------------------------------------------------------

    @OpenApi(
        summary = "Get all Food information",
        operationId = "getAllFoods",
        tags = ["Food Info"],
        path = "/api/foods",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<FoodDTO>::class)])]
    )
    fun getAllFoods(ctx: Context) {
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val foods = foodDAO.getAll()
        if(foods.size!=0){
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(mapper.writeValueAsString(foods))
    }


    @OpenApi(
        summary = "Get food information by user ID",
        operationId = "getFoodsByUserId",
        tags = ["Food Info"],
        path = "/api/foods/{user-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(FoodDTO::class)])]
    )
    fun getFoodsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val foods = foodDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (foods.size > 0) {
                ctx.json(foods)
                ctx.status(200)
            }
            else{
                ctx.status(404)
            }
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Get food information by food ID",
        operationId = "getFoodsByFoodId",
        tags = ["Food Info"],
        path = "/api/foods/{food-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("food-id", Int::class, "The food ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(FoodDTO::class)])]
    )
    fun getFoodsByFoodId(ctx: Context) {
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val food = foodDAO.findByFoodId((ctx.pathParam("food-id").toInt()))
        if (food != null){
            ctx.json(mapper.writeValueAsString(food))
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Add add food Data",
        operationId = "addFood",
        tags = ["Food Info"],
        path = "/api/foods",
        method = HttpMethod.POST,
        responses  = [OpenApiResponse("200")]
    )
    fun addFood(ctx: Context) {
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        val foodDTO : FoodDTO = jsonToObject(ctx.body())
        val userId = userDao.findById(foodDTO.userId)
        if (userId != null) {
            val foodId = foodDAO.save(foodDTO)
            if (foodId != null) {
                foodDTO.id = foodId
                ctx.json(mapper.writeValueAsString(foodDTO))
                ctx.status(201)
            }
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Delete food data by ID",
        operationId = "deleteFoodByFoodId",
        tags = ["Food Info"],
        path = "/api/foods/{food-id}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("food-id", Int::class, "The food ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteFoodByFoodId(ctx: Context){
        if (foodDAO.deleteByFoodId(ctx.pathParam("food-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)

    }

    fun deleteFoodByUserId(ctx: Context){
        if (foodDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Update food data by user ID",
        operationId = "updateFood",
        tags = ["Food Info"],
        path = "/api/foods/{food-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("food-id", Int::class, "The food ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateFood(ctx: Context){
        val food : FoodDTO = jsonToObject(ctx.body())
        if (foodDAO.updateByFoodId(
                foodId = ctx.pathParam("food-id").toInt(),
                foodDTO=food) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

}