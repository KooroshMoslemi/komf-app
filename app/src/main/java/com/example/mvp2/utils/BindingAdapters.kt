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

package com.example.mvp2.utils

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mvp2.R
import com.example.mvp2.domain.Course
import com.example.mvp2.domain.Vocab
import com.example.mvp2.lesson.LessonStatus
import com.example.mvp2.network.ApiStatus
import com.example.mvp2.search.SearchCourseAdapter

/**
 * Binding adapter used to hide the spinner once data is available.
 */
//@BindingAdapter("isNetworkError", "playlist")
//fun hideIfNetworkError(view: View, isNetWorkError: Boolean, videos: LiveData<List<Vocab>>) {
//    view.visibility = if (videos.value?.isNotEmpty() == true) View.GONE else View.VISIBLE
//    Log.e("binding1",view.visibility.toString())
//    if(isNetWorkError) {
//        view.visibility = View.GONE
//        Log.e("binding2",view.visibility.toString())
//    }
//}


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri =
            imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView,
                     data: List<Course>?) {
    val adapter = recyclerView.adapter as SearchCourseAdapter
    adapter.submitList(data)
}


@BindingAdapter("apiStatus")
fun bindStatus(statusImageView: ImageView,
               status: ApiStatus?) {

    when (status) {
        ApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        ApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        ApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}


@BindingAdapter("boldWord")
fun bindQuestionText(questionTextView: TextView,
               text: String?) {

    questionTextView.setText(makeSpannable(text,"<b>(.+?)</b>", "<b>", "</b>"))
}


@BindingAdapter("flashStatus")
fun flashcardControlButtonsStatus(statusButton: Button,
               status: LessonStatus?) {

    when(statusButton.contentDescription){
        "prev","next"->{
            when(status){
                LessonStatus.ACTIVE->{
                    statusButton.visibility = View.INVISIBLE
                }
                LessonStatus.COMPLETED->{
                    statusButton.visibility = View.VISIBLE
                }
                else -> Log.e("binding flash","Unexpected behaviour")
            }
        }
        "cross","check"->{
            when(status){
                LessonStatus.ACTIVE->{
                    statusButton.visibility = View.VISIBLE
                }
                LessonStatus.COMPLETED->{
                    statusButton.visibility = View.INVISIBLE
                }
                else -> Log.e("binding flash","Unexpected behaviour")
            }
        }
    }
}
