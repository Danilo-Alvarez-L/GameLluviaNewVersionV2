package io.github.some_example_name;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Gestiona los poderes activos, sus timers y expiración.
 * Cubre: GM1.6 (Composición y Encapsulamiento)
 */
public class PowerManager {

    private GameContext context;
    private List<Power> activePowers;
    
    // --- INICIO DE LA CORRECCIÓN ---
    // Lista para poderes que se acaban de añadir
    private List<Power> pendingPowers;
    // --- FIN DE LA CORRECCIÓN ---

    public PowerManager(GameContext context) {
        this.context = context;
        this.activePowers = new ArrayList<>();
        this.pendingPowers = new ArrayList<>(); // Inicializar la nueva lista
    }

    public void addPower(Power power) {
        // --- INICIO DE LA CORRECCIÓN ---
        // NO aplicar el poder inmediatamente.
        // Solo lo añadimos a la lista de "pendientes".
        this.pendingPowers.add(power);
        // power.apply(this.context); // <-- ESTA LÍNEA CAUSA EL CRASH
        // --- FIN DE LA CORRECCIÓN ---
    }

    /**
     * Debe ser llamado en el método update() principal del juego.
     * Gestiona los timers y la expiración de los poderes.
     * (MODIFICADO para aplicar poderes pendientes)
     * @param delta Tiempo desde el último frame.
     */
    public void update(float delta) {
        
        // --- INICIO DE LA CORRECCIÓN ---
        // 1. Aplicar y mover los poderes pendientes
        // Esto se ejecuta ANTES del bucle de render, evitando el crash.
        if (!pendingPowers.isEmpty()) {
            Iterator<Power> pendingIter = pendingPowers.iterator();
            while (pendingIter.hasNext()) {
                Power power = pendingIter.next();
                
                power.apply(this.context); // Aplicar el poder AHORA
                activePowers.add(power);   // Moverlo a la lista activa
                pendingIter.remove();      // Quitarlo de pendientes
            }
        }
        // --- FIN DE LA CORRECCIÓN ---

        // 2. Actualizar y expirar los poderes activos (lógica original)
        if (activePowers.isEmpty()) {
            return;
        }

        Iterator<Power> iterator = activePowers.iterator();
        while (iterator.hasNext()) {
            Power power = iterator.next();
            power.update(delta); // El poder actualiza su timer interno

            if (power.isFinished()) {
                power.revert(this.context); // Revierte el efecto (ej. apaga el escudo)
                iterator.remove(); // Elimina el poder de la lista activa
            }
        }
    }

    public boolean isShieldActive() {
        for (Power power : activePowers) {
            if (power.getId().equals("SHIELD") && !power.isFinished()) {
                return true;
            }
        }
        return false;
    }
}