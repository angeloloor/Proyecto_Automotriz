import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class paginaPrincipal extends JFrame{
    private JButton verproducto;
    private JButton actualizarproducto;
    private JButton crearproducto;
    private JButton eliminarproducto;
    private JPanel panel2;

    public paginaPrincipal() {

        setTitle("Login del sistema");
        setContentPane(panel2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setPreferredSize(new Dimension(300, 300));
        setMinimumSize(new Dimension(300, 300));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);


            verproducto.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    new LeerProducto();
                    setVisible(false);
                }
            });
            actualizarproducto.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    new ActualizarProducto();
                    setVisible(false);
                }
            });

            crearproducto.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    new CrearProducto();
                    setVisible(false);
                }
            });

            eliminarproducto.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    new EliminarProducto();
                    setVisible(false);
                }
            });
    }
}

