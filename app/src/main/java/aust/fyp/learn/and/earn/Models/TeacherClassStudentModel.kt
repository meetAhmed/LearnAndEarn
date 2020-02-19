package aust.fyp.learn.and.earn.Models

import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class TeacherClassStudentModel : RealmObject() {

    var subject_id: Int = 0
    var subject_name : String = ""
    var student_id: Int = 0
    var student_name: String = ""

}