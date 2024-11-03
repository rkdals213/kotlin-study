package kgp.liivm.batchstudy.batch03

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManagerFactory
import kgp.liivm.batchstudy.common.exposed.WriteExposedEntity
import kgp.liivm.batchstudy.common.jpa.QWriteJpaEntity.writeJpaEntity
import kgp.liivm.batchstudy.common.jpa.ReadJpaEntity
import kgp.liivm.batchstudy.common.jpa.WriteJpaEntity
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.JdbcCursorItemReader
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
class Batch03(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val entityManagerFactory: EntityManagerFactory,
    private val dataSource: DataSource,
    private val queryFactory: JPAQueryFactory
) {

    @Bean
    fun batch03Job(): Job {
        return JobBuilder("batch03Job", jobRepository)
            .start(batch03Step())
            .build()
    }

    @Bean
    @JobScope
    fun batch03Step(): Step {
        return StepBuilder("batch03Step", jobRepository)
            .chunk<ReadJpaEntity, ReadJpaEntity>(CHUNK_SIZE, transactionManager)
//            .chunk<BatchStudyDataEntity, BatchStudyDataEntity2>(CHUNK_SIZE, transactionManager)
            .reader(batch03JdbcCursorReader())
//            .processor(processor1())
//            .writer(batch03JpaWriter())
//            .writer(batch03JdbcWriter())
//            .writer(batch03QuerydslWriter())
            .writer(batch03ExposedWriter())
            .build()
    }

    @Bean
    fun batch03JdbcCursorReader(): JdbcCursorItemReader<ReadJpaEntity> {
        return JdbcCursorItemReaderBuilder<ReadJpaEntity>()
            .name("batch03Reader")
            .rowMapper(DataClassRowMapper(ReadJpaEntity::class.java))
            .sql("SELECT id, name, birthday, address FROM batch_study_data_entity b WHERE id <= 100000")
            .dataSource(dataSource)
            .build()
    }

//    @Bean
//    fun processor1(): ItemProcessor<BatchStudyDataEntity, BatchStudyDataEntity> {
//        return ItemProcessor {
//            BatchStudyDataExposedEntity2.
//        }
//    }

    @Bean
    fun processor2(): ItemProcessor<ReadJpaEntity, WriteJpaEntity> {
        return ItemProcessor {
            WriteJpaEntity(
                id = 0,
                name = it.name,
                birthday = it.birthday,
                address = it.address
            )
        }
    }

    @Bean // 30초
    fun batch03JpaWriter(): JpaItemWriter<WriteJpaEntity> {
        return JpaItemWriterBuilder<WriteJpaEntity>()
            .entityManagerFactory(entityManagerFactory)
            .build()
    }

    @Bean // 30초
    fun batch03QuerydslWriter(): ItemWriter<WriteJpaEntity> {
        return ItemWriter { list ->
            for (entity in list) {
                queryFactory.insert(writeJpaEntity)
                    .columns(writeJpaEntity.name, writeJpaEntity.birthday, writeJpaEntity.address)
                    .values(entity.name, entity.birthday, entity.address)
                    .execute()
            }
        }
    }

    @Bean // 3초
    fun batch03JdbcWriter(): JdbcBatchItemWriter<WriteJpaEntity> {
        return JdbcBatchItemWriterBuilder<WriteJpaEntity>()
            .dataSource(dataSource)
            .sql("insert into batch_study_data_entity2 (name, birthday, address) values (:name, :birthday, :address)")
            .beanMapped()
            .build()
    }

    @Bean // 3초
    fun batch03ExposedWriter(): ItemWriter<ReadJpaEntity> {
        return ItemWriter { list ->
            BatchInsertStatement(WriteExposedEntity).apply {
                list.forEach {
                    addBatch()
                    this[WriteExposedEntity.name] = it.name
                    this[WriteExposedEntity.birthday] = it.birthday
                    this[WriteExposedEntity.address] = it.address
                }
            }.execute(TransactionManager.current())
        }
    }

    companion object {
        private const val CHUNK_SIZE = 10000
    }
}
