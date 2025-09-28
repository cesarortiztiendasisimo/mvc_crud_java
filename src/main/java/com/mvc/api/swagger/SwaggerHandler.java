package com.mvc.api.swagger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Swagger UI Handler - Serves Swagger documentation interface
 * Provides interactive API documentation for the User Management API
 */
public class SwaggerHandler implements HttpHandler {
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        
        if (path.equals("/swagger") || path.equals("/swagger/")) {
            serveSwaggerUI(exchange);
        } else if (path.equals("/swagger/api-docs")) {
            serveApiDocs(exchange);
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
    }
    
    public void serveSwaggerUI(HttpExchange exchange) throws IOException {
        String swaggerUI = generateSwaggerUI();
        
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        byte[] response = swaggerUI.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, response.length);
        
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response);
        outputStream.close();
    }
    
    public void serveApiDocs(HttpExchange exchange) throws IOException {
        String apiDocs = generateOpenApiSpec();
        
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        
        byte[] response = apiDocs.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, response.length);
        
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response);
        outputStream.close();
    }
    
    public String generateSwaggerUI() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>MVC User Management API - Swagger Documentation</title>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"https://unpkg.com/swagger-ui-dist@3.52.5/swagger-ui.css\" />\n" +
                "    <style>\n" +
                "        html {\n" +
                "            box-sizing: border-box;\n" +
                "            overflow: -moz-scrollbars-vertical;\n" +
                "            overflow-y: scroll;\n" +
                "        }\n" +
                "        *, *:before, *:after {\n" +
                "            box-sizing: inherit;\n" +
                "        }\n" +
                "        body {\n" +
                "            margin:0;\n" +
                "            background: #fafafa;\n" +
                "        }\n" +
                "        .swagger-ui .topbar {\n" +
                "            background-color: #2c3e50;\n" +
                "        }\n" +
                "        .swagger-ui .topbar .link {\n" +
                "            color: #ecf0f1;\n" +
                "        }\n" +
                "        #swagger-ui {\n" +
                "            max-width: 1200px;\n" +
                "            margin: 0 auto;\n" +
                "        }\n" +
                "        .custom-header {\n" +
                "            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
                "            color: white;\n" +
                "            padding: 20px;\n" +
                "            text-align: center;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .custom-header h1 {\n" +
                "            margin: 0;\n" +
                "            font-size: 2.5em;\n" +
                "        }\n" +
                "        .custom-header p {\n" +
                "            margin: 10px 0 0 0;\n" +
                "            font-size: 1.2em;\n" +
                "            opacity: 0.9;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"custom-header\">\n" +
                "        <h1>🚀 MVC User Management API</h1>\n" +
                "        <p>RESTful API with Swagger Documentation - Patrón MVC Implementation</p>\n" +
                "    </div>\n" +
                "    \n" +
                "    <div id=\"swagger-ui\"></div>\n" +
                "    \n" +
                "    <script src=\"https://unpkg.com/swagger-ui-dist@3.52.5/swagger-ui-bundle.js\"></script>\n" +
                "    <script src=\"https://unpkg.com/swagger-ui-dist@3.52.5/swagger-ui-standalone-preset.js\"></script>\n" +
                "    <script>\n" +
                "    window.onload = function() {\n" +
                "        const ui = SwaggerUIBundle({\n" +
                "            url: '/swagger/api-docs',\n" +
                "            dom_id: '#swagger-ui',\n" +
                "            deepLinking: true,\n" +
                "            presets: [\n" +
                "                SwaggerUIBundle.presets.apis,\n" +
                "                SwaggerUIStandalonePreset\n" +
                "            ],\n" +
                "            plugins: [\n" +
                "                SwaggerUIBundle.plugins.DownloadUrl\n" +
                "            ],\n" +
                "            layout: \"StandaloneLayout\",\n" +
                "            validatorUrl: null,\n" +
                "            tryItOutEnabled: true,\n" +
                "            supportedSubmitMethods: ['get', 'post', 'put', 'delete'],\n" +
                "            onComplete: function() {\n" +
                "                console.log('Swagger UI loaded successfully');\n" +
                "            },\n" +
                "            onFailure: function(data) {\n" +
                "                console.error('Failed to load Swagger UI', data);\n" +
                "            }\n" +
                "        });\n" +
                "    };\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }
    
    public String generateOpenApiSpec() {
        return "{\n" +
                "  \"openapi\": \"3.0.3\",\n" +
                "  \"info\": {\n" +
                "    \"title\": \"MVC User Management API\",\n" +
                "    \"description\": \"RESTful API para gestión de usuarios implementada con patrón MVC en Java. Esta API permite realizar operaciones CRUD (Create, Read, Update, Delete) sobre usuarios.\",\n" +
                "    \"version\": \"1.0.0\",\n" +
                "    \"contact\": {\n" +
                "      \"name\": \"MVC Development Team\",\n" +
                "      \"email\": \"mvc@example.com\"\n" +
                "    },\n" +
                "    \"license\": {\n" +
                "      \"name\": \"MIT\",\n" +
                "      \"url\": \"https://opensource.org/licenses/MIT\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"servers\": [\n" +
                "    {\n" +
                "      \"url\": \"http://localhost:9090/api\",\n" +
                "      \"description\": \"Development Server\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"name\": \"users\",\n" +
                "      \"description\": \"Operaciones relacionadas con usuarios\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"paths\": {\n" +
                "    \"/users\": {\n" +
                "      \"get\": {\n" +
                "        \"tags\": [\"users\"],\n" +
                "        \"summary\": \"Obtener todos los usuarios\",\n" +
                "        \"description\": \"Retorna una lista con todos los usuarios registrados en el sistema\",\n" +
                "        \"operationId\": \"getAllUsers\",\n" +
                "        \"responses\": {\n" +
                "          \"200\": {\n" +
                "            \"description\": \"Lista de usuarios obtenida exitosamente\",\n" +
                "            \"content\": {\n" +
                "              \"application/json\": {\n" +
                "                \"schema\": {\n" +
                "                  \"type\": \"array\",\n" +
                "                  \"items\": {\n" +
                "                    \"$ref\": \"#/components/schemas/User\"\n" +
                "                  }\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          \"500\": {\n" +
                "            \"description\": \"Error interno del servidor\",\n" +
                "            \"content\": {\n" +
                "              \"application/json\": {\n" +
                "                \"schema\": {\n" +
                "                  \"$ref\": \"#/components/schemas/Error\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      },\n" +
                "      \"post\": {\n" +
                "        \"tags\": [\"users\"],\n" +
                "        \"summary\": \"Crear un nuevo usuario\",\n" +
                "        \"description\": \"Crea un nuevo usuario en el sistema con los datos proporcionados\",\n" +
                "        \"operationId\": \"createUser\",\n" +
                "        \"requestBody\": {\n" +
                "          \"description\": \"Datos del usuario a crear\",\n" +
                "          \"required\": true,\n" +
                "          \"content\": {\n" +
                "            \"application/json\": {\n" +
                "              \"schema\": {\n" +
                "                \"$ref\": \"#/components/schemas/UserInput\"\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        },\n" +
                "        \"responses\": {\n" +
                "          \"201\": {\n" +
                "            \"description\": \"Usuario creado exitosamente\",\n" +
                "            \"content\": {\n" +
                "              \"application/json\": {\n" +
                "                \"schema\": {\n" +
                "                  \"$ref\": \"#/components/schemas/User\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          \"400\": {\n" +
                "            \"description\": \"Datos de entrada inválidos\",\n" +
                "            \"content\": {\n" +
                "              \"application/json\": {\n" +
                "                \"schema\": {\n" +
                "                  \"$ref\": \"#/components/schemas/Error\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          \"500\": {\n" +
                "            \"description\": \"Error interno del servidor\",\n" +
                "            \"content\": {\n" +
                "              \"application/json\": {\n" +
                "                \"schema\": {\n" +
                "                  \"$ref\": \"#/components/schemas/Error\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"/users/{id}\": {\n" +
                "      \"get\": {\n" +
                "        \"tags\": [\"users\"],\n" +
                "        \"summary\": \"Obtener usuario por ID\",\n" +
                "        \"description\": \"Retorna los datos de un usuario específico basado en su ID\",\n" +
                "        \"operationId\": \"getUserById\",\n" +
                "        \"parameters\": [\n" +
                "          {\n" +
                "            \"name\": \"id\",\n" +
                "            \"in\": \"path\",\n" +
                "            \"description\": \"ID único del usuario\",\n" +
                "            \"required\": true,\n" +
                "            \"schema\": {\n" +
                "              \"type\": \"integer\",\n" +
                "              \"format\": \"int64\",\n" +
                "              \"example\": 1\n" +
                "            }\n" +
                "          }\n" +
                "        ],\n" +
                "        \"responses\": {\n" +
                "          \"200\": {\n" +
                "            \"description\": \"Usuario encontrado\",\n" +
                "            \"content\": {\n" +
                "              \"application/json\": {\n" +
                "                \"schema\": {\n" +
                "                  \"$ref\": \"#/components/schemas/User\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          \"404\": {\n" +
                "            \"description\": \"Usuario no encontrado\",\n" +
                "            \"content\": {\n" +
                "              \"application/json\": {\n" +
                "                \"schema\": {\n" +
                "                  \"$ref\": \"#/components/schemas/Error\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          \"400\": {\n" +
                "            \"description\": \"ID inválido\",\n" +
                "            \"content\": {\n" +
                "              \"application/json\": {\n" +
                "                \"schema\": {\n" +
                "                  \"$ref\": \"#/components/schemas/Error\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      },\n" +
                "      \"put\": {\n" +
                "        \"tags\": [\"users\"],\n" +
                "        \"summary\": \"Actualizar usuario\",\n" +
                "        \"description\": \"Actualiza los datos de un usuario existente\",\n" +
                "        \"operationId\": \"updateUser\",\n" +
                "        \"parameters\": [\n" +
                "          {\n" +
                "            \"name\": \"id\",\n" +
                "            \"in\": \"path\",\n" +
                "            \"description\": \"ID único del usuario a actualizar\",\n" +
                "            \"required\": true,\n" +
                "            \"schema\": {\n" +
                "              \"type\": \"integer\",\n" +
                "              \"format\": \"int64\",\n" +
                "              \"example\": 1\n" +
                "            }\n" +
                "          }\n" +
                "        ],\n" +
                "        \"requestBody\": {\n" +
                "          \"description\": \"Nuevos datos del usuario\",\n" +
                "          \"required\": true,\n" +
                "          \"content\": {\n" +
                "            \"application/json\": {\n" +
                "              \"schema\": {\n" +
                "                \"$ref\": \"#/components/schemas/UserInput\"\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        },\n" +
                "        \"responses\": {\n" +
                "          \"200\": {\n" +
                "            \"description\": \"Usuario actualizado exitosamente\",\n" +
                "            \"content\": {\n" +
                "              \"application/json\": {\n" +
                "                \"schema\": {\n" +
                "                  \"$ref\": \"#/components/schemas/User\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          \"400\": {\n" +
                "            \"description\": \"Datos de entrada inválidos\",\n" +
                "            \"content\": {\n" +
                "              \"application/json\": {\n" +
                "                \"schema\": {\n" +
                "                  \"$ref\": \"#/components/schemas/Error\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          \"404\": {\n" +
                "            \"description\": \"Usuario no encontrado\",\n" +
                "            \"content\": {\n" +
                "              \"application/json\": {\n" +
                "                \"schema\": {\n" +
                "                  \"$ref\": \"#/components/schemas/Error\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      },\n" +
                "      \"delete\": {\n" +
                "        \"tags\": [\"users\"],\n" +
                "        \"summary\": \"Eliminar usuario\",\n" +
                "        \"description\": \"Elimina un usuario del sistema de forma permanente\",\n" +
                "        \"operationId\": \"deleteUser\",\n" +
                "        \"parameters\": [\n" +
                "          {\n" +
                "            \"name\": \"id\",\n" +
                "            \"in\": \"path\",\n" +
                "            \"description\": \"ID único del usuario a eliminar\",\n" +
                "            \"required\": true,\n" +
                "            \"schema\": {\n" +
                "              \"type\": \"integer\",\n" +
                "              \"format\": \"int64\",\n" +
                "              \"example\": 1\n" +
                "            }\n" +
                "          }\n" +
                "        ],\n" +
                "        \"responses\": {\n" +
                "          \"200\": {\n" +
                "            \"description\": \"Usuario eliminado exitosamente\",\n" +
                "            \"content\": {\n" +
                "              \"application/json\": {\n" +
                "                \"schema\": {\n" +
                "                  \"type\": \"object\",\n" +
                "                  \"properties\": {\n" +
                "                    \"message\": {\n" +
                "                      \"type\": \"string\",\n" +
                "                      \"example\": \"User deleted successfully\"\n" +
                "                    }\n" +
                "                  }\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          \"404\": {\n" +
                "            \"description\": \"Usuario no encontrado\",\n" +
                "            \"content\": {\n" +
                "              \"application/json\": {\n" +
                "                \"schema\": {\n" +
                "                  \"$ref\": \"#/components/schemas/Error\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          \"400\": {\n" +
                "            \"description\": \"ID inválido\",\n" +
                "            \"content\": {\n" +
                "              \"application/json\": {\n" +
                "                \"schema\": {\n" +
                "                  \"$ref\": \"#/components/schemas/Error\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"/users/search\": {\n" +
                "      \"get\": {\n" +
                "        \"tags\": [\"users\"],\n" +
                "        \"summary\": \"Buscar usuarios\",\n" +
                "        \"description\": \"Busca usuarios por nombre, email, teléfono o dirección\",\n" +
                "        \"operationId\": \"searchUsers\",\n" +
                "        \"parameters\": [\n" +
                "          {\n" +
                "            \"name\": \"q\",\n" +
                "            \"in\": \"query\",\n" +
                "            \"description\": \"Término de búsqueda\",\n" +
                "            \"required\": true,\n" +
                "            \"schema\": {\n" +
                "              \"type\": \"string\",\n" +
                "              \"minLength\": 1,\n" +
                "              \"example\": \"juan\"\n" +
                "            }\n" +
                "          }\n" +
                "        ],\n" +
                "        \"responses\": {\n" +
                "          \"200\": {\n" +
                "            \"description\": \"Resultados de búsqueda\",\n" +
                "            \"content\": {\n" +
                "              \"application/json\": {\n" +
                "                \"schema\": {\n" +
                "                  \"type\": \"array\",\n" +
                "                  \"items\": {\n" +
                "                    \"$ref\": \"#/components/schemas/User\"\n" +
                "                  }\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          \"400\": {\n" +
                "            \"description\": \"Parámetro de búsqueda requerido\",\n" +
                "            \"content\": {\n" +
                "              \"application/json\": {\n" +
                "                \"schema\": {\n" +
                "                  \"$ref\": \"#/components/schemas/Error\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"components\": {\n" +
                "    \"schemas\": {\n" +
                "      \"User\": {\n" +
                "        \"type\": \"object\",\n" +
                "        \"description\": \"Modelo de usuario completo con ID\",\n" +
                "        \"required\": [\"id\", \"name\", \"email\", \"phone\", \"address\"],\n" +
                "        \"properties\": {\n" +
                "          \"id\": {\n" +
                "            \"type\": \"integer\",\n" +
                "            \"format\": \"int64\",\n" +
                "            \"description\": \"ID único del usuario\",\n" +
                "            \"example\": 1\n" +
                "          },\n" +
                "          \"name\": {\n" +
                "            \"type\": \"string\",\n" +
                "            \"description\": \"Nombre completo del usuario\",\n" +
                "            \"minLength\": 2,\n" +
                "            \"maxLength\": 100,\n" +
                "            \"example\": \"Juan Pérez\"\n" +
                "          },\n" +
                "          \"email\": {\n" +
                "            \"type\": \"string\",\n" +
                "            \"format\": \"email\",\n" +
                "            \"description\": \"Correo electrónico del usuario\",\n" +
                "            \"maxLength\": 100,\n" +
                "            \"example\": \"juan.perez@email.com\"\n" +
                "          },\n" +
                "          \"phone\": {\n" +
                "            \"type\": \"string\",\n" +
                "            \"description\": \"Número de teléfono (formato colombiano)\",\n" +
                "            \"pattern\": \"^\\\\\\\\+?57\\\\\\\\s?[0-9]{3}\\\\\\\\s?[0-9]{3}\\\\\\\\s?[0-9]{4}$|^[0-9]{10}$\",\n" +
                "            \"example\": \"+57 300 123 4567\"\n" +
                "          },\n" +
                "          \"address\": {\n" +
                "            \"type\": \"string\",\n" +
                "            \"description\": \"Dirección de residencia\",\n" +
                "            \"maxLength\": 200,\n" +
                "            \"example\": \"Calle 123 #45-67, Bogotá\"\n" +
                "          }\n" +
                "        }\n" +
                "      },\n" +
                "      \"UserInput\": {\n" +
                "        \"type\": \"object\",\n" +
                "        \"description\": \"Datos requeridos para crear o actualizar un usuario\",\n" +
                "        \"required\": [\"name\", \"email\", \"phone\", \"address\"],\n" +
                "        \"properties\": {\n" +
                "          \"name\": {\n" +
                "            \"type\": \"string\",\n" +
                "            \"description\": \"Nombre completo del usuario\",\n" +
                "            \"minLength\": 2,\n" +
                "            \"maxLength\": 100,\n" +
                "            \"example\": \"Juan Pérez\"\n" +
                "          },\n" +
                "          \"email\": {\n" +
                "            \"type\": \"string\",\n" +
                "            \"format\": \"email\",\n" +
                "            \"description\": \"Correo electrónico del usuario\",\n" +
                "            \"maxLength\": 100,\n" +
                "            \"example\": \"juan.perez@email.com\"\n" +
                "          },\n" +
                "          \"phone\": {\n" +
                "            \"type\": \"string\",\n" +
                "            \"description\": \"Número de teléfono (formato colombiano)\",\n" +
                "            \"pattern\": \"^\\\\\\\\+?57\\\\\\\\s?[0-9]{3}\\\\\\\\s?[0-9]{3}\\\\\\\\s?[0-9]{4}$|^[0-9]{10}$\",\n" +
                "            \"example\": \"+57 300 123 4567\"\n" +
                "          },\n" +
                "          \"address\": {\n" +
                "            \"type\": \"string\",\n" +
                "            \"description\": \"Dirección de residencia\",\n" +
                "            \"maxLength\": 200,\n" +
                "            \"example\": \"Calle 123 #45-67, Bogotá\"\n" +
                "          }\n" +
                "        }\n" +
                "      },\n" +
                "      \"Error\": {\n" +
                "        \"type\": \"object\",\n" +
                "        \"description\": \"Modelo de respuesta de error\",\n" +
                "        \"required\": [\"error\"],\n" +
                "        \"properties\": {\n" +
                "          \"error\": {\n" +
                "            \"type\": \"string\",\n" +
                "            \"description\": \"Mensaje descriptivo del error\",\n" +
                "            \"example\": \"User not found\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }
}
