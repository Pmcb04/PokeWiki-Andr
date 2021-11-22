package com.devgram.pokewiki.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.devgram.pokewiki.R
import com.devgram.pokewiki.listeners.SelectionListenerAchievement
import com.devgram.pokewiki.model.Achievement
import com.devgram.pokewiki.roomdb.AppDatabase
import com.devgram.pokewiki.util.AppExecutors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator


/**
 * A simple [Fragment] subclass.
 * Use the [AchievementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AchievementFragment() : Fragment() {

    private var mCallback: SelectionListenerAchievement? = null
    private lateinit var mRecyclerView : RecyclerView
    private lateinit var mAdapter : AchievementRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val v : View = inflater.inflate(R.layout.fragment_achievement, container, false)
        mRecyclerView = v.findViewById(R.id.achievement_list) as RecyclerView

        val db = AppDatabase.getInstance(requireContext())

        val achievementList: MutableList<Achievement> = mutableListOf()

        AppExecutors.instance!!.diskIO().execute {
//            db.getAchivementDao()?.insert(Achievement(1, "Prueba", "description", 100))
            val list = db.getAchievementDao().getAll()

            //insert into adapter list
            activity?.runOnUiThread { mAdapter.load(list) }
        }

        mAdapter = AchievementRecyclerViewAdapter(this, achievementList)
        mRecyclerView.adapter = mAdapter
        return v
    }

    class AchievementRecyclerViewAdapter(parent : AchievementFragment, items : MutableList<Achievement>)
        : RecyclerView.Adapter<AchievementRecyclerViewAdapter.AchievementViewHolder>() {

        private val mParentActivity: AchievementFragment = parent
        private var achievementList: MutableList<Achievement> = items
        private val mOnClickListener : View.OnClickListener = View.OnClickListener { view ->

            val item: Achievement = view.tag as Achievement
            mParentActivity.mCallback?.onListItemSelected(item)

            MaterialAlertDialogBuilder(parent.requireContext())
                .setMessage(item.description)
                .show()

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.achievement_list_content, parent, false)
            return AchievementViewHolder(view)
        }

        override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
            holder.name.text = achievementList[position].name
            holder.percentage.progress = achievementList[position].percentage
            holder.itemView.tag = achievementList[position]
            holder.itemView.setOnClickListener(mOnClickListener)
        }

        override fun getItemCount(): Int {
            return achievementList.size
        }

        inner class AchievementViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var name: TextView = view.findViewById<View>(R.id.name) as TextView
            var icon: ImageView = view.findViewById<View>(R.id.icon) as ImageView
            var percentage : CircularProgressIndicator = view.findViewById(R.id.progressBar)


        }

        fun load(list: MutableList<Achievement>) {
            achievementList.clear()
            achievementList = list
            notifyDataSetChanged()
        }


    }

}