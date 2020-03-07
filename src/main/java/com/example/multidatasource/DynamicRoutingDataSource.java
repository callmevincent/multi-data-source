package com.example.multidatasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<LoadedDataSources> dataSourceContext = new ThreadLocal<>();

    public DynamicRoutingDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    /**
     * Determine the current lookup key. This will typically be
     * implemented to check a thread-bound transaction context.
     * <p>Allows for arbitrary keys. The returned key needs
     * to match the stored lookup key type, as resolved by the
     * {@link #resolveSpecifiedLookupKey} method.
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSource();
    }

    public static void setDataSource(LoadedDataSources dataSource) {
        dataSourceContext.set(dataSource);
    }

    public static LoadedDataSources getDataSource() {
        return dataSourceContext.get();
    }

    public static void clearDataSource() {
        dataSourceContext.remove();
    }
}
