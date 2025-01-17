
DROP TABLE IF EXISTS admin.admin_users ;
CREATE TABLE admin.admin_users (
  `id` bigint(20) NOT NULL AUTO_INCREMENT comment '관리자 id 번호',
  `email` varchar(64) NOT NULL comment '관리자 이메일 주소',
  `password` varchar(64) NOT NULL comment '관리자 암호',
  `phone` varchar(64) NOT NULL comment '관리자 핸드폰 번호',
  `roles` varchar(255) NOT NULL DEFAULT 'ADMIN' comment '관리자 역할(ADMIN,MEMBER,AGENT)',
  `created_at` datetime NOT NULL comment '관리자 등록 시각',
  `updated_at` datetime NOT NULL comment '관리자 정보 변경 시각',
  `created_by` bigint(20) DEFAULT NULL comment '관리자 생성 id 번호',
  `updated_by` bigint(20) DEFAULT NULL comment '관리자 정보 변경 id 번호',
  `name` varchar(255) NOT NULL comment '관리자 이름',
  `nick` varchar(255) DEFAULT NULL comment '관리자 닉네임',
  `description` varchar(255) DEFAULT NULL comment '관리자 설명',
  `status` varchar(32) NOT NULL DEFAULT 'registered' comment '상태값: registered, leaved, unconfirmed, dormant, failed, saved, deleted',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_index_admin_users_on_email` (`email`),
  UNIQUE KEY `unique_index_admin_users_on_phone` (`phone`),
  KEY `index_admin_users_on_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci comment '관리자 테이블' ;

 List<MemerDto> result = queryFactory
          .select(Projections.bean(MemberDto.class,
                  member.username,
                  member.age))
          .from(member)
          .fetch();

 List<MemerDto> result = queryFactory
          .select(Projections.fields(MemberDto.class,
                  member.username,
                  member.age))
          .from(member)
          .fetch();


 List<UserDto> result = queryFactory
        .select(Projections.fields(UserDto.class,
                member.username.as("name"),
                ExpressionUtils.as(
                    JPAExpressions
                        .select(memberSub.age.max())
                    .from(memberSub), "age")
                 )
        ).from(member)
        .fetch();


 List<MemerDto> result = queryFactory
          .select(Projections.constructor(MemberDto.class,
                  member.username,
                  member.age))
          .from(member)
          .fetch();

 List<MemberDto> result = queryFactory
          .select(new QMemberDto(member.username, member.age))
          .from(member)
          .fetch();
select
  uuid,
  email,
  user_hash,
  encrypted_password,
  encrypted_password_set_at,
  reset_password_token,
  reset_password_sent_at,
  remember_created_at,
  sign_in_count,
  current_sign_in_at,
  last_sign_in_at,
  current_sign_in_ip,
  last_sign_in_ip,
  confirmation_token,
  confirmed_at,
  confirmation_sent_at,
  unconfirmed_email,
  failed_attempts,
  unlock_token,
  unlock_sent_at,
  locked_at,
  authentication_token,
  created_at,
  updated_at,
  name,
  nick,
  phone,
  permit,
  deleted_at,
  dob,
  gender,
  nationality,
  country_code,
  namechecked_at,
  terms,
  entity,
  registration_number,
  change_confirmation_token,
  change_confirmed_at,
  change_confirmation_sent_at,
  namecheck_status,
  namecheck_status_code,
  namechecked_by,
  captcha_at,
  no_captcha_until,
  join_reason,
  status,
  sign_up_platform_id,
  is_identified_for_coins,
  is_identified_for_fiats,
  is_corporation
from users ;