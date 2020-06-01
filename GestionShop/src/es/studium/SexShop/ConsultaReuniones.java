package es.studium.SexShop;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConsultaReuniones extends Frame implements ActionListener, WindowListener
{
	private static final long serialVersionUID = 1L;
	TextArea consulta = new TextArea(10,38);
	Button btnVolver = new Button("Volver");
	Button btnPdf = new Button("Exportar a PDF");


	ConsultaReuniones()
	{
		setTitle("Consulta de Reuniones");
		setLayout(new FlowLayout());

		// Conectar a la base de datos
		Connection con = conectar();

		// Seleccionar de la tabla reuniones
		// Sacar la información
		rellenarTextArea(con, consulta);

		// Cerrar la conexión
		desconectar(con);
		consulta.setEditable(false);
		add(consulta);
		add(btnVolver);
		add(btnPdf);
		btnVolver.addActionListener(this);
		btnPdf.addActionListener(this);
		addWindowListener(this);
		setSize(300,300);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		Object objetoPulsado = e.getSource();
		if(objetoPulsado.equals(btnVolver))
		{
			setVisible(false);
		}
		else
		{
			System.out.println("Exportando a PDF...");
		}
	}
	public void windowActivated(WindowEvent e){}
	public void windowClosed(WindowEvent e){}
	public void windowClosing(WindowEvent e)
	{
		//Solo cerramos esta ventana
		setVisible(false);
	}

	public void windowDeactivated(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowOpened(WindowEvent e)	{}

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

	public void rellenarTextArea(Connection con, TextArea t)
	{
		String sqlSelect = "SELECT * FROM reuniones";
		String [] fechaEuropea;
		try {
			// CREAR UN STATEMENT PARA UNA CONSULTA SELECT
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlSelect);
			while (rs.next()) 
			{
				if(t.getText().length()==0)
				{
					fechaEuropea= rs.getString("fechaReunion").split("-");
					t.setText(rs.getInt("idReunion")+
							"-"+rs.getString("puntosReunion")+" puntos"+"-con fecha del  "+
							fechaEuropea[2]+"\\"+fechaEuropea[1]+"\\"+fechaEuropea[0]);  //Se colocan dos barras porque sino tendria que interpretar lo que viene detras con una barra
				}
				else
				{
					fechaEuropea= rs.getString("fechaReunion").split("-");
					t.setText(t.getText() + "\n" +
							rs.getInt("idReunion")+
							"-"+rs.getInt("puntosReunion")+" puntos"+"-con fecha del  "+
							fechaEuropea[2]+"\\"+fechaEuropea[1]+"\\"+fechaEuropea[0]);
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex) 
		{
			System.out.println("ERROR:al consultar");
			ex.printStackTrace();
		}
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
