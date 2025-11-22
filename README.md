# GameLluviaNewVersionV2

Un juego de acción y habilidad estilo "atrapar la lluvia". El objetivo principal es controlar un tarro para recolectar gotas buenas, esquivar las malas y sobrevivir el tiempo suficiente para enfrentarte al poderoso Rey Tormenta.

> **Estado del Proyecto:** Versión Avanzada con Batalla de Jefe, PowerUps y Patrones de Diseño aplicados.

---

## Descripción del Juego

En esta nueva versión, el juego no es infinito. Debes demostrar tu habilidad para alcanzar el puntaje necesario e invocar al jefe final.

### Objetivo
1.  **Recolección:** Atrapa las gotas azules para sumar puntos.
2.  **Supervivencia:** Evita las gotas dañinas y proyectiles.
3.  **Batalla de Jefe:** Al llegar a **200 Puntos**, aparecerá el **Rey Tormenta**.
4.  **Victoria:** Debes utilizar el escudo y tus reflejos para devolver el daño o sobrevivir hasta derrotar al jefe.

### PowerUps y Habilidades
El juego cuenta con un sistema de poderes gestionado dinámicamente:
* **Escudo (Shield):** Te protege del daño temporalmente. Si el jefe te ataca mientras tienes el escudo, ¡le devolverás el daño!
* **Limpieza de Pantalla (Clean Screen):** Elimina todos los enemigos y gotas malas de la pantalla instantáneamente.
* **Vida Extra:** Recupera salud perdida.

---

## Controles

### Básicos
| Tecla | Acción |
| :--- | :--- |
| **Flecha Izquierda** | Mover el tarro a la izquierda. |
| **Flecha Derecha** | Mover el tarro a la derecha. |
| **Pausa Automática** | El juego se pausa si cambias de ventana. |

### Debug / Trucos (Development)
Teclas habilitadas para pruebas y demostración:
| Tecla | Acción |
| :--- | :--- |
| **G** | Activar/Desactivar "Modo Dios" (Invencibilidad). |
| **S** | Activar manualmente el Escudo. |
| **C** | Activar manualmente Limpiar Pantalla. |
| **B** | Forzar la aparición del Boss (si no está activo). |

---

## Detalles Técnicos y Patrones de Diseño

Este proyecto ha sido refactorizado para implementar buenas prácticas de programación orientada a objetos y patrones de diseño:

* **Singleton:** Utilizado en la clase `Assets` para la gestión centralizada y eficiente de recursos (texturas, sonidos).
* **Abstract Factory (`GameLevelFactory`):** Permite la creación de familias de objetos (gotas, enemigos, proyectiles del jefe) desacoplando la lógica de creación de la lógica del juego.
* **Strategy:** Implementado para definir diferentes comportamientos de movimiento en los objetos que caen (caída normal, zig-zag, etc.).
* **Composition & Encapsulation (`PowerManager`):** Gestión modular de los poderes activos, separando la lógica de duración y efectos de la clase principal del juego.

---

## Requisitos del Sistema

* **Java Development Kit (JDK):** Versión **11** (Requerido).
* **Sistema Operativo:** Windows, macOS o Linux.
* **Memoria:** Mínimo 512MB RAM libres.

---

## Instalación y Ejecución

El proyecto utiliza **Gradle** con un Wrapper incluido, por lo que no necesitas instalar nada extra más que el JDK 11.

### Opción 1: Ejecutar desde Terminal (Recomendado)

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/Danilo-Alvarez-L/GameLluviaNewVersionV2-main.git](https://github.com/Danilo-Alvarez-L/GameLluviaNewVersionV2-main.git)
    cd GameLluviaNewVersionV2-main
    ```

2.  **Ejecutar el juego:**
    * **En Windows:**
        ```bash
        .\gradlew.bat lwjgl3:run
        ```
    * **En macOS/Linux:**
        ```bash
        chmod +x gradlew
        ./gradlew lwjgl3:run
        ```

### Opción 2: Importar en Eclipse

1.  Asegúrate de tener configurado el **JDK 11** en `Window > Preferences > Java > Installed JREs`.
2.  Ve a `File > Import > Gradle > Existing Gradle Project`.
3.  Selecciona la carpeta raíz del proyecto.
4.  Una vez importado, navega a: `lwjgl3/src/main/java/.../Lwjgl3Launcher.java`.
5.  Click derecho -> `Run As` -> `Java Application`.

---

## Estructura del Proyecto (LibGDX)

* **core/**: Contiene toda la lógica del juego, clases, pantallas y mecánicas.
* **lwjgl3/**: Contiene el lanzador para escritorio (PC/Mac/Linux).
* **assets/**: Imágenes, sonidos y música.
 ## LINK AL REPOSITORIO:
https://github.com/Danilo-Alvarez-L/GameLluviaNewVersionV2.git
---
*Desarrollado con LibGDX y Java 11.*