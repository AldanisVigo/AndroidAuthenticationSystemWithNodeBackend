/*
    Author: Aldanis Vigo <aldanisvigo@gmail.com>
    Date: Tuesday, August 22, 2017
    Description : This example show's how to create an activity that uses a REST API in the backend
    to authenticate users using encryption to prevent MITM attacks. This example could be
    greatly improved with the implementation of a SALT for the hashed SHA1 version of the
    password before it get's sent to the server for authentication. The backend is contained in
    a folder named Node. You need Node.JS to run the backend on your system.
 */
package com.example.danny.codedgui;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Created by danny on 8/21/17.
 */
public class LoginAuthenticationRequest extends AsyncTask<String, Void, String> {
    //Global variables
    //private static final String USER_AGENT = "Mozilla/5.0";
    protected Context context = null;
    protected String serverIp = null;
    LoginAuthenticationRequest(Context context,String serverAddress){
        context = context;
        serverIp = serverAddress;
    }
    /*
        All the background work for the asynchronous task will be done in this method.
     */
    @Override
    protected String doInBackground(String... credentials){
        //Variable to store the hashed password
        String passwd = "";
        try {
            //Hash the password before sending the request
            passwd = sha1(credentials[1]);
            //Create the GET request URL
            String url = "http://" + serverIp + ":3000/?email=" + credentials[0] + "&pass=" + passwd;
            //Send the request and return the resulting JSON formatted string.
            System.out.println(url);
            return sendGETRequest(url);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        //If the try/catch fails then send back a generic error message
        return "{authcode:'0',authmsg:'There were errors connecting to the server.'}";
    }
    /*
       Perform the authentication request using a GET request.
    */
    private static String sendGETRequest(String url) throws IOException {
        //Create a URL object holding the address to the server
        URL server = new URL(url);
        //Cast it to an HttpURLConnection object and open the connection to the server
        HttpURLConnection connection = (HttpURLConnection) server.openConnection();
        //Set the request method to GET
        connection.setRequestMethod("GET");
        //con.setRequestProperty("User-Agent", USER_AGENT);
        //Get the response code from sending the request
        int responseCode = connection.getResponseCode();
        //System.out.println("GET Response Code :: " + responseCode);
        //If the request was sent successfully
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            //Create a BufferedReader to read the server's response
            BufferedReader responseReader = new BufferedReader(
                    new InputStreamReader(
                            //Set it's input stream to the input stream of the connection
                            connection.getInputStream()
                    )
            );
            //Create a StringBuffer and String to read in the server's response
            String serverResponse;
            StringBuffer response = new StringBuffer();
            //Read the response from the server using the responseReader and append it in serverResponse
            while ((serverResponse = responseReader.readLine()) != null) {
                response.append(serverResponse);
            }
            //Close the responseReader BufferedReader object
            responseReader.close();
            //Return the JSON formatted response to the Login activity
            return response.toString();
        } else {
            //If the response from the server was not OK then show a generic error to the user
            return "{authcode:'0',authmsg:'Error while sending authentication request to the server.'}";
        }
    }

    /*
        Perform the authentication request using a POST request.
     */
    private static String sendPOSTRequest(String url,String params) throws IOException {
        //Create a URL object holding the address to the server
        URL server = new URL(url);
        //Cast it to an HttpURLConnection object and open the connection to it
        //This will throw an exception if the connection fails with the server
        HttpURLConnection connection = (HttpURLConnection) server.openConnection();
        //Set the request method to POST
        connection.setRequestMethod("POST");
        //con.setRequestProperty("User-Agent", USER_AGENT);
        //These are required to do a POST request
        connection.setDoOutput(true);
        //Get the output stream of the connection
        OutputStream os = connection.getOutputStream();
        //Write the parameters to the output stream
        os.write(params.getBytes());
        //Flush the output stream
        os.flush();
        //Close the connection
        os.close();
        //Check the response from the server
        int responseCode = connection.getResponseCode();
        //System.out.println("POST Response Code :: " + responseCode);
        //If the request was received and a response get sent back
        if (responseCode == HttpURLConnection.HTTP_OK) {
            //Create a buffered reader
            BufferedReader responseReader = new BufferedReader(
                    new InputStreamReader(
                            //Set the input stream to the input stream of the connection
                            connection.getInputStream()
                    )
            );
            //Create a StringBuffer and a String to hold the server's response
            String serverResponse;
            StringBuffer response = new StringBuffer();
            //Read the response from the server using the responseReader and append it in serverResponse
            while ((serverResponse = responseReader.readLine()) != null) {
                response.append(serverResponse);
            }
            //Close the BufferedReader object
            responseReader.close();
            //Return the response from the server
            return response.toString();
        } else {
            //Return an error code and an error message
            return "{authcode:'0',authmsg:'Error while sending authentication request to the server.'}";
        }
    }

    /*
        Generate a SHA1 hash of the password before sending it in a request to the server
        this will prevent MITM attackers from having a plain text representation of the password.
        A salt should be included to increase the security of this hash
     */
    private static String sha1(String input) throws NoSuchAlgorithmException {
        //Create a MessageDigest object and get an instance of the SHA1 hash generator
        MessageDigest shaHasher = MessageDigest.getInstance("SHA1");
        //Feed the input string into the shaHasher MessageDigest object and get a result
        byte[] result = shaHasher.digest(input.getBytes());
        //Create a StringBuffer to hold the hashed version of the string
        StringBuffer hashedString = new StringBuffer();
        //Extract the hashed string from the resulting bytes
        for (int i = 0; i < result.length; i++) {
            hashedString.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        //Return the string to the doInBackground function
        return hashedString.toString();
    }
}

