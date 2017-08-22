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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity{
    //Global Variables
    private Context CurrentContext = this;
    //Change this string to the IP or Domain Name that points to the server running the Node.Js Backend script
    private String serverAddress = "192.168.0.7";
    //Authentication class
    private class MyLoginAuthMan extends LoginAuthenticationRequest{
        public MyLoginAuthMan(Context curContext,String serverAddress){
            super(curContext,serverAddress);
        }
        @Override
        protected void onPostExecute(String result){
            if(result != null && result.isEmpty() == false){
                try{
                    final JSONObject jsonResult = new JSONObject(result);
                    //Here we can also switch on the authentication code
                    if (jsonResult.getInt("authcode") == 1){
                        //Authenticated
                        Toast.makeText(
                                CurrentContext,
                                jsonResult.getString("authmsg"),
                                Toast.LENGTH_LONG).show();
                    }else{
                        //Not authenticated
                        Toast.makeText(
                                CurrentContext,
                                jsonResult.getString("authmsg"),
                                Toast.LENGTH_LONG).show();
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(
                        CurrentContext,
                        "There was no response from the server.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    /*
        Creates a Floating action button at the bottom right of the screen that then creates a
        Snackbar with the copyright information and author information
     */
    private void CreateCopyrightFloatingButton(RelativeLayout loginLayout){
        //Create a new Floating Action Button
        final FloatingActionButton fab = new FloatingActionButton(this);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Show the copyrght message to let user acknowledge
                Snackbar.make(
                        view,
                        "Copyright © 2017 - aldanisvigo@gmail.com",
                        Snackbar.LENGTH_LONG).setAction(
                                "Action",
                                null).show();
                //Hide the floating action button
                fab.setVisibility(View.INVISIBLE);
            }
        });
        //Create a LayoutParams object that will hold the layout parameters for the Floating Action Button
        RelativeLayout.LayoutParams fabDetails = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //Position the Floating Action Button at the bottom right of the view
        fabDetails.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        fabDetails.addRule(RelativeLayout.ALIGN_PARENT_END);
        //Add Copyright drawable graphic to the Floating Action Button
        Drawable fabBg = getResources().getDrawable(R.drawable.copyright,this.getTheme());
        fab.setImageDrawable(fabBg);
        //Add the Floating Action Button to the layout
        loginLayout.addView(fab,fabDetails);
    }

    /*
        Generates the Username, Password and Login buttons used to generate the login request
     */
    private void CreateLoginForm(RelativeLayout loginLayout){
        //Create an EditText where the user can enter their email
        final EditText username = new EditText(this);
        //Make the username EditText an email type text
        username.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        //Create a layout parameters object to hold the layout parameters of the username EditText
        RelativeLayout.LayoutParams usernameLayoutParameters = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        //Add some margin to the top of the username
        usernameLayoutParameters.setMargins(0,150,0,0);
        //Set the width of the username and password EditTexts
        int EditTextWidth = 350;
        username.setWidth(EditTextWidth);
        //Set the text of the username EditText to Email
        username.setText("Email");
        //Add event handler for the onClick event
        username.setOnClickListener(new View.OnClickListener(){
            int clickCount = 0;
            @Override
            public void onClick(View view){
                if(clickCount == 0){
                    username.setText("");
                }
                clickCount += 1;
            }
        });
        //Center the username EditText horizontally
        usernameLayoutParameters.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //Add the layout parameters to the username EditText
        username.setLayoutParams(usernameLayoutParameters);
        //Add the username EditText to the loginLayout
        loginLayout.addView(username,usernameLayoutParameters);
        //Create an EditText where the user can enter their password
        final EditText password = new EditText(this);
        //Make the password EditText a password type input
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //Create a LayoutParams object to store the layout parameters of the password EditText
        RelativeLayout.LayoutParams passwordLayoutParameters = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        //Add some margin to the top of the password EditText so we can position it under the email
        passwordLayoutParameters.setMargins(0,200,0,0);
        //Center the password EditText horizontally
        passwordLayoutParameters.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //Set the password layout parameters
        password.setLayoutParams(passwordLayoutParameters);
        //Add the text Password to the password EditText
        password.setText("Password");
        //Change the width of the password EditText to 260
        password.setWidth(EditTextWidth);
        //Add a click listener to the Password EditText
        password.setOnClickListener(new View.OnClickListener(){
            int clickCount = 0;
            @Override
            public void onClick(View view){
                if(clickCount == 0){
                    password.setText("");
                }
                clickCount++;
            }
        });
        //Add the password EditText to the loginLayout
        loginLayout.addView(password,passwordLayoutParameters);
        //Create a login button to send the HTTP request to our Node backend server
        Button loginBtn = new Button(this);
        //Change the text of the login button to Log In
        loginBtn.setText("Log In");
        //Create a Layout parameters object to store the layout parameters for the Login Button
        RelativeLayout.LayoutParams loginBtnLayoutParameters = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        //Center the login button horizontally
        loginBtnLayoutParameters.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //Add some margin at the top to place it under the Password EditText
        loginBtnLayoutParameters.setMargins(0,290,0,0);
        //Add the layout parameters to the login button
        loginBtn.setLayoutParams(loginBtnLayoutParameters);
        //Add a clickListener to the login button to send Authentication request to server
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try{
                    MyLoginAuthMan authMan = new MyLoginAuthMan(CurrentContext,serverAddress);
                    authMan.execute(username.getText().toString(), password.getText().toString());
                }catch(Exception e){
                    Toast.makeText(getBaseContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
        //Add the loginBtn to the loginLayout
        loginLayout.addView(loginBtn,loginBtnLayoutParameters);
    }

    /*
        Runs when the Activity is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        /*Code for using XML view
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        */
        //Create a new RelativeLayout
        RelativeLayout loginLayout = new RelativeLayout(this);
        loginLayout.setBackgroundColor(Color.WHITE);
        //Create a floating button that displays copyright information
        CreateCopyrightFloatingButton(loginLayout);
        //Create the Email and Password Fields and the Login Button
        CreateLoginForm(loginLayout);
        //Set the content in the view to our layout
        this.setContentView(loginLayout);
    }
}
