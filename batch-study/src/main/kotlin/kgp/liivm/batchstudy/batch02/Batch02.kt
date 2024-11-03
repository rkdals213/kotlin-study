package kgp.liivm.batchstudy.batch02

import com.querydsl.core.types.dsl.NumberPath
import jakarta.persistence.EntityManagerFactory
import kgp.liivm.batchstudy.common.entity.BatchStudyDataEntity
import kgp.liivm.batchstudy.common.entity.QBatchStudyDataEntity.batchStudyDataEntity
import kgp.liivm.batchstudy.common.querydsl_reader.QueryDslPagingItemReader
import kgp.liivm.batchstudy.common.querydsl_reader.expression.Expression
import kgp.liivm.batchstudy.common.querydsl_reader.options.QuerydslNoOffsetLongOptions
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.database.JdbcCursorItemReader
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.batch.item.file.FlatFileItemWriter
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor
import org.springframework.batch.item.file.transform.DelimitedLineAggregator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.transaction.PlatformTransactionManager
import java.lang.reflect.Method
import javax.sql.DataSource

@Configuration
class Batch02(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val entityManagerFactory: EntityManagerFactory,
    private val dataSource: DataSource,
) {
    @Bean
    fun batch02Job(): Job {
        return JobBuilder("batch02Job", jobRepository)
            .start(batch02Step())
            .build()
    }

    @Bean
    @JobScope
    fun batch02Step(): Step {
        return StepBuilder("batch02Step", jobRepository)
            .chunk<BatchStudyDataEntity, BatchStudyDataEntity>(CHUNK_SIZE, transactionManager)
//            .reader(batch02JpaPagingReader())
//            .reader(batch02JdbcCursorReader())
            .reader(batch02QueryDslPagingReader())
            .writer(batch02Writer())
            .build()
    }

    @Bean
    fun batch02JpaPagingReader(): JpaPagingItemReader<BatchStudyDataEntity> {
        return JpaPagingItemReaderBuilder<BatchStudyDataEntity>()
            .name("batch02Reader")
            .pageSize(CHUNK_SIZE)
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT b FROM batch_study_data_entity b")
            .build()
    }

    @Bean
    fun batch02JdbcCursorReader(): JdbcCursorItemReader<BatchStudyDataEntity> {
        return JdbcCursorItemReaderBuilder<BatchStudyDataEntity>()
            .name("batch02Reader")
            .rowMapper(DataClassRowMapper(BatchStudyDataEntity::class.java))
            .sql("SELECT id, name, birthday, address FROM batch_study_data_entity b order by id desc")
            .dataSource(dataSource)
            .build()
    }

    @Bean
    fun batch02QueryDslPagingReader(): QueryDslPagingItemReader<BatchStudyDataEntity> {
        val identifier: NumberPath<Long> = batchStudyDataEntity.id
        val method: Method = BatchStudyDataEntity::class.java.getMethod("getId")

        val queryDslPagingItemReader = QueryDslPagingItemReader<BatchStudyDataEntity>(
            entityManagerFactory,
            identifier,
            method,
            QuerydslNoOffsetLongOptions(batchStudyDataEntity.id, Expression.ASC)
        )
        queryDslPagingItemReader.pageSize = CHUNK_SIZE

        queryDslPagingItemReader.queryFunction {
            it.query()
                .select(batchStudyDataEntity)
                .from(batchStudyDataEntity)
        }

        return queryDslPagingItemReader
    }

    @Bean
    fun batch02Writer(): FlatFileItemWriter<BatchStudyDataEntity> {
        val extractor = BeanWrapperFieldExtractor<BatchStudyDataEntity>()
        extractor.setNames(arrayOf("id", "name", "birthday", "address"))

        val aggregator = DelimitedLineAggregator<BatchStudyDataEntity>()
        aggregator.setDelimiter(",")
        aggregator.setFieldExtractor(extractor)

        return FlatFileItemWriterBuilder<BatchStudyDataEntity>()
            .name("batch02Writer")
            .encoding("UTF-8")
            .resource(FileSystemResource("output/test.csv"))
            .lineAggregator(aggregator)
            .build()
    }

    companion object {
        private const val CHUNK_SIZE = 100000
    }
}


