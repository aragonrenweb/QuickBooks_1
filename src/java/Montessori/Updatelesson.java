/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.WebApplicationContextUtils;
import quickbooksync.QBInvoice;

/**
 *
 * @author nmohamed
 */
public class Updatelesson {
    Connection cn;
          private ServletContext servlet;
    
  
     public Updatelesson(ServletContext s)
    {
        this.servlet = s;
    }   

    public Updatelesson() {
      
    }
  
          
    
    private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
    public void updatelesson(String[] studentIds,Lessons newlessons) throws SQLException
    { String lessonid= null;
    List<String> equipmentids;
    List<String> oldstuds = new ArrayList<>();
    List<String> addstuds = new ArrayList<>();
    List<String> delstuds = new ArrayList<>();
    DriverManagerDataSource dataSource;
    try{
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",this.servlet);
       this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
        String test = "update lessons set name = '"+newlessons.getName()+"',level_id = '"+newlessons.getLevel().getName()+"' ,subject_id = '"+newlessons.getSubject().getName()+"',objective_id= '"+newlessons.getObjective().getName()+"',start ='"+newlessons.getStart()+"',finish='"+newlessons.getFinish()+"',comments='"+newlessons.getComments()+"',method_id='"+newlessons.getMethod().getName()+"' where id ='"+newlessons.getId()+"'";
                   st.executeUpdate(test);
                   //extract array of original studentids
                   String consulta = "select student_id from lesson_stud_att where lesson_id ='"+newlessons.getId()+"'";
                   ResultSet rs = st.executeQuery(consulta);
                   while(rs.next())
                   {
                       oldstuds.add(""+rs.getInt("student_id"));
                   }
                   //get the new students to be added
                   List<String> newList = Arrays.asList(studentIds);
                  addstuds = newList;
                   addstuds.removeAll(oldstuds);
            for( String x:addstuds)
            {
                st.executeUpdate("insert into lesson_stud_att(lesson_id,student_id) values ('"+lessonid+"','"+x+"')");
            }
            // get the studs tp be deleted
            delstuds = oldstuds;
            delstuds.removeAll(newList);
            //to avoid null pointer exception incase of lesson without content
            if(newlessons.getContentid()!=null){
                  equipmentids=newlessons.getContentid();
            
             for( int i = 0; i <= equipmentids.size() - 1; i++)
            {
                
                st.executeUpdate("insert into lesson_content(lesson_id,content_id) values ('"+lessonid+"','"+equipmentids.get(i)+"')");
            }
            }
          
    }
    catch (SQLException ex) {
            System.out.println("Error leyendo lessons: " + ex);
        }
  //          st.executeUpdate("insert into lessons_time(teacher_id,lesson_id,lesson_start,lesson_end) values (5,"+lessonid+",'"+newlessons.getStart()+"','"+newlessons.getFinish()+"')");
    }

    public void updateidea(Lessons newlessons) throws SQLException {
     int lessonid=0;
    List<String> equipmentids;
    DriverManagerDataSource dataSource;
    try{
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",this.servlet);
       this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
       
        String test = "update lessons set name = '"+newlessons.getName()+"',level_id = '"+newlessons.getLevel().getName()+"' ,subject_id = '"+newlessons.getSubject().getName()+"',objective_id= '"+newlessons.getObjective().getName()+"',comments='"+newlessons.getComments()+"',method_id='"+newlessons.getMethod().getName()+"' where id ='"+newlessons.getId()+"'";
    //   st.executeUpdate(test);
       st.executeUpdate(test);

          //need to do like in students, compare old list with new one
            //to avoid null pointer exception incase of lesson without content
            if(newlessons.getContentid()!=null){
                  equipmentids=newlessons.getContentid();
            
             for( int i = 0; i <= equipmentids.size()- 1; i++)
            {
                
                st.executeUpdate("insert into lesson_content(lesson_id,content_id) values ('"+lessonid+"','"+equipmentids.get(i)+"')");
            }
            }
          
    }
    catch (SQLException ex) {
            System.out.println("Error leyendo lessons: " + ex);
        }  
    
    }
       
       
       }
    

