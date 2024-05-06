package com.zlatamigas.animind.controller.recyclerview

class AnimeRVModal {

    var id: Int = -1
    var title: String? = null
    var rating: String? = null
    var episodes: String? = null
    var preview: String? = null

    constructor(id: Int, title: String?, rating: String?, episodes: String?, preview: String?) {
        this.id = id
        this.title = title
        this.rating = rating
        this.episodes = episodes
        this.preview = preview
    }
}