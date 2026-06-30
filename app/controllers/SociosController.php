<?php
require_once '../app/models/Socio.php';

class SociosController {

    private function verificarAuth() {
        if (!isset($_SESSION['user_id'])) {
            header('Location: /auth/index');
            exit;
        }
    }

    private function limpiarTexto($valor) {
        return trim((string) $valor);
    }

    private function normalizarDatosSocio($post) {
        return [
            'id' => isset($post['id']) ? (int) $post['id'] : null,
            'nombre' => $this->limpiarTexto($post['nombre'] ?? ''),
            'dni' => preg_replace('/\D/', '', $post['dni'] ?? ''),
            'email' => $this->limpiarTexto($post['email'] ?? ''),
            'telefono' => preg_replace('/\D/', '', $post['telefono'] ?? ''),
            'estado' => $this->limpiarTexto($post['estado'] ?? 'activo')
        ];
    }

    private function validarDatosSocio($datos, $socioModel, $idExcluir = null) {
        $errores = [];

        if ($datos['nombre'] === '') {
            $errores['nombre'] = 'Ingrese el nombre completo del socio.';
        } elseif (strlen($datos['nombre']) < 3 || strlen($datos['nombre']) > 100) {
            $errores['nombre'] = 'El nombre debe tener entre 3 y 100 caracteres.';
        } elseif (!preg_match("/^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\s'.-]+$/u", $datos['nombre'])) {
            $errores['nombre'] = 'El nombre solo debe contener letras y espacios.';
        }

        if ($datos['dni'] === '') {
            $errores['dni'] = 'Ingrese el DNI del socio.';
        } elseif (!preg_match('/^\d{8}$/', $datos['dni'])) {
            $errores['dni'] = 'El DNI debe tener exactamente 8 números.';
        } elseif ($socioModel->existeDni($datos['dni'], $idExcluir)) {
            $errores['dni'] = 'Ya existe un socio registrado con este DNI.';
        }

        if ($datos['telefono'] === '') {
            $errores['telefono'] = 'Ingrese el teléfono del socio.';
        } elseif (!preg_match('/^\d{9}$/', $datos['telefono'])) {
            $errores['telefono'] = 'El teléfono debe tener exactamente 9 números.';
        }

        if ($datos['email'] !== '' && !filter_var($datos['email'], FILTER_VALIDATE_EMAIL)) {
            $errores['email'] = 'Ingrese un correo electrónico válido o deje el campo vacío.';
        }

        $estadosPermitidos = ['activo', 'pendiente', 'inactivo'];
        if (!in_array($datos['estado'], $estadosPermitidos, true)) {
            $errores['estado'] = 'Seleccione un estado válido.';
        }

        return $errores;
    }

    private function validarFoto($archivo) {
        if (!isset($archivo) || $archivo['error'] === UPLOAD_ERR_NO_FILE) {
            return null;
        }

        if ($archivo['error'] !== UPLOAD_ERR_OK) {
            return 'No se pudo cargar la foto. Intente nuevamente.';
        }

        if ($archivo['size'] > 2 * 1024 * 1024) {
            return 'La foto no debe superar los 2 MB.';
        }

        $extension = strtolower(pathinfo($archivo['name'], PATHINFO_EXTENSION));
        $extensionesPermitidas = ['jpg', 'jpeg', 'png'];

        if (!in_array($extension, $extensionesPermitidas, true)) {
            return 'La foto debe estar en formato JPG, JPEG o PNG.';
        }

        $infoImagen = @getimagesize($archivo['tmp_name']);
        $mimesPermitidos = ['image/jpeg', 'image/png'];

        if ($infoImagen === false || !in_array($infoImagen['mime'], $mimesPermitidos, true)) {
            return 'El archivo seleccionado no es una imagen válida.';
        }

        return null;
    }

