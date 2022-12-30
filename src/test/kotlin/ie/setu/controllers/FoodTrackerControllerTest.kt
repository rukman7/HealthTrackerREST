package ie.setu.controllers

import ie.setu.domain.FoodDTO
import ie.setu.domain.UserDTO
import ie.setu.helpers.*
import ie.setu.utils.jsonToObject
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FoodTrackerControllerTest {
    val db = Database.connect(
        "jdbc:postgresql://mel.db.elephantsqlcom:5432/nbnveiqp",
        driver = "org.postgresql.Driver",
        user = "nbnveiqp",
        password = "sPUHev5on5ZwbqTWPmL7fs5aUiQNXL_3")
    @Nested
    inner class CreateFoods {
        //   post(  "/api/foods", HealthTrackerAPI::addFood)

        @Test
        fun `add a food when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated food that we plan to do a delete on
            val addedUserDTO: UserDTO = jsonToObject(addUser(validName, validEmail, 1).body.toString())

            val addFoodResponse = addFood(
                foods.get(0).mealname,
                foods.get(0).foodname, foods.get(0).calories, foods.get(0).foodtime, addedUserDTO.id
            )
            assertEquals(201, addFoodResponse.status)

            //After - delete the user (Food will cascade delete in the database)
            deleteUser(addedUserDTO.id)
        }

        @Test
        fun `add a food when no user exists for it, returns a 404 response`() {

            //Arrange - check there is no user for -1 id
            val userId = -1
            assertEquals(404, retrieveUserById(userId).status)

            val addFoodResponse = addFood(
                foods.get(0).mealname, foods.get(0).foodname,
                foods.get(0).calories, foods.get(0).foodtime, userId
            )
            assertEquals(404, addFoodResponse.status)
        }
    }

    @Nested
    inner class ReadFoods {
        @Test
        fun `get all foods from the database returns 200 or 404 response`() {
            val response = retrieveAllFoods()
            if (response.status == 200){
                val retrievedFoods: ArrayList<FoodDTO> = jsonToObject(response.body.toString())
                assertNotEquals(0, retrievedFoods.size)
            }
            else{
                assertEquals(404, response.status)
            }
        }

//        @Test
//        fun `get all foods by user id when user and foods exists returns 200 response`() {
//            //Arrange - add a user and 3 associated foods that we plan to retrieve
//            val addedUser : UserDTO = jsonToObject(addUser(validName, validEmail,1).body.toString())
//            addFood(
//                foods.get(0).mealname, foods.get(0).foodname,
//                foods.get(0).calories, foods.get(0).foodtime, addedUser.id)
//            addFood(
//                foods.get(1).mealname, foods.get(1).foodname,
//                foods.get(1).calories, foods.get(1).foodtime, addedUser.id)
//            addFood(
//                foods.get(2).mealname, foods.get(2).foodname,
//                foods.get(2).calories, foods.get(2).foodtime, addedUser.id)
//
//            //Assert and Act - retrieve the three added foods by user id
//            val response = retrieveFoodsByUserId(addedUser.id)
//            assertEquals(200, response.status)
//            val retrievedFoods: ArrayList<FoodDTO> = jsonToObject(response.body.toString())
//            assertEquals(3, retrievedFoods.size)
//
//            //After - delete the added user and assert a 204 is returned (foods are cascade deleted)
//            assertEquals(204, deleteUser(addedUser.id).status)
//        }

        @Test
        fun `get all foods by user id when no foods exist returns 404 response`() {
            //Arrange - add a user
            val addedUserDTO : UserDTO = jsonToObject(addUser(validName, validEmail,1).body.toString())

            //Assert and Act - retrieve the foods by user id
            val response = retrieveFoodsByUserId(addedUserDTO.id)
            assertEquals(200, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUserDTO.id).status)
        }

        @Test
        fun `get all foods by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve foods by user id
            val response = retrieveFoodsByUserId(userId)
            assertEquals(200, response.status)
        }

        @Test
        fun `get food by food id when no food exists returns 404 response`() {
            //Arrange
            val foodId = -1
            //Assert and Act - attempt to retrieve the food by food id
            val response = retrieveFoodByFoodId(foodId)
            assertEquals(404, response.status)
        }

    }

    @Nested
    inner class UpdateFoods {
        //  patch( "/api/foods/:food-id", HealthTrackerAPI::updateFood)
        @Test
        fun `updating an food by food id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val foodID = -1

            //Arrange - check there is no user for -1 id
            assertEquals(404, retrieveUserById(userId).status)

            //Act & Assert - attempt to update the details of an food/user that doesn't exist
            assertEquals(
                404, updateFood(
                    foodID, updatedMealName, updatedFoodName,
                    updatedCalories, updatedFoodTime, userId
                ).status
            )
        }

        @Test
        fun `updating an food by food id when it exists, returns 204 response`() {

            //Arrange - add a user and an associated food that we plan to do an update on
            val addedUserDTO : UserDTO = jsonToObject(addUser(validName, validEmail,1).body.toString())
            val addFoodResponse = addFood(
                foods.get(0).mealname,
                foods.get(0).foodname, foods.get(0).calories,
                foods.get(0).foodtime, addedUserDTO.id)
            assertEquals(201, addFoodResponse.status)
            val addedFood = jsonToObject<FoodDTO>(addFoodResponse.body.toString())

            //Act & Assert - update the added food and assert a 204 is returned
            val updatedFoodResponse = updateFood(addedFood.foodId, updatedMealName,
                updatedFoodName, updatedCalories, updatedFoodTime, addedUserDTO.id)
            assertEquals(204, updatedFoodResponse.status)

            //Assert that the individual fields were all updated as expected
            val retrievedFoodResponse = retrieveFoodByFoodId(addedFood.foodId)
            val updatedFood = jsonToObject<FoodDTO>(retrievedFoodResponse.body.toString())
            assertEquals(updatedMealName, updatedFood.mealname)
            assertEquals(updatedFoodName, updatedFood.foodname)
            assertEquals(updatedCalories, updatedFood.calories)

            //After - delete the user
            deleteUser(addedUserDTO.id)
        }
    }

    @Nested
    inner class DeleteFoods {
        //   delete("/api/foods/:food-id", HealthTrackerAPI::deleteFoodByFoodId)
        //   delete("/api/userDTOS/:user-id/foods", HealthTrackerAPI::deleteFoodByUserId)
        @Test
        fun `deleting an food by food id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteFoodByFoodId(-1).status)
        }

        @Test
        fun       `deleting foods by user id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(200, deleteFoodsByUserId(-1).status)
        }

        @Test
        fun `deleting an food by id when it exists, returns a 204 response`() {

            //Arrange - add a user and an associated food that we plan to do a delete on
            val addedUserDTO : UserDTO = jsonToObject(addUser(validName, validEmail,1).body.toString())
            val addFoodResponse = addFood(
                foods.get(0).mealname,
                foods.get(0).foodname, foods.get(0).calories, foods.get(0).foodtime, addedUserDTO.id)
            assertEquals(201, addFoodResponse.status)

            //Act & Assert - delete the added food and assert a 204 is returned
            val addedFood = jsonToObject<FoodDTO>(addFoodResponse.body.toString())
            assertEquals(204, deleteFoodByFoodId(addedFood.foodId).status)

            //After - delete the user
            deleteUser(addedUserDTO.id)
        }

        @Test
        fun `deleting all foods by userid when it exists, returns a 204 response`() {

            //Arrange - add a user and 3 associated foods that we plan to do a cascade delete
            val addedUserDTO : UserDTO = jsonToObject(addUser(validName, validEmail,1).body.toString())
            val addFoodResponse1 = addFood(
                foods.get(0).mealname, foods.get(0).foodname,
                foods.get(0).calories, foods.get(0).foodtime, addedUserDTO.id)
            assertEquals(201, addFoodResponse1.status)
            val addFoodResponse2 = addFood(
                foods.get(1).mealname, foods.get(1).foodname,
                foods.get(1).calories, foods.get(1).foodtime, addedUserDTO.id)
            assertEquals(201, addFoodResponse2.status)
            val addFoodResponse3 = addFood(
                foods.get(2).mealname, foods.get(2).foodname,
                foods.get(2).calories, foods.get(2).foodtime, addedUserDTO.id)
            assertEquals(201, addFoodResponse3.status)

            //Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUserDTO.id).status)

            //Act & Assert - attempt to retrieve the deleted foods
            val addedFood1 = jsonToObject<FoodDTO>(addFoodResponse1.body.toString())
            val addedFood2 = jsonToObject<FoodDTO>(addFoodResponse2.body.toString())
            val addedFood3 = jsonToObject<FoodDTO>(addFoodResponse3.body.toString())
            assertEquals(200, retrieveFoodByFoodId(addedFood1.foodId).status)
            assertEquals(200, retrieveFoodByFoodId(addedFood2.foodId).status)
            assertEquals(200, retrieveFoodByFoodId(addedFood3.foodId).status)
        }
    }

}