package com.ground0.chumbucket

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.ext.web.Router
import com.rabbitmq.client.ConnectionFactory



class MainVerticle : AbstractVerticle() {

    companion object {
        const val QUEUE_NAME = "test_queue"
    }

    @Throws(Exception::class)
    override fun start(future: Future<Void>) {

        val router = Router.router(vertx)

        router.route("/foo").handler {
            it.response().putHeader("content-type", "text/html")
                .end("<h1>FOO FIGHTERS!</h1>")
        }

        router.route("/send").handler{
            val factory = ConnectionFactory()
            factory.host = "localhost"
            val connection = factory.newConnection()
            val channel = connection.createChannel()

            channel.queueDeclare(QUEUE_NAME, false, false, false, null)
            val message = "Hello World!"
            channel.basicPublish("", QUEUE_NAME, null, message.toByteArray())
            println(" [x] Sent '$message'")


            channel.close()
            connection.close()

            it.response().putHeader("content-type", "text/html").end("<h1>Done</h1>")
        }

        vertx.createHttpServer()
            .requestHandler { router.accept(it) }
            .listen(
                8080,
                { if (it.succeeded()) future.complete() else future.fail(it.cause()) }
            )
    }


}
