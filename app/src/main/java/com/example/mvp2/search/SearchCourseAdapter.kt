package com.example.mvp2.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvp2.databinding.CourseItemBinding
import com.example.mvp2.domain.Course

class SearchCourseAdapter(private val onClickListener: OnClickListener) : ListAdapter<Course,
        SearchCourseAdapter.MarsPropertyViewHolder>(DiffCallback),Filterable {

    var filteredList : List<Course>? = null
    var unFilteredList : List<Course>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCourseAdapter.MarsPropertyViewHolder {
        return MarsPropertyViewHolder(CourseItemBinding.inflate(
                LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: SearchCourseAdapter.MarsPropertyViewHolder, position: Int) {
        val marsProperty = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(marsProperty)
        }
        holder.bind(marsProperty)
    }

    override fun submitList(list: List<Course>?) {
        super.submitList(list)

        if (unFilteredList.isNullOrEmpty())
            unFilteredList = list
    }


    companion object DiffCallback : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.courseId == newItem.courseId
        }
    }


    class MarsPropertyViewHolder(private var binding:
                                 CourseItemBinding):
            RecyclerView.ViewHolder(binding.root) {

        fun bind(marsProperty: Course) {
            binding.property = marsProperty
            binding.executePendingBindings()
        }
    }

    class OnClickListener(val clickListener: (marsProperty:Course) -> Unit) {
        fun onClick(marsProperty:Course) = clickListener(marsProperty)
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(query: CharSequence?): FilterResults {
                Log.e("SearchAapter","PerformingFilter:"+query)
                if(query.isNullOrEmpty()){
                    Log.e("SearchAapter","empty query1 ${unFilteredList?.size} ${filteredList?.size}")
                    filteredList = unFilteredList
                    Log.e("SearchAapter","empty query2 ${unFilteredList?.size} ${filteredList?.size}")
                }
                else{
                    Log.e("SearchAapter","not empty query1 ${unFilteredList?.size} ${filteredList?.size}")
                    filteredList = unFilteredList?.filter { course ->
                        course.courseTitle.contains(query)
                    }
                    Log.e("SearchAapter","not empty query2 ${unFilteredList?.size} ${filteredList?.size}")
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(query: CharSequence?, results: FilterResults?) {
                Log.e("SearchAapter","PublishingResults")
                 filteredList = results?.values as List<Course>
                submitList(filteredList)
            }

        }
    }

}