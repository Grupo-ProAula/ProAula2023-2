
package co.edu.unicolombo.ProyectoDeAula20232.Util;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Actividades;
import co.edu.unicolombo.ProyectoDeAula20232.Models.ActividadesProgramadas;
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

public class ActividadesExporterPDF {
       
    private List<ActividadesProgramadas> listaActividades;
    private Actividades actividad;

    public ActividadesExporterPDF(List<ActividadesProgramadas> listaActividades, Actividades actividad) {
        super();
        this.actividad = actividad;
        this.listaActividades = listaActividades;
    }
    
    private void cabeceraTabla(PdfPTable tabla){
        PdfPCell celda = new PdfPCell();
        celda.setBackgroundColor(Color.GRAY);
        celda.setPadding(5);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        Font fuente = FontFactory.getFont(FontFactory.HELVETICA);
        fuente.setColor(Color.WHITE);
        
        celda.setPhrase(new Phrase("Periodo", fuente));
        
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Fecha Inicio", fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Fecha Fin", fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Encargado", fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Estado", fuente));
        tabla.addCell(celda);
    }
    
    private void datosTabla(PdfPTable tabla){
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        PdfPCell celda = new PdfPCell();
        celda.setPadding(5);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        Font fuente = FontFactory.getFont(FontFactory.HELVETICA);
        fuente.setColor(Color.BLACK);
        for(ActividadesProgramadas ap : listaActividades){
            celda.setPhrase(new Phrase(ap.getPeriodo(), fuente));
            tabla.addCell(celda);
            celda.setPhrase(new Phrase(formato.format(ap.getFechaInicio()), fuente));
            tabla.addCell(celda);
            celda.setPhrase(new Phrase(formato.format(ap.getFechaFin()), fuente));
            tabla.addCell(celda);
            celda.setPhrase(new Phrase(ap.getEncargado().getNombre()+" "+ap.getEncargado().getApellidos(), fuente));
            tabla.addCell(celda);
            celda.setPhrase(new Phrase(ap.getEstado(), fuente));
            tabla.addCell(celda);
        }
    }
    
    public void Exportar(HttpServletResponse response) throws IOException{
        Document documento = new Document(PageSize.A4);
        PdfWriter.getInstance(documento, response.getOutputStream());
        
        documento.open();
        
        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fuenteTitulo.setColor(Color.BLACK);
        fuenteTitulo.setSize(18);
        
        Paragraph titulo = new Paragraph("Actividades Programadas De "+actividad.getNombre(), fuenteTitulo);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        documento.add(titulo);
                        
        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(15);
        tabla.setWidths(new float[]{3f,2.3f,2.3f,4f,2.5f});
        tabla.setWidthPercentage(110);
        
        cabeceraTabla(tabla);
        datosTabla(tabla);
        
        documento.add(tabla);
        documento.close();
    }
}
