package kgp.liivm.kotlinstudy.null_check

class MemberRepository {

    fun findByIdOrNull(id: Long): String? = if (id == 1L) "Kang" else null
}
