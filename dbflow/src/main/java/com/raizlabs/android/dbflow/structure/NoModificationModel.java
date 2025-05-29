package com.raizlabs.android.dbflow.structure;

import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.config.FlowInstanceWrapper;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

/**
 * Description: A convenience class for {@link ReadOnlyModel}.
 */
abstract class NoModificationModel implements ReadOnlyModel {

    private transient RetrievalAdapter adapter;

    @SuppressWarnings("unchecked")
    public boolean exists() {
        throw new IllegalArgumentException("NoModificationModel.exist didn't migrate yet");
        //return getRetrievalAdapter().exists(this);
    }

    @SuppressWarnings("unchecked")
    public boolean exists(@NonNull DatabaseWrapper databaseWrapper, @NonNull final String id) {
        return getRetrievalAdapter(id).exists(this, databaseWrapper);
    }

    @SuppressWarnings("unchecked")
    public void load() {
        throw new IllegalArgumentException("NoModificationModel.exist didn't migrate yet");
        //getRetrievalAdapter().load(this);
    }

    @SuppressWarnings("unchecked")
    public void load(@NonNull DatabaseWrapper wrapper, @NonNull final String id) {
        getRetrievalAdapter(id).load(this, wrapper);
    }

    public RetrievalAdapter getRetrievalAdapter(@NonNull final String id) {
        if (adapter == null) {
            adapter = FlowInstanceWrapper.getInstanceAdapter(id, getClass(), "getRetrievalAdapter");
        }
        return adapter;
    }

    /**
     * Gets thrown when an operation is not valid for the SQL View
     */
    static class InvalidSqlViewOperationException extends RuntimeException {

        InvalidSqlViewOperationException(String detailMessage) {
            super(detailMessage);
        }
    }
}
