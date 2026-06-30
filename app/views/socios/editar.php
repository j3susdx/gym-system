<?php
$errores = $errores ?? [];
$old = $old ?? $socio ?? [];

function oldSocioEditar($campo, $default = '') {
    global $old;
    return htmlspecialchars($old[$campo] ?? $default, ENT_QUOTES, 'UTF-8');
}

function rawSocioEditar($campo, $default = '') {
    global $old;
    return $old[$campo] ?? $default;
}

function errorSocioEditar($campo) {
    global $errores;
    return $errores[$campo] ?? '';
}

function claseInvalidaSocioEditar($campo) {
    return errorSocioEditar($campo) ? ' is-invalid' : '';
}
?>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Socio - Iron Gym</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>

<body class="bg-light">

    <?php require_once '../app/views/inc/navbar.php'; ?>

    <div class="container my-5">
        <div class="row justify-content-center">
            <div class="col-md-8">

                <div class="card shadow">
                    <div class="card-header bg-warning text-dark">
                        <h4 class="mb-0">
                            <i class="fas fa-user-edit"></i> Editar Socio
                        </h4>
                    </div>

                    <div class="card-body">

                        <?php if (!empty($errores['general'])): ?>
                            <div class="alert alert-danger">
                                <i class="fas fa-triangle-exclamation"></i>
                                <?= htmlspecialchars($errores['general'], ENT_QUOTES, 'UTF-8') ?>
                            </div>
                        <?php endif; ?>

                        <form id="form-socio-editar" action="/socios/actualizar" method="POST" enctype="multipart/form-data" novalidate>

                            <input type="hidden" name="id" value="<?= oldSocioEditar('id') ?>">
                            <input type="hidden" name="foto_actual" value="<?= oldSocioEditar('foto') ?>">

                            <div class="mb-4 text-center">
                                <label for="foto" class="form-label d-block fw-bold text-primary">
                                    Foto de Perfil <span class="text-muted fw-normal">(opcional)</span>
                                </label>

                                <div class="p-3 bg-white border rounded">

                                    <?php if (!empty(rawSocioEditar('foto'))): ?>
                                        <div class="mb-3">
                                            <img src="/img/socios/<?= oldSocioEditar('foto') ?>" alt="Foto actual"
                                                 class="img-thumbnail rounded-circle" width="120" height="120"
                                                 style="object-fit: cover;">

                                            <p class="text-muted small mt-1">Foto actual</p>
                                        </div>
                                    <?php endif; ?>

                                    <label for="foto" class="form-label small text-start w-100">
                                        Cambiar foto <span class="text-muted">(opcional)</span>
                                    </label>

                                    <input id="foto" type="file" class="form-control<?= claseInvalidaSocioEditar('foto') ?>" name="foto"
                                        accept="image/png, image/jpeg, image/jpg">

                                    <?php if (errorSocioEditar('foto')): ?>
                                        <div class="invalid-feedback d-block">
                                            <?= htmlspecialchars(errorSocioEditar('foto'), ENT_QUOTES, 'UTF-8') ?>
                                        </div>
                                    <?php endif; ?>

                                    <small class="text-muted">
                                        Deja en blanco para mantener la foto actual. Formatos: JPG o PNG. Máximo: 2 MB.
                                    </small>
                                </div>
                            </div>

                            <hr>

                            <div class="mb-3">
                                <label for="nombre" class="form-label">Nombre Completo</label>

                                <input id="nombre" type="text" class="form-control<?= claseInvalidaSocioEditar('nombre') ?>"
                                    name="nombre" value="<?= oldSocioEditar('nombre') ?>" minlength="3" maxlength="100"
                                    pattern="[A-Za-zÁÉÍÓÚáéíóúÑñÜü\s'.-]+" required>

                                <div class="invalid-feedback">
                                    <?= errorSocioEditar('nombre') ? htmlspecialchars(errorSocioEditar('nombre'), ENT_QUOTES, 'UTF-8') : 'Ingrese un nombre válido. Solo letras y espacios.' ?>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="dni" class="form-label">DNI</label>

                                    <input id="dni" type="text" class="form-control<?= claseInvalidaSocioEditar('dni') ?>"
                                        name="dni" value="<?= oldSocioEditar('dni') ?>" inputmode="numeric"
                                        maxlength="8" pattern="\d{8}" required>

                                    <div class="invalid-feedback">
                                        <?= errorSocioEditar('dni') ? htmlspecialchars(errorSocioEditar('dni'), ENT_QUOTES, 'UTF-8') : 'Ingrese un DNI válido de 8 números.' ?>
                                    </div>
                                </div>

                                <div class="col-md-6 mb-3">
                                    <label for="telefono" class="form-label">Teléfono</label>

                                    <input id="telefono" type="text" class="form-control<?= claseInvalidaSocioEditar('telefono') ?>"
                                        name="telefono" value="<?= oldSocioEditar('telefono') ?>" inputmode="numeric"
                                        maxlength="9" pattern="\d{9}" required>

                                    <div class="invalid-feedback">
                                        <?= errorSocioEditar('telefono') ? htmlspecialchars(errorSocioEditar('telefono'), ENT_QUOTES, 'UTF-8') : 'Ingrese un teléfono válido de 9 números.' ?>
                                    </div>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="emailSocio" class="form-label">
                                    Correo Electrónico <span class="text-muted">(opcional)</span>
                                </label>

                                <input id="emailSocio" type="email" class="form-control<?= claseInvalidaSocioEditar('email') ?>"
                                    name="email" value="<?= oldSocioEditar('email') ?>" maxlength="100">

                                <div class="invalid-feedback">
                                    <?= errorSocioEditar('email') ? htmlspecialchars(errorSocioEditar('email'), ENT_QUOTES, 'UTF-8') : 'Ingrese un correo válido o deje el campo vacío.' ?>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="estado" class="form-label">Estado</label>

                                <select id="estado" class="form-select<?= claseInvalidaSocioEditar('estado') ?>" name="estado" required>
                                    <option value="activo" <?= rawSocioEditar('estado', 'activo') === 'activo' ? 'selected' : '' ?>>Activo</option>
                                    <option value="inactivo" <?= rawSocioEditar('estado') === 'inactivo' ? 'selected' : '' ?>>Inactivo</option>
                                    <option value="pendiente" <?= rawSocioEditar('estado') === 'pendiente' ? 'selected' : '' ?>>Pendiente</option>
                                </select>

                                <div class="invalid-feedback">
                                    <?= errorSocioEditar('estado') ? htmlspecialchars(errorSocioEditar('estado'), ENT_QUOTES, 'UTF-8') : 'Seleccione un estado válido.' ?>
                                </div>
                            </div>

                            <div class="alert alert-info small">
                                <i class="fas fa-circle-info"></i>
                                Los campos sin la palabra <strong>(opcional)</strong> son necesarios para actualizar al socio.
                            </div>

                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="/socios/index" class="btn btn-secondary me-md-2">Cancelar</a>

                                <button type="submit" class="btn btn-warning">
                                    <i class="fas fa-sync"></i> Actualizar Socio
                                </button>
                            </div>

                        </form>

                    </div>
                </div>

            </div>
        </div>
    </div>

    <?php require_once '../app/views/inc/footer.php'; ?>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const form = document.getElementById('form-socio-editar');
            const dni = document.getElementById('dni');
            const telefono = document.getElementById('telefono');

            function soloNumeros(input, maxLength) {
                input.addEventListener('input', function () {
                    this.value = this.value.replace(/\D/g, '').slice(0, maxLength);
                });
            }

            soloNumeros(dni, 8);
            soloNumeros(telefono, 9);

            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }

                form.classList.add('was-validated');
            });
        });
    </script>

</body>
</html>