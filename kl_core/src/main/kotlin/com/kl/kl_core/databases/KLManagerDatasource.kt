package com.kl.kl_core.databases

import com.kl.kl_core.utils.KLutils
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.pool.HikariPool
import org.springframework.beans.DirectFieldAccessor
import java.sql.Connection
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import javax.sql.DataSource


class KLManagerDatasource {
    companion object {
        private val DB_POOLMAP: ConcurrentHashMap<String, DataSource> = ConcurrentHashMap<String, DataSource>()

        @Synchronized
        @Throws(Exception::class)
        fun getConnection(dbName: String?): Connection? {
            if (!DB_POOLMAP.containsKey(dbName)) {
                createDataSource(dbName!!)
                println("----Created datasource-----")
            }
            val dataSource = DB_POOLMAP[dbName]
            println("Max size: ${(dataSource as HikariDataSource).maximumPoolSize}" +
                    "poolName: ${dataSource.poolName}" +
                    "Conn Active: ${(DirectFieldAccessor(dataSource).getPropertyValue("pool") as HikariPool?)?.activeConnections}"
            )
            return dataSource!!.connection
        }

        @Throws(Exception::class)
        private fun createDataSource(dbName: String) {
            println("Creting the dataspurce for $dbName")
            val hikariConfig = getHikariConfig(dbName)
            val hikariDataSource = HikariDataSource(hikariConfig)
            println("Adding the datasource to the global map")
            DB_POOLMAP.put(dbName, hikariDataSource)
        }

        @Throws(Exception::class)
        private fun getHikariConfig(dbName: String): HikariConfig? {
            val properties = KLutils.getProperties()
            if (properties == null || properties["$dbName.jdbcUrl"] == null) {
                throw Exception("Database not defined")
            }
            val hikaConfig = HikariConfig()
            // to reduce the code duplication, using Function to get the value
            /* val getValue: java.util.function.Function<String, String> = Function<String, String>
             { key -> properties["$dbName.$key"].toString() }*/

            val lambdaKeys: (String) -> String = { key -> (properties["$dbName.$key"].toString()) }
            //This is same as passing the Connection info to the DriverManager class.
            //your jdbc url. in my case it is mysql.
            if(properties.containsKey("$dbName.jdbcUrl"))
                hikaConfig.jdbcUrl = lambdaKeys("jdbcUrl")
            //username
            if(properties.containsKey("$dbName.username"))
                hikaConfig.username = lambdaKeys("username")
            //password
            if(properties.containsKey("$dbName.password"))
                hikaConfig.password = lambdaKeys("password")
            //driver class name
            if(properties.containsKey("$dbName.driverClassName"))
                hikaConfig.driverClassName = lambdaKeys("driverClassName")
            // Information about the pool
            //pool name. This is optional you don't have to do it.
            if(properties.containsKey("$dbName.poolName"))
                hikaConfig.poolName = lambdaKeys("poolName")
            //the maximum connection which can be created by or resides in the pool
            if(properties.containsKey("$dbName.maximumPoolSize"))
                hikaConfig.maximumPoolSize = lambdaKeys("maximumPoolSize").toInt() //getValue.apply("poolsize").toInt()
            //how much time a user can wait to get a connection from the pool.
            //if it exceeds the time limit then a SQlException is thrown
            if(properties.containsKey("$dbName.connectionTimeout"))
                hikaConfig.connectionTimeout = Duration.ofSeconds(lambdaKeys("connectionTimeout").toLong()).toMillis()
            //The maximum time a connection can sit idle in the pool.
            // If it exceeds the time limit it is removed form the pool.
            // If you don't want to retire the connections simply put 0.
            if(properties.containsKey("$dbName.idleTimeout"))
                hikaConfig.idleTimeout = Duration.ofSeconds(lambdaKeys("idleTimeout").toLong()).toMillis()


            //maxLifetime
            if(properties.containsKey("$dbName.maxLifetime"))
                hikaConfig.maxLifetime = Duration.ofSeconds(lambdaKeys("maxLifetime").toLong()).toMillis()

            //add properties:
            if(properties.containsKey("$dbName.cachePrepStmts"))
                hikaConfig.addDataSourceProperty("cachePrepStmts", lambdaKeys("cachePrepStmts"))
            if(properties.containsKey("$dbName.prepStmtCacheSize"))
                hikaConfig.addDataSourceProperty("prepStmtCacheSize", lambdaKeys("prepStmtCacheSize"))
            if(properties.containsKey("$dbName.prepStmtCacheSqlLimit"))
                hikaConfig.addDataSourceProperty("prepStmtCacheSqlLimit", lambdaKeys("prepStmtCacheSqlLimit"));

            return hikaConfig
        }
    }
}
