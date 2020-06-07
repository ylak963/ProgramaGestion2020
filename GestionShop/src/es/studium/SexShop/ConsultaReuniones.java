package es.studium.SexShop;

import java.awt.Button;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ConsultaReuniones extends Frame implements WindowListener, ActionListener 
{
	private static final long serialVersionUID = 1L;
	TextArea consulta = new TextArea(10,38);
	Button btnVolver = new Button("Volver");
	Button btnPdf = new Button("Exportar a PDF");
	Registros registros = new Registros();
	Login logUsuario = new Login();
	
	ConsultaReuniones()
	{
		setTitle("Consulta de reuniones");
		setLayout(new FlowLayout());

		// Conectar a la base de datos
		Connection con = conectar();

		// Seleccionar de la tabla reuniones
		// Sacar la información
		consulta.setText(consultarReuniones(con));

		// Cerrar la conexión
		desconectar(con);
		add(consulta);

		btnPdf.addActionListener(this);
		add(btnPdf);
		btnVolver.addActionListener(this);
		add(btnVolver);

		addWindowListener(this);
		setSize(300,300);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent ae)
	{
		Object objetoPulsadoPdf=ae.getSource();
		Object objetoPulsado=ae.getSource();

		if(objetoPulsado.equals(btnVolver))
		{
			setVisible(false);
		}

		else if(objetoPulsadoPdf.equals(btnPdf))
		{
			Document documento = new Document();
			try
			{
				//Se crear el OutputStream para el fichero donde queremos dejar el pdf
				FileOutputStream ficheroPdf = new FileOutputStream("Reuniones.pdf");
				PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(22);
				//Se abre el documento
				documento.open();
				Paragraph titulo = new Paragraph("Informe de Reuniones",FontFactory.getFont("arial",22,Font.ITALIC,BaseColor.GRAY));
				titulo.setAlignment(Element.ALIGN_CENTER);
				documento.add(titulo);
				//Sacar los datos
				Connection con = conectar();
				String [] cadena= consultarReuniones(con).split("\n");
				desconectar(con);
				PdfPTable tabla = new PdfPTable(3); // Se indica el número de columnas
				tabla.setSpacingBefore(5); // Espaciado ANTES de la tabla
				tabla.addCell("Identificador Reunión");
				tabla.addCell("Puntos");
				tabla.addCell("Fecha");
				//En cada posición de cadena tenemos un registro completo
				//Cadena [0]="1-puntos-fecha"
				String [] subCadena;
				//En subCadena, separamos cada campo por-
				//SubCadena[0]=1
				//SubCadena[1]=puntos
				//SubCadena[2]=fecha
				
				for(int i=0; i<cadena.length; i++)
				{
					subCadena = cadena[i].split("-");
					for(int j=0; j<3;j++)
					{
						tabla.addCell(subCadena[j]);
					}
				}
				documento.add(tabla);
				documento.close();
				//Abrimos el archivo PDF recién creado
				try
				{
					File path = new File("Reuniones.pdf");
					Desktop.getDesktop().open(path);
				}
				catch(IOException ex)
				{
					System.out.println("Se ha producido un error al abrir el archivo PDF");
				}
			}
			catch(Exception e)
			{
				System.out.println("Se ha producido un error al generar el archivo PDF");
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
		} 
		catch (SQLException ex) 
		{
			System.out.println("ERROR:La dirección no es válida o el usuario y clave");
			ex.printStackTrace();
		} 
		catch (ClassNotFoundException cnfe) 
		{
			System.out.println("Error 1-" + cnfe.getMessage());
		}
		return con;
	}

	public String consultarReuniones(Connection con)
	{
		String resultado = "";
		String [] fechaReunionEuropea;
		String usuario = logUsuario.txtUsuario.getText();

		try
		{
			String sqlSelect = "SELECT * FROM reuniones";
			//Crear una sentencia
			Statement stm = con.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			ResultSet rs = stm.executeQuery(sqlSelect);
			while (rs.next())
			{
				fechaReunionEuropea = (rs.getString("fechaReunion")).split("-");

				resultado = resultado + rs.getInt("idReunion")+
						"-"+rs.getInt("puntosReunion")+
						"-"+fechaReunionEuropea[2]+"/"+fechaReunionEuropea[1]+"/"+fechaReunionEuropea[0]+"\n";
			}
			registros.registrarMovimiento(usuario,sqlSelect);
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
