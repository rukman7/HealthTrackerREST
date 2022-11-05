package ie.setu.domain.repository

import ie.setu.domain.Activity
import ie.setu.domain.BMI
import ie.setu.domain.WaterIntake
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Bmi
import ie.setu.utils.mapToActivity
import ie.setu.utils.mapToBmi
import ie.setu.utils.mapToWaterIntake
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class BmiDAO {

    //Save BMI data to the database
    fun save(bmiData: BMI): Int?{
        return transaction {
            Bmi.insert {
                it[user_id] = bmiData.user_id
                it[description] = bmiData.description
                it[height] = bmiData.height
                it[weight] = bmiData.weight
                it[bmi] = bmiData.bmi
            } get Bmi.user_id
        }
    }

    //Get all BMI information of all the users
    fun getAll(): ArrayList<BMI> {
        val bmiList: ArrayList<BMI> = arrayListOf()
        transaction {
            Bmi.selectAll().map {
                bmiList.add(mapToBmi(it)) }
        }
        return bmiList
    }

    //get BMI information based on a user id
    fun getByUserId(userId: Int){
        return transaction {
            Bmi
                .select{ Bmi.user_id eq userId }
                .map{ mapToBmi(it) }
        }
    }

    fun update(id: Int, bmiData: BMI): Int {
        return transaction {
            Bmi.update({
                Bmi.user_id eq id}) {
                it[description] = bmiData.description
                it[height] = bmiData.height
                it[weight] = bmiData.weight
                it[bmi] = bmiData.bmi
            }
        }
    }

    fun deleteByUserId(userId: Int): Int {
        return transaction {
            Bmi.deleteWhere{Bmi.user_id eq userId}
        }
    }
}