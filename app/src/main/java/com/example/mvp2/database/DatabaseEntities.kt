package com.example.mvp2.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mvp2.domain.Vocab

@Entity(tableName = "databasevocab")
data class DatabaseVocab constructor(
    @PrimaryKey
    val vocabId:Long,
    val lessonId:Long,
    val word:String,
    val syn:String,
    val def:String,
    val ex1:String,
    val ex2:String
)


fun List<DatabaseVocab>.asDomainModel(): List<Vocab> {
    return map {
        Vocab(
            vocabId = it.vocabId,
            lessonId = it.lessonId,
            word = it.word,
            syn = it.syn,
            def = it.def,
            ex1 = it.ex1,
            ex2 = it.ex2)
    }
}
