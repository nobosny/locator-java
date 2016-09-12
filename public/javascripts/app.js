(function() {
    angular
        .module('starterApp', ['ngMaterial', 'ngMap', 'ngAnimate', 'ngFx'])
        .config(ConfigFunc)
        .controller('SearchCtrl', SearchCtrl);

    function ConfigFunc($mdThemingProvider) {
        $mdThemingProvider.theme('default')
            .primaryPalette('blue')
            .accentPalette('light-blue');
    }

    function SearchCtrl($scope, $mdToast, $animate, $location, $anchorScroll, $filter, $http, $mdDialog, $compile, $window) {
        var searchObj = this;
        var myMap;

        $scope.$on('mapInitialized', function(evt, evtMap) {
            myMap = evtMap;

            //initialize onclick on the map to get the latlng
            google.maps.event.addListener(myMap, 'rightclick', function(event) {
                params.lat = event.latLng.lat();
                params.lon = event.latLng.lng();
                searchObj.locationResolved = null;
                searchObj.createUserLocation(event.latLng, 0);
                searchObj.location = event.latLng;
                circleSearch != null ? circleSearch.setMap(null) : "nothing";
                $scope.$apply();
            });
        });

        var params = {};
        //searchObj.callid = '';
        searchObj.location = '';
        searchObj.locationResolved = null;
        searchObj.locCenter = null;
        searchObj.taxNames = [];
        searchObj.distanceEnabled = true;
        searchObj.needsEnabled = false;
        searchObj.providersEnabled = false;
        searchObj.sectionEnabled = false;
        searchObj.miles = 10;
        searchObj.needsNumber = 0;
        searchObj.providers = 5;
        searchObj.sectionList = [];
        searchObj.section = null;
        searchObj.langSearch = '';

        searchObj.searching = false;
        searchObj.searchResults = [];
        searchObj.searchPoints = [];

        searchObj.drawerOpen = true;
        var infoWindow = null;
        var bounds = null;

        var circleSearch;

        $http.get('/needlist')
            .success(function(data, status, headers, config) {
                searchObj.queryNeedList = data;
            });

        searchObj.searchDo = function() {

            searchObj.clearMap();

            if ((searchObj.taxNames.length > 0) && (searchObj.locCenter != null)) {
                searchObj.searchResults = [];

                searchObj.searching = true;

                //params.lat = searchObj.locationResolved.geometry.location.A;
                //params.lon = searchObj.locationResolved.geometry.location.F;
                params.needs = "";
                for(i=0; i<searchObj.taxNames.length; i++) {
                    params.needs += "'" + searchObj.taxNames[i].code + "',";
                }
                params.needs = params.needs.slice(0, -1);
                params.distance = searchObj.distanceEnabled ? searchObj.miles : 7000;
                params.needsNumber = searchObj.needsEnabled ? searchObj.needsNumber : 0;
                params.providers = searchObj.providersEnabled ? searchObj.providers : 0;
                params.target = searchObj.sectionEnabled ? searchObj.section.code : "none";
                params.langSearch = searchObj.langSearch;

                circleSearch != null ? circleSearch.setMap(null) : "nothing";
                bounds = new google.maps.LatLngBounds();

                if (params.distance < 7000) {
                    circleSearch = new google.maps.Circle({
                        map: myMap,
                        center: searchObj.locCenter.position,
                        radius: params.distance * 1609.34,
                        strokeColor: '#FF0000',
                        strokeOpacity: 0.25,
                        strokeWeight: 2,
                        fillColor: '#FF0000',
                        fillOpacity: 0.1
                    });

                    //myMap.fitBounds(circleSearch.getBounds());
                    bounds = circleSearch.getBounds();

                    google.maps.event.addListener(circleSearch, 'rightclick', function(event) {
                        params.lat = event.latLng.lat();
                        params.lon = event.latLng.lng();
                        searchObj.locationResolved = null;
                        searchObj.createUserLocation(event.latLng, 0);
                        searchObj.location = event.latLng;
                        circleSearch != null ? circleSearch.setMap(null) : "nothing";
                        $scope.$apply();
                    });
                } else {

                    bounds.extend(searchObj.locCenter.position);
                }



                $location.hash('search');
                $anchorScroll();

                //searchObj.searchResults.push({title: 'Result 2', content: JSON.stringify(params)});

                $http.post('/search', JSON.stringify(params))
                    .success(function(data, status, headers, config) {
                        searchObj.searching = false;

                        for(i=0; i<data.length; i++) {
                            searchObj.searchResults.push({
                                index: i+1,
                                provider_id: data[i].provider_id,
                                title: data[i].provider_name,
                                //phone: data[i].telephone_number,
                                //aka: data[i].provider_aka,
                                //desc: data[i].provider_description,
                                //elig: data[i].eligibility,
                                //hand: data[i].handicap_access,
                                //hours: data[i].hours,
                                //intake: data[i].intake_procedure,
                                //lang: data[i].languages,
                                //web: data[i].website_address,
                                //address: data[i].line1,
                                //address2: data[i].line2,
                                //city: data[i].city,
                                //county: data[i].county,
                                //zipcode: data[i].postal_code,
                                //prov: data[i].province,
                                //country: data[i].country,
                                distance: data[i].distance,
                                needs: data[i].needses
                            });

                            searchObj.addMarkers(data[i], i+1);
                        }
                        myMap.fitBounds(bounds);
                    });

                //searchObj.searching = false;

            } else {
                if (searchObj.locCenter == null) {
                    $mdToast.show(
                        $mdToast.simple()
                            .content('You must enter a Location o select a point in the Map.')
                            .position('bottom left')
                            .hideDelay(3000)
                    );
                } else {
                    $mdToast.show(
                        $mdToast.simple()
                            .content('You must enter at least one taxonomy term. (Ex. Term1)')
                            .position('bottom left')
                            .hideDelay(3000)
                    );
                }
            }
        };

        searchObj.placeChanged = function() {
            searchObj.locationResolved = this.getPlace();

            searchObj.createUserLocation(searchObj.locationResolved.geometry.location, 14);

            circleSearch != null ? circleSearch.setMap(null) : "nothing";
        };

        searchObj.onLocationClick = function ($event) {
            $event.target.select();
        };

        searchObj.createUserLocation = function(latLngUser, zoom) {

            if (searchObj.locCenter != null) {
                searchObj.locCenter.setMap(null);
            }
            searchObj.locCenter = new google.maps.Marker({
                position: latLngUser,
                map: myMap,
                animation: google.maps.Animation.DROP,
                title: 'User location'
            });

            myMap.panTo(latLngUser);

            /*if (zoom > 0) {
                myMap.setZoom(zoom);
                myMap.panTo(latLngUser);
            }*/

            infowindow = new google.maps.InfoWindow({
                content: '<h3>User Location</h3>' +
                (searchObj.locationResolved != null
                    ? '<p><strong>Address:</strong> ' + searchObj.locationResolved.formatted_address + '</p>'
                    : "") +
                '<p>Latitude: ' + $filter('number')(latLngUser.lat(), 6) +
                '<br>Longitude: ' + $filter('number')(latLngUser.lng(), 6) + '</p>'
            });

            searchObj.locCenter.addListener('click', function() {
                infowindow.open(myMap, searchObj.locCenter);
            });

            params.lat = latLngUser.lat();
            params.lon = latLngUser.lng();
        };

        searchObj.toggleSidenav = function() {
            searchObj.drawerOpen = !searchObj.drawerOpen;
            window.setTimeout(function(){
                google.maps.event.trigger(myMap, 'resize');
            },500);
            //google.maps.event.trigger(myMap, 'resize');
            console.log("Hide/Show triggered");
        };

        searchObj.loadTargetPop = function() {
            return $http.get('/targetpopulation')
                .success(function(data, status, headers, config) {
                    searchObj.sectionList = data;
                });
        }

        searchObj.selectedItem = null;
        searchObj.searchText = null;
        searchObj.chipSearchResults = null;
        searchObj.querySearch = function(query) {
            return $http.get('/searchneed?searchStr=' + query)
                .then(function(response){ return response.data; });
        };

        searchObj.addMarkers = function(data, index) {
            infoWindow = new google.maps.InfoWindow();
            var infoContent = "<p><strong>" + $filter('number')(data.distance, 2) + " miles</strong><br/>" +
                data.provider_aka + "<br/>" +
                data.line1 + "<br/>" +
                data.city + ", FL " + data.postal_code + "<br/>" +
                "<strong>Hours:</strong> " + searchObj.lineBreaks(data.hours) + "<br/>" +
                (data.handicap_access == "t" ? "Handicap Access Available " :
                    (data.handicap_access == null ? "Information NOT Available" :  "NO Handicap Access Available")) + "<br/>" +
                "<strong>Phone:</strong> " + data.telephone_number + "<br/>" +
                "<strong>Languages:</strong> " + data.languages + "</p>"/* +
                "Routes<br/>" +
                '<a href="#" title="Driving" onclick="sctrl.showToastFromMap()"><img src="assets/images/icons/ic_directions_car_24px.svg" class="img-direction"></a>' +
                '<a href="#" title="Transit"><img src="assets/images/icons/ic_directions_transit_24px.svg" class="img-direction"></a>' +
                '<a href="#" title="Walking"><img src="assets/images/icons/ic_directions_walk_24px.svg" class="img-direction"></a>' +
                '<a href="#" title="Cycling"><img src="assets/images/icons/ic_directions_bike_24px.svg" class="img-direction"></a>'*/;

            var compiled = $compile(infoContent)($scope);

            var myMarker = new google.maps.Marker({
                position: { lat: data.latitude, lng: data.longitude},
                map: myMap,
                animation: google.maps.Animation.DROP,
                title: data.provider_name,
                icon: 'http://thydzik.com/thydzikGoogleMap/markerlink.php?text=' + index + '&color=55D7D7',
                content: infoContent
            });

            google.maps.event.addListener(myMarker, 'click', function() {
                infoWindow.close();
                infoWindow.setContent('<strong>' + this.title + '</strong>' + this.content);
                infoWindow.open(myMap, this);
            });

            searchObj.searchPoints.push(myMarker);
            bounds.extend(myMarker.position);
        };

        searchObj.clearMap = function() {
            for(i=0; i<searchObj.searchPoints.length; i++){
                searchObj.searchPoints[i].setMap(null);
            }
        };

        searchObj.lineBreaks = function(lineToBreak) {
            return lineToBreak.replace(/\\n/g, "<br/>");
        };

        searchObj.showProviderInfo = function (ev, providerId) {
            $mdDialog.show({
                controller: DialogController,
                templateUrl: '/provider/' + providerId,
                parent: angular.element(document.body),
                targetEvent: ev
            });
        };

        searchObj.showUserGuide = function (ev) {
            $mdDialog.show({
                controller: DialogController,
                templateUrl: '/userguide',
                parent: angular.element(document.body),
                targetEvent: ev
            });
        };

        searchObj.dialogOk = function() {
            $mdDialog.hide();
        };

        searchObj.showToastFromMap = function() {
            $mdToast.show(
                $mdToast.simple()
                    .content('Click on InfoWindow')
                    .position('bottom left')
                    .hideDelay(3000)
            );
        };

        searchObj.showConfirm = function(ev, newLocation) {
            var confirm = $mdDialog.confirm()
                .parent(angular.element(document.body))
                .title('Would you like to reset the search params?')
                .content("All fields will be emptied and set to its default values.")
                .ariaLabel('Reset Search')
                .ok('Reset')
                .cancel('Keep')
                .targetEvent(ev);

            $mdDialog.show(confirm).then(function() {
                $window.location.target = "_self";
                $window.location.href = newLocation;
            }, function() {
                //Do nothing
            });
        };

    }

    function DialogController($scope, $mdDialog) {
        $scope.hide = function() {
            $mdDialog.hide();
        };
        $scope.cancel = function() {
            $mdDialog.cancel();
        };
    }

})();



  
  