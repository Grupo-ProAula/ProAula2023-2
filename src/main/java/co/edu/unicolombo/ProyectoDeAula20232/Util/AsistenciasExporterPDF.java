
package co.edu.unicolombo.ProyectoDeAula20232.Util;

import co.edu.unicolombo.ProyectoDeAula20232.Models.ActividadesProgramadas;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Asistencias;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Estudiantes;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

public class AsistenciasExporterPDF {
    
    List<Asistencias> lista;
    ActividadesProgramadas actividad;
    Estudiantes estudiante;
    String horasTotales;

    public AsistenciasExporterPDF(List<Asistencias> lista, ActividadesProgramadas actividad, Estudiantes estudiante, String horasTotales) {
        this.lista = lista;
        this.actividad = actividad;
        this.estudiante = estudiante;
        this.horasTotales = horasTotales;
    }
    
    public void Exportar(HttpServletResponse response) throws IOException{
        Document documento = new Document(PageSize.A3);
        PdfWriter.getInstance(documento, response.getOutputStream());
        
        documento.open();
                
        Font fuenteCampo = FontFactory.getFont(FontFactory.HELVETICA_BOLD,11,Color.WHITE);
        Font fuenteDato = FontFactory.getFont(FontFactory.HELVETICA_BOLD,11,Color.BLACK);
        
        PdfPCell celdaCampo = new PdfPCell();
        PdfPCell celdaDato = new PdfPCell();
        
        celdaCampo.setBackgroundColor(Color.GRAY);
        celdaCampo.setPadding(4);
        celdaCampo.setVerticalAlignment(Element.ALIGN_LEFT);
        celdaCampo.setHorizontalAlignment(Element.ALIGN_LEFT);

        celdaDato.setPadding(4);
        celdaDato.setVerticalAlignment(Element.ALIGN_LEFT);
        celdaDato.setHorizontalAlignment(Element.ALIGN_LEFT);
        
        if(actividad == null){
            PdfPTable tablaEstudiante = new PdfPTable(4);
            tablaEstudiante.setWidthPercentage(100);
            tablaEstudiante.setSpacingBefore(15);
            tablaEstudiante.setWidths(new float[]{2.6f,7.15f,2.6f,7.15f});
            tablaEstudiante.setWidthPercentage(100);
            
            celdaCampo.setPhrase(new Phrase("Cedula:", fuenteCampo));
            tablaEstudiante.addCell(celdaCampo);
            celdaDato.setPhrase(new Phrase(estudiante.getCedula(), fuenteDato));
            tablaEstudiante.addCell(celdaDato);
            
            celdaCampo.setPhrase(new Phrase("Codigo:", fuenteCampo));
            tablaEstudiante.addCell(celdaCampo);
            celdaDato.setPhrase(new Phrase(estudiante.getCodigoEstudiantil(), fuenteDato));
            tablaEstudiante.addCell(celdaDato);
            
            celdaCampo.setPhrase(new Phrase("Estudiante:", fuenteCampo));
            tablaEstudiante.addCell(celdaCampo);
            celdaDato.setPhrase(new Phrase(estudiante.getNombre()+" "+estudiante.getApellidos(), fuenteDato));
            tablaEstudiante.addCell(celdaDato);
            
            celdaCampo.setPhrase(new Phrase("Programa:", fuenteCampo));
            tablaEstudiante.addCell(celdaCampo);
            celdaDato.setPhrase(new Phrase(estudiante.getPrograma().getNombre()+" - "+estudiante.getSemestre(), fuenteDato));
            tablaEstudiante.addCell(celdaDato);
            
            documento.add(tablaEstudiante);
        }else{
            PdfPTable tablaActividad = new PdfPTable(4);
            tablaActividad.setWidthPercentage(100);
            tablaActividad.setSpacingBefore(15);
            tablaActividad.setWidths(new float[]{3f,6.75f,2.6f,7.15f});
            tablaActividad.setWidthPercentage(100);

            SimpleDateFormat formatoActividad = new SimpleDateFormat("yyyy-MM-dd");
            
            celdaCampo.setPhrase(new Phrase("Actividad:", fuenteCampo));
            tablaActividad.addCell(celdaCampo);
            celdaDato.setPhrase(new Phrase(actividad.getActividad().getNombre(), fuenteDato));
            tablaActividad.addCell(celdaDato);
            
            celdaCampo.setPhrase(new Phrase("Periodo:", fuenteCampo));
            tablaActividad.addCell(celdaCampo);
            celdaDato.setPhrase(new Phrase(actividad.getPeriodo(), fuenteDato));
            tablaActividad.addCell(celdaDato);
            
            celdaCampo.setPhrase(new Phrase("Fecha Inicio:", fuenteCampo));
            tablaActividad.addCell(celdaCampo);
            celdaDato.setPhrase(new Phrase(formatoActividad.format(actividad.getFechaInicio()), fuenteDato));
            tablaActividad.addCell(celdaDato);
            
            celdaCampo.setPhrase(new Phrase("Fecha Fin:", fuenteCampo));
            tablaActividad.addCell(celdaCampo);
            celdaDato.setPhrase(new Phrase(formatoActividad.format(actividad.getFechaFin()), fuenteDato));
            tablaActividad.addCell(celdaDato);
            
            documento.add(tablaActividad);
        }
                
        PdfPTable tablaHoras = new PdfPTable(3);
        tablaHoras.setWidthPercentage(100);
        tablaHoras.setSpacingBefore(30);
        tablaHoras.setWidths(new float[]{13.5f,3f,3f});
        tablaHoras.setWidthPercentage(100);
        
        Font fuenteHoras = FontFactory.getFont(FontFactory.HELVETICA_BOLD,10,Color.WHITE);
        Font fuenteHorasTotales = FontFactory.getFont(FontFactory.HELVETICA_BOLD,10,Color.BLACK);
        
        PdfPCell celdaVacia = new PdfPCell(new Phrase());
        PdfPCell celdaHoras = new PdfPCell();
        PdfPCell celdaHorasTotales = new PdfPCell();
        
        celdaVacia.setBorder(0);
        
        celdaHoras.setBackgroundColor(Color.GRAY);
        celdaHoras.setPhrase(new Phrase("Horas Totales:", fuenteHoras));
        celdaHoras.setPadding(4);
        celdaHoras.setVerticalAlignment(Element.ALIGN_CENTER);
        celdaHoras.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        celdaHorasTotales.setPhrase(new Phrase(horasTotales, fuenteHorasTotales));
        celdaHorasTotales.setPadding(4);
        celdaHorasTotales.setVerticalAlignment(Element.ALIGN_CENTER);
        celdaHorasTotales.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        tablaHoras.addCell(celdaVacia);
        tablaHoras.addCell(celdaHoras);
        tablaHoras.addCell(celdaHorasTotales);
                                
        PdfPTable tabla = null;
        if(actividad == null){
            tabla = new PdfPTable(7);
            tabla.setWidths(new float[]{3f,2.5f,2.5f,3f,2.5f,3f,3f});
            tabla.setWidthPercentage(100);
        }else{
            tabla = new PdfPTable(6);
            tabla.setWidths(new float[]{2.5f,2.5f,4.3f,4.2f,3f,3f});
            tabla.setWidthPercentage(100);
        }
        
        cabeceraTabla(tabla);
        datosTabla(tabla);
        
        documento.add(tablaHoras);
        documento.add(tabla);
        documento.close();
    }
    
