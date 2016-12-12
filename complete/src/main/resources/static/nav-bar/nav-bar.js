'use strict';

// Define the `phoneList` module
angular.module('navBar', [])
.component('navBar', {
  templateUrl: 'nav-bar/nav-bar.html',
  controller: ['$http', function NavBarController($http) {
    var self = this;
    self.orderProp = 'age';

    // $http.get('phones/phones.json').then(function(response) {
    //   self.phones = response.data;
    // });

  }]
});
