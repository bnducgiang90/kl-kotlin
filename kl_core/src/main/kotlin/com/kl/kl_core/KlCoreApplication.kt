package com.kl.kl_core

import com.kl.kl_core.databases.EkttDevDatabase
import com.kl.kl_core.databases.KLManagerDatasource
import com.kl.kl_core.databases.TvanDBTestDatabase
import com.kl.kl_core.utils.KLutils
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

@SpringBootApplication
class KlCoreApplication

fun main(args: Array<String>) {
    runApplication<KlCoreApplication>(*args)
    KLutils.getProperties()

    println("--------------EkttDevDatabase------------------")
    val ekttDev = EkttDevDatabase()
    ekttDev.printConn()

    println("--------------TvanDBTestDatabase------------------")
    val tvanTestDB = TvanDBTestDatabase()
    tvanTestDB.printConn()
}
