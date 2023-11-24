
package co.edu.unicolombo.ProyectoDeAula20232.Util;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Actividades;
import co.edu.unicolombo.ProyectoDeAula20232.Models.ActividadesProgramadas;
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
        if(listaActividades.isEmpty()){
            celda.setPhrase(new Phrase("No Hay Programaciones En Esta Actividad", fuente));
            celda.setColspan(5);
            tabla.addCell(celda);
        }else{
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
        
        PdfPTable tablaActividad = new PdfPTable(4);
        tablaActividad.setWidthPercentage(100);
        tablaActividad.setSpacingBefore(15);
        tablaActividad.setWidths(new float[]{3f,6.75f,2.6f,7.15f});
        tablaActividad.setWidthPercentage(100);

        celdaCampo.setPhrase(new Phrase("Actividad:", fuenteCampo));
        tablaActividad.addCell(celdaCampo);
        celdaDato.setPhrase(new Phrase(actividad.getNombre(), fuenteDato));
        tablaActividad.addCell(celdaDato);

        celdaCampo.setPhrase(new Phrase("Categoria:", fuenteCampo));
        tablaActividad.addCell(celdaCampo);
        celdaDato.setPhrase(new Phrase(actividad.getTipo(), fuenteDato));
        tablaActividad.addCell(celdaDato);

        documento.add(tablaActividad);
                        
        PdfPTable tabla = new PdfPTable(5);
        tabla.setSpacingBefore(30);
        tabla.setWidths(new float[]{3f,2.3f,2.3f,4f,2.5f});
        tabla.setWidthPercentage(100);
        
        cabeceraTabla(tabla);
        datosTabla(tabla);
        
        documento.add(tabla);
        documento.close();
    }
}
