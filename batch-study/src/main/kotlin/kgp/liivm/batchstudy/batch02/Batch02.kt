package kgp.liivm.batchstudy.batch02

import jakarta.persistence.*
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
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDate
import javax.sql.DataSource

@Configuration
class Batch02(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val entityManagerFactory: EntityManagerFactory,
    private val dataSource: DataSource,
) {
    var current = 0

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
            .chunk<BatchStudyDataEntity, BatchStudyDataEntity>(1000, transactionManager)
            .reader(batch02Reader())
//            .reader(batch02Reader2())
            .writer(batch02Writer())
            .build()
    }

    @Bean
    fun batch02Reader(): JpaPagingItemReader<BatchStudyDataEntity> {
        return JpaPagingItemReaderBuilder<BatchStudyDataEntity>()
            .name("batch02Reader")
            .pageSize(100000)
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT b FROM batch_study_data_entity b")
            .build()
    }

    @Bean
    fun batch02Reader2(): JdbcCursorItemReader<BatchStudyDataEntity> {
        return JdbcCursorItemReaderBuilder<BatchStudyDataEntity>()
            .name("batch02Reader")
            .sql("SELECT id, name, birthday, address FROM batch_study_data_entity b")
            .dataSource(dataSource)
            .rowMapper { rs, _ ->
                BatchStudyDataEntity(
                    id = rs.getLong(1),
                    name = rs.getString(2),
                    birthday = LocalDate.parse(rs.getString(3)),
                    address = rs.getString(4),
                )
            }
            .build()
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
}

@Entity(name = "batch_study_data_entity")
class BatchStudyDataEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    val birthday: LocalDate,
    val address: String
) {
    override fun toString(): String {
        return "BatchStudyDataEntity(id=$id, name='$name', birthday=$birthday, address='$address')"
    }
}
