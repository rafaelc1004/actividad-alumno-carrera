package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Alumno;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class AlumnoController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AlumnoController() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		String vista;
		switch(action) {
		case "form":
			vista ="/WEB-INF/JSP/vista/alumno/alumno-form.jsp";
			request.getRequestDispatcher(vista)
			.forward(request, response);
			break;
		case "listar":
			try {
				List<Alumno> alumnos = getAlumno();
				request.setAttribute("alumnos", alumnos);
				vista ="/WEB-INF/JSP/vista/alumno/alumno-listado.jsp";
				request.getRequestDispatcher(vista)
				.forward(request, response);
			}catch(SQLException | NamingException e) {
				response.sendError(500);
			}
			break;
		case "eliminar":
			try {
			int idAlumno = Integer.parseInt(request.getParameter("id"));
			eliminarAlumno(idAlumno);
			vista ="/WEB-INF/JSP/vista/alumno/alumno-form.jsp";
			response./WEB-INF/JSP/vista/alumno/alumno-listado.jsp
			}catch(SQLException e) {
				e.printStackTrace();
			}catch(NamingException ne) {
				ne.p
				response.sendError(500);
			}
			break;
		default:
				response.sendError(404);
		}
	}

	
	private void eliminarAlumno(int idAlumno) throws SQLException, NamingException {
		
		try (
				Connection conexion = getConexion();
				PreparedStatement declaracion = conexion.prepareStatement("delete alumno where id = ?");
				
			) {
				declaracion.setInt(1, idAlumno);
				
				int filasEliminadas = declaracion.executeUpdate();
			}
		
	}
	
	public void updateAlumno() {
		
		try {
			Connection conexion = getConexion();
			conexion.prepareStatement("update alumno set nombre = ?, carrera = ? where id =?");
		}catch(SqlException e) {
			
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombre = request.getParameter("nombre");
		String carrera = request.getParameter("carrera");
		int id=0;
		try {
			id = Integer.parseInt(request.getParameter("id"));
			
		}catch(Exception e) {
			System.err.println("id se setea a 0 de manera automatica");
		}
		
		if(id == 0) {
			Alumno alumnoNuevo = new Alumno(nombre, carrera);
			
			try {
			crearAlumno(alumnoNuevo);
			}catch(SQLException | NamingException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Connection getConexion()throws NamingException, SQLException {
		InitialContext contextoInicial = new InitialContext();
		DataSource dataSource = (DataSource) contextoInicial.lookup("java:comp/env/jdbc/postgres");
		return dataSource.getConnection();
	}
	
	public void crearAlumno(Alumno alumno) throws SQLException, NamingException {
		try (
			Connection conexion = getConexion();
			PreparedStatement declaracion = conexion.prepareStatement("INSERT INTO alumnos(nombre, carrera) VALUES(?, ?)");
		) {
			declaracion.setString(1, alumno.getNombre());
			declaracion.setString(2, alumno.getCarrera());
			int filasInsertadas = declaracion.executeUpdate();
		}
	}
	
	public List<Alumno> getAlumno() throws SQLException, NamingException{
		try (
				Connection conexion = getConexion();
				Statement declaracion = conexion.createStatement();
			) {
				ResultSet rs = declaracion.executeQuery("Select * from alumnos");
				List<Alumno> alumnos = new ArrayList<>();
				while(rs.next()) {
					int id =rs.getInt("id");
					String nombre = rs.getString("nombre");
					String carrera = rs.getString("carrera");
					Alumno alumno = new Alumno(id, nombre, carrera);
					
					 alumnos.add(alumno);
				}
				return alumnos;
				
				
		}
		
		
	}

}
