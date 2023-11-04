
package co.edu.unicolombo.ProyectoDeAula20232.Util;

import co.edu.unicolombo.ProyectoDeAula20232.Models.ActividadesProgramadas;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Estudiantes;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Participaciones;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

public class ParticipacionesExporterPDF {
    
    List<Participaciones> lista;
    ActividadesProgramadas actividad;
    Estudiantes estudiante;

    public ParticipacionesExporterPDF(List<Participaciones> lista, ActividadesProgramadas actividad, Estudiantes estudiante) {
        this.lista = lista;
        this.actividad = actividad;
        this.estudiante = estudiante;
    }
    
    public void Exportar(HttpServletResponse response) throws IOException{
        Document documento = new Document(PageSize.LETTER);
        PdfWriter.getInstance(documento, response.getOutputStream());
        
        documento.open();
        
        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fuenteTitulo.setColor(Color.BLACK);
        fuenteTitulo.setSize(18);
        
        if(actividad == null){
            Paragraph titulo = new Paragraph("Inscripciones De "+estudiante.getNombre()+" "+estudiante.getApellidos(), fuenteTitulo);
            titulo.setAlignment(Paragraph.ALIGN_CENTER);
            documento.add(titulo);
        }else{
            Paragraph titulo = new Paragraph("Inscripciones De "+actividad.getActividad().getNombre(), fuenteTitulo);
            titulo.setAlignment(Paragraph.ALIGN_CENTER);
            documento.add(titulo);
        }
                        
        PdfPTable tabla = new PdfPTable(7);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(15);
        tabla.setWidths(new float[]{2.5f,2.5f,3f,3f,2.5f,4f,2.5f});
        tabla.setWidthPercentage(110);
        
        cabeceraTabla(tabla);
        datosTabla(tabla);
        
        documento.add(tabla);
        documento.close();
    }
    
    private void cabeceraTabla(PdfPTable tabla){
        PdfPCell celda = new PdfPCell();
        celda.setBackgroundColor(Color.GRAY);
        celda.setPadding(4);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        Font fuente = FontFactory.getFont(FontFactory.HELVETICA,10,Color.WHITE);
        
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
            celda.setPhrase(new Phrase("Encargado", fuente));
            tabla.addCell(celda);
            celda.setPhrase(new Phrase("Fecha Inscripcion", fuente));
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
            celda.setPhrase(new Phrase("Semestre", fuente));
            tabla.addCell(celda);
            celda.setPhrase(new Phrase("Correo", fuente));
            tabla.addCell(celda);
            celda.setPhrase(new Phrase("Fecha Inscripcion", fuente));
            tabla.addCell(celda);
        }        
    }
    
    private void datosTabla(PdfPTable tabla){
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        PdfPCell celda = new PdfPCell();
        celda.setPadding(4);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        Font fuente = FontFactory.getFont(FontFactory.HELVETICA,10,Color.BLACK);
        if(actividad == null){
            for(Participaciones p : lista){
                celda.setPhrase(new Phrase(p.getActividadProgramada().getActividad().getNombre(), fuente));
                tabla.addCell(celda);
                celda.setPhrase(new Phrase(p.getActividadProgramada().getActividad().getTipo(), fuente));
                tabla.addCell(celda);
                celda.setPhrase(new Phrase(p.getActividadProgramada().getPeriodo(), fuente));
                tabla.addCell(celda);
                celda.setPhrase(new Phrase(formato.format(p.getActividadProgramada().getFechaInicio()), fuente));
                tabla.addCell(celda);
                celda.setPhrase(new Phrase(formato.format(p.getActividadProgramada().getFechaFin()), fuente));
                tabla.addCell(celda);
                celda.setPhrase(new Phrase(p.getActividadProgramada().getEncargado().getNombre()+" "+p.getActividadProgramada().getEncargado().getApellidos(), fuente));
                tabla.addCell(celda);
                celda.setPhrase(new Phrase(formato.format(p.getFechaInscripcion()), fuente));
                tabla.addCell(celda);
            }
        }else{
            for(Participaciones p : lista){
                celda.setPhrase(new Phrase(p.getEstudiante().getCedula(), fuente));
                tabla.addCell(celda);
                celda.setPhrase(new Phrase(p.getEstudiante().getCodigoEstudiantil(), fuente));
                tabla.addCell(celda);
                celda.setPhrase(new Phrase(p.getEstudiante().getNombre()+" "+p.getEstudiante().getApellidos(), fuente));
                tabla.addCell(celda);
                celda.setPhrase(new Phrase(p.getEstudiante().getPrograma().getNombre(), fuente));
                tabla.addCell(celda);
                celda.setPhrase(new Phrase(p.getEstudiante().getSemestre()+"", fuente));
                tabla.addCell(celda);
                celda.setPhrase(new Phrase(p.getEstudiante().getCorreo(), fuente));
                tabla.addCell(celda);
                celda.setPhrase(new Phrase(formato.format(p.getFechaInscripcion()), fuente));
                tabla.addCell(celda);
            }
        }
        
    }
}
