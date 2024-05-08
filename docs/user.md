# ADMIN API SPEC

## Register Admin
- Endpoint : POST /api/users

Request Body : 

```json
{
  "username" : "mawar",
  "password" : "password",
  "nama" : "Mawar Yemima"
}
```

Response Body (Success) : 

```json
{
  "data" : "OK"
}
```

Response Body (Failed) : 

```json
{
  "errors" : "Username must not blank, ???"
}
```

## Login Admin
- Endpoint : POST /api/auth/login

Request Body :

```json
{
  "username" : "mawar",
  "password" : "password"
}
```

Response Body (Success) :

```json
{
  "data" : {
    "token" : "TOKEN",
    "expiredAt" : 23423432423423 // milliseconds
  }
}
```

Response Body (Failed) :

```json
{
  "errors" : "Username or Password Wrong, ???"
}
```

## Get User
- Endpoint : GET /api/users/current

Request Header : 

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "data" : {
    "username" : "Mawar",
    "name" : "Mawar Yemima"
  }
}
```

Response Body (Failed, 401) :

```json
{
  "errors" : "Unauthorized"
}
```

## UPDATE ADMIN
- Endpoint : PATCH /api/users/current

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body : 
```json
{
  "name" : "Mawar Cyntia", // put if only want to update name 
  "password" : "new password" // put if only want to update password
}
```

Response Body (Success) :

```json
{
  "data" : {
    "username" : "Mawar",
    "name" : "Mawar Yemima"
  }
}
```

Response Body (Failed, 401) :

```json
{
  "errors" : "Unauthorized"
}
```

## LOGOUT ADMIN

- Endpoint : DELETE /api/auth/logout

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "data" : "OK"
}
```