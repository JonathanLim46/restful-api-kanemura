# MENU

## CREATE NEW MENU

Endpoint : POST /api/auth/menus

Request Header : 
- X-API-TOKEN : Token (Mandatory)

Request Body : 
```json
{
  "nama_menu" : "Ramen Wow",
  "description" : "Makanan mie dahsyat",
  "harga" : "25000",
  "kategori" : "DRY RAMEN",
  "foto" : ""
}
```

Response Body (Success) : 
```json
{
  "data" : {
    "id_menu" : "integer",
    "nama_menu" : "Ramen Wow",
    "description" : "Makanan mie dahsyat",
    "harga" : "25000",
    "kategori" : "DRY RAMEN",
    "foto" : ""
  } 
}
```

Response Body (Failed) :
```json
{
  "errors" : "foto format invalid, ..."
}
```

## UPDATE MENU

Endpoint : PUT /api/auth/menus/{id_menu}

Request Header : 
- X-API-TOKEN : Token (Mandatory)

Request Body :
```json
{
  "nama_menu" : "Ramen Wow",
  "description" : "Makanan mie dahsyat",
  "harga" : "25000",
  "kategori" : "DRY RAMEN",
  "foto" : ""
}
```

Response Body (Success) : 
```json
{
  "data" : {
    "id_menu" : "integer",
    "nama_menu" : "Ramen Wow",
    "description" : "Makanan mie dahsyat",
    "harga" : "25000",
    "kategori" : "DRY RAMEN",
    "foto" : ""
  }
}
```

Response Body (Failed) :
```json
{
  "errors" : "foto format invalid, ..."
}
```

## GET MENU

Endpoint : GET /api/auth/menus/{id_menu}

Request Header :
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json
{
  "data" : {
    "id_menu" : "integer",
    "nama_menu" : "Ramen Wow",
    "description" : "Makanan mie dahsyat",
    "harga" : "25000",
    "kategori" : "DRY RAMEN",
    "foto" : ""
  }
}
```

Response Body (Failed, 404) :
```json
{
  "errors" : "data is not found"
}
```

## REMOVE MENU

Endpoint : DELETE /api/auth/menus/{id_menu}

Request Header :
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json
{
  "data" : "OK"
}
```

Response Body (Failed, 404) :
```json
{
  "errors" : "data is not found"
}
```
