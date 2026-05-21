package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks") // Todas las URLs empezarán con este prefijo
@CrossOrigin(origins = "*") // Permite que un frontend (como tu portafolio) se conecte sin errores de CORS
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // 1. GET: Obtener todas las tareas
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    // 2. POST: Crear una nueva tarea
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        // Devolvemos un código 201 (Created) que es la mejor práctica al crear recursos
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    // 3. PUT: Actualizar solo el estado de una tarea (Moverla en el tablero)
    @PutMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long id, @RequestParam TaskStatus status) {
        try {
            Task updatedTask = taskService.updateTaskStatus(id, status);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            // Si el servicio no encuentra la tarea, devolvemos un error 404 (Not Found)
            return ResponseEntity.notFound().build();
        }
    }

    // 4. DELETE: Eliminar una tarea
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build(); // Código 204 (No Content)
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}