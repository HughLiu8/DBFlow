package com.raizlabs.android.dbflow.runtime;

import android.content.ContentResolver;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.raizlabs.android.dbflow.config.FlowInstanceWrapper;
import com.raizlabs.android.dbflow.sql.SqlUtils;
import com.raizlabs.android.dbflow.sql.language.SQLOperator;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

/**
 * The default use case, it notifies via the {@link ContentResolver} system.
 */
public class ContentResolverNotifier implements ModelNotifier {

    @NonNull
    private final String contentAuthority;
    private final String id;

    public ContentResolverNotifier(@NonNull String contentAuthority, String id) {
        this.contentAuthority = contentAuthority;
        this.id = id;
    }

    @Override
    public <T> void notifyModelChanged(@NonNull T model, @NonNull ModelAdapter<T> adapter,
                                       @NonNull BaseModel.Action action) {
        if (FlowContentObserver.shouldNotify()) {
            FlowInstanceWrapper.getContext(id, "notifyModelChanged").getContentResolver()
                    .notifyChange(SqlUtils.getNotificationUri(contentAuthority,
                            adapter.getModelClass(), action,
                            adapter.getPrimaryConditionClause(model).getConditions(), id), null, true);
        }
    }

    @Override
    public <T> void notifyTableChanged(@NonNull Class<T> table, @NonNull BaseModel.Action action, @NonNull String id) {
        if (FlowContentObserver.shouldNotify()) {
            FlowInstanceWrapper.getContext(id, "notifyTableChanged").getContentResolver()
                    .notifyChange(SqlUtils.getNotificationUri(contentAuthority,
                            table, action, (SQLOperator[]) null, id), null, true);
        }
    }

    @Override
    public TableNotifierRegister newRegister() {
        return new FlowContentTableNotifierRegister(contentAuthority, id);
    }

    public static class FlowContentTableNotifierRegister implements TableNotifierRegister {
        private final FlowContentObserver flowContentObserver;
        private final String id;

        @Nullable
        private OnTableChangedListener tableChangedListener;

        public FlowContentTableNotifierRegister(@NonNull String contentAuthority, String id) {
            flowContentObserver = new FlowContentObserver(contentAuthority, id);
            flowContentObserver.addOnTableChangedListener(internalContentChangeListener);
            this.id = id;
        }

        @Override
        public <T> void register(@NonNull Class<T> tClass) {
            flowContentObserver.registerForContentChanges(FlowInstanceWrapper.getContext(id, "register"), tClass);
        }

        @Override
        public <T> void unregister(@NonNull Class<T> tClass) {
            flowContentObserver.unregisterForContentChanges(FlowInstanceWrapper.getContext(id, "unregister"));
        }

        @Override
        public void unregisterAll() {
            flowContentObserver.removeTableChangedListener(internalContentChangeListener);
            this.tableChangedListener = null;
        }

        @Override
        public void setListener(@Nullable OnTableChangedListener contentChangeListener) {
            this.tableChangedListener = contentChangeListener;
        }

        @Override
        public boolean isSubscribed() {
            return !flowContentObserver.isSubscribed();
        }

        private final OnTableChangedListener internalContentChangeListener
                = new OnTableChangedListener() {

            @Override
            public void onTableChanged(@Nullable Class<?> tableChanged, @NonNull BaseModel.Action action) {
                if (tableChangedListener != null) {
                    tableChangedListener.onTableChanged(tableChanged, action);
                }
            }
        };
    }
}
