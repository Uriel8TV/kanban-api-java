package com.example.demo.service;

import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    // Inyectamos el Repository que acabamos de crear para poder usarlo
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // 1. Obtener todas las tareas
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // 2. Obtener tareas por su estado (ej. Todas las que están "IN_PROGRESS")
    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    // 3. Obtener una sola tarea por su ID
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    // 4. Crear una nueva tarea
    public Task createTask(Task task) {
        // Al crear, forzamos que siempre empiece en la columna TO_DO
        task.setStatus(TaskStatus.TO_DO);
        return taskRepository.save(task);
    }

    // 5. Mover una tarea de columna (Actualizar el estado)
    public Task updateTaskStatus(Long id, TaskStatus newStatus) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setStatus(newStatus);
                    return taskRepository.save(task);
                })
                // Si la tarea no existe, lanzamos una excepción clara
                .orElseThrow(() -> new RuntimeException("Tarea con ID " + id + " no encontrada"));
    }

    // 6. Eliminar una tarea
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Tarea con ID " + id + " no encontrada");
        }
        taskRepository.deleteById(id);
    }
}
