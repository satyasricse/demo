angular.module('myApp', [])
    .controller('MyController', ['$scope', '$http', function ($scope, $http) {
        var self = $scope;
        self.bookModel = {};
        self.checkedInBookModel = {};
        self.checkOutModel = {};
        self.paymentModel = {};
        self.action = '';
        self.addBookVisibile = false;
        self.checkoutSuccessMsg = false;
        self.checkOutErrorMsg = '';
        self.newBorrower = {};
        $scope.showAddBook = function () {
            self.action = 'add-book';
            $scope.newBook = {};
        }
        $scope.showSearchBook = function () {
            self.action = 'search-book';
        }

        self.showCheckoutBook = function () {
            self.action = 'check-out';
        }
        self.showCheckinBook = function () {
            self.action = 'check-in';
        }

        self.showAddBorrower = function () {
            self.action = 'add-borrower';
        }

        $scope.searchBook = function () {
            if (self.bookModel.searchText && self.bookModel.searchText.length > 0) {
                $http.get('/search-book?searchText=' + self.bookModel.searchText).then(function (response) {
                    $scope.books = response.data;
                }, function (error) {
                    console.log(error)
                });
            }
        }
        $scope.createNewBook = function () {
            $http.post('/create-book', $scope.newBook).then(function (response) {
                $scope.books = response.data;
                self.createBookSuccess = true;
            }, function (error) {
                self.createBookError = true;
            });
        }

        self.checkoutBook = function () {
            self.checkOutErrorMsg = '';
            self.checkoutSuccessMsg = '';
            if (!self.checkOutModel.isbn) {
                self.checkOutErrorMsg = "Please enter ISBN";
            }
            if (!self.checkOutModel.cardId) {
                self.checkOutErrorMsg = "Please enter Card Num";
            }
            if (self.checkOutModel.isbn && self.checkOutModel.cardId) {
                $http.post('/checkout-book', $scope.checkOutModel).then(function (response) {
                    if (response.data.error) {
                        self.checkOutErrorMsg = response.data.error;
                    } else {
                        self.checkoutSuccessMsg = true;
                    }
                }, function (error) {
                    self.checkOutErrorMsg = true;
                });
            }

        }

        self.getCheckedInBooks = function () {
            if (self.checkedInBookModel.searchText && self.checkedInBookModel.searchText.length > 0) {
                $http.get('/checkedout-books?searchText=' + self.checkedInBookModel.searchText).then(function (response) {
                    $scope.checkedInBooks = response.data;
                }, function (error) {
                    console.log(error)
                });
            }
        }
        self.checkIn = function (isbn, cardNo) {
            $http.post('/checkin-book', {isbn: isbn, cardId: cardNo}).then(function (response) {
                if (response.data.error) {
                    self.checkInErrorMsg = response.data.error;
                } else {
                    self.checkInSuccessMsg = true;
                    self.getCheckedInBooks();
                }
            }, function (error) {
                self.checkOutErrorMsg = true;
            });
        }
        self.createNewBorrower = function () {
            $http.post("/create-borrower", self.newBorrower).then(function (response) {
                self.addBorrowerErrorMsg = '';
                self.addBorrowerSuccssMsg = '';
                if (response.data.error) {
                    self.addBorrowerErrorMsg = response.data.error;
                } else {
                    self.addBorrowerSuccssMsg = true;
                }
            }, function (error) {

            });
        }
        self.showFines = function () {
            self.action = 'show-fines';
            self.refreshFines();
        }
        self.refreshFines = function () {
            $http.get('/refresh-fines').then(function (res) {
                self.fines = res.data;
                for (const re of self.fines) {
                    re.fineAmount = parseFloat(re.fineAmount).toFixed(2);
                }
            }, function (err) {

            });
        }
        self.showPayFines = function () {
            self.action = 'pay-fine';
        }
        self.getFineAmount = function () {
            self.noFinePmtRecord = false;
            $http.get('/get-fine-amount?cardId=' + self.paymentModel.searchText).then(function (res) {
                if (res.data && Object.keys(res.data).length > 0) {
                    self.paymentFineObj = res.data;
                    self.paymentFineObj.paymentAmount = parseFloat(self.paymentFineObj.paymentAmount).toFixed(2);
                } else {
                    self.noFinePmtRecord = true;
                }


            }, function () {

            })
        }
        self.makePayment = function (paymentFineObj) {
            self.makePaymentSuccess = false;
            $http.put('/make-payment?cardId=' + paymentFineObj.cardId).then(function (res) {
                self.makePaymentSuccess = true;
            }, function (error) {

            })
        }
    }]);

angular.element(function () {
    angular.bootstrap(document, ['myApp']);
});