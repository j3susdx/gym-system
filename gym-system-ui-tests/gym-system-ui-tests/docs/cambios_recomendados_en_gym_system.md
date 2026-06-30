# Cambios recomendados en Gym System para Selenium

La guía de Semana 12 recomienda usar selectores estables y evitar XPath absolutos. Por eso, es mejor agregar `id` a los formularios principales del sistema.

## 1. Login

Archivo:

```text
app/views/auth/login.php
```

Cambiar el formulario por una versión con IDs:

```php
<form id="form-login" action="/auth/login" method="POST">
    <div class="mb-3">
        <label class="form-label text-secondary">Correo Electrónico</label>
        <input id="email" type="email" name="email" class="form-control form-control-lg" placeholder="admin@irongym.com" required>
    </div>

    <div class="mb-4">
        <label class="form-label text-secondary">Contraseña</label>
        <input id="password" type="password" name="password" class="form-control form-control-lg" placeholder="******" required>
    </div>

    <button id="btn-login" type="submit" class="btn btn-primary w-100 btn-lg">INGRESAR</button>
</form>
```

## 2. Registro de socio

Archivo:

```text
app/views/socios/crear.php
```

Agregar IDs a los campos:

```php
<form id="form-socio" action="/socios/guardar" method="POST" enctype="multipart/form-data">

<input id="nombre" type="text" class="form-control" name="nombre" required>

<input id="dni" type="text" class="form-control" name="dni" required>

<input id="telefono" type="text" class="form-control" name="telefono">

<input id="emailSocio" type="email" class="form-control" name="email">

<select id="estado" class="form-select" name="estado">
    <option value="activo">Activo</option>
    <option value="pendiente">Pendiente</option>
    <option value="inactivo">Inactivo</option>
</select>

<button id="btn-guardar-socio" type="submit" class="btn btn-success">
    <i class="fas fa-save"></i> Guardar Socio
</button>
```

## 3. Listado de socios

Archivo:

```text
app/views/socios/index.php
```

Agregar ID a la tabla:

```php
<table id="tabla-socios" class="table table-hover table-bordered align-middle table-data">
```

## Importante

El proyecto de pruebas también funciona con `name` como respaldo, pero los IDs dejan el trabajo más ordenado y más alineado con el patrón POM.
