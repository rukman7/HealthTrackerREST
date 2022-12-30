package ie.setu.controllers

import ie.setu.domain.UserDTO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*

private const val USER_ID = "user-id"

object UserController {

    private val userDao = UserDAO()

    @OpenApi(
        summary = "Get all users",
        operationId = "getAllUsers",
        tags = ["User"],
        path = "/api/users",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<UserDTO>::class)])]
    )
    fun getAllUsers(ctx: Context) {
        val users = userDao.getAll()
        if (users.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(users)
    }

    @OpenApi(
        summary = "Get user by ID",
        operationId = "getUserById",
        tags = ["User"],
        path = "/api/users/{user-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam(USER_ID, Int::class, "The user ID")],
        responses = [OpenApiResponse("200", [OpenApiContent(UserDTO::class)])]
    )
    fun getUserByUserId(ctx: Context) {
        val user = userDao.findById(ctx.pathParam(USER_ID).toInt())
        if (user != null) {
            ctx.json(user)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Get user by Email",
        operationId = "getUserByEmail",
        tags = ["User"],
        path = "/api/users/email/{email}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("email", Int::class, "The user email")],
        responses = [OpenApiResponse("200", [OpenApiContent(UserDTO::class)])]
    )
    fun getUserByEmail(ctx: Context) {
        val user = userDao.findByEmail(ctx.pathParam("email"))
        if (user != null) {
            ctx.json(user)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Add User",
        operationId = "addUser",
        tags = ["User"],
        path = "/api/users",
        method = HttpMethod.POST,
        pathParams = [OpenApiParam(USER_ID, Int::class, "The user ID")],
        responses = [OpenApiResponse("200")]
    )
    fun addUser(ctx: Context) {
        val userDTO: UserDTO = jsonToObject(ctx.body())
        val userId = userDao.save(userDTO)
        if (userId != null) {
            userDTO.id = userId
            ctx.json(userDTO)
            ctx.status(201)
        }
    }

    @OpenApi(
        summary = "Delete user by ID",
        operationId = "deleteUserById",
        tags = ["User"],
        path = "/api/users/{user-id}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam(USER_ID, Int::class, "The user ID")],
        responses = [OpenApiResponse("204")]
    )
    fun deleteUser(ctx: Context) {
        if (userDao.delete(ctx.pathParam(USER_ID).toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Update user by ID",
        operationId = "updateUserById",
        tags = ["User"],
        path = "/api/users/{user-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam(USER_ID, Int::class, "The user ID")],
        responses = [OpenApiResponse("204")]
    )
    fun updateUser(ctx: Context) {
        val foundUserDTO: UserDTO = jsonToObject(ctx.body())
        if ((userDao.update(id = ctx.pathParam(USER_ID).toInt(), userDTO = foundUserDTO)) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}
