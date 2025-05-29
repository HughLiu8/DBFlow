package com.raizlabs.android.dbflow.runtime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.raizlabs.android.dbflow.DbFlowDependencyHelper;
import com.raizlabs.android.dbflow.config.FlowInstanceWrapper;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

/**
 * Description: Distributes notifications to the {@link ModelNotifier}.
 */
public class NotifyDistributor implements ModelNotifier {

    private static NotifyDistributor distributor;

    @NonNull
    public static NotifyDistributor get() {
        if (distributor == null) {
            distributor = new NotifyDistributor();
        }
        return distributor;
    }

    @Override
    public TableNotifierRegister newRegister() {
        throw new RuntimeException("Cannot create a register from the distributor class");
    }

    @Override
    public <TModel> void notifyModelChanged(@NonNull TModel model,
                                            @NonNull ModelAdapter<TModel> adapter,
                                            @NonNull BaseModel.Action action) {
        final String id = adapter.getDatabaseId();
        FlowInstanceWrapper.getModelNotifierForTable(id, adapter.getModelClass(), "notifyModelChanged")
            .notifyModelChanged(model, adapter, action);
    }

    /**
     * Notifies listeners of table-level changes from the SQLite-wrapper language.
     */
    @Override
    public <TModel> void notifyTableChanged(@NonNull Class<TModel> table,
                                            @NonNull BaseModel.Action action,
                                            @NonNull String id) {
        FlowInstanceWrapper.getModelNotifierForTable(id, table, "notifyTableChanged").notifyTableChanged(table, action, id);
    }
}
