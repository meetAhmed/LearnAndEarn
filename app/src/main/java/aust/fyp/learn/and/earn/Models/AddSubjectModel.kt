package aust.fyp.learn.and.earn.Models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class AddSubjectModel() : RealmObject() {

    @PrimaryKey
    var recordID: Int = -99
    var userID: Int = -99
    var subject_name: String = ""
    var description: String = ""
    var price_per_month: String = ""
}

