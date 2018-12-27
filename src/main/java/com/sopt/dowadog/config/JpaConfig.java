package com.sopt.dowadog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@PropertySource({ "classpath:application.properties" })
@EnableJpaRepositories(basePackages = "com.sopt.dowadog.repository", entityManagerFactoryRef = "entityManager", transactionManagerRef = "platformTransactionManager")
@EnableJpaAuditing
public class JpaConfig {

    private final String driverClassName;
    private final String url;
    private final String username;
    private final String password;
    private final String ddl;
    private final String dialect;
    private final String namingStrategy;

    public JpaConfig(@Value("${spring.datasource.driverClassName}") String dirverClassName,
                     @Value("${spring.datasource.url}") String url,
                     @Value("${spring.datasource.username}") String username,
                     @Value("${spring.datasource.password}") String password,
                     @Value("${hibernate.ddl-auto}") String ddl,
                     @Value("${hibernate.dialect}") String dialect,
                     @Value("${hibernate.naming-strategy}") String namingStrategy){

        this.driverClassName = dirverClassName;
        this.url = url;
        this.username = username;
        this.password = password;
        this.ddl = ddl;
        this.dialect = dialect;
        this.namingStrategy = namingStrategy;
    }



    @Primary
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(this.driverClassName);
        dataSource.setUrl(this.url);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);

        return dataSource;
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManager() {
        LocalContainerEntityManagerFactoryBean entityManagerBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerBean.setDataSource(dataSource());
        entityManagerBean.setPackagesToScan(new String[] { "com.sopt.dowadog.model.domain" });
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerBean.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();

        properties.put("hibernate.hbm2ddl.auto", this.ddl);
        properties.put("hibernate.dialect", this.dialect);
        properties.put("hibernate.physical_naming_strategy", namingStrategy);

        entityManagerBean.setJpaPropertyMap(properties);

        return entityManagerBean;
    }

    @Primary
    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager().getObject());
        return transactionManager;
    }
}