    private function guardarFoto($archivo, $prefijo) {
        if (!isset($archivo) || $archivo['error'] === UPLOAD_ERR_NO_FILE) {
            return null;
        }

        $carpeta = __DIR__ . '/../../public/img/socios/';

        if (!file_exists($carpeta)) {
            mkdir($carpeta, 0777, true);
        }

        $extension = strtolower(pathinfo($archivo['name'], PATHINFO_EXTENSION));
        $nombreArchivo = $prefijo . '_' . time() . '_' . rand(100, 999) . '.' . $extension;
        $rutaDestino = $carpeta . $nombreArchivo;

        if (move_uploaded_file($archivo['tmp_name'], $rutaDestino)) {
            return $nombreArchivo;
        }

        return null;
    }

    public function index() {
        $this->verificarAuth();

        $socioModel = new Socio();
        $stmt = $socioModel->obtenerTodos();
        $socios = $stmt->fetchAll(PDO::FETCH_ASSOC);

        require_once '../app/views/socios/index.php';
    }

    public function crear() {
        $this->verificarAuth();

        $errores = [];
        $old = [];

        require_once '../app/views/socios/crear.php';
    }

    public function guardar() {
        $this->verificarAuth();

        if ($_SERVER['REQUEST_METHOD'] == 'POST') {
            $socioModel = new Socio();

            $datos = $this->normalizarDatosSocio($_POST);
            $errores = $this->validarDatosSocio($datos, $socioModel);

            $errorFoto = $this->validarFoto($_FILES['foto'] ?? null);

            if ($errorFoto !== null) {
                $errores['foto'] = $errorFoto;
            }

            if (!empty($errores)) {
                $old = $datos;
                require_once '../app/views/socios/crear.php';
                return;
            }

            $nombreFoto = $this->guardarFoto($_FILES['foto'] ?? null, 'socio');

            $datos['email'] = $datos['email'] !== '' ? $datos['email'] : null;
            $datos['foto'] = $nombreFoto;

            if ($socioModel->agregar($datos)) {
                header('Location: /socios/index');
                exit;
            }

            $errores['general'] = 'Error al guardar el socio. Revise los datos e intente nuevamente.';
            $old = $datos;

            require_once '../app/views/socios/crear.php';
        }
    }

    public function editar($id) {
        $this->verificarAuth();

        $socioModel = new Socio();
        $socio = $socioModel->obtenerPorId((int) $id);

        if ($socio) {
            $errores = [];
            $old = $socio;

            require_once '../app/views/socios/editar.php';
        } else {
            header('Location: /socios/index');
            exit;
        }
    }

    public function actualizar() {
        $this->verificarAuth();

        if ($_SERVER['REQUEST_METHOD'] == 'POST') {
            $socioModel = new Socio();

            $datos = $this->normalizarDatosSocio($_POST);
            $id = (int) ($datos['id'] ?? 0);

            $socio = $socioModel->obtenerPorId($id);

            if (!$socio) {
                header('Location: /socios/index');
                exit;
            }

            $errores = $this->validarDatosSocio($datos, $socioModel, $id);

            $errorFoto = $this->validarFoto($_FILES['foto'] ?? null);

            if ($errorFoto !== null) {
                $errores['foto'] = $errorFoto;
            }

            if (!empty($errores)) {
                $old = array_merge($socio, $datos);
                require_once '../app/views/socios/editar.php';
                return;
            }

            $nombreFotoFinal = $_POST['foto_actual'] ?? null;

            $fotoNueva = $this->guardarFoto($_FILES['foto'] ?? null, 'socio_upd');

            if ($fotoNueva !== null) {
                $nombreFotoFinal = $fotoNueva;
            }

            $datos['email'] = $datos['email'] !== '' ? $datos['email'] : null;
            $datos['foto'] = $nombreFotoFinal;

            if ($socioModel->actualizar($datos)) {
                header('Location: /socios/index');
                exit;
            }

            $errores['general'] = 'Error al actualizar el socio. Revise los datos e intente nuevamente.';
            $old = array_merge($socio, $datos);

            require_once '../app/views/socios/editar.php';
        }
    }

    public function cambiarEstado($id, $estado) {
        $this->verificarAuth();

        $socioModel = new Socio();

        if ($socioModel->cambiarEstado((int) $id, $estado)) {
            header('Location: /socios/index');
            exit;
        }
    }
}