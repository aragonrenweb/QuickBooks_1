/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports.DataFactoryFolder;

import Montessori.DBConect;
import Montessori.Objective;
import Montessori.Subject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author nmoha
 */
public class FactoryProgressReport_Pre_Primary extends DataFactory {

    public FactoryProgressReport_Pre_Primary() {
        nameStudent = "";
        dob = "";
        age = "";
        grade = "";
        term = "";
    }

    public Collection getDataSource(HttpServletRequest hsr, String idStudent, ServletContext servlet) throws SQLException, ClassNotFoundException {

        String studentId = idStudent;
        String consulta = "";
        ResultSet rs;
        cargarAlumno(studentId); // tarda 1

        String yearId = "" + hsr.getSession().getAttribute("yearId");
        String termId  = ""+hsr.getSession().getAttribute("termId");

        ArrayList<String> lessons = new ArrayList<>();
        java.util.Vector coll = new java.util.Vector();
        ArrayList<Subject> subjects = new ArrayList<>();
        //  rs = DBConect.eduweb.executeQuery("SELECT lesson_id from lesson_stud_att where student_id = '" + studentId + "' and attendance != 'null' and attendance !=' '");
        rs = DBConect.eduweb.executeQuery("SELECT * from lesson_stud_att inner join lessons on lessons.id = lesson_stud_att.lesson_id where student_id = '" + studentId + "' and attendance != 'null' and attendance !=' ' "
                + "and term_id = "+termId+" and yearterm_id = "+yearId);
        while (rs.next()) {
            lessons.add("" + rs.getInt("lesson_id"));
        }
        //select the subjects of these lessons
        ArrayList<String> subids = new ArrayList<>();
        for (String b : lessons) {
            ResultSet rs2 = DBConect.eduweb.executeQuery("select subject_id from lessons where id =" + b);

            while (rs2.next()) {
                // this for avoidind duplicate subjects
                if (!subids.contains("" + rs2.getInt("subject_id"))) {
                    subids.add("" + rs2.getInt("subject_id"));
                    Subject su = new Subject();
                    String[] id = new String[1];
                    id[0] = "" + rs2.getInt("subject_id");
                    su.setId(id);
                    subjects.add(su);
                }
            }
        }

        for (Subject x : subjects) {
            String[] id = x.getId();
            ArrayList<String> os = new ArrayList<>();
            for (String b : lessons) {
           //     select * from lessons inner join objective on lessons.objective_id = objective.id where lessons.id = 129 and lessons.subject_id = 383 and objective.name like 'R%'
                ResultSet rs1 = DBConect.eduweb.executeQuery("select * from lessons inner join objective on lessons.objective_id = objective.id "
                        + "where lessons.id = " + b + " and lessons.subject_id =" + id[0] + "and (objective.name like 'R_%' or objective.name like 'r_%')");
                while (rs1.next()) {
                    if (!os.contains("" + rs1.getInt("objective_id"))) {
                        os.add("" + rs1.getInt("objective_id"));
                    }
                }
            }
            int counter = 0;
            ArrayList<String> finalratings = new ArrayList<>();
            ArrayList<Integer> indEliminarObjectives = new ArrayList<Integer>();
            for (String d : os) {
                Objective b = new Objective();
                String name = b.fetchName(Integer.parseInt(d), servlet);
                consulta = "SELECT rating.name FROM rating where id in"
                        + "(select rating_id from progress_report where student_id = '" + studentId + "'"
                        + " AND comment_date = (select max(comment_date)   from public.progress_report "
                        + "where student_id = '" + studentId + "' AND objective_id = '" + d + "' "
                        + "and generalcomment = false and rating_id not in(6,7)) "
                        + "AND objective_id ='" + d + "'and generalcomment = false )";
                ResultSet rs2 = DBConect.eduweb.executeQuery(consulta);
                if (!rs2.next()) {
                    indEliminarObjectives.add(counter);
                } else {
                    rs2 = DBConect.eduweb.executeQuery(consulta);
                    while (rs2.next()) {
                        String var = rs2.getString("name");

                        var = rs2.getString("name");
                        //  finalratings.add("Mastered");//rs2.getString("name"));
                        finalratings.add(rs2.getString("name"));
                    }
                }
                os.set(counter, name);
                counter = counter + 1;
            }
            x.setObjectives(os);
            Subject t = new Subject();

            for (int i = indEliminarObjectives.size() - 1; i >= 0; i--) {
                int k = indEliminarObjectives.get(i);
                os.remove(k);
            }

            //obtener comentario del subject
            //  boolean existeComentario = false;
            consulta = "SELECT comment FROM report_comments where subject_id=" + id[0] + " and studentid=" + idStudent + " ORDER BY date_created DESC";
            ResultSet rs3 = DBConect.eduweb.executeQuery(consulta);
            if (rs3.next()) {
                //     existeComentario = true;
                os.add(rs3.getString("comment"));
            }
            //=============================
            //os.add("comentario sobre este subject");
            //FUNCIONA PERO LO HACE INNECESARIAMENTE 3 VECES NO SE PORQUE CUANDO LO HAGO FUERA FALLA.

            //============================================================
            if (os.size() > 0) {
                ArrayList<String> subjectName = x.fetchNameAndElective(Integer.parseInt(id[0]), servlet);
                if (subjectName.get(1).equals("false")) { // compruebo que no sea electivo
                    BeanWithList bean = new BeanWithList(subjectName.get(0), os, finalratings, nameStudent, dob, age);
                    coll.add(bean);
                }
            }
        }
        // FALTA TERMINAR PODRIAMOS TOMAR LOS SUBJECT_ID DE LOS ALUMNOS QUE SEAN ELECTIVOS Y COMPROBAR QUE NO EXISTAN EN LA UQERY SIGUIENTE
        // tarda 2
        ArrayList<String> os = new ArrayList<>();
        ArrayList<String> as = new ArrayList<>();
        Subject s = new Subject();
        consulta = "SELECT lessons.subject_id,progress_report.comment FROM progress_report join lessons on (progress_report.lesson_id = lessons.id) where student_id= '" + studentId + "'";
        rs = DBConect.eduweb.executeQuery(consulta);

        while (rs.next()) {
            String comment = rs.getString("comment");
            //   if (!comment.equals("")) {
            //os.add("" + rs.getInt("subject_id"));
            ArrayList<String> nombSubject = s.fetchNameAndElective(rs.getInt("subject_id"), servlet);
            if (nombSubject.get(1).equals("true")) {
                os.add(nombSubject.get(0));
                as.add(comment);
            }
            //  }
        }
        //============================================================
        if (os.size() > 0) {
            BeanWithList bean = new BeanWithList("Comments", os, as, nameStudent, dob, age);
            coll.add(bean);
        }
        //==========================================
        ArrayList<String> os2 = new ArrayList<>();
        os2.add("Informacion relacionada con Social Development");

        BeanWithList bean2 = new BeanWithList("SocialDevelopment", os2, new ArrayList<String>(), nameStudent, dob, age);

        coll.add(bean2);
        //============================

        ArrayList<String> os3 = new ArrayList<>();
        os3.add("Informacion relacionada con General");

        BeanWithList bean3 = new BeanWithList("General", os3, new ArrayList<String>(), nameStudent, dob, age);

        coll.add(bean3);
        //============================
        return coll;
        // return new JRBeanCollectionDataSource(coll);
    }

    @Override
    protected void cargarAlumno(String studentId) throws SQLException {
        String consulta = "SELECT * FROM Students where StudentId = '" + studentId + "'";
        ResultSet rs = DBConect.ah.executeQuery(consulta);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        while (rs.next()) {
            this.nameStudent = rs.getString("LastName") + ", " + rs.getString("FirstName") + " " + rs.getString("MiddleName");
            this.dob = rs.getString("Birthdate");
            this.dob = dob.split(" ")[0];
            this.age = "" + (year - Integer.parseInt("" + dob.charAt(0) + dob.charAt(1) + dob.charAt(2) + dob.charAt(3)));
            this.grade = rs.getString("GradeLevel");
        }

    }

    @Override
    public String getNameReport() {
        return "Pre-Primary_Progress_Report_December2017_v2_4.jasper";
    }
}
