package conduit.handler

import conduit.model.*
import conduit.repository.ConduitDatabase
import conduit.util.HttpException
import conduit.util.generateToken
import conduit.util.hash
import org.http4k.core.Status

interface LoginHandler {
    operator fun invoke(loginUserDto: LoginUserDto): UserDto
}

class LoginHandlerImpl(val database: ConduitDatabase) : LoginHandler {
    override operator fun invoke(loginUserDto: LoginUserDto): UserDto {
        val user = database.tx {
            getUser(loginUserDto.email) ?: throw InvalidUserPassException() // TODO: Change exception
        }

        if (loginUserDto.password.hash() != user.password) throw InvalidUserPassException()

        val token = generateToken(user.username, user.email)

        return UserDto(
            user.email,
            token,
            user.username,
            user.bio,
            user.image
        )
    }
}

class InvalidUserPassException : HttpException(Status.UNAUTHORIZED, "Invalid username or password.")

data class LoginUserDto(val email: Email, val password: Password)
data class UserDto(val email: Email, val token: Token, val username: Username, val bio: Bio?, val image: Image?)
