package kr.co.korbit.gia.jpa.korbit.repository.impl

import kr.co.korbit.gia.jpa.common.Querydsl4RepositorySupport
import kr.co.korbit.gia.jpa.korbit.model.LneQuest
import kr.co.korbit.gia.jpa.korbit.model.LneQuiz
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomLneQuizRepository
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.SQLException
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.persistence.EntityManager
import kotlin.reflect.KClass

class LneQuizRepositoryImpl(
    val jpaKorbitEntityManager: EntityManager,
    val korbitJdbcTemplate: JdbcTemplate
    ) : Querydsl4RepositorySupport<LneQuest>(
        jpaKorbitEntityManager,
        korbitJdbcTemplate,
        LneQuest::class as KClass<Any>
    ), CustomLneQuizRepository {


    fun saveAll(quizList: List<LneQuiz>) {
        var batchCount = 0
        val subItems: MutableList<LneQuiz> = ArrayList()
        for (i in quizList.indices) {
            subItems.add(quizList[i])
            if ((i + 1) % super.batchSize === 0) {
                batchCount = batchInsert(super.batchSize, batchCount, subItems)
            }
        }
        if (!subItems.isEmpty()) {
            batchCount = batchInsert(super.batchSize, batchCount, subItems)
        }
        println("batchCount: $batchCount")
    }

    private fun batchInsert(
        batchSize: Int,
        batchCount: Int,
        subItems: MutableList<LneQuiz>
    ): Int {
        var batchCount = batchCount
        jdbcTemplate.batchUpdate("INSERT INTO lne_quizzes (quest_id, title, type, description, answers, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
            object : BatchPreparedStatementSetter {
                @Throws(SQLException::class)
                override fun setValues(ps: PreparedStatement, i: Int) {
                    subItems[i].quest.id?.let { ps.setLong(1, it) }
                    ps.setString(2, subItems[i].title)
                    ps.setString(3, subItems[i].type)
                    ps.setString(4, subItems[i].description)
                    ps.setString(5, subItems[i].answers)
                    ps.setDate(
                        6,
                        Date.from(
                            LocalDateTime.now().toInstant(ZoneOffset.UTC)
                        ) as Date
                    )
                    ps.setDate(
                        7,
                        Date.from(
                            LocalDateTime.now().toInstant(ZoneOffset.UTC)
                        ) as Date
                    )
                }

                override fun getBatchSize(): Int {
                    return subItems.size
                }
            })
        subItems.clear()
        batchCount++
        return batchCount
    }

}