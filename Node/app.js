/*
	Backend authentication service for Login Service project
*/
var express = require('express')
var mysql = require('mysql')

var app = new express()


var connection = mysql.createPool({
	connectionLimit: 50,
	host: 'localhost',
	user: 'root',
	password: 'MYSQLPASSWORDHERE',
	database: 'AndroidLoginService'
})

//Listen for get requests
app.get('/', function(req,res){
	let email = req.query.email
	let pass  = req.query.pass

	//Database test
	connection.getConnection(function(error, temp){
		if(!!error){
			temp.release()
			console.log(error)
		}else{
			console.log('Connected');
			var newQuery = "SELECT * FROM users WHERE email='" + email + "' AND password='" + pass + "' LIMIT 1;"
			console.log(newQuery)
			temp.query(newQuery, function(error, rows, fields){
				temp.release()
				if(!!error){
					console.log(error)
				}else{
					if(rows.length != 0){
						res.json({
							authcode:"1", 
							authmsg:"You have succesfully logged in."
						});
					}else{
						res.json({
							authcode:"0", 
							authmsg:"There was a problem authenticating the account, please check your email and password and try again."
						});
					}
				}
			});
		}
	});
})

app.listen(3000)