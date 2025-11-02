# GameLluviaNewVersionV2

Un juego de "atrapar la lluvia" donde el objetivo es controlar un tarro para conseguir el puntaje más alto. Se deben esquivar las gotas malas, atrapar las buenas y utilizar poderes especiales para sobrevivir.
---

## Requisitos del Sistema

Para compilar y ejecutar este proyecto, el entorno de desarrollo debe cumplir los siguientes requisitos.

* **Java Development Kit (JDK) 11:** El proyecto está configurado para usar **Java 11**. Versiones más nuevas o más antiguas de Java pueden causar errores de compilación.
* **IDE (Eclipse/IntelliJ)** o **Terminal** para ejecutar Gradle.

---

## Configuración del Entorno (JDK 11)

El proyecto ya está configurado para compilar con Java 11. No necesita modificar los archivos de compilación, pero sí debe asegurarse de que su IDE (Eclipse) esté configurado para usar JDK 11.

### Opción 1: Configurar Eclipse (Recomendado)

Si Eclipse se está ejecutando con una versión de Java diferente (como 8 o 17), debe configurarlo para que reconozca y compile este proyecto con JDK 11.

**1. Añadir el JRE de JDK 11 a Eclipse:**
* Vaya a `Window > Preferences`.
* Navegue a `Java > Installed JREs`.
* Haga clic en `Add...`, seleccione `Standard VM`, y navegue hasta la carpeta de instalación de su **JDK 11**.
* Márquelo como el JRE por defecto (o selecciónelo para este proyecto).

**2. Establecer el Compilador de Java en 11:**
* Vaya a `Window > Preferences`.
* Navegue a `Java > Compiler`.
* En el menú desplegable "Compiler compliance level", seleccione **"11"**.
* Haga clic en `Apply and Close`.

**3. Importar el Proyecto:**
* Vaya a `File > Import...`.
* Seleccione `Gradle > Existing Gradle Project`.
* Navegue hasta la carpeta raíz del proyecto y siga los pasos del asistente.

### Opción 2: Configuración por Terminal (Línea de Comandos)

Si prefiere ejecutar el juego sin un IDE, debe asegurarse de que su variable de entorno `JAVA_HOME` apunte a JDK 11.

**1. Verificar su Versión de Java:**
Abra una terminal y ejecute `java -version`. Si no es la 11, configúrela.

**2. Cambiar su JDK Activo (Ejemplo):**
* **En Windows:** `set JAVA_HOME="C:\Program Files\Java\jdk-11.0.x"`
* **En macOS/Linux:** `export JAVA_HOME="/usr/lib/jvm/java-11-openjdk-amd64"`

---

## Cómo Instalar y Ejecutar

Este proyecto utiliza **Gradle** para gestionar las dependencias. No necesita instalar Gradle manualmente, ya que se utiliza el *Gradle Wrapper* (`gradlew`) incluido en el repositorio.

**1. Clonar o Descargar el Repositorio:**
```bash
git clone [https://github.com/Danilo-Alvarez-L/GameLluviaNewVersionV2-main.git](https://github.com/Danilo-Alvarez-L/GameLluviaNewVersionV2-main.git)
cd GameLluviaNewVersionV2-main


2. Ejecutar el Juego:

Desde Eclipse:

Una vez importado el proyecto, busque el módulo lwjgl3.

Navegue a lwjgl3/src/main/java/io/github/some_example_name/lwjgl3.

Haga clic derecho en Lwjgl3Launcher.java y seleccione Run As > Java Application.

Desde la Terminal (Línea de Comandos): Asegúrese de que su JDK 11 esté activo (ver sección de configuración).

En Windows:

Bash

.\gradlew.bat lwjgl3:run
En macOS o Linux:

Bash

# (Solo la primera vez, para dar permisos de ejecución)
chmod +x gradlew

# Ejecutar el proyecto
./gradlew lwjgl3:run
Controles del Juego
Flecha Izquierda: Mover el tarro a la izquierda.

Flecha Derecha: Mover el tarro a la derecha.

Pausa: El juego entra en modo de pausa (abriendo la PausaScreen) automáticamente cuando la ventana del juego pierde el foco (ej. al minimizar o cambiar de ventana).

Controles de Depuración (Debug)
G: Activa/Desactiva el "Modo Dios" (invencible).

S: Activa el poder de Escudo manualmente.

C: Activa el poder de Limpieza de Pantalla manually.

Detalles Técnicos
Motor de Juego: libGDX versión 1.12.1.

Lenguaje: Java 11.
