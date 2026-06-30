package utils;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportExtension implements TestWatcher {

    private static final Path RUTA_REPORTE = Path.of("target", "reporte-ui", "reporte-resultados.txt");
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void testSuccessful(ExtensionContext context) {
        escribirResultado(context, "PASS", "Prueba ejecutada correctamente");
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        escribirResultado(context, "FAIL", cause.getMessage());
    }

    private synchronized void escribirResultado(ExtensionContext context, String estado, String detalle) {
        try {
            Files.createDirectories(RUTA_REPORTE.getParent());

            if (!Files.exists(RUTA_REPORTE)) {
                Files.writeString(
                        RUTA_REPORTE,
                        "REPORTE DE AUTOMATIZACIÓN UI - GYM SYSTEM\n" +
                                "Formato: fecha | estado | clase | prueba | detalle\n\n",
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE
                );
            }

            String linea = String.format(
                    "%s | %s | %s | %s | %s%n",
                    LocalDateTime.now().format(FORMATO),
                    estado,
                    context.getRequiredTestClass().getSimpleName(),
                    context.getDisplayName(),
                    detalle == null ? "" : detalle.replaceAll("[\r\n]+", " ")
            );

            Files.writeString(RUTA_REPORTE, linea, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("No se pudo escribir el reporte: " + e.getMessage());
        }
    }
}
