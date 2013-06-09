package com.traderapist.models

class FantasyPoints {

    Integer season
    Integer week
    Double points
    String system
	Boolean projection

    static belongsTo = [player: Player]

    static constraints = {
        season nullable: false
        week nullable: false
        week range: -1..17
        points nullable: false
        system blank: false
	    projection nullable: true
    }

    static mapping = {
        cache true
        table "fantasy_points"
        version false
    }
}
