package ie.setu.domain.repository

import ie.setu.domain.BMIDTO
import ie.setu.domain.db.Bmi
import ie.setu.utils.mapToBmi
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class BmiDAO {

    //Save BMIDTO data to the database
    fun save(BMIDTOData: BMIDTO): Int?{
        return transaction {
            Bmi.insert {
                it[userId] = BMIDTOData.userId
                it[description] = BMIDTOData.description
                it[height] = BMIDTOData.height
                it[weight] = BMIDTOData.weight
                it[bmi] = BMIDTOData.bmi
                it[timestamp] = BMIDTOData.timestamp
            } get Bmi.userId
        }
    }

    //Get all BMIDTO information of all the users
    fun getAll(): ArrayList<BMIDTO> {
        val BMIDTOList: ArrayList<BMIDTO> = arrayListOf()
        transaction {
            Bmi.selectAll().map {
                BMIDTOList.add(mapToBmi(it)) }
        }
        return BMIDTOList
    }

    //get BMIDTO information based on a user id
    fun getByUserId(userId: Int): BMIDTO?{
        return transaction {
            Bmi
                .select{ Bmi.userId eq userId }
                .map{ mapToBmi(it) }
                .firstOrNull()
        }
    }

    //get BMIDTO information based on a bmi id
    fun getByBmiId(bmiId: Int): BMIDTO?{
        return transaction {
            Bmi
                .select{ Bmi.id eq bmiId }
                .map{ mapToBmi(it) }
                .firstOrNull()
        }
    }

    fun update(id: Int, BMIDTOData: BMIDTO): Int {
        return transaction {
            Bmi.update({
                Bmi.id eq id}) {
                it[description] = BMIDTOData.description
                it[height] = BMIDTOData.height
                it[weight] = BMIDTOData.weight
                it[bmi] = BMIDTOData.bmi
                it[timestamp] = BMIDTOData.timestamp
                it[userId] = BMIDTOData.userId
            }
        }
    }

    fun deleteByUserId(userId: Int): Int {
        return transaction {
            Bmi.deleteWhere{Bmi.userId eq userId}
        }
    }

    fun deleteByBmiId(userId: Int): Int {
        return transaction {
            Bmi.deleteWhere{Bmi.id eq userId}
        }
    }
}