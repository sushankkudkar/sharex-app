package com.sharex.app.infrastructure.persistence.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {
                "com.sharex.app.module",                             // read-side repository interfaces
                "com.sharex.app.infrastructure.persistence.read"    // read-side JPA repo package
        },
        includeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = ".*\\.persistence\\.read.*"
        ),
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = ".*\\.persistence\\.write.*"
        ),
        entityManagerFactoryRef = "readEntityManagerFactory",
        transactionManagerRef = "readTransactionManager"
)
public class ReadDbConfig {

    @Bean(name = "readEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean readEntityManagerFactory(
            @Qualifier("readDataSource") DataSource readDataSource
    ) {

        LocalContainerEntityManagerFactoryBean emf =
                new LocalContainerEntityManagerFactoryBean();

        emf.setDataSource(readDataSource);

        // ⭐ MUST scan entity package
        emf.setPackagesToScan(
                "com.sharex.app.infrastructure.persistence.read",   // read ENTITIES
                "com.sharex.app.module"                             // read repos if needed
        );

        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setJpaPropertyMap(Map.of(
                "hibernate.hbm2ddl.auto", "validate"
        ));

        emf.setPersistenceUnitName("readPU");
        return emf;
    }

    @Bean(name = "readTransactionManager")
    public PlatformTransactionManager readTransactionManager(
            @Qualifier("readEntityManagerFactory") EntityManagerFactory emf
    ) {
        return new JpaTransactionManager(emf);
    }
}
