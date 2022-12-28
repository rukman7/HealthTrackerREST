package ie.setu.config

import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.name

class DbConfig{

    private val logger = KotlinLogging.logger {}

    fun getDbConnection() :Database{

        logger.info { "Starting DB connection..." }
        val dbConfig = Database.connect(
            "jdbc:postgresql://mel.db.elephantsql.com:5432/kfhlqhhv",
            driver = "org.postgresql.Driver",
                user = "kfhlqhhv",
            password = "SLR4ONJFsG-m1mj-NFkm0wUG6vnoJN3j")

        logger.info{"DbConfig name = " + dbConfig.name}
        logger.info{"DbConfig url = " + dbConfig.url}

        return dbConfig
    }
}