# odooApiExample
Example of using Odoo API to retrieve fields of a model (Employee), written in Kotlin and translated to Java.

## Description
This example does the following:
1. get user-id from odoo instance
2. authenticate with password (password passed as argument to program)
3. calls `fields_get` on `hr.employee` to get fields, on endpoint `xmlrpc/2/object`
4. calls `fields_get` on `hr.employee` to get fields, on endpoint `xmlrpc/object`
5. the results are saved to `employee_fields_xmlrpc_2.xml` and `employee_fields_xmlrpc_1.xml`, respectively. You can find them in the project files - I've ran this on my own odoo.

## Usage:
It's best used from inside IntelliJ.
Simply run one of the Main functions (they do the same thing), and pass the 4 parameters in the run-configuration
