package com.kl.kl_core

import com.kl.kl_core.databases.KLManagerDatasource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

@SpringBootApplication
class KlCoreApplication

fun main(args: Array<String>) {
    runApplication<KlCoreApplication>(*args)

    val conn: Connection? = KLManagerDatasource.getConnection("e_ktt_dev")
    println("isClosed: ${conn?.isClosed} ")
    println("driverName: ${conn?.metaData?.driverName} ")
    println("conn1: ${conn} ")
    val stmt: Statement? = conn?.createStatement()
    val rs: ResultSet? = stmt?.executeQuery("select 1 as c1 from dual")
    if (rs != null) {
        while (rs.next()){
            val c1: Int = rs.getInt("c1")
            println("c1: ${c1}")
        }
        rs.close()
        stmt.close()
    }
    conn?.close()

    val conn2: Connection? = KLManagerDatasource.getConnection("e_ktt_dev")
    println("--------------------------------")
    println("isClosed: ${conn2?.isClosed} ")
    println("driverName: ${conn2?.metaData?.driverName} ")
    println("conn2: ${conn2} ")
    conn2?.close()


    val conn3: Connection? = KLManagerDatasource.getConnection("e_ktt_dev")
    println("--------------------------------")
    println("isClosed: ${conn3?.isClosed} ")
    println("driverName: ${conn3?.metaData?.driverName} ")
    println("conn3: ${conn3} ")
    conn3?.close()

    val conn4: Connection? = KLManagerDatasource.getConnection("tvandb_test")
    println("--------------------------------")
    println("isClosed: ${conn4?.isClosed} ")
    println("driverName: ${conn4?.metaData?.driverName} ")
    println("conn4: ${conn4} ")
    conn4?.close()

    val conn5: Connection? = KLManagerDatasource.getConnection("tvandb_test")
    println("--------------------------------")
    println("isClosed: ${conn5?.isClosed} ")
    println("driverName: ${conn5?.metaData?.driverName} ")
    println("conn5: ${conn5} ")
    conn5?.close()

    val conn6: Connection? = KLManagerDatasource.getConnection("tvandb_test")
    println("--------------------------------")
    println("isClosed: ${conn6?.isClosed} ")
    println("driverName: ${conn6?.metaData?.driverName} ")
    println("conn6: ${conn6} ")
    conn6?.close()
}
