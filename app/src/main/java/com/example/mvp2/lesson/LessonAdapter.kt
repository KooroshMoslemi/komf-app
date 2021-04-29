package com.example.mvp2.lesson

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mvp2.R
import com.example.mvp2.course.CourseViewPagerAdapter
import com.example.mvp2.domain.Course
import com.example.mvp2.domain.Lesson
import com.github.vipulasri.timelineview.TimelineView
import com.github.vipulasri.timelineview.sample.utils.VectorDrawableUtils
import kotlinx.android.synthetic.main.lesson_item.view.*

class LessonAdapter(private val mFeedList: List<Lesson>,private val onClickListener: LessonAdapter.OnClickListener) : RecyclerView.Adapter<LessonAdapter.TimeLineViewHolder>() {

    private lateinit var mLayoutInflater: LayoutInflater

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {

        if(!::mLayoutInflater.isInitialized) {
            mLayoutInflater = LayoutInflater.from(parent.context)
        }

        return TimeLineViewHolder(mLayoutInflater.inflate(R.layout.lesson_item, parent, false), viewType)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {

        val timeLineModel = mFeedList[position]

        when {
            timeLineModel.lessonStatus == LessonStatus.INACTIVE -> {
                setMarker(holder, R.drawable.ic_marker_inactive, R.color.colorPrimary)
            }
            timeLineModel.lessonStatus == LessonStatus.ACTIVE -> {
                setMarker(holder, R.drawable.ic_marker_active, R.color.colorPrimary)
                holder.progressbar.visibility = View.VISIBLE
            }
            else -> {
                setMarker(holder, R.drawable.ic_marker, R.color.colorPrimary)
            }
        }
        holder.title.text = timeLineModel.lessonTitle
        holder.progressbar.progress = timeLineModel.lessonProgress
        holder.lessonItem.setOnClickListener { view->
            if (timeLineModel.lessonStatus != LessonStatus.INACTIVE)
            {
                onClickListener.onClick(timeLineModel)
//                Log.e("before navigation","${timeLineModel.remainedVocabIds.size}")
//                view.findNavController().navigate(LessonFragmentDirections.actionLessonFragmentToFlashcardFragment(timeLineModel))
            }
        }
    }

    private fun setMarker(holder: TimeLineViewHolder, drawableResId: Int, colorFilter: Int) {
        holder.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, drawableResId, ContextCompat.getColor(holder.itemView.context, colorFilter))
    }

    override fun getItemCount() = mFeedList.size

    inner class TimeLineViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {

        val title = itemView.lesson_title
        val progressbar = itemView.lesson_progress
        val timeline = itemView.timeline
        val lessonItem = itemView.lesson_item

        init {
//            timeline.markerSize = 70
//            timeline.lineWidth = 20
            timeline.initLine(viewType)
        }
    }

    class OnClickListener(val clickListener: (lessonProperty: Lesson) -> Unit) {
        fun onClick(lessonProperty: Lesson) = clickListener(lessonProperty)
    }

}
