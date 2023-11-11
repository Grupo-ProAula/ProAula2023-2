
package co.edu.unicolombo.ProyectoDeAula20232.Controllers;

import co.edu.unicolombo.ProyectoDeAula20232.Models.ActividadesProgramadas;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Asistencias;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Estudiantes;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Horarios;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Participaciones;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Usuarios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IActividadProgramadaServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IAsistenciaServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IEstudianteServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IHorarioServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IParticipacionesServicios;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequestMapping("/Asistencias")
public class AsistenciaControlador {
    
    @Autowired
    IActividadProgramadaServicios programActivityService;
    
    @Autowired
    IAsistenciaServicios asistenciaService;
    
    @Autowired
    IEstudianteServicios studentService;
    
    @Autowired
    IParticipacionesServicios participanteService;
    
    @Autowired
    IHorarioServicios horarioService;
    
    @GetMapping("")
    public String listarAsistencias(Model modelo, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Estudiante")){
            return "redirect:/";
        }
        Estudiantes e = studentService.buscarEstudiante(logueado.getIdUsuario());
        List<Asistencias> listaAsistencias = (List<Asistencias>)asistenciaService.listarAsistencias(e.getIdUsuario(),null);
        String horasTotales = sumarHoras(listaAsistencias);
        log.info("Horas Totales: "+horasTotales);
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("estudiante", e);
        modelo.addAttribute("actividad", new ActividadesProgramadas());
        modelo.addAttribute("totalHoras", horasTotales);
        modelo.addAttribute("asistencias", listaAsistencias);
        return "Asistencias/ListaAsistencias";
    }
    
    @GetMapping("/Estudiante/{idUsuario}")
    public String listarAsistenciasEstudiante(@PathVariable(name="idUsuario")int id, Model modelo, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(logueado.getTipo().equals("Estudiante")){
            return "redirect:/";
        }
        Estudiantes e = studentService.buscarEstudiante(id);
        List<Asistencias> listaAsistencias = (List<Asistencias>)asistenciaService.listarAsistencias(e.getIdUsuario(),null);
        String horasTotales = sumarHoras(listaAsistencias);
        log.info("Horas Totales: "+horasTotales);
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("estudiante", e);
        modelo.addAttribute("actividad", new ActividadesProgramadas());
        modelo.addAttribute("totalHoras", horasTotales);
        modelo.addAttribute("asistencias", listaAsistencias);
        return "Asistencias/ListaAsistencias";
    }
    
    @GetMapping("/Actividad/{idActividadProgramada}")
    public String listarAsistenciasActividad(@PathVariable(name="idActividadProgramada")int id, Model modelo, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(logueado.getTipo().equals("Estudiante")){
            return "redirect:/";
        }
        ActividadesProgramadas ap = programActivityService.buscarActividadProgramada(id);
        List<Asistencias> listaAsistencias = (List<Asistencias>)asistenciaService.listarAsistencias(null,ap.getIdActividadProgramada());
        String horasTotales = sumarHoras(listaAsistencias);
        log.info("Horas Totales: "+horasTotales);
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("estudiante", new Estudiantes());
        modelo.addAttribute("actividad", ap);
        modelo.addAttribute("totalHoras", horasTotales);
        modelo.addAttribute("asistencias", listaAsistencias);
        return "Asistencias/ListaAsistencias";
    }
    
    @GetMapping("/Add/{idActividadProgramada}")
    public String registrarAsistencia(@PathVariable(name="idActividadProgramada")int id, Model modelo, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Encargado")){
            return "redirect:/";
        }
        ActividadesProgramadas ap = programActivityService.buscarActividadProgramada(id);
        List<Participaciones> listaParticipaciones = (List<Participaciones>)participanteService.listarParticipaciones(null,ap.getIdActividadProgramada() , null);        
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("participantes", listaParticipaciones);
        modelo.addAttribute("actividad", ap);
        return "Asistencias/FormularioAsistencias";
    }
    
    @PostMapping("/Save")
    public String guardarAsistencia(@RequestParam(required = false,value = "estudiantes[]") Participaciones[] participantes, @RequestParam(value = "fechaAsistencia") String fecha, @RequestParam(value = "idActividad") int idActividad, RedirectAttributes atributos,  Model modelo, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        String fechaString = fecha;
        try {
            ActividadesProgramadas ap = programActivityService.buscarActividadProgramada(idActividad);
            Date f = formato.parse(fechaString);
            if(participantes == null){
                atributos.addFlashAttribute("danger", "Debes Seleccionar Minimo Un Estudiante");
                return "redirect:/Asistencias/Add/"+ap.getIdActividadProgramada();
            }else if(ap.getFechaInicio().after(f)){
                atributos.addFlashAttribute("danger", "La fecha de inicio de esta actividad no puede ser mayor a la fecha de asistencia");
                return "redirect:/Asistencias/Add/"+ap.getIdActividadProgramada();
            }else if(ap.getFechaFin().before(f)){
                atributos.addFlashAttribute("danger", "La fecha de fin de esta actividad no puede ser menor a la fecha de asistencia");
                return "redirect:/Asistencias/Add/"+ap.getIdActividadProgramada();
            }else{
                List<Horarios> lista = horarioService.listarHorarios(idActividad);
                if(lista.isEmpty()){
                    atributos.addFlashAttribute("danger", "Esta Actividad No cuenta Con Horarios Asignados");
                    return "redirect:/Asistencias/Add/"+ap.getIdActividadProgramada();
                }else{
                    Horarios horario = validarFecha(f, lista);
                    if(horario != null){
                        String mensaje = "Los Siguientes Estudiantes: (";
                        Date horas = calcularAsistencia(horario);
                        java.sql.Date fecha2 = new java.sql.Date(f.getTime());
                        for(Participaciones p : participantes){
                            Asistencias a = asistenciaService.verficiarAsistencia(idActividad, p.getEstudiante().getIdUsuario() , fecha2);
                            if(a != null){
                                mensaje += ""+p.getEstudiante().getNombre()+", ";
                                continue;
                            }
                            Asistencias asistencia = new Asistencias();
                            asistencia.setParticipacion(p);
                            asistencia.setFechaAsistencia(f);
                            asistencia.setHorasAsistidas(new Time(horas.getTime()));
                            asistencia.setEstado("Activo");
                            asistenciaService.guardarAsistencia(asistencia);
                        }
                        if(!mensaje.equals("Los Siguientes Estudiantes: (")){
                            atributos.addFlashAttribute("warning", mensaje+") Ya tienen Una Asistencia en la fecha Seleccionada, por ende no se les ha registrado una nueva.\n El resto de asistencias fueron registradas exitosamente");
                        }else{
                            atributos.addFlashAttribute("success", "Asistencia(s) Registrada(s) Exitosamente");
                        }
                        return "redirect:/Asistencias/Actividad/"+ap.getIdActividadProgramada();
                    }else{
                        atributos.addFlashAttribute("danger", "Esta Actividad No Cuenta Con Un Horario En la fecha seleccionada");
                        return "redirect:/Asistencias/Add/"+ap.getIdActividadProgramada();
                    }
                }
            }
        } catch (ParseException ex) {
            String mensaje= ex.getMessage();
            atributos.addFlashAttribute("danger", mensaje);
            return "redirect:/Asistencias/Add/"+idActividad;
        }
    }
    
    private String sumarHoras(List<Asistencias> lista){
        long milisegundos = 0;
        int horas = 0;
        int minutos = 0;
        int segundos = 0;
        for(Asistencias a : lista){
            milisegundos = a.getHorasAsistidas().getTime();
            Calendar calendario = Calendar.getInstance();
            calendario.setTimeInMillis(milisegundos);
            horas += calendario.get(Calendar.HOUR);
            int m = minutos + calendario.get(Calendar.MINUTE);
            if(m > 60){
                horas += 1;
                int var = m - 60;
                minutos = var;
            }else{
                minutos += calendario.get(Calendar.MINUTE);
            }
            segundos += calendario.get(Calendar.SECOND);
        }
        String horasString = String.format("%02d", horas);
        String minutosString = String.format("%02d", minutos);
        String segundosString = String.format("%02d", segundos);
        String total = horasString+":"+minutosString+":"+segundosString;
        return total;
    }
    
    private Horarios validarFecha(Date fecha, List<Horarios> lista){
        Horarios horario = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String dia = diaSemana(day);
        for(Horarios h : lista){
            if(h.getDia().equals(dia)){
                horario = h;
                break;
            }
        }
        return horario;
    }
    
    private String diaSemana(int dia){
        String diaString = "";
        diaString = switch (dia) {
            case 1 -> "Domingo";
            case 2 -> "Lunes";
            case 3 -> "Martes";
            case 4 -> "Miercoles";
            case 5 -> "Jueves";
            case 6 -> "Viernes";
            case 7 -> "Sabado";
            default -> "No valido";
        };
        return diaString;
    }
    
    private Date calcularAsistencia(Horarios horario){
        Calendar calInicio =Calendar.getInstance();
        calInicio.setTime(horario.getHoraInicio());
        Calendar calSalida =Calendar.getInstance();
        calSalida.setTime(horario.getHoraFin());
        int difHoras =calSalida.get(Calendar.HOUR_OF_DAY) - calInicio.get(Calendar.HOUR_OF_DAY);
        int difMinutos =calSalida.get(Calendar.MINUTE) - calInicio.get(Calendar.MINUTE);
        int difSegundos =calSalida.get(Calendar.SECOND) - calInicio.get(Calendar.SECOND);
        Calendar calDif =Calendar.getInstance();
        calDif.set(Calendar.HOUR_OF_DAY, difHoras);
        calDif.set(Calendar.MINUTE, difMinutos);
        calDif.set(Calendar.SECOND, difSegundos);
        Date horas = new Date();
        horas.setTime(calDif.getTimeInMillis());
        return horas;
    }
}
