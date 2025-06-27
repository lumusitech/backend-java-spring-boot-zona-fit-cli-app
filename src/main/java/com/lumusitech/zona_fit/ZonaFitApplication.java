package com.lumusitech.zona_fit;

import com.lumusitech.zona_fit.model.Customer;
import com.lumusitech.zona_fit.service.ICustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class ZonaFitApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ZonaFitApplication.class);
    @Autowired
    private ICustomerService customerService;

    public static void main(String[] args) {
        logger.info("Iniciando aplicación...");
        SpringApplication.run(ZonaFitApplication.class, args);
        logger.info("Aplicación finalizada");
    }

    private static int showMenu(Scanner scanner) {
        logger.info("");
        logger.info("*** Aplicación Zona Fit (GYM) ***");
        logger.info("""                         
                1. Listar clientes
                2. Buscar Cliente
                3. Agregar cliente
                4. Actualiar cliente
                5. Eliminar cliente
                6. Salir
                Elige una opción:\s""");
        return Integer.parseInt(scanner.nextLine());
    }

    private boolean handleOption(Scanner scanner, Integer option) {
        var exit = false;

        switch (option) {
            case 1 -> {
                logger.info("--- Lista de Clientes ---");
                List<Customer> customers = this.customerService.findAll();
                customers.forEach(customer -> logger.info(customer.toString()));
                break;
            }
            case 2 -> {
                logger.info("--- Buscar Cliente ---");
                Integer id = getId(scanner);
                var customer = this.customerService.findById(id);
                if (customer != null) logger.info("Cliente encontrado: {}", customer);
                else logger.info("Cliente con id {} NO encontrado!", id);
                break;
            }
            case 3 -> {
                logger.info("--- Guardar Cliente ---");
                saveCustomer(scanner);
                break;
            }
            case 4 -> {
                logger.info("--- Actualizar Cliente ---");
                updateCustomer(scanner);
                break;
            }
            case 5 -> {
                logger.info("--- Borrar Cliente ---");
                Integer id = getId(scanner);
                Customer customer = new Customer();
                customer.setId(id);
                this.customerService.delete(customer);
                break;
            }
            case 6 -> {
                logger.info("Gracias por usar la aplicación Zona Fit App");
                exit = true;
                break;
            }
            default -> logger.info("Opción inválida.");
        }
        return exit;
    }

    @Override
    public void run(String... args) throws Exception {
        zonaFitApp();
    }

    private void zonaFitApp() {
        var exit = false;
        Scanner scanner = new Scanner(System.in);

        while (!exit) {
            try {
                var option = showMenu(scanner);
                exit = handleOption(scanner, option);
                logger.info("");
            } catch (Exception e) {
                logger.info("Error al mostrar las opciones del menú");
            }
        }
    }

    private void saveCustomer(Scanner scanner) {
        String name = getData("Nombre: ", scanner);
        String lastname = getData("Apellido: ", scanner);
        Integer membership = getMembership(scanner);
        Customer customer = new Customer(null, name, lastname, membership);
        this.customerService.save(customer);
        logger.info("Cliente guardado: {}", customer);
    }

    private void updateCustomer(Scanner scanner) {
        Integer id = getId(scanner);
        String name = getData("Nombre: ", scanner);
        String lastname = getData("Apellido: ", scanner);
        Integer membership = getMembership(scanner);
        Customer customer = new Customer(id, name, lastname, membership);
        this.customerService.save(customer);
        logger.info("Cliente actualizado: {}", customer);
    }

    private Integer getId(Scanner scanner) {
        logger.info("ID: ");
        Integer id = null;
        try {
            id = Integer.parseInt(scanner.nextLine());

        } catch (Exception e) {
            logger.info("Error: ID debe ser un valor numérico");
        }

        return id;
    }

    private Integer getMembership(Scanner scanner) {
        logger.info("Membership: ");
        Integer membership = null;

        try {
            membership = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            logger.info("La membresía debe ser un valor numérico");
        }

        return membership;
    }

    private String getData(String label, Scanner scanner) {
        logger.info(label);
        return scanner.nextLine();
    }
}
