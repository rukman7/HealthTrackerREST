package ie.setu.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import ie.setu.domain.db.Activities
import ie.setu.domain.ActivityDTO
import ie.setu.domain.repository.ActivityDAO
import ie.setu.helpers.activities
import ie.setu.helpers.populateActivityTable
import ie.setu.helpers.populateUserTable
import org.junit.jupiter.api.Assertions.assertEquals

//retrieving some test data from Fixtures
private val activity1 = activities.get(0)
private val activity2 = activities.get(1)
private val activity3 = activities.get(2)

class ActivityDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadActivities {

        @Test
        fun `getting all activites from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three userDTOS and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                //Act & Assert
                assertEquals(3, activityDAO.getAll().size)
            }
        }

        @Test
        fun `get activity by user id that has no activities, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three userDTOS and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                //Act & Assert
                assertEquals(0, activityDAO.findByUserId(3).size)
            }
        }

        @Test
        fun `get activity by user id that exists, results in a correct activitie(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three userDTOS and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                //Act & Assert
                assertEquals(activity1, activityDAO.findByUserId(1).get(0))
                assertEquals(activity2, activityDAO.findByUserId(1).get(1))
                assertEquals(activity3, activityDAO.findByUserId(2).get(0))
            }
        }

        @Test
        fun `get all activities over empty table returns none`() {
            transaction {

                //Arrange - create and setup activityDAO object
                SchemaUtils.create(Activities)
                val activityDAO = ActivityDAO()

                //Act & Assert
                assertEquals(0, activityDAO.getAll().size)
            }
        }

        @Test
        fun `get activity by activity id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three userDTOS and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                //Act & Assert
                assertEquals(null, activityDAO.findByActivityId(4))
            }
        }

        @Test
        fun `get activity by activity id that exists, results in a correct activity returned`() {
            transaction {
                //Arrange - create and populate tables with three userDTOS and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                //Act & Assert
                assertEquals(activity1, activityDAO.findByActivityId(1))
                assertEquals(activity3, activityDAO.findByActivityId(3))
            }
        }
    }

    @Nested
    inner class CreateActivities {

        @Test
        fun `multiple activities added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with three userDTOS and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                //Act & Assert
                assertEquals(3, activityDAO.getAll().size)
                assertEquals(activity1, activityDAO.findByActivityId(activity1.id))
                assertEquals(activity2, activityDAO.findByActivityId(activity2.id))
                assertEquals(activity3, activityDAO.findByActivityId(activity3.id))
            }
        }
    }

    @Nested
    inner class UpdateActivities {

        @Test
        fun `updating existing activity in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three userDTOS and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                val activityDTO3Updated = ActivityDTO(id = 3, description = "Cardio", duration = 42.0,
                    calories = 220, started = DateTime.now(), userId = 2)
                activityDAO.updateByActivityId(activityDTO3Updated.id, activityDTO3Updated)
                assertEquals(activityDTO3Updated, activityDAO.findByActivityId(3))
            }
        }

        @Test
        fun `updating non-existant activity in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three userDTOS and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                val activityDTO4Updated = ActivityDTO(id = 4, description = "Cardio", duration = 42.0,
                    calories = 220, started = DateTime.now(), userId = 2)
                activityDAO.updateByActivityId(4, activityDTO4Updated)
                assertEquals(null, activityDAO.findByActivityId(4))
                assertEquals(3, activityDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteActivities {

        @Test
        fun `deleting a non-existant activity (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three userDTOS and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(3, activityDAO.getAll().size)
                activityDAO.deleteByActivityId(4)
                assertEquals(3, activityDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing activity (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three userDTOS and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(3, activityDAO.getAll().size)
                activityDAO.deleteByActivityId(activity3.id)
                assertEquals(2, activityDAO.getAll().size)
            }
        }


        @Test
        fun `deleting activities when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three userDTOS and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(3, activityDAO.getAll().size)
                activityDAO.deleteByUserId(3)
                assertEquals(3, activityDAO.getAll().size)
            }
        }

        @Test
        fun `deleting activities when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three userDTOS and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(3, activityDAO.getAll().size)
                activityDAO.deleteByUserId(1)
                assertEquals(1, activityDAO.getAll().size)
            }
        }
    }
}