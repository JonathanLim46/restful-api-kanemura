# MENU

## CREATE NEW MENU

Endpoint : POST /api/auth/menus

Request Header : 
- X-API-TOKEN : Token (Mandatory)

Request Body : 
multipart/form-data


{
    
    namaMenu : "Ramen", 
    description : "Kuah Gulai",
    harga : 15000,
    idKategori : 1,
    image : imagefile,
    signature : true
}

Response Body (Success) : 
```json
{
  "data" : {
    "id" : "integer",
    "namaMenu" : "Ramen",
    "description" : "Kuah Gulai",
    "harga" : "15000",
    "kategori" : "1",
    "signature" : true,
    "nama_kategori" : "name_kategori_from_id",
    "nama_img" : "image_file_name.filetype"
  } 
}
```

Response Body (Failed) :
```json
{
  "errors" : "Kategori Tidak Ditemukan, Gagal Membuat Menu"
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
  "kategori" : "1"
}
```

Response Body (Success) : 
```json
{
  "data" : {
    "id" : "integer",
    "namaMenu" : "Ramen",
    "description" : "Kuah Gulai",
    "harga" : "15000",
    "kategori" : "1",
    "signature" : false,
    "nama_kategori" : "name_kategori_from_id",
    "nama_img" : "image_file_name.filetype"
  }
}
```

Response Body (Failed) :
```json
{
  "errors" : "Kategori Tidak Ditemukan, Menu Tidak Ditemukan"
}
```

## GET MENU

Endpoint : GET /api/menus/{id_menu}

Response Body (Success) :
```json
{
  "data" : {
    "id" : "integer",
    "namaMenu" : "Ramen",
    "description" : "Kuah Gulai",
    "harga" : "15000",
    "kategori" : "1",
    "signature" : false,
    "nama_kategori" : "name_kategori_from_id",
    "nama_img" : "image_file_name.filetype"
  }
}
```

Response Body (Failed) :
```json
{
  "errors" : "Menu Tidak Ditemukan"
}
```

## Get All User
- Endpoint : GET /api/menus

Response Body (Success) :

```json
{
  "data": [
{
  "data" : {
    "id" : "integer",
    "namaMenu" : "Ramen",
    "description" : "Kuah Gulai",
    "harga" : "15000",
    "kategori" : "1",
    "signature" : false,
    "nama_kategori" : "name_kategori_from_id",
    "nama_img" : "image_file_name.filetype"
  }
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

Response Body (Failed) :
```json
{
  "errors" : "Menu Tidak Ditemukan"
}
```

## UPDATE IMAGE

Endpoint : POST /image/fileSystem

Request Header :
- X-API-TOKEN : Token (Mandatory)

Request Body :
multipart/form-data


{

  
    id : menuId,
    image : imagefile

}


Response Body (Success) :

```json
{
  "File berhasil terupload dengan id menu " :  5876
}
```

Response Body (Failed, 401) :

```json
{
  "errors" : "Unauthorized"
}
```

## GET MENU BY ID

Endpoint : GET /image/fileSystem/images/{id_menu}

Response Body (Success) :

**Show a Image**

Response Body (Failed) :
```json
{
  "errors" : "Gambar Tidak Ditemukan dengan id menu : id_menu"
}
```

## GET MENU BY NAME

Endpoint : GET /image/fileSystem/images/{image_name}

Response Body (Success) :

**Show a Image**

Response Body (Failed) :
```json
{
  "errors" : "Gambar Tidak Ditemukan : image_name"
}
```



