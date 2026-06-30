<?php
$errores = $errores ?? [];
$old = $old ?? [];

function oldSocio($campo, $default = '') {
    global $old;
    return htmlspecialchars($old[$campo] ?? $default, ENT_QUOTES, 'UTF-8');
}

function errorSocio($campo) {
    global $errores;
    return $errores[$campo] ?? '';
}

function claseInvalidaSocio($campo) {
    return errorSocio($campo) ? ' is-invalid' : '';
}
?>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nuevo Socio - Iron Gym</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>

<body class="bg-light">

    <?php require_once '../app/views/inc/navbar.php'; ?>

    <div class="container my-5">
        <div class="row justify-content-center">
            <div class="col-md-8">

                <div class="card shadow">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0">
                            <i class="fas fa-user-plus"></i> Registrar Nuevo Socio
                        </h4>
                    </div>

                    <div class="card-body">

                        <?php if (!empty($errores['general'])): ?>
                            <div class="alert alert-danger">
                                <i class="fas fa-triangle-exclamation"></i>
                                <?= htmlspecialchars($errores['general'], ENT_QUOTES, 'UTF-8') ?>
                            </div>
                        <?php endif; ?>

                        <form id="form-socio" action="/socios/guardar" method="POST" enctype="multipart/form-data" novalidate>

                            <div class="mb-4 text-center">
                                <label for="foto" class="form-label d-block fw-bold text-primary">
                                    Foto de Perfil <span class="text-muted fw-normal">(opcional)</span>
                                </label>

                                <div class="p-3 bg-white border rounded">
                                    <input id="foto" type="file" class="form-control<?= claseInvalidaSocio('foto') ?>" name="foto"
                                        accept="image/png, image/jpeg, image/jpg">

                                    <?php if (errorSocio('foto')): ?>
                                        <div class="invalid-feedback d-block">
                                            <?= htmlspecialchars(errorSocio('foto'), ENT_QUOTES, 'UTF-8') ?>
                                        </div>
                                    <?php endif; ?>

                                    <small class="text-muted">
                                        Formatos permitidos: JPG o PNG. Tamaño máximo: 2 MB.
                                    </small>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="nombre" class="form-label">Nombre Completo</label>

                                <input id="nombre" type="text" class="form-control<?= claseInvalidaSocio('nombre') ?>"
                                    name="nombre" value="<?= oldSocio('nombre') ?>" minlength="3" maxlength="100"
                                    pattern="[A-Za-zÁÉÍÓÚáéíóúÑñÜü\s'.-]+" required>

                                <div class="invalid-feedback">
                                    <?= errorSocio('nombre') ? htmlspecialchars(errorSocio('nombre'), ENT_QUOTES, 'UTF-8') : 'Ingrese un nombre válido. Solo letras y espacios.' ?>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="dni" class="form-label">DNI</label>

                                    <input id="dni" type="text" class="form-control<?= claseInvalidaSocio('dni') ?>"
                                        name="dni" value="<?= oldSocio('dni') ?>" inputmode="numeric"
                                        maxlength="8" pattern="\d{8}" required>

                                    <div class="invalid-feedback">
                                        <?= errorSocio('dni') ? htmlspecialchars(errorSocio('dni'), ENT_QUOTES, 'UTF-8') : 'Ingrese un DNI válido de 8 números.' ?>
                                    </div>
                                </div>

                                <div class="col-md-6 mb-3">
                                    <label for="telefono" class="form-label">Teléfono</label>

                                    <input id="telefono" type="text" class="form-control<?= claseInvalidaSocio('telefono') ?>"
                                        name="telefono" value="<?= oldSocio('telefono') ?>" inputmode="numeric"
                                        maxlength="9" pattern="\d{9}" required>

                                    <div class="invalid-feedback">
                                        <?= errorSocio('telefono') ? htmlspecialchars(errorSocio('telefono'), ENT_QUOTES, 'UTF-8') : 'Ingrese un teléfono válido de 9 números.' ?>
                                    </div>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="emailSocio" class="form-label">
                                    Correo Electrónico <span class="text-muted">(opcional)</span>
                                </label>

                                <input id="emailSocio" type="email" class="form-control<?= claseInvalidaSocio('email') ?>"
                                    name="email" value="<?= oldSocio('email') ?>" maxlength="100">

                                <div class="invalid-feedback">
                                    <?= errorSocio('email') ? htmlspecialchars(errorSocio('email'), ENT_QUOTES, 'UTF-8') : 'Ingrese un correo válido o deje el campo vacío.' ?>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="estado" class="form-label">Estado Inicial</label>

                                <select id="estado" class="form-select<?= claseInvalidaSocio('estado') ?>" name="estado" required>
                                    <option value="activo" <?= oldSocio('estado', 'activo') === 'activo' ? 'selected' : '' ?>>Activo</option>
                                    <option value="pendiente" <?= oldSocio('estado') === 'pendiente' ? 'selected' : '' ?>>Pendiente</option>
                                    <option value="inactivo" <?= oldSocio('estado') === 'inactivo' ? 'selected' : '' ?>>Inactivo</option>
                                </select>

                                <div class="invalid-feedback">
                                    <?= errorSocio('estado') ? htmlspecialchars(errorSocio('estado'), ENT_QUOTES, 'UTF-8') : 'Seleccione un estado válido.' ?>
                                </div>
                            </div>

                            <div class="alert alert-info small">
                                <i class="fas fa-circle-info"></i>
                                Los campos sin la palabra <strong>(opcional)</strong> son necesarios para registrar al socio.
                            </div>

                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="/socios/index" class="btn btn-secondary me-md-2">Cancelar</a>

                                <button id="btn-guardar-socio" type="submit" class="btn btn-success">
                                    <i class="fas fa-save"></i> Guardar Socio
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
            const form = document.getElementById('form-socio');
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