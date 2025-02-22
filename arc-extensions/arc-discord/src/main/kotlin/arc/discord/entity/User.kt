package arc.discord.entity

data class User(
    val name: String,
    val discriminator: String,
    val id: Long,
    val avatar: String?
) {

    val avatarUrl: String?
        get() = if (avatar == null) null else ("https://cdn.discordapp.com/avatars/$id/$avatar"
                + (if (avatar.startsWith("a_")) ".gif" else ".png"))

    val effectiveAvatarUrl: String?
        get() = if(avatarUrl == null) defaultAvatarUrl else avatarUrl

    val defaultAvatarId: String
        get() = DefaultAvatar.entries[discriminator.toInt() % DefaultAvatar.entries.size]
            .toString()

    val defaultAvatarUrl: String
        get() = "https://discordapp.com/assets/$defaultAvatarId.png"


    fun getAsMention(): String {
        return "<@$id>"
    }

    override fun toString(): String {
        return "U:$name($id)"
    }

    enum class DefaultAvatar(private val text: String) {

        BLURPLE("6debd47ed13483642cf09e832ed0bc1b"),
        GREY("322c936a8c8be1b803cd94861bdfa868"),
        GREEN("dd4dbc0016779df1378e7812eabaa04d"),
        ORANGE("0e291f67c9274a1abdddeb3fd919cbaa"),
        RED("1cbd08c76f8af6dddce02c5138971129");

        override fun toString(): String {
            return text
        }
    }
}