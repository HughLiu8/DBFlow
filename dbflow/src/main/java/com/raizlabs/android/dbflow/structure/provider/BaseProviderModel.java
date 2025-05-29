package com.raizlabs.android.dbflow.structure.provider;

import android.content.ContentProvider;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.raizlabs.android.dbflow.config.FlowInstanceWrapper;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.FlowCursor;

/**
 * Description: Provides a base implementation of a {@link Model} backed
 * by a content provider. All model operations are overridden using the {@link ContentUtils}.
 * Consider using a {@link BaseSyncableProviderModel} if you wish to
 * keep modifications locally from the {@link ContentProvider}
 */
public abstract class BaseProviderModel
    extends BaseModel implements ModelProvider {

    @Override
    public boolean delete(String id) {
        return ContentUtils.delete(getDeleteUri(), this, id) > 0;
    }

    @Override
    public boolean save(String id) {
        int count = ContentUtils.update(getUpdateUri(), this, id);
        if (count == 0) {
            return ContentUtils.insert(getInsertUri(), this, id) != null;
        } else {
            return count > 0;
        }
    }

    @Override
    public boolean update(String id) {
        return ContentUtils.update(getUpdateUri(), this, id) > 0;
    }

    @Override
    public long insert(String id) {
        ContentUtils.insert(getInsertUri(), this, id);
        return 0;
    }

    /**
     * Runs a query on the {@link ContentProvider} to see if it returns data.
     *
     * @return true if this model exists in the {@link ContentProvider} based on its primary keys.
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean exists(String id) {
        Cursor cursor = ContentUtils.query(FlowInstanceWrapper.getContext(id, "BaseProviderModel_exists").getContentResolver(),
            getQueryUri(), getModelAdapter(id).getPrimaryConditionClause(this), "");
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void load(String id, @NonNull OperatorGroup whereConditions,
                     @Nullable String orderBy, String... columns) {
        FlowCursor cursor = FlowCursor.from(ContentUtils.query(FlowInstanceWrapper.getContext(id, "BaseProviderModel_load").getContentResolver(),
            getQueryUri(), whereConditions, orderBy, columns));
        if (cursor != null && cursor.moveToFirst()) {
            getModelAdapter(id).loadFromCursor(cursor, this);
            cursor.close();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void load(String id) {
        load(id, getModelAdapter().getPrimaryConditionClause(this), "");
    }

}
