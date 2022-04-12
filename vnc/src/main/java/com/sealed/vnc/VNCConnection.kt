package com.sealed.vnc

import net.thecybershadow.vnc.ConnectionBean

class VNCConnection(private val address: String, private val port: Int) {

    val connectionBean = ConnectionBean().apply {
        address = this@VNCConnection.address
        port = this@VNCConnection.port
        nickname = "test"
        password = "gdc45^2wEdDghT67"
    }

}