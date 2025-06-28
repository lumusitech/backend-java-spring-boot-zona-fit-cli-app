package com.lumusitech.zona_fit;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.lumusitech.zona_fit.gui.ZonaFitForm;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class ZonaFitSwing {
    public static void main(String[] args) {
        // Dark Mode
        FlatDarculaLaf.setup();

        // Generate the factory Spring instance
        ConfigurableApplicationContext context = new SpringApplicationBuilder(ZonaFitSwing.class)
                .headless(false)
                .web(WebApplicationType.NONE)
                .run(args);

        // Create a Swing object after Spring factory get ready
        SwingUtilities.invokeLater(() -> {
            ZonaFitForm zonaFitForm = context.getBean(ZonaFitForm.class);
            zonaFitForm.setVisible(true);
        });
    }
}
