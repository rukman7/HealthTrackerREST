package ie.setu.domain.repository

import ie.setu.domain.ActivityDTO
import ie.setu.domain.db.Activities
import ie.setu.utils.mapToActivity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class ActivityDAO {

    //Get all the activities in the database regardless of user id
    fun getAll(): ArrayList<ActivityDTO> {
        val activitiesList: ArrayList<ActivityDTO> = arrayListOf()
        transaction {
            Activities.selectAll().map {
                activitiesList.add(mapToActivity(it)) }
        }
        return activitiesList
    }

    //Find a specific activity by activity id
    fun findByActivityId(id: Int): ActivityDTO?{
        return transaction {
            Activities
                .select() { Activities.id eq id}
                .map{mapToActivity(it)}
                .firstOrNull()
        }
    }

    //Find all activities for a specific user id
    fun findByUserId(userId: Int): List<ActivityDTO>{
        return transaction {
            Activities
                .select {Activities.userId eq userId}
                .map {mapToActivity(it)}
        }
    }

    //Save an activityDTO to the database
    fun save(activityDTO: ActivityDTO): Int?{
        return transaction {
            Activities.insert {
                it[description] = activityDTO.description
                it[duration] = activityDTO.duration
                it[started] = activityDTO.started
                it[calories] = activityDTO.calories
                it[userId] = activityDTO.userId
            } get Activities.id
        }
    }

    fun updateByActivityId(activityId: Int, activityDTO: ActivityDTO): Int {
        return transaction {
            Activities.update ({
                Activities.id eq activityId}) {
                it[description] = activityDTO.description
                it[duration] = activityDTO.duration
                it[started] = activityDTO.started
                it[calories] = activityDTO.calories
                it[userId] = activityDTO.userId
            }
        }
    }
    fun deleteByActivityId (activityId: Int): Int{
        return transaction{
            Activities.deleteWhere { Activities.id eq activityId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            Activities.deleteWhere { Activities.userId eq userId }
        }
    }
}