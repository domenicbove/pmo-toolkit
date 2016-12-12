// Your Client ID can be retrieved from your project in the Google
// Developer Console, https://console.developers.google.com
var CLIENT_ID = '302601344077-krpuptgms6i2540v6qaasqi570tci0pa.apps.googleusercontent.com';

var SCOPES = [ 'https://www.googleapis.com/auth/drive.metadata.readonly' ];

function checkAuth() {
	console.log('checkauth called');
	gapi.auth.authorize({
		'client_id' : CLIENT_ID,
		'scope' : SCOPES.join(' '),
		'immediate' : true
	}, handleAuthResult);
}

function handleAuthResult(authResult) {
	var authorizeDiv = document.getElementById('authorize-div');
	if (authResult && !authResult.error) {
		// Hide auth UI, then load client library.
		authorizeDiv.style.display = 'none';
		// loadDriveApi();

		// send token to backend
		console.log(gapi.auth.getToken());
		// reroute to home page
		// window.location.replace('newindex.html');
		console.log('logged in');
	} else {
		// Show auth UI, allowing the user to initiate authorization by
		// clicking authorize button.
		authorizeDiv.style.display = 'inline';
		console.log('not logged in');
	}
}

function handleAuthClick() {
	gapi.auth.authorize({
		client_id : CLIENT_ID,
		scope : SCOPES,
		immediate : false
	}, handleAuthResult);
	console.log("handleAuthClick");
}

angular.module('login', []).component('login', {
	templateUrl : 'login/login.html',
	controller : [ '$http', function LoginController($http) {
		var self = this;
		

		// $http.get('phones/phones.json').then(function(response) {
		//   self.phones = response.data;
		// });

	} ]
});
