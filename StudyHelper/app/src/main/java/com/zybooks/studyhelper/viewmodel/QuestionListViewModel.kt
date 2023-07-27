package com.zybooks.studyhelper.viewmodel

import android.app.Application
import com.zybooks.studyhelper.model.Question
import com.zybooks.studyhelper.repo.StudyRepository

class QuestionListViewModel(application: Application) {

    private val studyRepo = StudyRepository.getInstance(application.applicationContext)

    fun getQuestions(subjectId: Long): List<Question> = studyRepo.getQuestions(subjectId)
}
