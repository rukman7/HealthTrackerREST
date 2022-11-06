package ie.setu.repository

import ie.setu.domain.WaterIntake
import ie.setu.domain.repository.WaterIntakeDAO
import ie.setu.helpers.PopulateWaterIntake
import ie.setu.helpers.waterintakelist
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val waterintake1 = waterintakelist.get(0)
val waterintake2 = waterintakelist.get(1)
val waterintake3 = waterintakelist.get(2)

class WaterIntakeDAOTest {

    companion object {

        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class Readwaterintake{

        @Test
        fun `getting all waterintakes from a populated table returns all rows`(){
            transaction {
                val waterintdao = PopulateWaterIntake()

                assertEquals(3,waterintdao.getAll().size)
            }
        }

        @Test
        fun `get waterintake by id that doesn't exist, results in no waterintake returned`(){
            transaction {
                val waterintakedao = PopulateWaterIntake()

                assertEquals(null,waterintakedao.getByUserId(5))
            }
        }

        @Test
        fun `get waterintake by id that exists, results in a correct waterintake returned`(){
            transaction {
                SchemaUtils.create(ie.setu.domain.db.WaterIntake)
                val waterintakeDAO = WaterIntakeDAO()

                waterintakeDAO.save(waterintake1)
                waterintakeDAO.save(waterintake2)
                waterintakeDAO.save(waterintake3)

                assertEquals(null,waterintakeDAO.getByUserId(5))

            }
        }

        @Test
        fun `get all waterintakes over empty table returns none`(){
            transaction {
                SchemaUtils.create(ie.setu.domain.db.WaterIntake)
                val waterintakeDAO = WaterIntakeDAO()

                assertEquals(0,waterintakeDAO.getAll().size)


            }
        }

    }


    @Nested
    inner class CreateWaterintake{
        @Test
        fun `multiple waterintakes added to table can be retrieved successfully`(){
            transaction {
                val waterintakedao = PopulateWaterIntake()

                assertEquals(3,waterintakedao.getAll().size)
                assertEquals(waterintake1,waterintakedao.getByUserId(1))
                assertEquals(waterintake2,waterintakedao.getByUserId(2))
                assertEquals(waterintake3,waterintakedao.getByUserId(3))
            }
        }

    }

    @Nested
    inner class UpdateWaterintake{

        @Test
        fun `updating existing waterintake in table results in successful update`(){
            transaction {
                val waterintakedao = PopulateWaterIntake()

                val waterintake2updated = WaterIntake(2,"WaterIntake 2 Updated",5,7)
                waterintakedao.update(waterintake2.user_id,waterintake2updated)
                assertEquals(waterintake2updated, waterintakedao.getByUserId(2))

            }
        }

        @Test
        fun `updating non-existent waterintake in table results in no updates`(){
            transaction {
                val waterintakedao = PopulateWaterIntake()

                val waterintake4updated = WaterIntake(4,"This is 4",3,5)
                waterintakedao.update(4,waterintake4updated)
                assertEquals(null,waterintakedao.getByUserId(4))
                assertEquals(3,waterintakedao.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteWaterintake{

        @Test
        fun `deleting a non-existent waterintake in table results in no deletion`(){
            transaction {
                val waterintakedao = PopulateWaterIntake()

                assertEquals(3,waterintakedao.getAll().size)
                waterintakedao.deleteByUserId(4)
                assertEquals(3,waterintakedao.getAll().size)
            }
        }

        @Test
        fun `deleting an existing user in table results in record being deleted`(){
            transaction {
                val waterintakedao = PopulateWaterIntake()

                assertEquals(3,waterintakedao.getAll().size)
                waterintakedao.deleteByUserId(3)
                assertEquals(2,waterintakedao.getAll().size)
            }
        }
    }

}