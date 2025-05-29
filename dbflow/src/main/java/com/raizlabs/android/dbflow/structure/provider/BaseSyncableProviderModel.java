package com.raizlabs.android.dbflow.structure.provider;

import android.content.ContentProvider;
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
 * by a content provider. All operations sync with the content provider in this app from a {@link ContentProvider}
 */
public abstract class BaseSyncableProviderModel extends BaseModel implements ModelProvider {

    @Override
    public long insert(String id) {
        long rowId = super.insert(id);
        ContentUtils.insert(getInsertUri(), this, id);
        return rowId;
    }

    @Override
    public boolean save(String id) {
        if (exists()) {
            return super.save(id) && ContentUtils.update(getUpdateUri(), this, id) > 0;
        } else {
            return super.save(id) && ContentUtils.insert(getInsertUri(), this, id) != null;
        }
    }

    @Override
    public boolean delete(String id) {
        return super.delete(id) && ContentUtils.delete(getDeleteUri(), this, id) > 0;
    }

    @Override
    public boolean update(String id) {
        return super.update(id) && ContentUtils.update(getUpdateUri(), this, id) > 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void load(String id, @NonNull OperatorGroup whereOperatorGroup,
                     @Nullable String orderBy, String... columns) {
        FlowCursor cursor = FlowCursor.from(ContentUtils.query(FlowInstanceWrapper.getContext(id, "BaseSyncableProviderModel_load").getContentResolver(),
            getQueryUri(), whereOperatorGroup, orderBy, columns));
        if (cursor != null && cursor.moveToFirst()) {
            getModelAdapter(id).loadFromCursor(cursor, this);
            cursor.close();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void load(String id) {
        load(id, getModelAdapter(id).getPrimaryConditionClause(this), "");
    }
}
