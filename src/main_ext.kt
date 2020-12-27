package com.utsman

import io.ktor.application.*

fun Application.logi(msg: String) = log.info(msg)
fun Application.logd(msg: String) = log.debug(msg)
