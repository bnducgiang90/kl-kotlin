package com.kl.kl_core.utils

import java.io.IOException
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
    }
}