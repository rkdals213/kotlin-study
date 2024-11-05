package kgp.liivm.kotlinstudy.common.config

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class Tx(
    _txAdvice: TxAdvice
) {

    init {
        txAdvice = _txAdvice
    }

    companion object {
        private lateinit var txAdvice: TxAdvice

        fun <T> jpaTxm(function: () -> T): T {
            return txAdvice.jpaTxm(function)
        }

        fun <T> exposedTxm(function: () -> T): T {
            return txAdvice.exposedTxm(function)
        }
    }

    @Component
    class TxAdvice {

        @Transactional(transactionManager = "jpaTransactionManager")
        fun <T> jpaTxm(function: () -> T): T {
            return function.invoke()
        }

        @Transactional(transactionManager = "exposedTransactionManager")
        fun <T> exposedTxm(function: () -> T): T {
            return function.invoke()
        }
    }
}
