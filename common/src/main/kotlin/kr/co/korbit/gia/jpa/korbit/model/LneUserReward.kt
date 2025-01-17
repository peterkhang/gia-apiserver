package kr.co.korbit.gia.jpa.korbit.model

import com.fasterxml.jackson.annotation.JsonInclude
import kr.co.korbit.gia.jpa.common.BasePersistable
import org.hibernate.annotations.Type
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "LneUserReward")
@Table(name = "korbit.lne_user_rewards")
@Cacheable
@JsonInclude(JsonInclude.Include.NON_NULL)
class LneUserReward(
    var user_id: Long,

    @OneToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "quest_id", referencedColumnName = "id")
    var lneQuest: LneQuest,

    var currency: String,
    var amount: Double,

    @Type(type = "yes_no")
    var paid: Boolean,

    @Column(name = "paid_at")
    var paidAt: LocalDateTime,

    @Column(name = "rewarded_amount_in_krw")
    var rewardedAmountInKrw: Double = 0.0,

    @Column(name = "paid_amount_in_krw")
    var paidAmountInKrw: Double = 0.0,

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null
): BasePersistable() {
    
}