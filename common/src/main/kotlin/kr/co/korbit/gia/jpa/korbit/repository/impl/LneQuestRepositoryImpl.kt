package kr.co.korbit.gia.jpa.korbit.repository.impl

import com.querydsl.core.QueryResults
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.korbit.gia.jpa.common.Querydsl4RepositorySupport
import kr.co.korbit.gia.jpa.common.WhereBuilder
import kr.co.korbit.gia.jpa.korbit.model.LneQuest
import kr.co.korbit.gia.jpa.korbit.model.QLneQuest
import kr.co.korbit.gia.jpa.korbit.model.QLneQuest.*
import kr.co.korbit.gia.jpa.korbit.model.QLneQuizz.*
import kr.co.korbit.gia.jpa.korbit.repository.custom.CustomLneQuestRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.*
import java.time.LocalDateTime

class LneQuestRepositoryImpl(@Qualifier("jpaKorbitQueryFactory") val jpaKorbitQueryFactory: JPAQueryFactory)
    : Querydsl4RepositorySupport<LneQuest>(), CustomLneQuestRepository {


    init {
        super.queryFactory = jpaKorbitQueryFactory
    }

    val path: PathBuilder<LneQuest> = PathBuilder<LneQuest>(LneQuest::class.java, "path")
    //val lneQuest: QLneQuest = QLneQuizz("lneQuest")

    override fun findAllBy(type: String?, from: LocalDateTime?, to: LocalDateTime?, pageable: Pageable): Page<LneQuest> {
//        lateinit var pageableReal: Pageable
//        if (pageable != null) {
//            pageableReal = pageable
//        } else {
//            pageableReal = PageRequest.of(1, 20).first()
//
//        }
        val quest = QLneQuest("quest")

        val sql =
            select()
                .from(lneQuest)
                .join(lneQuest.quizzList, lneQuizz)
                .where(
                    WhereBuilder()
                        .optionalAnd(from, { lneQuest.createdAt.after(from) })
                        .optionalAnd(to, { lneQuest.createdAt.before(to) })
                        .optionalAnd(type, { lneQuest.type.eq(type) })
                )
                .orderBy(*ordable(pageable.sort, path).toTypedArray())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
        val queryResults: QueryResults<LneQuest> = sql.fetchResults()
        return PageImpl<LneQuest>(queryResults.results, pageable, queryResults.total)
    }


}