    private void cabeceraTabla(PdfPTable tabla){
        PdfPCell celda = new PdfPCell();
        celda.setBackgroundColor(Color.GRAY);
        celda.setPadding(4);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD,10,Color.WHITE);
        
        if(actividad == null){
            celda.setPhrase(new Phrase("Actividad", fuente));
            tabla.addCell(celda);
            celda.setPhrase(new Phrase("Categoria", fuente));
            tabla.addCell(celda);
            celda.setPhrase(new Phrase("Periodo", fuente));
            tabla.addCell(celda);
            celda.setPhrase(new Phrase("Fecha Inicio", fuente));
            tabla.addCell(celda);
            celda.setPhrase(new Phrase("Fecha Fin", fuente));
            tabla.addCell(celda);
            celda.setPhrase(new Phrase("Fecha Asistencia", fuente));
            tabla.addCell(celda);
            celda.setPhrase(new Phrase("Horas Asistidas", fuente));
            tabla.addCell(celda);
        }else{
            celda.setPhrase(new Phrase("Cedula", fuente));
            tabla.addCell(celda);
            celda.setPhrase(new Phrase("Codigo", fuente));
            tabla.addCell(celda);
            celda.setPhrase(new Phrase("Estudiante", fuente));
            tabla.addCell(celda);
            celda.setPhrase(new Phrase("Programa", fuente));
            tabla.addCell(celda);
            celda.setPhrase(new Phrase("Fecha Asistencia", fuente));
            tabla.addCell(celda);
            celda.setPhrase(new Phrase("Horas Asistidas", fuente));
            tabla.addCell(celda);
        }        
    }
    
    private void datosTabla(PdfPTable tabla){
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatoHoras = new SimpleDateFormat("HH:mm");
        PdfPCell celda = new PdfPCell();
        celda.setPadding(4);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        Font fuente = FontFactory.getFont(FontFactory.HELVETICA,10,Color.BLACK);
        if(lista.isEmpty()){
            if(actividad == null){
                celda.setPhrase(new Phrase("El Estudiante No Tiene Asistencias Registradas", fuente));
                celda.setColspan(7);
                tabla.addCell(celda);
            }else{
                celda.setPhrase(new Phrase("No Hay Asistencias Registradas En Esta Actividad", fuente));
                celda.setColspan(6);
                tabla.addCell(celda);
            }
        }else{
            if(actividad == null){
                for(Asistencias a : lista){
                    celda.setPhrase(new Phrase(a.getParticipacion().getActividadProgramada().getActividad().getNombre(), fuente));
                    tabla.addCell(celda);
                    celda.setPhrase(new Phrase(a.getParticipacion().getActividadProgramada().getActividad().getTipo(), fuente));
                    tabla.addCell(celda);
                    celda.setPhrase(new Phrase(a.getParticipacion().getActividadProgramada().getPeriodo(), fuente));
                    tabla.addCell(celda);
                    celda.setPhrase(new Phrase(formato.format(a.getParticipacion().getActividadProgramada().getFechaInicio()), fuente));
                    tabla.addCell(celda);
                    celda.setPhrase(new Phrase(formato.format(a.getParticipacion().getActividadProgramada().getFechaFin()), fuente));
                    tabla.addCell(celda);
                    celda.setPhrase(new Phrase(formato.format(a.getFechaAsistencia()), fuente));
                    tabla.addCell(celda);
                    celda.setPhrase(new Phrase(formatoHoras.format(a.getHorasAsistidas()), fuente));
                    tabla.addCell(celda);
                }
            }else{
                for(Asistencias a : lista){
                    celda.setPhrase(new Phrase(a.getParticipacion().getEstudiante().getCedula(), fuente));
                    tabla.addCell(celda);
                    celda.setPhrase(new Phrase(a.getParticipacion().getEstudiante().getCodigoEstudiantil(), fuente));
                    tabla.addCell(celda);
                    celda.setPhrase(new Phrase(a.getParticipacion().getEstudiante().getNombre()+" "+a.getParticipacion().getEstudiante().getApellidos(), fuente));
                    tabla.addCell(celda);
                    celda.setPhrase(new Phrase(a.getParticipacion().getEstudiante().getPrograma().getNombre(), fuente));
                    tabla.addCell(celda);
                    celda.setPhrase(new Phrase(formato.format(a.getFechaAsistencia()), fuente));
                    tabla.addCell(celda);
                    celda.setPhrase(new Phrase(formatoHoras.format(a.getHorasAsistidas()), fuente));
                    tabla.addCell(celda);
                }
            }
        }
    }
}
