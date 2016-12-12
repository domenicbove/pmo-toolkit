'use strict';

// Define the `phonecatApp` module
angular.module('pmoToolkit', [
  'ngRoute',
  'projectForm',
  'login',
  'navBar'
])
.config(['$locationProvider' ,'$routeProvider',
  function config($locationProvider, $routeProvider) {
    $locationProvider.hashPrefix('!');

    $routeProvider.
      when('/form', {
        template: '<project-form></project-form>'
      }).
      when('/login', {
          template: '<login></login>'
        }).
      otherwise('/form');
  }
]);
