package aust.fyp.learn.and.earn.Models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class MessageModel() : RealmObject() {

    @PrimaryKey
    var ID: Int = -99
    var sender_id: Int = -99
    var receiver_id: Int = -99
    var message: String = ""
    var date: Long = 0L

}