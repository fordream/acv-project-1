var promises = [];
var promises1 = [];
 
function sendPush(deviceToken, deviceType, data, message, jsonTrackOrders) {
    console.log("---- sendPush ----");
    console.log("deviceToken="+deviceToken);
    console.log("deviceType="+deviceType);
    console.log("data="+data);
    console.log("message=" + message);
 
    // update database
    var query1 = new Parse.Query(Parse.Installation);
    query1.equalTo('deviceToken', deviceToken);
    promises1.push(query1.first({
        success: function(object) {
            console.log("Update success");
            object.set("trackorders", JSON.stringify(jsonTrackOrders));
            object.save();
            console.log("Saving success:" + JSON.stringify(jsonTrackOrders));
        },
        error: function() {
            console.log("Saving Error");
        }
    }));
 
    //
    var query = new Parse.Query(Parse.Installation);
    query.equalTo('deviceToken', deviceToken);
 
    if (deviceType == 'ios') {
        return Parse.Push.send({
                    where: query,
                    data: {
                        aps:{
                             
                            badge:"Increment",
                            alert:message
                        },
                        acme:{
                            action:"com.rogue.rogueFitness",
                            data:data
                        }
                    }, 
                }, {
                    success: function() {
                        console.log("ios push success!");
                    },
                    error: function(error) {
                        console.log(error);
                    }
                }
            );
    } else if (deviceType == 'android') {
        return Parse.Push.send({
                    where: query,
                    data: {
                        alert: message,
                        data:data,
                        action:"com.rogue.rogueFitness.package.MESSAGE"
                    }, 
                }, {
                    success: function() {
                        console.log("android push success!");
                    },
                    error: function(error) {
                        console.log(error);
                    }
                }
            );
    }
}
 
function callAPI(deviceToken, deviceType, params, htbTrackOrders, jsonTrackOrders){
    console.log("------call api-------");
    return Parse.Cloud.httpRequest({
        method:'GET',
        url:'http://www.roguefitness.com/shippingtracker/api/getMassInfo?'+params,
        headers: {
            'Authorization': 'Basic cm9ndWU6cm9ndWVkZXY='
        },
        success: function(httpResponse) {
            try {
                 
                console.log("response="+httpResponse.text);
                var jsonResponse = JSON.parse(httpResponse.text);
                var canPush = false;
                var pushData = '[';
                var pushMessage = '';
                var newTrackorders = '';
                //create push content
                     
                for (var orderNumber in jsonResponse) {
                    var packages = '';
                    var numOfStatusChanges = 0;
                    var newStatus = '';
                    console.log("orderNumber=" + orderNumber);
                    for (var i = 0; i < jsonResponse[orderNumber].packages.length; i++) {
                        var trackingNumber = jsonResponse[orderNumber].packages[i]["trackingNumber"];
                        if (trackingNumber instanceof Array)
                            trackingNumber = trackingNumber[0];
                        if (trackingNumber == "")
                            trackingNumber = "EmptyString";
                        var status = jsonResponse[orderNumber].packages[i]["status"];
                        console.log(trackingNumber + " - " + status + " - " + htbTrackOrders[orderNumber][trackingNumber]);
                        //console.log(orderNumber + " - " + htbTrackOrders[orderNumber][trackingNumber]);
                        if (htbTrackOrders[orderNumber][trackingNumber] === undefined)
                            continue;
                        if (htbTrackOrders[orderNumber][trackingNumber] != status) {
                            packages += '"' + trackingNumber + '":"' + status + '",';
                            numOfStatusChanges += 1;
                            canPush = true;
                            newStatus = status;
                            // update jsonTrackOrders
                            for (var i = 0; i < jsonTrackOrders.length; ++i) {
                                if (jsonTrackOrders[i].orderumber == orderNumber) {
                                    for (var j = 0; j < jsonTrackOrders[i].packages.length; ++j) {
                                        if (jsonTrackOrders[i].packages[j].trackingNumber == trackingNumber) {
                                            jsonTrackOrders[i].packages[j].status = newStatus;
                                        }
                                    }
                                }
                            }
                        }
                    }
 
                    if (canPush) {
                        packages = packages.substring(0, packages.length - 1);
                        pushData += '{"ordernumber":"'+orderNumber+
                                        '","email":"' + htbTrackOrders[orderNumber].email +
                                        '","store":"' + htbTrackOrders[orderNumber].store +
                                        '","packages":{' + packages + '}},';
 
                        pushMessage += "Your Order #" + orderNumber + " Has Been Updated";
                        if (numOfStatusChanges == 1)
                            pushMessage += " To '" + newStatus + "'\n";
                        //console.log("pushMessage="+pushMessage);
                    }
                }
 
                pushData = pushData.substring(0, pushData.length - 1);
                pushData += ']';
 
                // send push
                console.log("can push="+canPush);
                if (canPush)
                    promises.push(sendPush(deviceToken, deviceType, pushData, pushMessage, jsonTrackOrders));
            } catch (e) {
 
            }
        },
        error: function(httpResponse) {
        } 
    });
}
 
