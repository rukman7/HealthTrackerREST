package ie.setu.domain.repository

import ie.setu.domain.WaterIntake
import ie.setu.utils.mapToWaterIntake
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class WaterIntakeDAO {

    //Save water intake data to the database
    fun save(waterIntake: WaterIntake){
        transaction {
            ie.setu.domain.db.WaterIntake.insert {
                it[user_id] = waterIntake.user_id
                it[description] = waterIntake.description
                it[cups] = waterIntake.cups
                it[target] = waterIntake.target
            }
        }
    }

    //Get all water intake information of all the users
    fun getAll(): ArrayList<WaterIntake> {
        val waterIntakeList: ArrayList<WaterIntake> = arrayListOf()
        transaction {
            ie.setu.domain.db.WaterIntake.selectAll().map {
                waterIntakeList.add(mapToWaterIntake(it)) }
        }
        return waterIntakeList
    }

    fun update(id: Int, waterIntake: WaterIntake): Int {
        return transaction {
            ie.setu.domain.db.WaterIntake.update({
                ie.setu.domain.db.WaterIntake.user_id eq id}) {
                it[description] = waterIntake.description
                it[cups] = waterIntake.cups
                it[target] = waterIntake.target
            }
        }
    }

    fun deleteByUserId(userId: Int): Int {
        return transaction {
            ie.setu.domain.db.WaterIntake.deleteWhere{ie.setu.domain.db.WaterIntake.user_id eq userId}
        }
    }
}