angular.module('leagueApp',
    ['editableTableWidgets', 'frontendServices', 'spring-security-csrf-token-interceptor'])
    .controller('HomeCtrl',['$scope' , 'UserService', '$timeout',
    function ($scope, UserService, $timeout) {

        $scope.vm = {
            currentPage: 1,
            totalPages: 0,
            isSelectionEmpty: true,
            errorMessages: [],
            infoMessages: [],
            matches: [],
            pendingChallenges: [],
            acceptedChallenges: [],
            players: [],
            stats: {},
            leaderBoard: []
        };

        function updatePlayerInfo() {
            UserService.getPlayerInfo().then (function (playerInfo) {
                console.log(JSON.stringify(playerInfo));
                window.playerInfo = playerInfo;
                $scope.vm.players = filterPlayers(playerInfo);
                $scope.vm.matches = getMatches($scope.vm.players);
                $scope.vm.stats = getStats($scope.vm.matches);
                $scope.vm.pendingChallenges = getPendingChallenges($scope.vm.players);
                $scope.vm.acceptedChallenges = getAcceptedChallenges($scope.vm.players);
            }, function (errorMessage) {
                showErrorMessage(errorMessage);
                markAppAsInitialized();
            });
        }

        function updateUserInfo() {
            UserService.getUserInfo()
                .then(function (userInfo) {
                    //console.log(JSON.stringify(userInfo));
                    window.userInfo = userInfo;
                    $scope.vm.userName = userInfo.firstName;
                    //$scope.vm.leaderBoard = userInfo.leaderBoard;
                },
                function (errorMessage) {
                    showErrorMessage(errorMessage);
                    markAppAsInitialized();
                });
        }

        function filterPlayers(players) {
            return _.filter(players,function (p) {
                return p.division.challenge
            });
        }



        function getMatches(players) {
            var m = [];
            _.forEach(players,
                function (p) {
                    m = m.concat(p.teamMatches);
                }
            );
            return _.filter(m, function(match) {
                return match.result !== null;
            });
        }

        function getPendingChallenges(players) {
            var c = [];

            _.forEach(players, function(p) {
                    c = c.concat(p.challenges);
                }
            );

            return _.filter(c,function(challenge){
                    return challenge.status == 'PENDING';
                }
            );
        }

        function getAcceptedChallenges(players) {
              var c = [];

            _.forEach(players, function(p) {
                    c = c.concat(p.challenges);
                }
            );

            return _.filter(c,function(challenge){
                    return challenge.status == 'ACCEPTED' && challenge.teamMatch.result === null;
                }
            );

        }

        function getStats(matches) {

            var stats = {};
            stats.wins = 0;
            stats.matches = 0;
            stats.loses = 0;
            stats.racks = 0;
            stats.percentage = 0;

            _.map(matches,function(m){
                if (m.result === null)
                    return;
                stats.matches++;
                if(m.win) {
                    stats.wins++;
                    stats.racks += m.winnerRacks;
                } else {
                    stats.loses++;
                    stats.racks += m.loserRacks;
                }
            });

            if (stats.matches >0) {
                stats.percentage = stats.loses == 0 ? 1 : Math.round((stats.loses/ stats.matches)*100) ;
            }

            return stats;
        }

        function showErrorMessage(errorMessage) {
            clearMessages();
            $scope.vm.errorMessages.push({description: errorMessage});
        }

        function markAppAsInitialized() {
            if ($scope.vm.appReady == undefined) {
                $scope.vm.appReady = true;
            }
        }

        function clearMessages() {
            $scope.vm.errorMessages = [];
            $scope.vm.infoMessages = [];
        }

        function showInfoMessage(infoMessage) {
            $scope.vm.infoMessages = [];
            $scope.vm.infoMessages.push({description: infoMessage});
            $timeout(function () {
                $scope.vm.infoMessages = [];
            }, 1000);
        }

        $scope.selectionChanged = function () {
            $scope.vm.isSelectionEmpty = !_.any($scope.vm.meals, function (meal) {
                return meal.selected && !meal.deleted;
            });
        };

        $scope.pages = function () {
            return _.range(1, $scope.vm.totalPages + 1);
        };

        $scope.search = function (page) {

            var fromDate = new Date($scope.vm.fromDate);
            var toDate = new Date($scope.vm.toDate);

            console.log('search from ' + $scope.vm.fromDate + ' ' + $scope.vm.fromTime + ' to ' + $scope.vm.toDate + ' ' + $scope.vm.toTime);

            var errorsFound = false;

            if ($scope.vm.fromDate && !$scope.vm.toDate || !$scope.vm.fromDate && $scope.vm.toDate) {
                showErrorMessage("Both from and to dates are needed");
                errorsFound = true;
                return;
            }

            if (fromDate > toDate) {
                showErrorMessage("From date cannot be larger than to date");
                errorsFound = true;
            }

            if (fromDate.getTime() == toDate.getTime() && $scope.vm.fromTime &&
                $scope.vm.toTime && $scope.vm.fromTime > $scope.vm.toTime) {
                showErrorMessage("Inside same day, from time cannot be larger than to time");
                errorsFound = true;
            }

            if (!errorsFound) {
                loadMealData($scope.vm.fromDate, $scope.vm.fromTime, $scope.vm.toDate, $scope.vm.toTime, page == undefined ? 1 : page);
            }

        };

        $scope.previous = function () {
            if ($scope.vm.currentPage > 1) {
                $scope.vm.currentPage-= 1;
                loadMealData($scope.vm.fromDate, $scope.vm.fromTime,
                    $scope.vm.toDate, $scope.vm.toTime, $scope.vm.currentPage);
            }
        };

        $scope.next = function () {
            if ($scope.vm.currentPage < $scope.vm.totalPages) {
                $scope.vm.currentPage += 1;
                loadMealData($scope.vm.fromDate, $scope.vm.fromTime,
                    $scope.vm.toDate, $scope.vm.toTime, $scope.vm.currentPage);
            }
        };

        $scope.goToPage = function (pageNumber) {
            if (pageNumber > 0 && pageNumber <= $scope.vm.totalPages) {
                $scope.vm.currentPage = pageNumber;
                loadMealData($scope.vm.fromDate, $scope.vm.fromTime, $scope.vm.toDate, $scope.vm.toTime, pageNumber);
            }
        };

        $scope.add = function () {
            $scope.vm.meals.unshift({
                id: null,
                datetime: null,
                description: null,
                calories: null,
                selected: false,
                new: true
            });
        };

        $scope.delete = function () {
            var deletedMealIds = _.chain($scope.vm.meals)
                .filter(function (meal) {
                    return meal.selected && !meal.new;
                })
                .map(function (meal) {
                    return meal.id;
                })
                .value();

            MealService.deleteMeals(deletedMealIds)
                .then(function () {
                    clearMessages();
                    showInfoMessage("deletion successful.");

                    _.remove($scope.vm.meals, function(meal) {
                        return meal.selected;
                    });

                    $scope.selectionChanged();
                    updateUserInfo();

                },
                function () {
                    clearMessages();
                    $scope.vm.errorMessages.push({description: "deletion failed."});
                });
        };

        $scope.reset = function () {
            $scope.vm.meals = $scope.vm.originalMeals;
        };

        $scope.logout = function () {
            UserService.logout();
        }

        updatePlayerInfo();
        updateUserInfo();
        markAppAsInitialized();
    }]);

