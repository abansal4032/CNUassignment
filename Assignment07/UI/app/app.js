
'use strict';


var app = angular.module('myApp', ['ngRoute']);
app.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
        when('/category/:param', {
            templateUrl: 'categorydetails.html',
            controller: 'RouteController'
        }).
        when('/:param', {
            templateUrl: 'productdetails.html',
            controller: 'RouteController'
        }).
        otherwise({
            templateUrl: 'start.html'
            // redirectTo: '/'
        });
    }]);
app.controller('customersCtrl', function($scope, $http) {
    $http.get("http://127.0.0.1:8000/api/products/")
        .then(function (response) {$scope.prod_names = response.data.data;});
    $http.get("http://127.0.0.1:8000/api/categories")
        .then(function (response) {$scope.cat_names = response.data.data;});
});

app.controller("RouteController", function($scope, $http, $routeParams) {
        $http.get("http://127.0.0.1:8000/api/products/"+$routeParams.param)
        .then(function (response) {$scope.product_name = response.data;});
    $http.get("http://127.0.0.1:8000/api/categoryproduct/"+$routeParams.param)
        .then(function (response) {$scope.categorised = response.data.data;});
});

