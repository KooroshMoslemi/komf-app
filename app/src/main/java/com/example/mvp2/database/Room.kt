/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mvp2.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvp2.database.DatabaseVocab

@Dao
interface VocabDao {

    @Query("select * from databasevocab")
    fun getAllLessonVocabs(): LiveData<List<DatabaseVocab>>

    @Query("select * from databasevocab where lessonId = :lessonId and vocabId in (:vocabIds)")
    fun getInProgressLessonVocabs(lessonId:Long,vocabIds:List<Long>): LiveData<List<DatabaseVocab>>


    @Query("select * from databasevocab where lessonId = :lessonId")
    fun getCompletedLessonVocabs(lessonId:Long): LiveData<List<DatabaseVocab>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( vocabs: List<DatabaseVocab>)
}


@Database(entities = [DatabaseVocab::class], version = 1)
abstract class VocabsDatabase: RoomDatabase() {
    abstract val vocabDao: VocabDao
}



private lateinit var INSTANCE: VocabsDatabase
fun getDatabase(context: Context): VocabsDatabase {
    synchronized(VocabsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                VocabsDatabase::class.java,
                    "vocabs").build()
        }
    }
    return INSTANCE
}
