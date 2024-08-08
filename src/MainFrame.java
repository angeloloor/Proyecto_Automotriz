import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *Esta es la interfaz del menu para hacer las demas tareas
 * @author Angelo Loor
 * @version 2023 B
 */
public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("AutoPartsXpress - Gestión de Repuestos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu gestionMenu = new JMenu("Gestión");
        menuBar.add(gestionMenu);

        JMenuItem gestionClientesItem = new JMenuItem("Gestión de Clientes");
        gestionMenu.add(gestionClientesItem);

        JMenuItem gestionProductosItem = new JMenuItem("Gestión de Productos");
        gestionMenu.add(gestionProductosItem);

        JMenuItem gestionTransaccionesItem = new JMenuItem("Gestión de Transacciones");
        gestionMenu.add(gestionTransaccionesItem);

        JMenuItem gestionStockItem = new JMenuItem("Gestión de Stock");
        gestionMenu.add(gestionStockItem);

        JMenuItem visualizacionImagenesItem = new JMenuItem("Visualización de Imágenes");
        gestionMenu.add(visualizacionImagenesItem);

        gestionClientesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new GestionClientes().setVisible(true);
            }
        });

        gestionProductosItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new GestionProducto().setVisible(true);
            }
        });

        gestionTransaccionesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new GestionTransaccion().setVisible(true);
            }
        });

        gestionStockItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new GestionStock().setVisible(true);
            }
        });

        visualizacionImagenesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new VisualizacionImagen().setVisible(true);
            }
        });
    }

}
