package com.ground0.chumbucket

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.ext.web.Router

class MainVerticle : AbstractVerticle() {

    @Throws(Exception::class)
    override fun start(future: Future<Void>) {

        val router = Router.router(vertx)

        router.route("/foo").handler {
            it.response().putHeader("content-type", "text/html")
                .end("<h1>FOO FIGHTERS!</h1>")
        }
        vertx.createHttpServer()
            .requestHandler { router.accept(it) }
            .listen(
                8080,
                { if (it.succeeded()) future.complete() else future.fail(it.cause()) }
            )
    }


}
