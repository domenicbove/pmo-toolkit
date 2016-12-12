'use strict';

// Define the `phoneList` module
angular.module('projectForm', [])
.component('projectForm', {
  templateUrl: 'project-form/project-form.html',
  controller: [function FormController() {
    var self = this;

    self.emails = ["dom", "bove"];

    self.keyPress = function(event){
      if (event.keyCode === 13) {
        if (self.email === undefined) {
          alert('Please input valid email')
        } else {
          self.emails.push(self.email);
          self.email = '';
        }
      }

    }

  }]
});
