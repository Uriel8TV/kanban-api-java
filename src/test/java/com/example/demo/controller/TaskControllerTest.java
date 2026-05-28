package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // Configura automáticamente el MockMvc para simular peticiones HTTP
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc; // Nuestra herramienta para "disparar" peticiones a los endpoints

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos Java a texto JSON y viceversa

    // Limpiamos la base de datos en memoria antes de cada prueba para tener un entorno fresco
    @BeforeEach
    void cleanDatabase() {
        taskRepository.deleteAll();
    }

    @Test
    void createTask_ShouldReturn201AndTheCreatedTask() throws Exception {
        // 1. Arrange: Preparamos la tarea que vamos a enviar en el cuerpo de la petición (Body)
        Task newTask = new Task();
        newTask.setTitle("Configurar CI/CD");
        newTask.setDescription("Crear un pipeline con GitHub Actions");

        // Convertimos el objeto Java a un string JSON real
        String taskJson = objectMapper.writeValueAsString(newTask);

        // 2. Act & 3. Assert: Ejecutamos la petición POST y validamos la respuesta al mismo tiempo
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated()) // Esperamos un 201 Created
                .andExpect(jsonPath("$.id").exists()) // Esperamos que la base de datos le haya asignado un ID
                .andExpect(jsonPath("$.title").value("Configurar CI/CD")) // El título debe coincidir
                .andExpect(jsonPath("$.status").value(TaskStatus.TO_DO.toString())); // El servicio debe haber forzado el estado
    }
}