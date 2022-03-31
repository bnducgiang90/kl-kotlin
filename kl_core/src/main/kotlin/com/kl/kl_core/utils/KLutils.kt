package com.kl.kl_core.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import java.io.IOException
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.util.*


class KLutils {
    companion object {
        private var prop: Properties? = null
        // read the properties file to get the credentials of databases
        fun getProperties(): Properties? {
            //return the existing properties if loaded earlier
            if (prop != null) {
                return prop
            }
            println("Loading the configuration File")
            val propertiesFileName = "application.properties"
            try {
                KLutils::class.java.classLoader.getResourceAsStream(propertiesFileName).use { stream ->
                    val properties = Properties()
                    properties.load(stream)
                    prop = properties
                    return properties
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
        }


        fun convertToJson(rs: ResultSet?): JsonNode? {
            if (rs == null) return null
            val mapper = ObjectMapper()
            return try {
                val rsmd = rs.metaData
                var first: ObjectNode? = null
                var array: ArrayNode? = null
                if (rs.next()) {
                    first = getObjectNode(rs, mapper, rsmd)
                }
                while (rs.next()) {
                    if (array == null) {
                        array = mapper.createArrayNode()
                        array.add(first)
                    }
                    array!!.add(getObjectNode(rs, mapper, rsmd))
                }
                array ?: first
            } catch (e: SQLException) {
                e.printStackTrace()
                null
            }
        }

        private fun getObjectNode(rs: ResultSet, mapper: ObjectMapper, rsmd: ResultSetMetaData): ObjectNode? {
            val obj: ObjectNode = mapper.createObjectNode()
            try {
                val columnCount = rsmd.columnCount
                for (i in 1..columnCount) {
                    val column = rsmd.getColumnName(i)
                    val value = rs.getObject(i)
                    if (value == null) {
                        obj.putNull(column)
                    } else if (value is String) {
                        obj.put(column, value)
                    } else if (value is Int) {
                        obj.put(column, value)
                    } else if (value is Long) {
                        obj.put(column, value)
                    } else if (value is Double) {
                        obj.put(column, value)
                    } else if (value is BigDecimal) {
                        obj.put(column, value)
                    } else if (value is BigInteger) {
                        obj.put(column, value)
                    } else {
                        throw IllegalArgumentException("Unmappable object type: " + value.javaClass)
                    }
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            return obj
        }
    }

}