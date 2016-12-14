// Your Client ID can be retrieved from your project in the Google
// Developer Console, https://console.developers.google.com
var CLIENT_ID = '302601344077-krpuptgms6i2540v6qaasqi570tci0pa.apps.googleusercontent.com';

var SCOPES = [ 'https://www.googleapis.com/auth/drive.metadata.readonly' ];

/**
 * Check if current user has authorized this application.
 */
function checkAuth() {
	gapi.auth.authorize({
		'client_id' : CLIENT_ID,
		'scope' : SCOPES.join(' '),
		'immediate' : true
	}, handleAuthResult);
}

/**
 * Handle response from authorization server.
 *
 * @param {Object} authResult Authorization result.
 */
function handleAuthResult(authResult) {
	var authorizeDiv = document.getElementById('authorize-div');
	if (authResult && !authResult.error) {
		// Hide auth UI, then load client library.
		authorizeDiv.style.display = 'none';

		// send token to backend
		console.log(gapi.auth.getToken());
		// reroute to home page
		// window.location.replace('newindex.html');
		console.log('logged in');
		var http = new XMLHttpRequest();
		var url = "/login";
		http.open("POST", url, true);

		//Send the proper header information along with the request
		//http.setRequestHeader("Content-type", "text");

		http.onreadystatechange = function() {//Call a function when the state changes.
			if (http.readyState == 4 && http.status == 200) {
				alert(http.responseText);
			}
		}
		http.send('' + gapi.auth.getToken().access_token);

		loadDriveApi();
	} else {
		// Show auth UI, allowing the user to initiate authorization by
		// clicking authorize button.
		authorizeDiv.style.display = 'inline';
	}
}

/**
 * Initiate auth flow in response to user clicking authorize button.
 *
 * @param {Event} event Button click event.
 */
function handleAuthClick(event) {
	gapi.auth.authorize({
		client_id : CLIENT_ID,
		scope : SCOPES,
		immediate : false
	}, handleAuthResult);
	return false;
}

/**
 * Load Drive API client library.
 */
function loadDriveApi() {
	gapi.client.load('drive', 'v3', listFiles);
}

/**
 * Print files.
 */
function listFiles() {
	var request = gapi.client.drive.files.list({
		'pageSize' : 10,
		'fields' : "nextPageToken, files(id, name)"
	});

	request.execute(function(resp) {
		appendPre('Files:');
		var files = resp.files;
		if (files && files.length > 0) {
			for (var i = 0; i < files.length; i++) {
				var file = files[i];
				appendPre(file.name + ' (' + file.id + ')');
			}
		} else {
			appendPre('No files found.');
		}
	});
}

/**
 * Append a pre element to the body containing the given message
 * as its text node.
 *
 * @param {string} message Text to be placed in pre element.
 */
function appendPre(message) {
	var pre = document.getElementById('output');
	var textContent = document.createTextNode(message + '\n');
	pre.appendChild(textContent);
}