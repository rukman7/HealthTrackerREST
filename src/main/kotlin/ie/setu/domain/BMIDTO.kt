package ie.setu.domain

import org.joda.time.DateTime

data class BMIDTO (var userId: Int,
                   var id: Int,
                   var timestamp: DateTime,
                   var description:String,
                   var height: Double,
                   var weight: Double,
                   var bmi: Int)