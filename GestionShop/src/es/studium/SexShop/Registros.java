package es.studium.SexShop;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Registros
{
	public void registrarMovimiento(String usuario, String mensaje)
	{
		try
		{
			//Destino de los datos
			FileWriter fw = new FileWriter("registros.log",true);
			//Buffer de escritura
			BufferedWriter bw = new BufferedWriter(fw);
			//Objeto para la escritura
			PrintWriter salida = new PrintWriter(bw);
			//Guardamos la fecha y hora actuales
			Date fechaHoraActual = new Date();
			//Formato fecha
			DateFormat fechaHoraFormato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			salida.print("["+fechaHoraFormato.format(fechaHoraActual)+"]");
			salida.print("["+usuario+"]");
			salida.println("["+mensaje+"]");
			//Cerrar el objeto salida, el objeto bw y el fw
			salida.close();
			bw.close();
			fw.close();			
		}
		catch(IOException i)
		{
			System.out.println("Se produjo un error de archivo");
		}
	}
}
