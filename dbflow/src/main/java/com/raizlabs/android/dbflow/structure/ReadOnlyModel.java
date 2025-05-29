package com.raizlabs.android.dbflow.structure;

import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.migration.Migration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

public interface ReadOnlyModel {

    /**
     * Loads from the database the most recent version of the model based on it's primary keys.
     */
    void load();
    default void load(String id) {
        throw new IllegalArgumentException("The default function ReadOnlyModel::load shouldn't be called");
    }

    /**
     * Loads from the database the most recent version of the model based on it's primary keys.
     *
     * @param wrapper Database object to use. Useful for {@link Migration} classes.
     */
    void load(@NonNull DatabaseWrapper wrapper, @NonNull final String id);

    /**
     * @return true if this object exists in the DB. It combines all of it's primary key fields
     * into a SELECT query and checks to see if any results occur.
     */
    boolean exists();
    default boolean exists(String id) {
        throw new IllegalArgumentException("The default function ReadOnlyModel::exists shouldn't be called");
    }

    /**
     * @param wrapper Database object to use. Useful for {@link Migration} classes.
     * @return true if this object exists in the DB. It combines all of it's primary key fields
     * into a SELECT query and checks to see if any results occur.
     */
    boolean exists(@NonNull DatabaseWrapper wrapper, @NonNull final String id);
}
