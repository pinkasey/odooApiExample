package org.animalsnow.odoo;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;

final class MainJava {

    public static void main(String[] args) throws FileNotFoundException, XmlRpcException, MalformedURLException {
        new MainJava().start(
                args[0], //https://your.odoo.instance.org
                args[1], //your_db_name
                args[2], //username@example.org"
                args[3]  //password123
        );
    }
    
    private void start(
            String odooUrl,
            String db,
            String username,
            String password
    ) throws MalformedURLException, XmlRpcException, FileNotFoundException {
        XmlRpcClient client = new XmlRpcClient();
        XmlRpcClientConfigImpl commonConfig = new XmlRpcClientConfigImpl();
        commonConfig.setServerURL(new URL(odooUrl + "/xmlrpc/2/common"));
        Integer uid = (Integer) client.execute(commonConfig, "authenticate", asList(db, username, password, Collections.emptyMap()));

        System.out.println("uid: " + uid);
        this.getFields(odooUrl, "/xmlrpc/2/object", db, uid, password, "./employee_fields_xmlrpc_2.xml");
        this.getFields(odooUrl, "/xmlrpc/object", db, uid, password, "./employee_fields_xmlrpc_1.xml");
    }

    private void getFields(
            final String baseUrl,
            final String endpointUrl,
            String db,
            int uid,
            String password,
            String filePath
    ) throws MalformedURLException, XmlRpcException, FileNotFoundException {
        XmlRpcClient models = new XmlRpcClient() {
            {
                this.setConfig(new XmlRpcClientConfigImpl() {
                    {
                        this.setServerURL(new URL(baseUrl + endpointUrl));
                    }
                });
            }
        };

        Object fieldsResponse = models.execute("execute_kw", asList(db, uid, password, "hr.employee", "fields_get", Collections.emptyList(),
                new HashMap<String, List<String>>(){{
                    put("attributes", asList("string", "help", "type"));
                }}));
        String var9 = "executeKwRes: " + fieldsResponse;

        System.out.println(var9);
        try (
                XMLEncoder xmlEncoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filePath)))
        ) {
            xmlEncoder.writeObject(fieldsResponse);
            xmlEncoder.flush();
        }
    }
}
