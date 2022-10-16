package ie.setu.config

import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.name

class DbConfig{

    private val logger = KotlinLogging.logger {}

    fun getDbConnection() :Database{

        logger.info { "Starting DB connection..." }
        val dbConfig = Database.connect(
            "jdbc:postgresql://ec2-52-70-45-163.compute-1.amazonaws.com:5432/d4i6ds0rediadt?sslmode=require",
            driver = "org.postgresql.Driver",
            user = "rfdbbzpuuzrlpv",
            password = "0994ed6c7d464d74a48e8bcaf51bde015312df784d0430614e76f8f575efbfc7")

        logger.info{"DbConfig name = " + dbConfig.name}
        logger.info{"DbConfig url = " + dbConfig.url}

        return dbConfig
    }
}