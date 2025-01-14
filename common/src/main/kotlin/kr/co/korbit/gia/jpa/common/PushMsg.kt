package kr.co.korbit.gia.jpa.common

import com.fasterxml.jackson.annotation.JsonInclude
import kr.co.korbit.gia.jpa.test.model.User

@JsonInclude(JsonInclude.Include.NON_NULL)
class PushMsg(
    var msg_id: String? = null,
    var move_type1: String? = null,
    var move_type2: String? = null,
    var image_path: String? = null,
    var image_path1: String? = null,
    var subject: String? = null,
    var contents: String? = null,
    var move_target: String? = null,
    var move_target_string: String? = null,
    var appType: String? = null,
    var pushCase: Int? = null,
    var ios: Boolean? = null,
    var aos: Boolean? = null,
    var sender: User? = null,
    var receivers: List<User>? = null,
    var targets: List<UserApp>? = null
) : BasePersistable() {
    companion object {
        val serialVersionUID = 7825983033219974064L
    }
}