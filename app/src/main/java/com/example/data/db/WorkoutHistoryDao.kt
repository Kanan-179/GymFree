package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutHistoryDao {
    @Query("SELECT * FROM workout_history ORDER BY completedTimestamp DESC")
    fun getAllHistory(): Flow<List<WorkoutHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(item: WorkoutHistoryEntity)

    @Query("DELETE FROM workout_history WHERE id = :id")
    suspend fun deleteHistory(id: Long)

    @Query("SELECT COUNT(*) FROM workout_history")
    fun getTotalWorkoutsCount(): Flow<Int>
}
