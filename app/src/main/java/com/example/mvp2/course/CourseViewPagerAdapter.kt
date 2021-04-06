package com.example.mvp2.course

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvp2.databinding.PagerItemBinding
import com.example.mvp2.domain.Course

class CourseViewPagerAdapter(private val onClickListener: OnClickListener) : ListAdapter<Course,
        CourseViewPagerAdapter.CoursePagerViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewPagerAdapter.CoursePagerViewHolder {



        val inflater = LayoutInflater.from(parent.context)


        val itemPagerCardBinding: PagerItemBinding =
            PagerItemBinding.inflate(inflater, parent, false)

//        return CoursePagerViewHolder(
//            PagerItemBinding.inflate(
//                LayoutInflater.from(parent.context)
//            )
//        )
        return CoursePagerViewHolder(itemPagerCardBinding)
    }

    override fun onBindViewHolder(
        holder: CourseViewPagerAdapter.CoursePagerViewHolder,
        position: Int
    ) {
        val courseProperty = getItem(position)
        holder.binding.cardViewCourse.setOnClickListener {
            onClickListener.onClick(courseProperty)
        }
        holder.bind(courseProperty)
    }


    companion object DiffCallback : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.courseId == newItem.courseId
        }
    }


    class CoursePagerViewHolder(
        var binding:
        PagerItemBinding
    ):
        RecyclerView.ViewHolder(binding.root) {


        fun bind(courseProperty: Course) {
            binding.property = courseProperty
            binding.executePendingBindings()
        }
    }

    class OnClickListener(val clickListener: (courseProperty: Course) -> Unit) {
        fun onClick(courseProperty: Course) = clickListener(courseProperty)
    }

}