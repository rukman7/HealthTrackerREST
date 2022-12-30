package ie.setu.domain.repository

import ie.setu.domain.FoodDTO
import ie.setu.domain.db.Foods
import ie.setu.utils.mapToFoodDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class FoodDAO {

    //Get all the food items in the database regardless of user id
    fun getAll(): ArrayList<FoodDTO> {
        val foodList: ArrayList<FoodDTO> = arrayListOf()
        transaction {
            Foods.selectAll().map {
                foodList.add(mapToFoodDTO(it))
            }
        }
        return foodList
    }

    //Find a specific food item by food item id
    fun findByFoodId(id: Int): FoodDTO? {
        return transaction {
            Foods
                .select { Foods.foodId eq id }
                .map { mapToFoodDTO(it) }
                .firstOrNull()
        }
    }

    //Find all Foods for a specific user id
    fun findByUserId(userId: Int): List<FoodDTO> {
        return transaction {
            Foods
                .select { Foods.userId eq userId }
                .map { mapToFoodDTO(it) }
        }
    }

    //Save a food item to the database
    fun save(foodDTO: FoodDTO): Int {
        return transaction {
            Foods.insert {
                it[mealname] = foodDTO.mealname
                it[foodname] = foodDTO.foodname
                it[calories] = foodDTO.calories
                it[foodtime] = foodDTO.foodtime
                it[userId] = foodDTO.userId
            }
        } get Foods.foodId
    }

    //update a specific food item by food item id
    fun updateByFoodId(foodId: Int, foodDTO: FoodDTO): Int {
        return transaction {
            Foods.update({
                Foods.foodId eq foodId
            }) {
                it[mealname] = foodDTO.mealname
                it[foodname] = foodDTO.foodname
                it[foodtime] = foodDTO.foodtime
                it[calories] = foodDTO.calories
                it[userId] = foodDTO.userId
            }
        }
    }

    //delete a specific food item by food item id
    fun deleteByFoodId(foodId: Int): Int {
        return transaction {
            Foods.deleteWhere { Foods.foodId eq foodId }
        }
    }

    //delete a specific food item by userId
    fun deleteByUserId(userId: Int): Int {
        return transaction {
            Foods.deleteWhere { Foods.userId eq userId }
        }
    }

}