package com.kl.kl_core.databases

import com.fasterxml.jackson.databind.JsonNode
import com.kl.kl_core.utils.KLutils
import oracle.jdbc.OracleTypes
import java.sql.ResultSet

class EkttDevDatabase : AbstractDatabase() {
    init {
        this.dbKey = "e_ktt_dev"
        this.isCreatePoll = (KLutils.getProperties()?.get("${this.dbKey}.createPool") as String).toBoolean()
    }
    // create query database
    fun printConn() {
        this.begin()
        println("${this::class.simpleName} isClosed: ${this.conn?.isClosed} ")
        println("${this::class.simpleName} driverName: ${this.conn?.metaData?.driverName} ")
        println("${this::class.simpleName} conn: ${this.conn} ")
        this.stmt = this.conn?.createStatement()
        val rs: ResultSet? = this.stmt?.executeQuery("select to_char(sysdate, 'dd-MM-yyyy HH24:mi:ss') as c1 from dual")
        if (rs != null) {
            while (rs.next()){
                val c1: String = rs.getString("c1")
                println("${this::class.simpleName} c1: $c1")
            }
            rs.close()
        }
        this.commit()
        this.release()
    }

    fun getThueSuat(): JsonNode? {
        var resultSet: ResultSet? = null
        var jsonNode: JsonNode? = null
        try {
            this.begin()
            this.callableStatement = this.conn?.prepareCall(
                "{call PKG_KT_DM_CHUNG.SP_SELECTITEM_KT_DM_THUESUAT(?)}"
            )
            this.callableStatement?.registerOutParameter("V_CURSOR", OracleTypes.CURSOR)
            this.callableStatement?.executeUpdate()
            resultSet = this.callableStatement?.getObject("V_CURSOR") as ResultSet?
            println("resultSet: $resultSet")

            jsonNode = KLutils.convertToJson(resultSet)
            println("jsonNode: $jsonNode")
        }
        catch (e: Exception){
            e.printStackTrace()
            this.rollback()
        }
        finally {
            if (!resultSet?.isClosed!!) resultSet?.close()
            this.release()
        }
        return  jsonNode
    }
}