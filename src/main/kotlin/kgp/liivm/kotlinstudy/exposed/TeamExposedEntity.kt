package kgp.liivm.kotlinstudy.exposed

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object TeamExposedEntity : Table("team_entity") {
    val id = long("id").autoIncrement()
    val name = varchar("name", 255)
    val createdDatetime = datetime("create_datetime")

    override val primaryKey = PrimaryKey(id)
}
