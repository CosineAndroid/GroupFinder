package kr.cosine.groupfinder

import org.junit.Test
import org.mindrot.jbcrypt.BCrypt

class HashPasswordTest {

    @Test
    fun hash_password_test() {
        val firstPassword = BCrypt.hashpw("xptmxm1*", BCrypt.gensalt())
        val secondPassword = BCrypt.hashpw("xptmxm1*", BCrypt.gensalt())
        println("firstPassword: $firstPassword")
        println("secondPassword: $secondPassword")
    }
}