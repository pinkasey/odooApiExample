package org.animalsnow.odoo

import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.beans.XMLEncoder
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.net.URL
import java.util.Collections.emptyList
import java.util.Collections.emptyMap


fun main(vararg args: String) {
    Main().start(
        args[0], //https://your.odoo.instance.org
        args[1], //your_db_name
        args[2], //username@example.org"
        args[3]  //password123
    )
}

class Main {

    fun start(
        odooUrl: String,
        db: String,
        username: String,
        password: String
    ) {
        val client = XmlRpcClient()

        val commonConfig = XmlRpcClientConfigImpl()

        commonConfig.serverURL = URL("$odooUrl/xmlrpc/2/common")

        val uid = client.execute(
            commonConfig, "authenticate", listOf(
                db, username, password, emptyMap<Any, Any>()
            )
        ) as Int

        println("uid: $uid")


        getFields(odooUrl, "/xmlrpc/2/object", db, uid, password, "./employee_fields_xmlrpc_2.xml")
        getFields(odooUrl, "/xmlrpc/object", db, uid, password, "./employee_fields_xmlrpc_1.xml")
    }

    private fun getFields(
        baseUrl: String,
        endpointUrl: String,
        db: String,
        uid: Int,
        password: String,
        filePath: String
    ) {
        val models: XmlRpcClient = object : XmlRpcClient() {
            init {
                setConfig(object : XmlRpcClientConfigImpl() {
                    init {
                        serverURL = URL("$baseUrl$endpointUrl")
                    }
                })
            }
        }

        val fieldsResponse = models.execute(
            "execute_kw", listOf(
                db, uid, password,
                "hr.employee", "fields_get",
                emptyList<String>(),
                mapOf("attributes" to listOf("string", "help", "type"))
            )
        )

        println("executeKwRes: $fieldsResponse")
        XMLEncoder(BufferedOutputStream(FileOutputStream(filePath))).use { xmlEncoder ->
            xmlEncoder.writeObject(fieldsResponse)
            xmlEncoder.flush()
        }
    }
}
