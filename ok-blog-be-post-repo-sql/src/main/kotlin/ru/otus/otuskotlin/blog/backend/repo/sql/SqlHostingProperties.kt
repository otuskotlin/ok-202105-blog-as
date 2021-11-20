package ru.otus.otuskotlin.blog.backend.repo.sql

import java.io.File
import java.io.FileInputStream
import java.util.Properties

object SqlHostingProperties {
    const val SQL_URL = "jdbcUrl"
    const val SQL_DROP_DB = "ok.blog.sql_drop_db" // withDefaultValue(false)
    const val SQL_FAST_MIGRATION = "ok.blog.sql_fast_migration" // withDefaultValue(true)
}

fun loadFromFile(propFileName: String): Properties {
    val propFile = File(propFileName)
    val fileStream = if (propFile.isFile) {
        FileInputStream(propFile)
    } else {
        SqlHostingProperties::class.java.classLoader.getResourceAsStream(propFileName)
    }
    return if (fileStream != null) {
        Properties().apply { load(fileStream) }
    } else {
        throw IllegalArgumentException("Cannot find property file: $propFileName")
    }
}

fun Properties.property(key: String): String? = getProperty(key, null)

fun Properties.withoutCustom() = Properties().also { props ->
    filterNot { it.key.toString().startsWith("ok.blog.") }.forEach {
        props[it.key] = it.value
    }
}
