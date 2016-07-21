
'use strict';


var app = angular.module('myApp', ['ngRoute', 'ngStorage']);

app.service('sharedProperties', function () {
    var property = 'First';
    return {
        getProperty: function () {
            return property;
        },
        setProperty: function(value) {
            property = value;
        }
    };
});

app.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
        when('/checkout', {
            templateUrl: 'checkout.html',
            controller: 'CheckoutController'
        }).
        when('/contact', {
            templateUrl: 'contact.html',
            controller: 'ContactController'
        }).
        when('/cart', {
            templateUrl: 'cart.html',
            controller: 'CartController'
        }).
        when('/:param/add', {
            templateUrl: 'add.html',
            controller: 'OrderController'
        }).
        when('/:param/details', {
            templateUrl: 'details.html',
            controller: 'RouteController'
        }).
        when('/:param/image', {
            templateUrl: 'images.html',
            controller: 'RouteController'
        }).
        when('/category/:param', {
            templateUrl: 'categorydetails.html',
            controller: 'RouteController'
        }).
        when('/:param', {
            templateUrl: 'images.html',
            controller: 'RouteController'
        }).
        otherwise({
            templateUrl: 'start.html',
        });
    }]);


app.controller('MainController', function($scope, $http, $localStorage) {
    $scope.count = $localStorage.count;
    console.log("in maincontroller " + $scope.count);
    $scope.count = function() {
        return $localStorage.count;
    }
});
app.controller('customersCtrl', function($scope, $http, $localStorage) {

    $http.get("http://127.0.0.1:8000/api/products/")
        .then(function (response) {$scope.prod_names = response.data.data; console.log("Allproducts");
        $localStorage.products = response.data.data});

    $http.get("http://127.0.0.1:8000/api/categories")
        .then(function (response) {$scope.cat_names = response.data.data;  console.log("categories") });
});



app.controller("RouteController", function($scope, $http, $routeParams, sharedProperties, $localStorage) {

    $scope.cartUpdate = function(productid) {
        alert("Product  added to cart");
        console.log("In cart update of RouteController");
    if ($localStorage.id == undefined) {
        $http.post("http://127.0.0.1:8000/api/orders/")
            .then(function (response) {
                console.log(response);
                console.log("here");
                $localStorage.id = response.data.data.id;
                console.log($localStorage.id);
                console.log("Adding in cart qty=" + $scope.qty + " from new order");
                console.log("New order adding " + productid + " qty=" +$scope.qty);
                $http.post("http://127.0.0.1:8000/api/orders/" + $localStorage.id + "/orderLineItem/", {
                    'product_id': productid,
                    'qty': $scope.qty
                });

            })
    }
    else {
        console.log($localStorage.id);
        console.log("Adding in cart qty=" + $scope.qty);
        $http.post("http://127.0.0.1:8000/api/orders/" + $localStorage.id + "/orderLineItem/", {
            'product_id': productid,
            'qty': $scope.qty
        });
    }
        $localStorage.count = $localStorage.count + 1;
            console.log($localStorage.count);
}


    console.log($routeParams.param + " = Param");
    $http.get("http://127.0.0.1:8000/api/products/" + $routeParams.param)
        .then(function (response) {
            $scope.product_name = response.data;
            console.log("products")
        });

    $http.get("http://127.0.0.1:8000/api/categoryproduct/" + $routeParams.param)
        .then(function (response) {
            $scope.categorised = response.data.data;
            console.log("categoryProduct")
        });

    $scope.changeLog = function() {
        console.log("in changeLog of add to cart" + $scope.qty);
        sharedProperties.setProperty($scope.qty);
        console.log(sharedProperties.getProperty());
    }
});

app.controller("OrderController", function($scope, $http, $routeParams, $localStorage, sharedProperties) {

    $scope.qty = sharedProperties.getProperty();

    if($localStorage.id == undefined) {
        $http.post("http://127.0.0.1:8000/api/orders/")
            .then(function (response) {
                $scope.qty = sharedProperties.getProperty();
                console.log(response);
                console.log("here");
                $localStorage.id = response.data.data.id;
                console.log($localStorage.id);
                console.log("Adding in cart qty=" + $scope.qty);
                $http.post("http://127.0.0.1:8000/api/orders/"+$localStorage.id+"/orderLineItem/", {'product_id': $routeParams.param,'qty': $scope.qty});
            })
    }
    else {
        $scope.qty = sharedProperties.getProperty();
        console.log($localStorage.id);
        console.log("Adding in cart qty=" + $scope.qty);
        $http.post("http://127.0.0.1:8000/api/orders/"+$localStorage.id+"/orderLineItem/", {'product_id': $routeParams.param,'qty': $scope.qty});
    }


    console.log($routeParams.param + " = Param");
    console.log($scope.qty + " = Qty");

    $scope.cartUpdate2 = function() {
        $localStorage.count = $localStorage.count + 1;
        console.log($localStorage.count);
    }
});



