package aust.fyp.learn.and.earn.Models

import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class StudentRegisteredCourses : RealmObject() {

    var subject_id: Int = 0
    var subject_name: String = ""
    var teacher_id: Int = 0
    var teacher_name: String = ""

}