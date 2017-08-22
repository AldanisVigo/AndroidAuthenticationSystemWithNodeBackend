# AndroidAuthenticationSystemWithNodeBackend
This is an example of a simple authentication system using Android and a Node.js backend server and script. It takes in an email and password and then sends and authentication request to the server. The server looks for the credentials in a MySQL database and if the user exists allows the connection and sends back a success message. If it fails it send a failure message. This example could be made more secure by implementing a different hashing algorithm as well as introducing a SALT.


In order to use this, you have to do a few things. You have to make sure you have Node.js and NPM installed on your system. The server side script is included in the Node folder in the repository. 

The server side script depends on a MySQL databse which stores all the user information. There is a database.sql file inside of the Node folder that contains the minimal structure for the MySQL database. So make sure you have mysql installed on your system and run the command

mysql -u USERNAME -pPASSWORD < database.sql

That should get the database set up. Now you have to add a user to the users table so you can test the login mechanism. To do this:
Issue the following commands in MySQL

use AndroidLoginService;
INSERT INTO users(email,password) VALUE('YOUREMAIL@YOUREMAILPROVIDER.com',SHA('YOURPASSWORD'));

Make sure you change the email and password with your own.

The final setup thing you have to do is get the IP/Domain of the server that is running the Node backend script and add it to the Login.java file on line 35. This will tell the app where the server is so that it sends the authentication request to the right place. 

Now that all that is set up, all you have to do is compile and run the Android app and test it.

