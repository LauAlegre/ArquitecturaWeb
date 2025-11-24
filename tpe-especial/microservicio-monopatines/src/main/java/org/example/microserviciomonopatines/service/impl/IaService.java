package org.example.microserviciomonopatines.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.example.microserviciomonopatines.client.CuentasClient;
import org.example.microserviciomonopatines.client.GroqClient;
import org.example.microserviciomonopatines.dto.RespuestaApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class IaService {

    @PersistenceContext
    private EntityManager entityManager;

    private final CuentasClient cuentasClient;
    private final GroqClient groqChatClient;
    private final String CONTEXTO_SQL;

    private static final Logger log = LoggerFactory.getLogger(IaService.class);

    // Patrón para aceptar SOLO SELECT/INSERT/UPDATE/DELETE
    private static final Pattern SQL_ALLOWED =
            Pattern.compile("(?is)\\b(SELECT|INSERT|UPDATE|DELETE)\\b[\\s\\S]*?;");

    // Bloquear comandos peligrosos
    private static final Pattern SQL_FORBIDDEN =
            Pattern.compile("(?i)\\b(DROP|TRUNCATE|ALTER|CREATE|GRANT|REVOKE)\\b");

    @Autowired
    public IaService(CuentasClient cuentasClient, GroqClient groqChatClient) {
        this.cuentasClient = cuentasClient;
        this.groqChatClient = groqChatClient;
        this.CONTEXTO_SQL = cargarEsquemaSQL("esquema_completo.sql");
    }

    private String cargarEsquemaSQL(String archivo) {
        try (InputStream inputStream = new ClassPathResource(archivo).getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error al leer esquema SQL: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ResponseEntity<?> procesarPrompt(String promptUsuario, Long idCuenta) {

        // 🟣 VALIDACIÓN PREMIUM
        if (!cuentasClient.esPremium(idCuenta)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new RespuestaApi<>(false,
                            "Solo usuarios PREMIUM pueden usar el chatbot.", null));
        }

        try {
            // 🟦 Construcción del prompt
            String promptFinal = """
                    Este es el esquema de mi base de datos MySQL:
                    %s

                    Basándote exclusivamente en este esquema, devolvé UNA sentencia SQL válida
                    (SELECT/INSERT/UPDATE/DELETE), sin texto adicional.

                    %s
                    """.formatted(CONTEXTO_SQL, promptUsuario);

            log.info("PROMPT ---> \n{}", promptFinal);

            // 🧠 Llamada al modelo Groq
            String respuestaIa = groqChatClient.preguntar(promptFinal);
            log.info("RESPUESTA GROQ ---> \n{}", respuestaIa);

            // 🟡 Extraer SQL
            String sql = extraerConsultaSQL(respuestaIa);
            if (sql == null || sql.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new RespuestaApi<>(false,
                                "No se encontró SQL válida en la respuesta.", null));
            }

            log.info("SQL EXTRAÍDA ---> {}", sql);

            // Quitar el ";"
            String sqlExec = sql.endsWith(";") ? sql.substring(0, sql.length() - 1) : sql;

            // 🟢 Ejecutar según tipo
            if (sqlExec.toUpperCase().startsWith("SELECT")) {
                List<Object[]> filas =
                        entityManager.createNativeQuery(sqlExec).getResultList();
                return ResponseEntity.ok(new RespuestaApi<>(true, "SELECT OK", filas));

            } else {
                int filas = entityManager.createNativeQuery(sqlExec).executeUpdate();
                return ResponseEntity.ok(new RespuestaApi<>(true, "DML OK", filas));
            }

        } catch (Exception e) {
            log.error("Error procesando prompt", e);
            return ResponseEntity.internalServerError()
                    .body(new RespuestaApi<>(false, e.getMessage(), null));
        }
    }

    // 🟥 Extractor seguro de SQL
    private String extraerConsultaSQL(String respuesta) {
        if (respuesta == null) return null;

        Matcher m = SQL_ALLOWED.matcher(respuesta);
        if (!m.find()) return null;

        String sql = m.group().trim();

        int pos = sql.indexOf(';');
        if (pos > -1) {
            sql = sql.substring(0, pos + 1);
        }

        if (SQL_FORBIDDEN.matcher(sql).find()) {
            return null;
        }

        return sql;
    }
}
