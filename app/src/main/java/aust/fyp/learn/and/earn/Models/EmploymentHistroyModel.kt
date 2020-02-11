package aust.fyp.learn.and.earn.Models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class EmploymentHistroyModel() : RealmObject() {

    @PrimaryKey
    var recordID: Int = -99
    var userID: Int = -99
    var work_as_a: String = ""
    var organization_name: String = ""
    var experience: String = ""




}