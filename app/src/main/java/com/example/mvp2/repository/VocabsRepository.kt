package com.example.mvp2.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.mvp2.database.VocabsDatabase
import com.example.mvp2.database.asDomainModel
import com.example.mvp2.domain.Vocab
import com.example.mvp2.network.Network
import com.example.mvp2.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VocabsRepository(private val database: VocabsDatabase) {

    val vocabs: LiveData<List<Vocab>> = Transformations.map(database.vocabDao.getAllLessonVocabs()) {
        it.asDomainModel()
    }

    suspend fun refreshVocabs() {
        withContext(Dispatchers.IO) {
            Log.d("repository","refresh vocabs is called");
            val vocabs = Network.instance.getVocabs().await()
            database.vocabDao.insertAll(vocabs.asDatabaseModel())
        }
    }
}