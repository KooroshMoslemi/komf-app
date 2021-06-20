package com.example.mvp2.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.mvp2.database.VocabsDatabase
import com.example.mvp2.database.asDomainModel
import com.example.mvp2.domain.Vocab


class VocabsRepository(private val database: VocabsDatabase) {

    val vocabs: LiveData<List<Vocab>> = Transformations.map(database.vocabDao.getAllLessonVocabs()) {
        it.asDomainModel()
    }

//    suspend fun refreshVocabs() {
//        withContext(Dispatchers.IO) {
//            Log.d("repository","refresh vocabs is called");
//            val vocabs = Network.instance.getVocabs().await()
//            database.vocabDao.insertAll(vocabs.asDatabaseModel())
//        }
//    }
}