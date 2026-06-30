<?php
class Socio {
    private $conn;
    private $table_name = "socios";

    public function __construct() {
        $database = new Database();
        $this->conn = $database->getConnection();
    }

    public function obtenerTodos() {
        $query = "SELECT * FROM " . $this->table_name . " ORDER BY id DESC";
        $stmt = $this->conn->prepare($query);
        $stmt->execute();

        return $stmt;
    }

    public function obtenerPorId($id) {
        $query = "SELECT * FROM " . $this->table_name . " WHERE id = ? LIMIT 1";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $id, PDO::PARAM_INT);
        $stmt->execute();

        return $stmt->fetch(PDO::FETCH_ASSOC);
    }

    public function existeDni($dni, $excluirId = null) {
        if ($excluirId) {
            $query = "SELECT id FROM " . $this->table_name . " WHERE dni = :dni AND id <> :id LIMIT 1";
            $stmt = $this->conn->prepare($query);
            $stmt->bindParam(":dni", $dni);
            $stmt->bindParam(":id", $excluirId, PDO::PARAM_INT);
        } else {
            $query = "SELECT id FROM " . $this->table_name . " WHERE dni = :dni LIMIT 1";
            $stmt = $this->conn->prepare($query);
            $stmt->bindParam(":dni", $dni);
        }

        $stmt->execute();

        return $stmt->fetch(PDO::FETCH_ASSOC) ? true : false;
    }

    public function agregar($datos) {
        $query = "INSERT INTO " . $this->table_name . " 
                 (nombre, dni, email, telefono, estado, foto) 
                 VALUES (:nombre, :dni, :email, :telefono, :estado, :foto)";

        $stmt = $this->conn->prepare($query);

        $stmt->bindParam(":nombre", $datos['nombre']);
        $stmt->bindParam(":dni", $datos['dni']);
        $stmt->bindParam(":email", $datos['email']);
        $stmt->bindParam(":telefono", $datos['telefono']);
        $stmt->bindParam(":estado", $datos['estado']);
        $stmt->bindParam(":foto", $datos['foto']);

        return $stmt->execute();
    }

    public function actualizar($datos) {
        $query = "UPDATE " . $this->table_name . " 
                 SET nombre = :nombre, 
                     dni = :dni, 
                     email = :email, 
                     telefono = :telefono, 
                     estado = :estado, 
                     foto = :foto 
                 WHERE id = :id";

        $stmt = $this->conn->prepare($query);

        $stmt->bindParam(":nombre", $datos['nombre']);
        $stmt->bindParam(":dni", $datos['dni']);
        $stmt->bindParam(":email", $datos['email']);
        $stmt->bindParam(":telefono", $datos['telefono']);
        $stmt->bindParam(":estado", $datos['estado']);
        $stmt->bindParam(":foto", $datos['foto']);
        $stmt->bindParam(":id", $datos['id'], PDO::PARAM_INT);

        return $stmt->execute();
    }

    public function cambiarEstado($id, $estado) {
        $query = "UPDATE " . $this->table_name . " SET estado = :estado WHERE id = :id";
        $stmt = $this->conn->prepare($query);

        $stmt->bindParam(":estado", $estado);
        $stmt->bindParam(":id", $id, PDO::PARAM_INT);

        return $stmt->execute();
    }
}