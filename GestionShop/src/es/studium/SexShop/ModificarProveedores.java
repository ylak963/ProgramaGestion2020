package es.studium.SexShop;

import java.awt.Button;
import java.awt.Choice;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ModificarProveedores extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	Choice choProveedor = new Choice();
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");

	Connection con = null;
	String [] cadena ;
	int idProveedorModificar = 0;
	Registros registros = new Registros();
	Login logUsuario = new Login();

	public ModificarProveedores()
	{
		setTitle("Modificar Proveedor");
		setLayout(new FlowLayout());
		// Rellenar el Choice
		choProveedor.add("Seleccionar un Proveedor...");
		// Conectar BD
		Connection con = conectar();
		// Sacar los datos de la tabla proveedores
		// Rellenar el Choice
		cadena=(consultarProveedorChoice(con)).split("#"); //
		for(int i=0; i<cadena.length; i++)
		{
			choProveedor.add(cadena[i]);
		}
		
		// Cerrar la conexión
		//desconectar(con);		
		add(choProveedor);
		btnAceptar.addActionListener(this);
		btnLimpiar.addActionListener(this);
		add(btnAceptar);
		add(btnLimpiar);
		addWindowListener(this);
		setSize(400,300);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

	}

	public void actionPerformed(ActionEvent e)
	{		
		Object objetoPulsado = e.getSource();
		if(objetoPulsado.equals(btnLimpiar))
		{
			choProveedor.select(0);
		}
		else if(objetoPulsado.equals(btnAceptar))
		{
			if(choProveedor.getSelectedItem().equals("Seleccionar un proveedor..."))
			{
				// No ocurriría nada
			}
			else
			{
				// Coger el elemento seleccionado
				String [] tabla = choProveedor.getSelectedItem().split("-");
				// El idArticulo que quiero editar está en tabla[0]
				idProveedorModificar = Integer.parseInt(tabla[0]);
				new ModificarProveedoresDos(idProveedorModificar); 
			}
		}
	}

	public void windowActivated(WindowEvent e){}
	public void windowClosed(WindowEvent e){}
	public void windowClosing(WindowEvent e)
	{	
		setVisible(false);
		
	}
	public void windowDeactivated(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowOpened(WindowEvent e){}

	public Connection conectar()
	{
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/gestionshop?autoReconnect=true&useSSL=false";
		String user = "root";
		String password = "1234";
		Connection con = null;
		try 
		{
			// Cargar los controladores para el acceso a la BD
			Class.forName(driver);

			// Establecer la conexión con la BD empresa
			con = DriverManager.getConnection(url, user, password);
			if (con != null) 
			{
				System.out.println("Conectado a la base de datos");
			}
		} catch (SQLException ex) 
		{
			System.out.println("ERROR:La dirección no es válida o el usuario y clave");
			ex.printStackTrace();
		} catch (ClassNotFoundException cnfe) 
		{
			System.out.println("Error 1-" + cnfe.getMessage());
		}
		return con;
	}

	
	public String consultarProveedorChoice(Connection c)
	{
		String resultado = "";
		String usuario = logUsuario.txtUsuario.getText();
		try
		{
			String sentencia = "SELECT * FROM proveedores";
			//Crear una sentencia
			Statement stmt = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			ResultSet rs = stmt.executeQuery(sentencia);
			while (rs.next())
			{
			choProveedor.add(rs.getInt("idProveedor")+
						"-"+rs.getString("nombreProveedor"));
			}
			registros.registrarMovimiento(usuario,sentencia);
		}
		catch (SQLException sqle)
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		return (resultado);
	}
	
	public void desconectar(Connection con)
	{
		try
		{
			con.close();
		}
		catch(Exception e) {}
	}
}
