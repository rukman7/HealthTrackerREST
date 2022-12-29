package ie.setu.repository

import ie.setu.domain.BMIDTO
import ie.setu.domain.db.Bmi
import ie.setu.domain.repository.BmiDAO
import ie.setu.helpers.PopulateBMITable
import ie.setu.helpers.BMIDTOS
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val bmi1 = BMIDTOS.get(0)
val bmi2 = BMIDTOS.get(1)
val bmi3 = BMIDTOS.get(2)

val emptyBMIDTOList = arrayListOf<BMIDTO>()

class BMIDAOTest {

    companion object {

        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadBMIDTO{

        @Test
        fun `getting all bmis from a populated table returns all rows`(){
            transaction {
                val bmiDAO = PopulateBMITable()

                assertEquals(3,bmiDAO.getAll().size)
            }
        }

        @Test
        fun `get bmi by id that doesn't exist, results in no user returned`(){
            transaction {
                val bmiDAO = PopulateBMITable()

                assertEquals(null,bmiDAO.getByUserId(5))
            }
        }

        @Test
        fun `get bmi user by id that exists, results in a correct user returned`() {
            transaction {
                //Arrange - create and populate table with three BMIDTOS data
                SchemaUtils.create(Bmi)
                val bmiDAO = BmiDAO()
                bmiDAO.save(bmi1)
                bmiDAO.save(bmi2)
                bmiDAO.save(bmi3)

                //Act & Assert
                bmiDAO.getByUserId(2)?.let { assertEquals(2, it.userId) }
            }
        }

        @Test
        fun `get all users over empty table returns none`(){
            transaction {
                SchemaUtils.create(Bmi)
                val bmiDAO = BmiDAO()

                assertEquals(0,bmiDAO.getAll().size)
            }
        }

    }

    @Nested
    inner class CreateBmi{

        @Test
        fun `multiple Bmis added to table can be retrieved successfully`(){
            transaction {
                val bmiDao = PopulateBMITable()

                assertEquals(3,bmiDao.getAll().size)
                assertEquals(bmi1,bmiDao.getByUserId(bmi1.userId))
                assertEquals(bmi2,bmiDao.getByUserId(bmi2.userId))
                assertEquals(bmi3,bmiDao.getByUserId(bmi3.userId))
            }
        }
    }

    @Nested
    inner class UpdateBmi{

        @Test
        fun `updating existing BMI in table results in successful update`(){
            transaction {
                val bmiDAO = PopulateBMITable()

                val BMIDTO3Updated = BMIDTO(3,"This is updated BMIDTO 3",6.6,76.0,29)
                bmiDAO.update(3,BMIDTO3Updated)
                assertEquals(BMIDTO3Updated,bmiDAO.getByUserId(3))
            }
        }

        @Test
        fun `updating non-existent BMI in table results in no updates`(){
            transaction {
                val bmiDAO = PopulateBMITable()

                val BMIDTO4Updated = BMIDTO(4,"BMIDTO 4",65.0,78.0,25)
                assertEquals(null,bmiDAO.getByUserId(4))
                assertEquals(3,bmiDAO.getAll().size)
            }
        }

    }

    @Nested
    inner class DeleteBMIDTO{

        @Test
        fun `deleting a non-existent user in table results in no deletion`(){
            transaction {
                val bmiDAO = PopulateBMITable()

                assertEquals(3,bmiDAO.getAll().size)
                bmiDAO.deleteByUserId(4)
                assertEquals(3,bmiDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing Bmi in table results in record being deleted`(){
            transaction {
                val bmidao = PopulateBMITable()

                assertEquals(3,bmidao.getAll().size)
                bmidao.deleteByUserId(3)
                assertEquals(2,bmidao.getAll().size)

            }
        }

    }

}