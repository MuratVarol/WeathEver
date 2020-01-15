package com.varol.weathever.data.local.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import io.reactivex.Single

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<T>): Single<List<Long>>

    @Update
    fun update(obj: T): Single<Int>

    @Delete
    fun delete(obj: T): Single<Int>

}