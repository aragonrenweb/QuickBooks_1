/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 *
 * @author nmohamed
 */
public class CreateLessonControlador extends MultiActionController{
    
      Connection cn;
      
//      private ServletContext servlet;
    
    private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
    
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createlesson");
       
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        mv.addObject("createlesson", this.getStudents());
         Statement st = this.cn.createStatement();
         ResultSet rs = st.executeQuery("SELECT GradeLevel FROM AH_ZAF.dbo.GradeLevels");
         List <String> grades = new ArrayList();
         while(rs.next())
         {
         grades.add(rs.getString("GradeLevel"));
         }
        mv.addObject("gradelevels", grades);
        
        return mv;
    }
     public ModelAndView levellist(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createlesson");
       
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
         Statement st = this.cn.createStatement();
         ResultSet rs = st.executeQuery("SELECT GradeLevel FROM AH_ZAF.dbo.GradeLevels");
         List <String> grades = new ArrayList();
         while(rs.next())
         {
         grades.add(rs.getString("GradeLevel"));
         }
        mv.addObject("gradelevels", grades);
       
        
        return mv;
    }
    public ModelAndView studentlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createlesson");
       
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        List <Students> studentsgrades = new ArrayList();
        studentsgrades =this.getStudentslevel(hsr.getParameter("seleccion"));
         mv.addObject("createlesson",studentsgrades );
        
        return mv;
    }
    public ModelAndView subjectlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createlesson");
        List<String> subjects = new ArrayList<>();
       try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        
        
            
             Statement st = this.cn.createStatement();
             int levelid = 0;
            String consulta = "SELECT GradeLevelID FROM AH_ZAF.dbo.GradeLevels where GradeLevel ='"+hsr.getParameter("seleccion1")+"'";
            ResultSet rs = st.executeQuery(consulta);
          
            while (rs.next())
            {
                levelid = rs.getInt("GradeLevelID");
            }
            cn.close();
            dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
             this.cn = dataSource.getConnection();
             st = this.cn.createStatement();
          ResultSet rs1 = st.executeQuery("select nombre_subject from subject where id_level="+levelid);
           while (rs1.next())
            {
                subjects.add(rs1.getString("nombre_subject"));
            }
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Subjects: " + ex);
        }
        
      
         mv.addObject("subjects",subjects );
        
        return mv;
    }
     public ArrayList<Students> getStudents() throws SQLException
    {
//        this.conectarOracle();
        ArrayList<Students> listaAlumnos = new ArrayList<>();
        try {
            
             Statement st = this.cn.createStatement();
             
            String consulta = "SELECT * FROM AH_ZAF.dbo.Students where Status = 'Enrolled'";
            ResultSet rs = st.executeQuery(consulta);
          
            while (rs.next())
            {
                Students alumnos = new Students();
                alumnos.setId_students(rs.getInt("StudentID"));
                alumnos.setNombre_students(rs.getString("FirstName")+","+rs.getString("LastName"));
                alumnos.setFecha_nacimiento(rs.getString("Birthdate"));
                alumnos.setFoto(rs.getString("PathToPicture"));
                alumnos.setLevel_id(rs.getString("GradeLevel"));
                alumnos.setPlacement("Placement");
                alumnos.setSubstatus("Substatus");
                listaAlumnos.add(alumnos);
            }
            //this.finalize();
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
        }
       
        return listaAlumnos;
    }
     public ArrayList<Students> getStudentslevel(String grade) throws SQLException
    {
//        this.conectarOracle();
        ArrayList<Students> listaAlumnos = new ArrayList<>();
        try {
            
             Statement st = this.cn.createStatement();
             
            String consulta = "SELECT * FROM AH_ZAF.dbo.Students where Status = 'Enrolled' and GradeLevel ='"+grade+"'";
            ResultSet rs = st.executeQuery(consulta);
          
            while (rs.next())
            {
                Students alumnos = new Students();
                alumnos.setId_students(rs.getInt("StudentID"));
                alumnos.setNombre_students(rs.getString("FirstName")+","+rs.getString("LastName"));
                alumnos.setFecha_nacimiento(rs.getString("Birthdate"));
                alumnos.setFoto(rs.getString("PathToPicture"));
                alumnos.setLevel_id(rs.getString("GradeLevel"));
                alumnos.setPlacement("Placement");
                alumnos.setSubstatus("Substatus");
                listaAlumnos.add(alumnos);
            }
            //this.finalize();
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
        }
       
        return listaAlumnos;
    }
     public ModelAndView createlesson(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createlesson");
       
         
        
       String[] studentIds = hsr.getParameterValues("destino[]");
       String args = hsr.getParameter("TXTnombreLessons");
       
       Createlesson c = new Createlesson(hsr.getServletContext());
       c.newlesson(studentIds,args);
        
        mv.addObject("message", "Lesson created");
        
        return mv;
    }


      
}


