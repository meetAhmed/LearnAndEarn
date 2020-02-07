package aust.fyp.learn.and.earn.Models

import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class SubjectModel() : RealmObject() {
    var ID: Int = 0
    var subject_name: String = ""
    var description: String = ""
    var price_per_month: Double = 0.0
    var userID: Int = 0
    var category: String = ""
    var teacher_name: String = ""
    var profile_addresss: String = ""
}