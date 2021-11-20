package ru.otus.otuskotlin.blog.backend.repo.sql

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.DEFAULT_ISOLATION_LEVEL
import org.jetbrains.exposed.sql.transactions.transaction

class SqlConnector(
    private val url: String = "jdbc:postgresql://localhost:5432/blogdevdb",
    private val user: String = "postgres",
    private val password: String = "blog-pass",
    private val schema: String = "blog",
    private val driver: String = "org.postgresql.Driver",
    private val databaseConfig: DatabaseConfig = DatabaseConfig { defaultIsolationLevel = DEFAULT_ISOLATION_LEVEL }
) {
    private val globalConnection = Database.connect(url, driver, user, password, databaseConfig = databaseConfig)

    // Ensure creation of new connection with options to migrate/pre-drop database
    fun connect(vararg tables: Table): Database {
        // Create schema if such not exists
        transaction(globalConnection) {
            connection.prepareStatement("CREATE SCHEMA IF NOT EXISTS $schema", false).executeUpdate()
        }

        // Create connection for all supported db types
        val connect = Database.connect(
            url, driver, user, password,
            databaseConfig = databaseConfig,
            setupConnection = { connection ->
                {
                    connection.transactionIsolation = DEFAULT_ISOLATION_LEVEL
                    connection.schema = schema
                }
            }
        )

        // Update schema:
        //   - drop if needed (for ex, in tests);
        //   - exec migrations if needed;
        //   - otherwise unsure to create tables
        transaction(connect) {
            if (System.getenv("ok.blog.sql_drop_db")?.toBoolean() == true) {
                SchemaUtils.drop(*tables, inBatch = true)
                SchemaUtils.create(*tables, inBatch = true)
            } else if (System.getenv("ok.blog.sql_fast_migration").toBoolean()) {
                // TODO: Place to exec migration: create and ensure tables
            } else {
                SchemaUtils.createMissingTablesAndColumns(*tables, inBatch = true)
            }
        }
        return connect
    }
}
