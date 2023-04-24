package org.reckful.archive.twitter.model

data class TwitterArchive(
    val profile: Profile,
    val postUsers: List<User>,
    val posts: List<Post>
)