app.controller("CartController", function($scope, $http, $routeParams, $localStorage) {

$http.get("http://127.0.0.1:8000/api/orders/"+$localStorage.id+"/orderLineItem/")
    .then(function (response) {
    console.log(response);
    console.log("here in cartcontroller");
    $scope.cartdetails = response.data.data;
        console.log($scope.cartdetails.length + "=length");
        $scope.valid = $localStorage.valid;
        $localStorage.total = 0;
        for(var i=0;i<$scope.cartdetails.length;i++) {
            $localStorage.total += $scope.cartdetails[i].qty * $scope.cartdetails[i].price;
        }

        for(var i = 0; i < $scope.cartdetails.length; i++) {
            var cart = $scope.cartdetails[i];
            for (var j = 0; j < $localStorage.products.length; j++) {
                var product = $localStorage.products[j];
                if (product.id == cart.product_id) {
                    $scope.cartdetails[i].product_id = $localStorage.products[j].code;
                }
            }
        }

        $scope.total = $localStorage.total;
        console.log("total = " + $scope.total);
})

    $scope.check = function(cartdetails) {
    $localStorage.valid = 1;
    console.log("in check valid = " + $localStorage.valid);
        console.log(cartdetails.length + "=cartdetailsLengths");
        console.log($localStorage.products.length + "=lengthProduct");
        for(var i = 0; i < $localStorage.products.length; i++) {
            var product = $localStorage.products[i];
            console.log(product.quantity);
        }
        for(var i = 0; i < cartdetails.length; i++) {
            var cart = cartdetails[i];
            console.log(cart);
            for (var j = 0; j < $localStorage.products.length; j++) {
                var product = $localStorage.products[j];
                console.log(cart.product_id);
                if (product.code == cart.product_id && product.quantity < cart.qty) {
                    $localStorage.valid = 0;
                    break;
                }
            }
            if($localStorage.valid == 0)
                break;
        }
        console.log("After check() " + $localStorage.valid + "= valid");
        $scope.valid = $localStorage.valid;
    }


    $scope.updatep = function(orderline, qty) {
        var index =$scope.cartdetails.indexOf(orderline);
        $localStorage.total = 0;
        for(var i=0;i<$scope.cartdetails.length;i++) {
            $localStorage.total += $scope.cartdetails[i].qty * $scope.cartdetails[i].price;
        }
        $scope.total = $localStorage.total;
        console.log("in updatep");
        console.log("http://127.0.0.1:8000/api/orders/" + $localStorage.id + "/orderLineItem/"+orderline.id+"/");
        $http.patch("http://127.0.0.1:8000/api/orders/" + $localStorage.id + "/orderLineItem/"+orderline.id+"/", {
            'qty': qty,
        });
        //TODO:Add patch here
    }

    $scope.deletep = function(orderline) {
    var i=$scope.cartdetails.indexOf(orderline);
    $scope.cartdetails.splice(i,1);
    $localStorage.valid = 1;
    for(var i = 0; i < $scope.cartdetails.length; i++) {
        var cart = $scope.cartdetails[i];
        for (var i = 0; i < $localStorage.products.length; i++) {
            var product = $localStorage.products[i];
            if (product.id == cart.product_id && (product.quantity == null || product.quantity < cart.qty)) {
                $localStorage.valid = 0;
            }
        }
    }

    $localStorage.total = 0;
    for(var i=0;i<$scope.cartdetails.length;i++) {
        $localStorage.total += $scope.cartdetails[i].qty * $scope.cartdetails[i].price;
    }
    $scope.total = $localStorage.total;
    console.log("total = " + $scope.total);
    console.log($localStorage.valid + "= valid");
    $scope.valid = $localStorage.valid;
    console.log("deleting orderlineid " + orderline.id);
    $localStorage.count = $localStorage.count - 1;
    $http.delete("http://127.0.0.1:8000/api/orders/"+100+"/orderLineItem/"+orderline.id+"/");
    }
});

app.controller("CheckoutController", function($scope, $http, $routeParams, $localStorage) {

    $scope.valid = $localStorage.valid;

    $scope.checkout = function() {
        if($localStorage.id == undefined){
            alert("Already Checked out");
        }
        else {
            $localStorage.username = $scope.username;
            $localStorage.address = $scope.address;
            console.log($localStorage.id + "in checkout controller " + $scope.username + " " + $scope.address + " " + $scope.status);
            $http.patch("http://127.0.0.1:8000/api/orders/" + $localStorage.id + "/", {
                'username': $scope.username,
                'address': $scope.address,
                'status': $scope.status
            });
            $localStorage.count = 0;
            $localStorage.id = undefined;
            console.log($localStorage.count + " " + $localStorage.id);
            $scope.username = "";
            $scope.address = "";
            $scope.status = "";
            alert("Order Placed \n " +
                "Details: \n" +
                "Username: " +$localStorage.username + "\n" +
                "Address: " + $localStorage.address + "\n" +
                "Total: " + $localStorage.total + "\n" +
                "Thank you for visiting!!");
        }
    }
});

app.controller("ContactController", function($scope, $http, $routeParams, $localStorage) {
    console.log("In contact");

    $scope.save = function() {
        alert("Thank you for your valuable contribution!!");
        }

});
