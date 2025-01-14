package kr.co.korbit.gia.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kr.co.korbit.gia.env.Env
import mu.KotlinLogging
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.StringDeserializer
import java.nio.charset.Charset

private val logger = KotlinLogging.logger {}

class KeyDeserializer(
    private val objectMapper: ObjectMapper = ObjectMapper()
        .registerModule(JavaTimeModule())
        .registerModule(KotlinModule())
) : Deserializer<String?> {

    override fun deserialize(topic: String, data: ByteArray): String {
        return try {

            val key = String(data, Charset.defaultCharset())
            if( !key.equals(Env.korbit) ) {
                if (Env.branch.equals("master")) {
                    logger.error("unkown kafka event's key : " + key)
                    "unknown"
                } else {
                    Env.korbit
                }
            } else {
                Env.korbit
            }

        } catch (e: Throwable) {

            if( Env.branch.equals("master") ) {
                logger.error("Error when deserializing of kafka event's key byte[] to string : " + e.stackTrace)
                "unknown"
            } else {
                logger.warn("Error when deserializing of kafka event's key byte[] to string : " + e.stackTrace)
                Env.korbit
            }
        }
    }
}