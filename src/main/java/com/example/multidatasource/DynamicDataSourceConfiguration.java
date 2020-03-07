package com.example.multidatasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class DynamicDataSourceConfiguration implements EnvironmentAware {
    private static final String PROPERTIES_PREFIX = "app.datasource";
    private static Map<LoadedDataSources, Properties> loadedProperties = new HashMap<>();

    /**
     * Set the {@code Environment} that this component runs in.
     *
     * @param environment properties
     */
    @Override
    public void setEnvironment(Environment environment) {
        Iterable<ConfigurationPropertySource> sources = ConfigurationPropertySources.get(environment);
        Binder binder = new Binder(sources);

        for (LoadedDataSources loaded : LoadedDataSources.values()) {
            BindResult<Properties> bindResult = binder.bind(PROPERTIES_PREFIX + "." + loaded.getName(), Properties.class);
            loadedProperties.put(loaded, bindResult.get());
        }
    }

    @Bean
    public DataSource firstDataSource() {
        return new HikariDataSource(new HikariConfig(loadedProperties.get(LoadedDataSources.PRIMARY)));
    }

    @Bean
    public DataSource secondDataSource() {
        return new HikariDataSource(new HikariConfig(loadedProperties.get(LoadedDataSources.FIRST)));
    }

    @Bean
    @Primary
    public DynamicRoutingDataSource dataSource(DataSource firstDataSource, DataSource secondDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(LoadedDataSources.PRIMARY, firstDataSource);
        targetDataSources.put(LoadedDataSources.FIRST, secondDataSource);
        return new DynamicRoutingDataSource(firstDataSource, targetDataSources);
    }

}
