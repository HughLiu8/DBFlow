package com.raizlabs.android.dbflow.sql.language;

import android.database.Cursor;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteStatement;
import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.DbFlowDependencyHelper;
import com.raizlabs.android.dbflow.config.FlowInstanceWrapper;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.NotifyDistributor;
import com.raizlabs.android.dbflow.sql.SqlUtils;
import com.raizlabs.android.dbflow.sql.queriable.Queriable;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatementWrapper;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.FlowCursor;

/**
 * Description: Base implementation of something that can be queried from the database.
 */
public abstract class BaseQueriable<TModel> implements Queriable, Actionable {


    private final Class<TModel> table;
    protected String id;

    protected BaseQueriable(Class<TModel> table) {
        this.table = table;
    }
    protected BaseQueriable(Class<TModel> table, @NonNull String id) {
        DbFlowDependencyHelper.checkIdWithWarning(id, "BaseQueriable_BaseQueriable");
        this.table = table;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    /**
     * @return The table associated with this INSERT
     */
    @NonNull
    public Class<TModel> getTable() {
        return table;
    }

    /**
     * Execute a statement that returns a 1 by 1 table with a numeric value.
     * For example, SELECT COUNT(*) FROM table.
     * Please see {@link SQLiteStatement#simpleQueryForLong()}.
     * <p>
     * catches a {@link SQLiteDoneException} if result is not found and returns 0. The error can safely be ignored.
     */
    @Override
    public long count(@NonNull DatabaseWrapper databaseWrapper) {
        return longValue(databaseWrapper);
    }

    /**
     * Execute a statement that returns a 1 by 1 table with a numeric value.
     * For example, SELECT COUNT(*) FROM table.
     * Please see {@link SQLiteStatement#simpleQueryForLong()}.
     */
    @Override
    public long count() {
        return longValue();
    }

    @Override
    public long longValue() {
        return longValue(FlowInstanceWrapper.getWritableDatabaseForTable(id, table, "longValue"));
    }

    @Override
    public long longValue(DatabaseWrapper databaseWrapper) {
        try {
            String query = getQuery();
            FlowLog.log(FlowLog.Level.V, "Executing query: " + query);
            return SqlUtils.longForQuery(databaseWrapper, query);
        } catch (SQLiteDoneException sde) {
            // catch exception here, log it but return 0;
            FlowLog.log(FlowLog.Level.W, sde);
        }
        return 0;
    }

    @Override
    public boolean hasData() {
        return count() > 0;
    }

    @Override
    public boolean hasData(@NonNull DatabaseWrapper databaseWrapper) {
        return count(databaseWrapper) > 0;
    }

    @Override
    public FlowCursor query() {
        query(FlowInstanceWrapper.getWritableDatabaseForTable(id, table, "query"));
        return null;
    }

    @Override
    public FlowCursor query(@NonNull DatabaseWrapper databaseWrapper) {
        if (getPrimaryAction().equals(BaseModel.Action.INSERT)) {
            // inserting, let's compile and insert
            DatabaseStatement databaseStatement = compileStatement(databaseWrapper);
            databaseStatement.executeInsert();
            databaseStatement.close();
        } else {
            String query = getQuery();
            FlowLog.log(FlowLog.Level.V, "Executing query: " + query);
            databaseWrapper.execSQL(query);
        }
        return null;
    }

    @Override
    public long executeInsert() {
        return executeInsert(FlowInstanceWrapper.getWritableDatabaseForTable(id, table, "executeInsert"));
    }

    @Override
    public long executeInsert(@NonNull DatabaseWrapper databaseWrapper) {
        DatabaseStatement statement = compileStatement(databaseWrapper);
        long rows;
        try {
            rows = statement.executeInsert();
        } finally {
            statement.close();
        }
        return rows;
    }

    @Override
    public void execute() {
        Cursor cursor = query();
        if (cursor != null) {
            cursor.close();
        } else {
            if (id == null
                    && DbFlowDependencyHelper.getDependency() != null
                    && DbFlowDependencyHelper.getDependency().isMultipleDatabaseEnabled()) {
                throw new IllegalArgumentException("id can't be null in BaseQueriable_execute");
            }
            // we dont query, we're executing something here.
            NotifyDistributor.get().notifyTableChanged(getTable(), getPrimaryAction(), id);
        }
    }

    @Override
    public void execute(@NonNull DatabaseWrapper databaseWrapper) {
        Cursor cursor = query(databaseWrapper);
        if (cursor != null) {
            cursor.close();
        } else {
            if (id == null
                    && DbFlowDependencyHelper.getDependency() != null
                    && DbFlowDependencyHelper.getDependency().isMultipleDatabaseEnabled()) {
                throw new IllegalArgumentException("id can't be null in BaseQueriable_execute2");
            }
            // we dont query, we're executing something here.
            NotifyDistributor.get().notifyTableChanged(getTable(), getPrimaryAction(), id);
        }
    }

    @NonNull
    @Override
    public DatabaseStatement compileStatement() {
        return compileStatement(FlowInstanceWrapper.getWritableDatabaseForTable(id, table, "compileStatement"));
    }

    @NonNull
    @Override
    public DatabaseStatement compileStatement(@NonNull DatabaseWrapper databaseWrapper) {
        String query = getQuery();
        FlowLog.log(FlowLog.Level.V, "Compiling Query Into Statement: " + query);
        return new DatabaseStatementWrapper<>(databaseWrapper.compileStatement(query), this);
    }

    @Override
    public String toString() {
        return getQuery();
    }

    @NonNull
    public abstract BaseModel.Action getPrimaryAction();
}
