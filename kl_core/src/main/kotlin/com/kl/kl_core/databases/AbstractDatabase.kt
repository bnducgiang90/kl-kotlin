package com.kl.kl_core.databases

import com.kl.kl_core.utils.KLutils
import java.sql.CallableStatement
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.Statement
import java.util.Properties

abstract class AbstractDatabase {
    protected lateinit var dbKey: String
    protected var isCreatePoll: Boolean = true
    protected var conn: Connection? = null
    protected var preparedStmt: PreparedStatement? = null
    protected var stmt: Statement? = null
    protected var callableStatement: CallableStatement? = null

    private fun beginSession() {
        if (this.conn == null || this.conn!!.isClosed) {
            val prop: Properties? = KLutils.getProperties()
            if (this.isCreatePoll) {
                this.conn = KLManagerDatasource.getConnection(this.dbKey)!!
            } else {
                println("Create connection without Pool!")
                Class.forName(prop?.get("$dbKey.driverClassName") as String)
                this.conn = DriverManager.getConnection(
                    prop?.get("$dbKey.jdbcUrl") as String,
                    prop?.get("$dbKey.username") as String,
                    prop?.get("$dbKey.password") as String
                )
            }
        }
    }

    protected fun begin() {
        if (this.conn == null) {
            this.beginSession()
            this.conn?.autoCommit = false
        }
    }

    protected fun commit() {
        if (this.stmt != null) this.stmt!!.close()
        if (this.preparedStmt != null) this.preparedStmt!!.close()
        if (this.callableStatement != null) this.callableStatement!!.close()
        if (this.conn != null) {
            this.conn?.commit()
            this.conn = null
        }
    }

    protected fun rollback() {
        if (this.stmt != null) this.stmt!!.close()
        if (this.preparedStmt != null) this.preparedStmt!!.close()
        if (this.callableStatement != null) this.callableStatement!!.close()
        if (this.conn != null) {
            this.conn?.rollback()
            this.conn = null
        }
    }

    protected fun release() {
        if (this.stmt != null) this.stmt!!.close()
        if (this.preparedStmt != null) this.preparedStmt!!.close()
        if (this.callableStatement != null) this.callableStatement!!.close()
        if (this.conn != null) {
            this.conn!!.close()
            this.conn = null
        }
    }

}