Parse.Cloud.job("push", function(request, response) {
    Parse.Cloud.useMasterKey();
    var query = new Parse.Query(Parse.Installation);
    query.select("deviceToken","trackorders", "deviceType");
    query.find({
        success: function(results) {
            for (var i = 0; i < results.length; i++) {
                try {
                    var row = results[i];
                    var trackOrders = row.get("trackorders");
                    var deviceToken = row.get("deviceToken");
                    var deviceType = row.get("deviceType");
 
                    if (deviceType != 'android' && deviceType != 'ios')
                        response.success("deviceType="+deviceType);
 
                    // synthesize params
                    console.log('trackOrders='+trackOrders);
                    var jsonTrackOrders = JSON.parse(trackOrders);
                    var params = '';
                    var htbTrackOrders = {}; // hash table of track orders
                    for (var j = 0; j < jsonTrackOrders.length; j++) {
                        params += 'data['+jsonTrackOrders[j].orderumber + ']='+jsonTrackOrders[j].email+'&';            
                        //htbTrackOrders[jsonTrackOrders[j].orderumber] = {jsonTrackOrders[j];}
                        htbTrackOrders[jsonTrackOrders[j].orderumber] = {'email':jsonTrackOrders[j].email,
                                                                         'store':jsonTrackOrders[j].store};
                        for (var k = 0; k < jsonTrackOrders[j].packages.length; k++) {
                            var trackingNumber = jsonTrackOrders[j].packages[k]["trackingNumber"];
                            if (trackingNumber instanceof Array)
                                trackingNumber = trackingNumber[0];
                            if (trackingNumber == "")
                                trackingNumber = "EmptyString";
                            var status = jsonTrackOrders[j].packages[k]["status"];
                            htbTrackOrders[jsonTrackOrders[j].orderumber][trackingNumber] = status;
                        }
 
                    }
                    params = params.substring(0, params.length - 1);
                    params += "&token=7KAh3EPZqXWLxpqgKZ8PmVt";
                    console.log('params='+params);
 
                    // call api
                    console.log("htb:" + JSON.stringify(htbTrackOrders));
                    promises.push(callAPI(deviceToken, deviceType, params, htbTrackOrders, jsonTrackOrders));
                } catch (e) {
 
                }
            }
            //response.success("lookup ok");
        },
        error: function() {
            //response.error("lookup error");
        }
    }).then(function() {
         
        Parse.Promise.when(promises).then(function(results) {
            Parse.Promise.when(promises1).then(function(results) {
                response.success("OK");
            }, function (err) {
                console.log("ERR1");
            });
             
        }, function(err) {
            console.log("ERROR");
        });
    });
});
 
Parse.Cloud.job("checkStatus", function(request, status) {
    //status.success("Migration completed successfully.");
    //response.success('abc');
    Parse.Cloud.useMasterKey();
    var query = new Parse.Query("_Installation");
    //query.limit(10);
    query.select("trackorders");
    query.find().then(function(results) {
        console.log("trackOrders");
    });
 
    status.success("OK");
});
 
Parse.Cloud.define("testsendpush", function(request, response){
    var query = new Parse.Query(Parse.Installation);
    query.equalTo('objectId', 'zeDVhFXnoL');
 
    Parse.Push.send({
        where: query,
        data: {
            alert: "Test",
            action:"com.rogue.rogueFitness.package.MESSAGE"
        }
    }, {
        success: function() {
            response.success("Hello world!");
        },
        error: function(error) {
            response.error(error);
        }
    });
});
 
Parse.Cloud.job("testpromise", function(request, response){
    var promise = [];
    var query = new Parse.Query(Parse.Installation);
    query.equalTo('objectId', 'zeDVhFXnoL');
 
    promise.push(Parse.Push.send({
        where: query,
        data: {
            alert: "Test",
            action:"com.rogue.rogueFitness.package.MESSAGE"
        }
    }, {
        success: function() {
            console.log("success 1");
        },
        error: function(error) {
            console.log("error 1");
        }
    }));
 
    var query1 = new Parse.Query(Parse.Installation);
    query1.equalTo('objectId', 'zeDVhFXnoL');
    query1.first({
        success: function(object) {
            console.log("Update success");
            //object.set("trackingOrders", JSON.stringify(jsonTrackOrders));
            //object.save();
            //console.log("Saving success:" + JSON.stringify(jsonTrackOrders));
            //promise.resolve();
        },
        error: function() {
            console.log("Saving Error");
            //promise.resolve();
        }
    }).then(function(obj){
        console.log("@@@@");
    });
 
    //promise.push (new Parse.Promise());
    //
    // var query1 = new Parse.Query(Parse.Installation);
    // query1.equalTo('objectId', 'iO9MgYKxr5');
 
    // promise.push(Parse.Push.send({
    //  where: query1,
    //  data: {
    //      alert: "Test",
    //      action:"com.acv.rouge.package.MESSAGE"
    //  }
    // }, {
    //  success: function() {
    //      console.log("success 2");
    //  },
    //  error: function(error) {
    //      console.log("error 2");
    //  }
    // }));
 
    // Parse.Push.send({
    //  where: query,
    //  data: {
    //      alert: message,
    //      action:"com.acv.rouge.package.MESSAGE"
    //  }, 
    // }, {
    //  success: function() {
    //      console.log("Push success!");
    //  },
    //  error: function(error) {
    //      console.log(error);
    //  }
    // }
    // );
 
    Parse.Promise.when(promise).then(function(results) {
        response.success("Hello world!");
    });
 
    //response.success("Hi");
});
