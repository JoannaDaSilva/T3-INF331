package com.tienda.fidelidad;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.tienda.fidelidad.data.ClienteData;
import com.tienda.fidelidad.data.CompraData;
import com.tienda.fidelidad.model.Cliente;
import com.tienda.fidelidad.model.Compra;
import com.tienda.fidelidad.services.ClienteService;
import com.tienda.fidelidad.services.CompraService;
import com.tienda.fidelidad.services.PuntosService;

/**
 * Clase principal que inicia y gestiona la aplicación de consola del programa de fidelidad.
 */
public class Main {

    // --- Instancias de los servicios y el scanner ---
    // Se declaran estáticos para ser accesibles desde los métodos de menú.
    private static final ClienteData clienteData = new ClienteData();
    private static final CompraData compraData = new CompraData();
    
    private static final PuntosService puntosService = new PuntosService(compraData);
    private static final ClienteService clienteService = new ClienteService(clienteData);
    private static final CompraService compraService = new CompraService(clienteData, compraData);
    
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Punto de entrada de la aplicación.
     */
    public static void main(String[] args) {
        System.out.println("Sistema de Puntos de Fidelidad Iniciado");
        bucleMenuPrincipal();
        scanner.close();
        System.out.println("Gracias por usar el sistema. ¡Hasta pronto!");
    }

    /**
     * Muestra y gestiona el menú principal de la aplicación.
     */
    private static void bucleMenuPrincipal() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Gestión de Clientes");
            System.out.println("2. Gestión de Compras");
            System.out.println("3. Mostrar Puntos / Nivel de un Cliente");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");

            switch (leerOpcion()) {
                case 1:
                    menuGestionClientes();
                    break;
                case 2:
                    menuGestionCompras();
                    break;
                case 3:
                    mostrarPuntosCliente();
                    break;
                case 4:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
            }
        }
    }

    /**
     * Muestra y gestiona el submenú de clientes.
     */
    private static void menuGestionClientes() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- GESTIÓN DE CLIENTES ---");
            System.out.println("1. Agregar Nuevo Cliente");
            System.out.println("2. Listar Todos los Clientes");
            System.out.println("3. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            switch (leerOpcion()) {
                case 1:
                    agregarCliente();
                    break;
                case 2:
                    listarClientes();
                    break;
                case 3:
                    volver = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    /**
     * Muestra y gestiona el submenú de compras.
     */
    private static void menuGestionCompras() {
         boolean volver = false;
        while (!volver) {
            System.out.println("\n--- GESTIÓN DE COMPRAS ---");
            System.out.println("1. Registrar Nueva Compra");
            System.out.println("2. Listar Compras de un Cliente");
            System.out.println("3. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            
            switch (leerOpcion()) {
                case 1:
                    registrarCompra();
                    break;
                case 2:
                    listarComprasDeCliente();
                    break;
                case 3:
                    volver = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    // --- Lógica de las acciones ---

    private static void agregarCliente() {
        try {
            System.out.print("Ingrese nombre del cliente: ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese correo del cliente: ");
            String correo = scanner.nextLine();
            
            Cliente nuevo = clienteService.agregarCliente(nombre, correo);
            System.out.println("Cliente agregado con éxito! ID: " + nuevo.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void listarClientes() {
        List<Cliente> clientes = clienteService.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("ℹNo hay clientes registrados.");
            return;
        }
        System.out.println("\n--- LISTA DE CLIENTES ---");
        for (Cliente c : clientes) {
            System.out.printf("ID: %d | Nombre: %s | Correo: %s | Puntos: %d | Nivel: %s%n",
                c.getId(), c.getNombre(), c.getCorreo(), c.getPuntos(), c.getNivel());
        }
    }

    private static void registrarCompra() {
        try {
            System.out.print("Ingrese ID del cliente: ");
            int idCliente = leerOpcion();
            
            // Buscamos al cliente para asegurar que existe y tener el objeto.
            Optional<Cliente> optCliente = clienteService.buscarClientePorId(idCliente);
            if (optCliente.isEmpty()) {
                System.out.println("Error: No se encontró un cliente con el ID " + idCliente);
                return;
            }
            Cliente cliente = optCliente.get();

            System.out.print("Ingrese monto de la compra: ");
            double monto = leerDouble();
            
            // 1. Calcular puntos ANTES de registrar la compra para aplicar bonos correctamente.
            int puntosGanados = puntosService.calcularPuntos(cliente, monto);
            
            // 2. Registrar la compra.
            Compra nuevaCompra = compraService.registrarCompra(idCliente, monto);
            System.out.println("Compra registrada con éxito. ID Compra: " + nuevaCompra.getIdCompra());

            // 3. Agregar los puntos al cliente y actualizarlo.
            cliente.agregarPuntos(puntosGanados);
            clienteService.actualizarCliente(cliente);
            
            System.out.printf("Puntos ganados en esta compra: %d. Total de puntos: %d. Nuevo Nivel: %s.%n",
                puntosGanados, cliente.getPuntos(), cliente.getNivel());

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void listarComprasDeCliente() {
        System.out.print("Ingrese ID del cliente: ");
        int idCliente = leerOpcion();

        List<Compra> compras = compraService.listarComprasPorCliente(idCliente);
        if (compras.isEmpty()) {
            System.out.println("ℹEl cliente con ID " + idCliente + " no tiene compras registradas.");
            return;
        }

        System.out.println("\n--- HISTORIAL DE COMPRAS (ID Cliente: " + idCliente + ") ---");
        for (Compra c : compras) {
            System.out.printf("ID Compra: %d | Fecha: %s | Monto: $%.2f%n",
                c.getIdCompra(), c.getFecha().toLocalDate(), c.getMonto());
        }
    }

    private static void mostrarPuntosCliente() {
        System.out.print("Ingrese ID del cliente: ");
        int idCliente = leerOpcion();
        
        Optional<Cliente> optCliente = clienteService.buscarClientePorId(idCliente);
        if (optCliente.isPresent()) {
            Cliente cliente = optCliente.get();
            System.out.println("\n--- DETALLES DEL CLIENTE ---");
            System.out.println("Nombre: " + cliente.getNombre());
            System.out.println("Puntos acumulados: " + cliente.getPuntos());
            System.out.println("Nivel de fidelidad: " + cliente.getNivel());
        } else {
            System.out.println("Error: No se encontró un cliente con el ID " + idCliente);
        }
    }
    
    // --- Métodos de ayuda para la lectura de consola ---
    
    private static int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static double leerDouble() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Se usará 0.0.");
            return 0.0;
        }
    }
}