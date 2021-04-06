package com.example.mvp2.domain

data class Vocab(val vocabId:Long,
                 val lessonId:Long,
                 val word:String,
                 val syn:String,
                 val def:String,
                 val ex1:String,
                 val ex2:String
                )