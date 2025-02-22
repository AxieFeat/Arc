package arc.discord.entity

import org.json.JSONArray
import org.json.JSONObject
import java.time.OffsetDateTime

/**
 * An encapsulation of all data needed to properly construct a JSON RichPresence payload.
 *
 *
 * These can be built using [RichPresence.Builder].
 */
class RichPresence(
    private val state: String?,
    private val details: String?,
    private val startTimestamp: OffsetDateTime?,
    private val endTimestamp: OffsetDateTime?,
    private val largeImageKey: String?,
    private val largeImageText: String?,
    private val smallImageKey: String?,
    private val smallImageText: String?,
    private val partyId: String?,
    private val partySize: Int,
    private val partyMax: Int,
    private val matchSecret: String?,
    private val joinSecret: String?,
    private val spectateSecret: String?,
    private val instance: Boolean
) {
    /**
     * Constructs a [JSONObject] representing a payload to send to discord
     * to update a user's Rich Presence.
     *
     *
     * This is purely internal, and should not ever need to be called outside of
     * the library.
     *
     * @return A JSONObject payload for updating a user's Rich Presence.
     */
    fun toJson(): JSONObject {
        return JSONObject()
            .put("state", state)
            .put("details", details)
            .put(
                "timestamps", JSONObject()
                    .put("start", startTimestamp?.toEpochSecond())
                    .put("end", endTimestamp?.toEpochSecond())
            )
            .put(
                "assets", JSONObject()
                    .put("large_image", largeImageKey)
                    .put("large_text", largeImageText)
                    .put("small_image", smallImageKey)
                    .put("small_text", smallImageText)
            )
            .put(
                "party", if (partyId == null) null else JSONObject()
                    .put("id", partyId)
                    .put("size", JSONArray().put(partySize).put(partyMax))
            )
            .put(
                "secrets", JSONObject()
                    .put("join", joinSecret)
                    .put("spectate", spectateSecret)
                    .put("match", matchSecret)
            )
            .put("instance", instance)
    }

    /**
     * A chain builder for a [RichPresence] object.
     *
     *
     * An accurate description of each field and it's functions can be found
     * [here](https://discordapp.com/developers/docs/rich-presence/how-to#updating-presence-update-presence-payload-fields)
     */
    class Builder {
        private var state: String? = null
        private var details: String? = null
        private var startTimestamp: OffsetDateTime? = null
        private var endTimestamp: OffsetDateTime? = null
        private var largeImageKey: String? = null
        private var largeImageText: String? = null
        private var smallImageKey: String? = null
        private var smallImageText: String? = null
        private var partyId: String? = null
        private var partySize = 0
        private var partyMax = 0
        private var matchSecret: String? = null
        private var joinSecret: String? = null
        private var spectateSecret: String? = null
        private var instance = false

        /**
         * Builds the [RichPresence] from the current state of this builder.
         *
         * @return The RichPresence built.
         */
        fun build(): RichPresence {
            return RichPresence(
                state, details, startTimestamp, endTimestamp,
                largeImageKey, largeImageText, smallImageKey, smallImageText,
                partyId, partySize, partyMax, matchSecret, joinSecret,
                spectateSecret, instance
            )
        }

        /**
         * Sets the state of the user's current party.
         *
         * @param state The state of the user's current party.
         *
         * @return This Builder.
         */
        fun setState(state: String?): Builder {
            this.state = state
            return this
        }

        /**
         * Sets details of what the player is currently doing.
         *
         * @param details The details of what the player is currently doing.
         *
         * @return This Builder.
         */
        fun setDetails(details: String?): Builder {
            this.details = details
            return this
        }

        /**
         * Sets the time that the player started a match or activity.
         *
         * @param startTimestamp The time the player started a match or activity.
         *
         * @return This Builder.
         */
        fun setStartTimestamp(startTimestamp: OffsetDateTime?): Builder {
            this.startTimestamp = startTimestamp
            return this
        }

        /**
         * Sets the time that the player's current activity will end.
         *
         * @param endTimestamp The time the player's activity will end.
         *
         * @return This Builder.
         */
        fun setEndTimestamp(endTimestamp: OffsetDateTime?): Builder {
            this.endTimestamp = endTimestamp
            return this
        }

        /**
         * Sets the key of the uploaded image for the large profile artwork, as well as
         * the text tooltip shown when a cursor hovers over it.
         *
         *
         * These can be configured in the [applications](https://discordapp.com/developers/applications/me)
         * page on the discord website.
         *
         * @param largeImageKey A key to an image to display.
         * @param largeImageText Text displayed when a cursor hovers over the large image.
         *
         * @return This Builder.
         */
        fun setLargeImage(largeImageKey: String?, largeImageText: String?): Builder {
            this.largeImageKey = largeImageKey
            this.largeImageText = largeImageText
            return this
        }

        /**
         * Sets the key of the uploaded image for the large profile artwork.
         *
         *
         * These can be configured in the [applications](https://discordapp.com/developers/applications/me)
         * page on the discord website.
         *
         * @param largeImageKey A key to an image to display.
         *
         * @return This Builder.
         */
        fun setLargeImage(largeImageKey: String?): Builder {
            return setLargeImage(largeImageKey, null)
        }

        /**
         * Sets the key of the uploaded image for the small profile artwork, as well as
         * the text tooltip shown when a cursor hovers over it.
         *
         *
         * These can be configured in the [applications](https://discordapp.com/developers/applications/me)
         * page on the discord website.
         *
         * @param smallImageKey A key to an image to display.
         * @param smallImageText Text displayed when a cursor hovers over the small image.
         *
         * @return This Builder.
         */
        fun setSmallImage(smallImageKey: String?, smallImageText: String?): Builder {
            this.smallImageKey = smallImageKey
            this.smallImageText = smallImageText
            return this
        }

        /**
         * Sets the key of the uploaded image for the small profile artwork.
         *
         *
         * These can be configured in the [applications](https://discordapp.com/developers/applications/me)
         * page on the discord website.
         *
         * @param smallImageKey A key to an image to display.
         *
         * @return This Builder.
         */
        fun setSmallImage(smallImageKey: String?): Builder {
            return setSmallImage(smallImageKey, null)
        }

        /**
         * Sets party configurations for a team, lobby, or other form of group.
         *
         *
         * The `partyId` is ID of the player's party.
         * <br></br>The `partySize` is the current size of the player's party.
         * <br></br>The `partyMax` is the maximum number of player's allowed in the party.
         *
         * @param partyId The ID of the player's party.
         * @param partySize The current size of the player's party.
         * @param partyMax The maximum number of player's allowed in the party.
         *
         * @return This Builder.
         */
        fun setParty(partyId: String?, partySize: Int, partyMax: Int): Builder {
            this.partyId = partyId
            this.partySize = partySize
            this.partyMax = partyMax
            return this
        }

        /**
         * Sets the unique hashed string for Spectate and Join.
         *
         * @param matchSecret The unique hashed string for Spectate and Join.
         *
         * @return This Builder.
         */
        fun setMatchSecret(matchSecret: String?): Builder {
            this.matchSecret = matchSecret
            return this
        }

        /**
         * Sets the unique hashed string for chat invitations and Ask to Join.
         *
         * @param joinSecret The unique hashed string for chat invitations and Ask to Join.
         *
         * @return This Builder.
         */
        fun setJoinSecret(joinSecret: String?): Builder {
            this.joinSecret = joinSecret
            return this
        }

        /**
         * Sets the unique hashed string for Spectate button.
         *
         * @param spectateSecret The unique hashed string for Spectate button.
         *
         * @return This Builder.
         */
        fun setSpectateSecret(spectateSecret: String?): Builder {
            this.spectateSecret = spectateSecret
            return this
        }

        /**
         * Marks the [matchSecret][.setMatchSecret] as a game
         * session with a specific beginning and end.
         *
         * @param instance Whether or not the `matchSecret` is a game
         * with a specific beginning and end.
         *
         * @return This Builder.
         */
        fun setInstance(instance: Boolean): Builder {
            this.instance = instance
            return this
        }
    }
}