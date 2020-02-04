package aust.fyp.learn.and.earn.Models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class EducationHistoryModel() : RealmObject() {

    @PrimaryKey
    var recordID: Int = -99
    var userID: Int = -99
    var degree_title: String = ""
    var description: String = ""

    constructor(
        _recordID: Int,
        _userID: Int,
        _degree_title: String,
        _description: String
    ) : this() {
        this.recordID = _recordID
        this.userID = _userID
        this.degree_title = _degree_title
        this.description = _description
    }

}