package com.zybooks.studyhelper.repo

import android.content.Context
import com.zybooks.studyhelper.model.Question
import com.zybooks.studyhelper.model.Subject
import java.util.*

class StudyRepository private constructor(context: Context) {

    private val subjectList = mutableListOf<Subject>()
    private val questionMap = mutableMapOf<Long, MutableList<Question>>()

    companion object {
        private var instance: StudyRepository? = null

        fun getInstance(context: Context): StudyRepository {
            if (instance == null) {
                instance = StudyRepository(context)
            }
            return instance!!
        }
    }

    init {
        addStarterData()
    }

    fun addSubject(subject: Subject) {
        subjectList.add(subject)
        questionMap[subject.id] = mutableListOf()
    }

    fun getSubjects(): List<Subject> {
        return Collections.unmodifiableList(subjectList)
    }

    fun addQuestion(question: Question) {
        questionMap[question.subjectId]?.add(question)
    }

    fun getQuestions(subjectId: Long): List<Question> {
        return Collections.unmodifiableList(questionMap[subjectId]!!)
    }

    private fun addStarterData() {

        addSubject(Subject(1, "Math"))
        addQuestion(Question(1, "What is 2 + 3?", "2 + 3 = 5", 1))
        addQuestion(Question(2, "What is pi?",
            "The ratio of a circle's circumference to its diameter.", 1))

        addSubject(Subject(2, "History"))
        addQuestion(Question(3,
            "On what date was the U.S. Declaration of Independence adopted?",
            "July 4, 1776", 2))

        addSubject(Subject(3, "Computing"))
    }
}