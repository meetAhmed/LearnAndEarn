package aust.fyp.learn.and.earn.Models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class ChatHeadModel() : RealmObject() {

    @PrimaryKey
    var ID: Int = -99
    var sender_id: Int = -99
    var receiver_id: Int = -99
    var message: String = ""
    var date: Long = 0L
    var sender_name = ""
    var receiver_name = ""
    var sender_profile = ""
    var receiver_profile = ""
}