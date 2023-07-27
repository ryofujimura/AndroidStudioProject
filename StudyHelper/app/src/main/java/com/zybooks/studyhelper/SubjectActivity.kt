package com.zybooks.studyhelper

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zybooks.studyhelper.model.Subject
import com.zybooks.studyhelper.viewmodel.SubjectListViewModel

class SubjectActivity : AppCompatActivity(),
    SubjectDialogFragment.OnSubjectEnteredListener {

    private var subjectAdapter = SubjectAdapter(mutableListOf())
    private lateinit var subjectRecyclerView: RecyclerView
    private lateinit var subjectColors: IntArray
    private lateinit var subjectListViewModel: SubjectListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject)

        subjectListViewModel = SubjectListViewModel(application)

        subjectColors = resources.getIntArray(R.array.subjectColors)

        findViewById<FloatingActionButton>(R.id.add_subject_button).setOnClickListener {
            addSubjectClick() }

        subjectRecyclerView = findViewById(R.id.subject_recycler_view)
        subjectRecyclerView.layoutManager = GridLayoutManager(applicationContext, 2)

        // Show the subjects
        updateUI(subjectListViewModel.getSubjects())
    }

    private fun updateUI(subjectList: List<Subject>) {
        subjectAdapter = SubjectAdapter(subjectList as MutableList<Subject>)
        subjectRecyclerView.adapter = subjectAdapter
    }

    override fun onSubjectEntered(subjectText: String) {
        if (subjectText.isNotEmpty()) {
            val subject = Subject(0, subjectText)
            subjectListViewModel.addSubject(subject)
            updateUI(subjectListViewModel.getSubjects())

            Toast.makeText(this, "Added $subjectText", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addSubjectClick() {
        val dialog = SubjectDialogFragment()
        dialog.show(supportFragmentManager, "subjectDialog")
    }

    private inner class SubjectHolder(inflater: LayoutInflater, parent: ViewGroup?) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recycler_view_items, parent, false)),
        View.OnClickListener {

        private var subject: Subject? = null
        private val subjectTextView: TextView

        init {
            itemView.setOnClickListener(this)
            subjectTextView = itemView.findViewById(R.id.subject_text_view)
        }

        fun bind(subject: Subject, position: Int) {
            this.subject = subject
            subjectTextView.text = subject.text

            // Make the background color dependent on the length of the subject string
            val colorIndex = subject.text.length % subjectColors.size
            subjectTextView.setBackgroundColor(subjectColors[colorIndex])
        }

        override fun onClick(view: View) {
            // Start QuestionActivity with the selected subject
            val intent = Intent(this@SubjectActivity, QuestionActivity::class.java)
            intent.putExtra(QuestionActivity.EXTRA_SUBJECT_ID, subject!!.id)
            intent.putExtra(QuestionActivity.EXTRA_SUBJECT_TEXT, subject!!.text)

            startActivity(intent)
        }
    }

    private inner class SubjectAdapter(private val subjectList: MutableList<Subject>) :
        RecyclerView.Adapter<SubjectHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectHolder {
            val layoutInflater = LayoutInflater.from(applicationContext)
            return SubjectHolder(layoutInflater, parent)
        }

        override fun onBindViewHolder(holder: SubjectHolder, position: Int) {
            holder.bind(subjectList[position], position)
        }

        override fun getItemCount(): Int {
            return subjectList.size
        }
    }
}