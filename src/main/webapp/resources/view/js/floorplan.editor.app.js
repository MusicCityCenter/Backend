var theApp = angular
		.module(
				'floorplan.editor',
				[ 'ngRoute','mgcrea.ngStrap'],
				function($httpProvider) {
					// Use x-www-form-urlencoded Content-Type
					$httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';

					// Override $http service's default transformRequest
					$httpProvider.defaults.transformRequest = [ function(data) {
						/**
						 * The workhorse; converts an object to
						 * x-www-form-urlencoded serialization.
						 * 
						 * @param {Object}
						 *            obj
						 * @return {String}
						 */
						var param = function(obj) {
							var query = '';
							var name, value, fullSubName, subName, subValue, innerObj, i;

							for (name in obj) {
								value = obj[name];

								if (value instanceof Array) {
									for (i = 0; i < value.length; ++i) {
										subValue = value[i];
										fullSubName = name + '[' + i + ']';
										innerObj = {};
										innerObj[fullSubName] = subValue;
										query += param(innerObj) + '&';
									}
								} else if (value instanceof Object) {
									for (subName in value) {
										subValue = value[subName];
										fullSubName = name + '[' + subName
												+ ']';
										innerObj = {};
										innerObj[fullSubName] = subValue;
										query += param(innerObj) + '&';
									}
								} else if (value !== undefined
										&& value !== null) {
									query += encodeURIComponent(name) + '='
											+ encodeURIComponent(value) + '&';
								}
							}

							return query.length ? query.substr(0,
									query.length - 1) : query;
						};

						return angular.isObject(data)
								&& String(data) !== '[object File]' ? param(data)
								: data;
					} ];
				});

theApp.directive('validNumber', function() {
	  return {
		    require: '?ngModel',
		    link: function(scope, element, attrs, ngModelCtrl) {
		      if(!ngModelCtrl) {
		        return; 
		      }
		      
		      ngModelCtrl.$parsers.push(function(val) {
		        var clean = val.replace( /[^0-9]+/g, '');
		        if (val !== clean) {
		          ngModelCtrl.$setViewValue(clean);
		          ngModelCtrl.$render();
		        }
		        return clean;
		      });
		      
		      element.bind('keypress', function(event) {
		        if(event.keyCode === 32) {
		          event.preventDefault();
		        }
		      });
		    }
		  };
		});

theApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/floorplan/:floorplanId', {
        templateUrl: '/resources/view/floorplan.graph.editor.html',
        controller: 'floorplanEditorController'
      }).
      when('/open', {
          templateUrl: '/resources/view/floorplan.open.html',
          controller: 'floorplanOpener'
      }).
      when('/new', {
            templateUrl: '/resources/view/floorplan.new.html',
            controller: 'floorplanCreator'
      }).
      when('/beacons/:floorplanId', {
          templateUrl: '/resources/view/floorplan.beacons.html',
          controller: 'beaconsEditor'
      }).
      when('/events/:floorplanId', {
          templateUrl: '/resources/view/floorplan.events.editor.html',
          controller: 'eventsEditor'
      }).
      otherwise({
        redirectTo: '/floorplan/mcc'
      });
  }]);
