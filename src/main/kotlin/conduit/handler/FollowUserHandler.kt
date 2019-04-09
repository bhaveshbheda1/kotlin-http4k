package conduit.handler

import conduit.model.Profile
import conduit.model.Username
import conduit.model.extractEmail
import conduit.repository.ConduitDatabase
import conduit.repository.OldRepo
import conduit.repository.UserNotFoundException
import conduit.util.HttpException
import conduit.util.TokenAuth
import org.http4k.core.Status

interface FollowUserHandler {
    operator fun invoke(username: Username, token: TokenAuth.TokenInfo): Profile
}

class FollowUserHandlerImpl(val database: ConduitDatabase) : FollowUserHandler {
    override fun invoke(username: Username, token: TokenAuth.TokenInfo) =
        database.tx {
            val targetUser = getUser(username) ?: throw HttpException(Status.NOT_FOUND, "$username not found.")
            val email = token.extractEmail()
            val sourceUser =
                getUser(email) ?: throw HttpException(Status.NOT_FOUND, "User with email $email not found.")
            val following = getFollowing(sourceUser.id, targetUser.id)
            if (!following) {
                insertFollowing(sourceUser.id, targetUser.id)
            }
            Profile(
                targetUser.username,
                targetUser.bio,
                targetUser.image,
                true
            )
        }
}