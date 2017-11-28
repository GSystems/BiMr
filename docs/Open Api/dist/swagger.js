var spec = {
  "swagger": "2.0",
  "info": {
    "description": "The application will provide an REST API which can be used to access the data collected by the aplication. The API will define a set of functions which will offer information about the data, from where has been collected, statistics about the data and the data itself. Developers can perform requests and receive responses via HTTP protocol such as GET and POST. The responses will be in JSON format.",
    "version": "1.0.0",
    "title": "Birds Migration Tool"
  },
  "tags": [
    {
      "name": "tweets",
      "description": "The tweets stored by the application"
    },
    {
      "name": "data",
      "description": "Access to Petstore orders"
    },
    {
      "name": "statistics",
      "description": "Operations about user"
    }
  ],
  "schemes": [
    "http"
  ],
  "paths": {
    "/tweet/all": {
      "get": {
        "tags": [
          "tweets"
        ],
        "summary": "Returns all the tweets stored by the application",
        "description": "",
        "operationId": "findTweets",
        "produces": [
          "application/json"
        ],
        "parameters": [],
        "responses": {
          "200": {
            "description": "successful operation"
          },
          "404": {
            "description": "failure operation"
          }
        }
      }
    },
    "/tweet/date/{dateTime}": {
      "get": {
        "tags": [
          "tweets"
        ],
        "summary": "Returns all the tweets stored by the applcation in that date",
        "description": "Multiple status values can be provided with comma separated strings",
        "operationId": "findTweetByDate",
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "name": "dateTime",
            "in": "path",
            "description": "Date value that needs for filter",
            "required": true,
            "type": "integer",
            "format": "int64"
          }
        ],
        "responses": {
          "200": {
            "description": "successful operation"
          },
          "400": {
            "description": "Invalid status value"
          }
        }
      }
    },
    "/tweet/date/between/{startDate}/{endDate}": {
      "get": {
        "tags": [
          "tweets"
        ],
        "summary": "Returns all tweets in the time interval",
        "description": "test",
        "operationId": "findPetsByTags",
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "name": "startDate",
            "in": "path",
            "description": "Start Date",
            "required": true,
            "type": "integer"
          },
          {
            "name": "endDate",
            "in": "path",
            "description": "End Date",
            "required": true,
            "type": "integer"
          }
        ],
        "responses": {
          "200": {
            "description": "successful operation"
          },
          "400": {
            "description": "Invalid tag value"
          }
        }
      }
    },
    "/data/recent/{days}": {
      "get": {
        "tags": [
          "data"
        ],
        "summary": "Returns the data in the last number of days or if the number of days isn't specified it will return the data in the last seven days.",
        "description": "Returns a collection of objects",
        "operationId": "getDataRecent",
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "name": "days",
            "in": "query",
            "description": "number of days",
            "required": false,
            "type": "integer",
            "format": "int64"
          }
        ],
        "responses": {
          "200": {
            "description": "successful operation",
            "schema": {
              "$ref": "#/definitions/DataResponse"
            }
          },
          "400": {
            "description": "Invalid parameter supplied"
          },
          "404": {
            "description": "Data not found"
          }
        }
      }
    },
    "/data/location/{lng}{lat}": {
      "get": {
        "tags": [
          "data"
        ],
        "summary": "Returns all the data from the specified location in a 50 km range",
        "description": "",
        "operationId": "getDataLocation",
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "name": "lat",
            "in": "query",
            "description": "latitudine",
            "required": true,
            "type": "number"
          },
          {
            "name": "lng",
            "in": "path",
            "description": "longitudine",
            "required": true,
            "type": "number"
          },
          {
            "name": "range",
            "in": "body",
            "description": "range from which the data will be extracted",
            "required": false,
            "type": "integer"
          }
        ],
        "responses": {
          "200": {
            "description": "successful operation",
            "schema": {
              "$ref": "#/definitions/DataResponse"
            }
          },
          "400": {
            "description": "Invalid parameters suplied"
          },
          "404": {
            "description": "Data not found"
          }
        }
      }
    },
    "/data/time/{dateTime}": {
      "get": {
        "tags": [
          "data"
        ],
        "summary": "Return all date stored by the aplication from the specfied date",
        "description": null,
        "operationId": "getDataTime",
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "name": "dateTime",
            "in": "query",
            "description": "date",
            "required": true,
            "type": "integer"
          }
        ],
        "responses": {
          "200": {
            "description": "successful operation",
            "schema": {
              "$ref": "#/definitions/DataResponse"
            }
          },
          "400": {
            "description": "Invalid parameters suplied"
          },
          "404": {
            "description": "Data not found"
          }
        }
      }
    },
    "/data/time/between/{startDate}{endDate}": {
      "get": {
        "tags": [
          "data"
        ],
        "summary": "Return all date stored by the aplication the time interval specified",
        "description": "",
        "operationId": "placeOrder",
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "name": "startDate",
            "in": "query",
            "description": "Start Date",
            "required": true
          },
          {
            "name": "endDate",
            "in": "query",
            "description": "End Date",
            "required": true
          }
        ],
        "responses": {
          "200": {
            "description": "successful operation",
            "schema": {
              "$ref": "#/definitions/DataResponse"
            }
          },
          "400": {
            "description": "Invalid date"
          },
          "404": {
            "description": "Data not found"
          }
        }
      }
    },
    "/data/bird/{commonName}": {
      "get": {
        "tags": [
          "data"
        ],
        "summary": "Returns all the available data about the specified species",
        "description": null,
        "operationId": "getDataBirdSpecie",
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "name": "commonName",
            "in": "path",
            "description": "the common name of a bird or the scientific one",
            "required": true,
            "type": "string"
          }
        ],
        "responses": {
          "200": {
            "description": "successful operation",
            "schema": {
              "$ref": "#/definitions/DataResponse"
            }
          },
          "400": {
            "description": "Invalid name given"
          },
          "404": {
            "description": "Order not found"
          }
        }
      }
    },
    "/statistic/birds/{name}": {
      "get": {
        "tags": [
          "statistics"
        ],
        "summary": "Returns a json object containg statistics about the species specified",
        "description": null,
        "operationId": "getStatisticsName",
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "name": "name",
            "in": "query",
            "description": "The common name or the scientific name of the bird",
            "required": true
          }
        ],
        "responses": {
          "200": {
            "description": "successful operation"
          },
          "400": {
            "description": "Invalid name given"
          },
          "404": {
            "description": "Data not found"
          }
        }
      }
    },
    "/statistics/location/{lat}{lng}": {
      "get": {
        "tags": [
          "statistics"
        ],
        "summary": "Returns a json object containg statistics about the number of birds, species in that location in a range of 50 km.",
        "description": "",
        "operationId": "getStatisticsLocation",
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "name": "lat",
            "in": "querry",
            "description": "latitude",
            "required": true
          },
          {
            "name": "lng",
            "in": "query",
            "description": "longtitude",
            "required": true
          }
        ],
        "responses": {
          "200": {
            "description": "successful operation"
          },
          "400": {
            "description": "Invalid coordonates"
          },
          "404": {
            "description": "Data not found"
          }
        }
      }
    }
  },
  "definitions": {
    "DataResponse": {
      "type": "object",
      "properties": {
        "lng": {
          "type": "integer",
          "format": "double"
        },
        "lat": {
          "type": "integer",
          "format": "double"
        },
        "locName": {
          "type": "string"
        },
        "howMany": {
          "type": "integer"
        },
        "sciName": {
          "type": "string"
        },
        "commonName": {
          "type": "string"
        },
        "obsDate": {
          "type": "string",
          "format": "date-time"
        }
      }
    }
  }
}