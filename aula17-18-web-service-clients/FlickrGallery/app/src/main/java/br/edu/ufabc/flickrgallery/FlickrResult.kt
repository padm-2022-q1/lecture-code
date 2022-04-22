package br.edu.ufabc.flickrgallery

typealias Photos = List<FlickrResult.Item>

data class FlickrResult(
    val title: String,
    val link: String,
    val description: String,
    val modified: String,
    val generator: String,
    val items: Photos
) {
    data class Item(
        val title: String,
        val link: String,
        val media: Media,
        val date_taken: String,
        val description: String,
        val published: String,
        val author: String,
        val author_id: String,
        val tags: String
    ) {
        data class Media(val m: String)
    }
}



