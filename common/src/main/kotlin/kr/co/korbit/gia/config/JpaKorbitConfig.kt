package kr.co.korbit.gia.config

import com.querydsl.jpa.impl.JPAQueryFactory
import com.zaxxer.hikari.HikariDataSource
import kr.co.korbit.gia.env.Env
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager
import org.springframework.orm.jpa.vendor.Database
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource
import kotlin.collections.HashMap

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "jpaKorbitEntityManagerFactory",
        transactionManagerRef = "jpaKorbitTransactionManager",
        basePackages = ["kr.co.korbit.gia.jpa.korbit.repository"])
class JpaKorbitConfig {
    val dialect = "org.hibernate.dialect.MySQL57Dialect"
    var ddlAuto = "validate"
    var showSql = "true"
    var formatSql = "true"
    var useNewIdGeneratorMappings = "false"
    var implicitStrategy = "kr.co.korbit.gia.config.CamelCaseToSnakeCaseNamingStrategy"
    var physicalStrategy = "kr.co.korbit.gia.config.CamelCaseToSnakeCaseNamingStrategy"

    @Autowired(required = false)
    private val persistenceUnitManager: PersistenceUnitManager? = null


    @Bean(name = ["jpaKorbitDataSource"])
    @ConfigurationProperties(prefix = "spring.datasource.korbit")
    @Primary
    fun jpaKorbitDataSource(): DataSource {
        val dataSource = DataSourceBuilder.create().type(HikariDataSource::class.java).build()
        //dataSource.connectionInitSql = "SET NAMES utf8mb4; set @@session.time_zone = '+00:00'"
        return dataSource
    }


    @Bean(name = ["jpaKorbitEntityManagerFactoryBuilder"])
    @Primary
    fun jpaKorbitEntityManagerFactoryBuilder(): EntityManagerFactoryBuilder {
        val adapter = HibernateJpaVendorAdapter()
        adapter.setShowSql(true)
        adapter.setPrepareConnection(true)
        adapter.setDatabase(Database.MYSQL)
        adapter.setDatabasePlatform(dialect)
        adapter.setGenerateDdl(false)
        val properties = HashMap<String, String?>()
//        properties["hibernate.ddl-auto"] = ddlAuto
//        properties["show-sql"] = showSql
//        properties["format-sql"] = formatSql
//        properties["format_sql"] = formatSql
//        properties["hibernate.naming.implicit-strategy}"] = implicitStrategy
//        properties["hibernate.naming.physical-strategy}"] = physicalStrategy
//        properties["hibernate.use-new-id-generator-mappings"] = useNewIdGeneratorMappings
        //builder.setCallback(getVendorCallback());
        return EntityManagerFactoryBuilder(
                adapter, properties, persistenceUnitManager)
    }


    @Bean(name = ["jpaKorbitEntityManagerFactory"])
    @Primary
    fun jpaKorbitEntityManagerFactory(
            @Qualifier("jpaKorbitEntityManagerFactoryBuilder") builder: EntityManagerFactoryBuilder,
            @Qualifier("jpaKorbitDataSource") jpaKorbitDataSource: DataSource?): LocalContainerEntityManagerFactoryBean {
        val factory =  builder
                .dataSource(jpaKorbitDataSource)
//                .packages("kr.co.korbit.gia.jpa.kotbit.model")
                .persistenceUnit("korbit")
                .build()

        factory.setPackagesToScan("kr.co.korbit.gia.jpa.korbit.model")
        return factory
    }


    @Bean(name = ["jpaKorbitTransactionManager"])
    @Primary
    fun jpaKorbitTransactionManager(
            @Qualifier("jpaKorbitEntityManagerFactory") jpaKorbitEntityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager(jpaKorbitEntityManagerFactory)
    }

    @Bean(name = ["jpaKorbitQueryFactory"])
    fun jpaKorbitQueryFactory(@Qualifier("jpaKorbitEntityManagerFactory") jpaKorbitEntityManagerFactory: EntityManagerFactory): JPAQueryFactory {

        return JPAQueryFactory(jpaKorbitEntityManagerFactory.createEntityManager())
    }
}