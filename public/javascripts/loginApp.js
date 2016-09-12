/**
 * Created by Nobosny on 6/17/2015.
 */

(function() {
    angular
        .module('loginApp', ['ngMaterial'])
        .config(ConfigFunc)
        .controller('LoginCtrl', LoginCtrl);

    function ConfigFunc($mdThemingProvider) {
        $mdThemingProvider.theme('default')
            .primaryPalette('blue')
            .accentPalette('light-blue')
            .warnPalette('red');
    }

    function LoginCtrl($scope) {
        var loginObj = this;
    }

})();
