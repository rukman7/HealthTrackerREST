package ie.setu.config

import ie.setu.controllers.*
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.ui.ReDocOptions
import io.swagger.v3.oas.models.info.Info

class JavalinConfig {

    fun startJavalinService(): Javalin {

        val app = Javalin.create {
            it.registerPlugin(getConfiguredOpenApiPlugin())
            it.defaultContentType = "application/json"
        }.apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 - Not Found") }
        }.start(getHerokuAssignedPort())

        registerRoutes(app)
        return app
    }

    private fun registerRoutes(app: Javalin) {
        app.routes {
            path("/api/users") {
                get(UserController::getAllUsers)
                post(UserController::addUser)
                path("{user-id}"){
                    get(UserController::getUserByUserId)
                    delete(UserController::deleteUser)
                    patch(UserController::updateUser)
                    path("activities"){
                        get(ActivityController::getActivitiesByUserId)
                    }
                    path("foods"){
                        get(FoodTrackerController::getAllFoods)
                        delete(FoodTrackerController::deleteFoodByFoodId)
                    }

                }
                path("/email/{email}"){
                    get(UserController::getUserByEmail)
                }
            }
            path("/api/activities") {
                get(ActivityController::getAllActivities)
                post(ActivityController::addActivity)
                path("{activity-id}") {
                    get(ActivityController::getActivitiesByActivityId)
                    delete(ActivityController::deleteActivityByActivityId)
                    patch(ActivityController::updateActivity)
                }
            }

            path("/api/waterintake") {
                get(WaterIntakeController::getAllWaterIntake)
                post(WaterIntakeController::addWaterIntake)
                path("{user-id}"){
                    get(WaterIntakeController::getWaterIntakeByUser)
                    patch(WaterIntakeController::updateWaterIntake)
                    delete(WaterIntakeController::deleteWaterIntakeByUserId)
                }
            }
            path("/api/bmi"){
                get(BMIController::getAllBmiInfo)
                post(BMIController::addBmiData)
                path("{user-id}"){
                    get(BMIController::getBmiInfoByUser)
                    patch(BMIController::updateBmiData)
                    delete(BMIController::deleteBmiDataByUserId)
                }
            }

            path("/api/foods"){
                get(FoodTrackerController::getAllFoods)
                post(FoodTrackerController::addFood)
                path("{food-id}"){
                    get(FoodTrackerController::getFoodsByFoodId)
                    delete(FoodTrackerController::deleteFoodByFoodId)
                    patch(FoodTrackerController::updateFood)
                }
            }
        }
    }

    private fun getHerokuAssignedPort(): Int {
        val herokuPort = System.getenv("PORT")
        return if (herokuPort != null) {
            Integer.parseInt(herokuPort)
        } else 8000
    }

    fun getConfiguredOpenApiPlugin() = OpenApiPlugin(
        OpenApiOptions(
            Info().apply {
                title("Health Tracker App")
                version("1.0")
                description("Health Tracker API")
            }
        ).apply {
            path("/swagger-docs") // endpoint for OpenAPI json
            swagger(SwaggerOptions("/swagger-ui")) // endpoint for swagger-ui
            reDoc(ReDocOptions("/redoc")) // endpoint for redoc
        }
    )
}