package com.zybooks.studyhelper.viewmodel

import android.app.Application
import com.zybooks.studyhelper.model.Subject
import com.zybooks.studyhelper.repo.StudyRepository

class SubjectListViewModel(application: Application) {

    private val studyRepo = StudyRepository.getInstance(application.applicationContext)

    fun getSubjects(): List<Subject> = studyRepo.getSubjects()

    fun addSubject(subject: Subject) = studyRepo.addSubject(subject)
}