<%-- 
    Document   : createlesson
    Created on : 30-ene-2017, 14:59:17
    Author     : nmohamed
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Lessons</title>
        
        <link href="recursos/css/bootstrap.css" rel="stylesheet" type="text/css"/>
      
        <link href="recursos/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css" />
        <link href="recursos/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="recursos/css/bootstrap-toggle.css" rel="stylesheet" type="text/css"/>
        <script src="recursos/js/jquery-2.2.0.js" type="text/javascript"></script>
        
        <script src="recursos/js/bootstrap.js" type="text/javascript"></script>
        <script src="recursos/js/bootstrap-toggle.js" type="text/javascript"></script>
<!--        <script src="recursos/js/bootstrap-modal.js" type="text/javascript"></script>-->
        <script src="recursos/js/moment.js" type="text/javascript"></script>
        <script src="recursos/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script src="recursos/js/es.js" type="text/javascript"></script>
        <script src="recursos/js/ar.js" type="text/javascript"></script>
        
        

 <link href="recursos/css/dataTables/dataTables.bootstrap.min.css" rel="stylesheet" type="text/css"/>

    <link href="recursos/css/dataTables/dataTables.foundation.css" rel="stylesheet" type="text/css"/>

    <link href="recursos/css/dataTables/dataTables.jqueryui.css" rel="stylesheet" type="text/css"/>

    <link href="recursos/css/dataTables/dataTables.semanticui.css" rel="stylesheet" type="text/css"/>

<link href="recursos/css/dataTables/jquery.dataTables.min.css" rel="stylesheet" type="text/css"/>
    <link href="recursos/css/dataTables/jquery.dataTables_themeroller.css" rel="stylesheet" type="text/css"/>
    
    
    <script src="recursos/js/dataTables/dataTables.bootstrap.js" type="text/javascript"></script>

    <script src="recursos/js/dataTables/dataTables.bootstrap4.js" type="text/javascript"></script>

    <script src="recursos/js/dataTables/dataTables.foundation.js" type="text/javascript"></script>
<!--    <script src="recursos/js/dataTables/dataTables.foundation.min.js" type="text/javascript"></script>-->
    <script src="recursos/js/dataTables/dataTables.jqueryui.js" type="text/javascript"></script>
<!--    <script src="recursos/js/dataTables/dataTables.jqueryui.min.js" type="text/javascript"></script>-->
    <script src="recursos/js/dataTables/dataTables.material.js" type="text/javascript"></script>
<!--    <script src="recursos/js/dataTables/dataTables.material.min.js" type="text/javascript"></script>-->
<!--    <script src="recursos/js/dataTables/dataTables.semanticui.js" type="text/javascript"></script>-->
<!--    <script src="recursos/js/dataTables/dataTables.semanticui.min.js" type="text/javascript"></script>-->
<!--    <script src="recursos/js/dataTables/dataTables.uikit.js" type="text/javascript"></script>-->
<!--    <script src="recursos/js/dataTables/dataTables.uikit.min.js" type="text/javascript"></script>-->
    <script src="recursos/js/dataTables/jquery.dataTables.js"></script>
<!--    <script src="recursos/js/dataTables/jquery.dataTables.min.js" type="text/javascript"></script>-->
<!--    <script src="recursos/js/dataTables/jquery.js" type="text/javascript"></script>-->

        <script>
      
        
        var ajax;

    function funcionCallBackSubject()
    {
           if (ajax.readyState===4){
                if (ajax.status===200){
                    document.getElementById("origen").innerHTML= ajax.responseText;
                    }
                }
            }
             function funcionCallBack()
    {
           if (ajax.readyState===4){
                if (ajax.status===200){
                    document.getElementById("table").innerHTML= ajax.responseText;
                    }
                }
            }
            


    
  
    function comboSelectionLevel()
    {
        if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }

//        $('#createOnClick').attr('disabled', true);
        ajax.onreadystatechange = funcionCallBackSubject;
        var seleccion1 = document.getElementById("level").value;
        ajax.open("POST","settings.htm?option=subjectlistLevel&seleccion1="+seleccion1,true);
        
        ajax.send("");
       
    }
    function drawtable()
    {
        if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }

        ajax.onreadystatechange = funcionCallBack;
        var seleccion2 = document.getElementById("origen").value;
        ajax.open("POST","settings.htm?option=objectivelistSubject&seleccion2="+seleccion2,true);
        
        ajax.send("");
       
    }
 

        </script>
        <style>
            textarea 
            {
            resize: none;
            }
        </style>
    </head>
    <%@ include file="menu.jsp" %>
    <body>
 
        
        <div class="container">
        <h1 class="text-center">Create Lessons</h1>

        
        
        <form>    
            <fieldset>
                    <legend>Select subject</legend>
                    <div class="col-xs-12">
                        <div class="col-xs-2"></div>
                        <div class="col-xs-3">
                            <label>Filter</label>

                            
                        </div>
                    </div>
                    <div class="col-xs-12">
                        <div class="col-xs-2">
                            <select class="form-control" name="levelSubject" id="level" style="width: 100% !important;" onchange="comboSelectionLevel()">

                                <c:forEach var="levels" items="${gradelevels}">
                                    <option value="${levels.id[0]}" >${levels.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-xs-3">
                            <select class="form-control" size="20" name="origen[]" id="origen" style="width: 100% !important;">
                                <c:forEach var="subject" items="${subjects}">
                                    <option value="${subject.id[0]}" >${subject.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                <div class="col-xs-4"></div>
            </fieldset>
        </form>

<input type="checkbox" name="id" value="Java">Objectives<BR>
<input type="checkbox" name="id" value=".NET">Contents<BR>
<input type="checkbox" name="id" value="PHP">Methods<BR>
<input type="button" value="Submit" onclick="drawtable()"/>



    <div class="col-xs-12">
                <table id="table" >
                    <thead>
                        <tr>
                            <td>Objective name</td>
                            <td>Comment</td>
                          
                          
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="objective" items="${objectives}" >
                        <tr>
                            <td>${objective.name}</td>
                              
                            <td>${objective.description}</td>
                       
                        </tr>
                    </c:forEach>
                    </tbody>
            </table>
           
            </div> 
        </div>

    </body>
</html>
