package es.studium.SexShop;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ModificarArticulos extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	Label lblArticulo = new Label("Articulo a modificar:");
	Choice choArticulo = new Choice();
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");
	Button btnEditar = new Button("Editar");

	Toolkit t = Toolkit.getDefaultToolkit();
	Dialog dlgBajaArticulos = new Dialog (this,"Mensaje",true);
	Label mensaje = new Label("");

	Registros registros = new Registros();
	Login logUsuario = new Login();

	ModificarArticulos()
	{
		setTitle("Modificar articulos");
		setLayout(new FlowLayout());
		// Montar el Choice
		choArticulo.add("Seleccionar uno... ");

		// Conectar a la base de datos
		Connection con = conectar();

		// Sacar los datos de la tabla articulos
		// Rellenar el Choice
		String sqlSelect = "SELECT * FROM articulos";
		try {
			// CREAR UN STATEMENT PARA UNA CONSULTA SELECT
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlSelect);
			while (rs.next()) 
			{
				choArticulo.add(rs.getInt("idArticulo")+
						"-"+rs.getString("nombreArticulo")/*+
						"-"+rs.getString("descripcionArticulo")+
						"-"+rs.getString("precioArticulo")*/);
			}
			rs.close();
			stmt.close();
		} 
		catch (SQLException ex) 
		{
			System.out.println("ERROR:al consultar");
			ex.printStackTrace();
		}
		// Cerrar la conexión
		desconectar(con);

		add(choArticulo);
		btnAceptar.addActionListener(this);
		btnLimpiar.addActionListener(this);
		add(btnAceptar);
		add(btnLimpiar);		
		addWindowListener(this);
		setSize(280,300);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{		
		Object objetoPulsado = e.getSource();
		if(objetoPulsado.equals(btnLimpiar))
		{
			choArticulo.select(0);
		}
		else if(objetoPulsado.equals(btnAceptar))
		{
			// Crear un Frame igual que el ALTA
			Label lblNombreArticulo = new Label("Nombre:");
			TextField txtNombreArticulo = new TextField(20);
			Label lblTamanioArticulo = new Label("Tamaño:");
			TextField txtTamanioArticulo = new TextField(20);
			Label lblDescripArticulo = new Label("Descripción:");
			TextField txtDescripArticulo = new TextField(20);
			Label lblPrecioArticulo = new Label("Precio:");
			TextField txtPrecioArticulo = new TextField(20);
			Label lblidProvFK = new Label("IDproveedor:");
			TextField txtidProvFK = new TextField(20);
			//Button btnAceptar = new Button("Aceptar");
			//Button btnLimpiar = new Button("Limpiar");
			Button btnEditar = new Button("Editar");
			//Button btnCancelar = new Button("Cancelar");

			setTitle("Editar");
			setLayout(new FlowLayout());
			add(lblNombreArticulo);
			add(txtNombreArticulo);
			add(lblTamanioArticulo);
			add(txtTamanioArticulo);
			add(lblDescripArticulo);
			add(txtDescripArticulo);
			add(lblPrecioArticulo);
			add(txtPrecioArticulo);
			add(lblidProvFK);
			add(txtidProvFK);
			
			btnAceptar.addActionListener(this);
			btnLimpiar.addActionListener(this);
			btnEditar.addActionListener(this);
			add(btnAceptar);
			add(btnLimpiar);
			add(btnEditar);
			//btnCancelar.addActionListener(this);
			addWindowListener(this);

			setSize(240,400);

			setResizable(true);
			setLocationRelativeTo(null);
			setVisible(true);
			// Pero relleno-->
			txtNombreArticulo.getText();
			txtTamanioArticulo.getText();
			txtDescripArticulo.getText();
			txtPrecioArticulo.getText();
			// Conectar a la base de datos
			Connection con = conectar();
			// Seleccionar los datos del elemento
			String sql = "SELECT * FROM articulos"; 
			try
			{
				Statement sta = con.createStatement();

				ResultSet rs = sta.executeQuery(sql);
				rs.next();
				//Poner en los TextField los valores obtenidos
				txtNombreArticulo.setText(rs.getString("nombreArticulo"));
				txtTamanioArticulo.setText(Integer.toString(rs.getInt("tamanioArticulo")));
				txtDescripArticulo.setText(rs.getString("descripcionArticulo"));
				txtPrecioArticulo.setText(Double.toString((double) rs.getDouble("precioArticulo")));
				txtTamanioArticulo.setText(Integer.toString(rs.getInt("tamanioArticulo")));
				txtidProvFK.setText(Integer.toString(rs.getInt("idProveedorFK")));
				
				// seleccionado
				// Mostrarlos
			}
			catch(SQLException er)
			{
				System.out.println("Error en la sentencia SQL");
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

	public int borrar(Connection con, int idArticulo)
	{
		int respuesta = 0;
		String sql = "DELETE FROM articulos WHERE idArticulo = " + idArticulo;
		System.out.println(sql);
		try 
		{
			// Creamos un STATEMENT para una consulta SQL INSERT.
			Statement sta = con.createStatement();
			sta.executeUpdate(sql);
			sta.close();
		} 
		catch (SQLException ex) 
		{
			System.out.println("ERROR:al hacer un Delete");
			ex.printStackTrace();
			respuesta = 1;
		}
		return respuesta;
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
