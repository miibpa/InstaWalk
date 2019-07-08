package org.mibanez.instawalk.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flickrPhotosUrl")
data class FlickrPhotoEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var url: String
)