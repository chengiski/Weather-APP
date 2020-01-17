const path = require('path')
const express = require('express')
const https = require('https')
const app = express()
const port = 8080

//const router = express.Router();


//solve cross-domain
app.all('/*', function(req, res, next) {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', 'Content-Type, Content-Length, Authorization, Accept, X-Requested-With');
    res.header('Access-Control-Allow-Methods', 'PUT, POST, GET, DELETE, OPTIONS');
    next();
})
//AutoComplete
app.get('/autocomplete', function(request,response) {
    var cityfromkey = request.query.City;
    var url = 'https://maps.googleapis.com/maps/api/place/autocomplete/json?input='+cityfromkey+'&types=(cities)&language=en&key=AIzaSyCc3pxReBk_P7LvpjI3wtQnS1I0Fdrj5Lc';
    https.get(url, function(req, res) {
        var res_content = '';
        req.on('data',function(data) {
            res_content += data;
        });
        req.on('end', function() {
            return response.send(res_content);
        });
    })
})
//CurrentLocation
app.get('/currweather', function(request,response) {
    var location = encodeURIComponent(request.query.Lat+","+request.query.Lon);
    var url = 'https://api.darksky.net/forecast/9f02d80eaa0a03ce18a8c57d6d4ee687/'+location;
    https.get(url, function(req, res) {
        var res_content = '';
        req.on('data',function(data) {
            res_content += data;
        });
        req.on('end', function() {
            return response.send(res_content);
        });
    })
})
//get Lat&Lng and use them to get weather information
app.get("/weather",function(request,response){
    var address = encodeURIComponent(request.query.street+request.query.city+request.query.state);
    var url = 'https://maps.googleapis.com/maps/api/geocode/json?address=['+address+']&key=AIzaSyCc3pxReBk_P7LvpjI3wtQnS1I0Fdrj5Lc';
    https.get(url, function(req, res) {
        var res_content = '';
        req.on('data', function(data) {
            res_content += data;
        });
        req.on('end', function() {
            var parsed = JSON.parse(res_content);
            if (parsed.status == 'ZERO_RESULTS'){
                return response.send({ifError:'error'});
            }
            else{
                lat = parsed.results[0].geometry.location.lat;
                lng = parsed.results[0].geometry.location.lng;
                var weather_url = 'https://api.darksky.net/forecast/9f02d80eaa0a03ce18a8c57d6d4ee687/'+lat+','+lng;
                https.get(weather_url, function(reqWeather, resWeather) {
                    var resWeather_content = '';
                    reqWeather.on('data', function(data){
                        resWeather_content += data;
                    });
                    reqWeather.on('end', function(){
                        return response.send(resWeather_content);
                    });
                })
            }
        })
    })
})
//StateSeal
app.get('/stateseal', function(request,response) {
    var state = encodeURIComponent(request.query.State);
    var engineID = '003329472873084252077:konoeznukdd';
    var url = 'https://www.googleapis.com/customsearch/v1?q='+state+'%20State%20Seal&cx='+engineID+'&imgSize=huge&imgType=news&num=1&searchType=image&key=AIzaSyCc3pxReBk_P7LvpjI3wtQnS1I0Fdrj5Lc';
    https.get(url, function(req, res) {
        var res_content = '';
        req.on('data',function(data) {
            res_content += data;
        });
        req.on('end', function() {
            return response.send(res_content);
        });
    })
})
//cityphoto
app.get('/cityphoto', function(request,response) {
    var city = encodeURIComponent(request.query.City);
    var engineID = '003329472873084252077:konoeznukdd';
    var url = 'https://www.googleapis.com/customsearch/v1?q='+city+'&cx='+engineID+'&imgSize=huge&imgType=news&num=8&searchType=image&key=AIzaSyCc3pxReBk_P7LvpjI3wtQnS1I0Fdrj5Lc';
    https.get(url, function(req, res) {
        var res_content = '';
        req.on('data',function(data) {
            res_content += data;
        });
        req.on('end', function() {
            return response.send(res_content);
        });
    })
})
//Weekly Detail
app.get('/weeklydetail', function(request,response) {
    var locAndTime = encodeURIComponent(request.query.Lat+","+request.query.Lon+","+request.query.Time);
    var url = 'https://api.darksky.net/forecast/9f02d80eaa0a03ce18a8c57d6d4ee687/'+locAndTime;
    https.get(url, function(req, res) {
        var res_content = '';
        req.on('data',function(data) {
            res_content += data;
        });
        req.on('end', function() {
            return response.send(res_content);
        });
    })
})

app.get('/favorites', (req, res) => res.send('favorites'))
app.use(function(req, res) {
    res.status(404).send("Sorry, this route does not exist.")
})
app.listen(port, () => console.log('Example app listening on port: '+port))

module.exports = app;