# ADMIN API SPEC

## Register Admin
- Endpoint : POST /api/auth/users

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
  "errors" : "Username must not blank"
}
```

Response Body (Failed) :

```json
{
  "errors" : "Username already registered"
}
```

## Login Admin
- Endpoint : POST /api/login

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
    "expiredAt" : 23423432423423 // milliseconds & 30 days expired
  }
}
```

Response Body (Failed) :

```json
{
  "errors" : "invalid credentials"
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

## Get All User
- Endpoint : GET /api/auth/users/

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "data": [
    {
      "username": "mawar",
      "name": "mawar putri"
    }
  ],
  "paging" : {
    "currentPage" : 0,
    "totalPage" : 10,
    "size" : 10
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
- Endpoint : PUT /api/users/current

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