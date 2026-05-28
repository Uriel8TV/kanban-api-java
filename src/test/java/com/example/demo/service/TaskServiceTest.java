package com.example.demo.service;

import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock // Simulamos el repositorio para no tocar la base de datos real
    private TaskRepository taskRepository;

    @InjectMocks // Inyectamos el mock dentro de nuestro servicio real
    private TaskService taskService;

    @Test
    void createTask_ShouldSetStatusToToDo() {
        // 1. Arrange (Preparar los datos)
        Task inputTask = new Task();
        inputTask.setTitle("Aprender Mockito");

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("Aprender Mockito");
        savedTask.setStatus(TaskStatus.TO_DO);

        // Le decimos al mock: "Cuando alguien llame a save(), devuelve savedTask"
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // 2. Act (Ejecutar el método que queremos probar)
        Task result = taskService.createTask(inputTask);

        // 3. Assert (Verificar que el resultado es el esperado)
        assertNotNull(result);
        assertEquals(TaskStatus.TO_DO, result.getStatus());
        assertEquals("Aprender Mockito", result.getTitle());
    }
}