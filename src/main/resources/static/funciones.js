function eliminar(id, urlEliminar, url){
    Swal.fire({
        title: '¿Quieres eliminar el rergistro?',
        icon: 'warning',
        showDenyButton: true,
        confirmButtonText: 'Aceptar',
        denyButtonText: `Cancelar`
      }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url:urlEliminar+id,
                success: function(res){
                    console.log(res);
                }
            });
            Swal.fire('El registro ha sido eliminado!', '', 'success').then((ok)=>{
                if(ok){
                    location.href=url;
                }
            });
        } else if (result.isDenied) {
            Swal.fire('No se ha eliminado el registro', '', 'info');
        }
      });
}

function inscribirse(id, urlInscripcion, url){
    Swal.fire({
        title: '¿Quieres Inscribirte En Esta Actividad?',
        icon: 'warning',
        showDenyButton: true,
        confirmButtonText: 'Aceptar',
        denyButtonText: `Cancelar`
      }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url:urlInscripcion+id,
                success: function(res){
                    console.log(res);
                }
            });
            Swal.fire('Ya Estas Inscrito!', '', 'success').then((ok)=>{
                if(ok){
                    location.href=url;
                }
            });
        } else if (result.isDenied) {
            Swal.fire('Inscripcion Cancelada', '', 'info');
        }
      });
}

function desinscribirse(id, urlDesinscripcion, url){
    Swal.fire({
        title: '¿Quieres Desinscribirte De Esta Actividad?',
        icon: 'warning',
        showDenyButton: true,
        confirmButtonText: 'Aceptar',
        denyButtonText: `Cancelar`
      }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url:urlDesinscripcion+id,
                success: function(res){
                    console.log(res);
                }
            });
            Swal.fire('Ya Estas Desinscrito!', '', 'success').then((ok)=>{
                if(ok){
                    location.href=url;
                }
            });
        } else if (result.isDenied) {
            Swal.fire('Desinscripcion Cancelada', '', 'info');
        }
      });
}

$(document).ready(function() {
    var tabla = $('#tabla').DataTable( {
        "responsive": true,
        "language": {
            "lengthMenu": "Mostrando _MENU_ Registros Por Pagina",
            "zeroRecords": "No Hay Coincidencias",
            "info": "Mostrando Pagina _PAGE_ de _PAGES_",
            "infoEmpty": "No Hay Registros",
            "infoFiltered": "(Filtrado De _MAX_ Registros Totales)",
            "search": "Buscar",
            "paginate": {
                "sFirst": "Primero",
                "sPrevious": "Anterior",
                "sNext": "Siguiente",
                "sLast": "Último"
            }      
        }
    } );
    
    var tabla2 = $('#tabla2').DataTable( {
        "dom": 'lrtip',
        "searching": true, 
        "responsive": true,
        "language": {
            "lengthMenu": "Mostrando _MENU_ Registros Por Pagina",
            "zeroRecords": "No Hay Coincidencias",
            "info": "Mostrando Pagina _PAGE_ de _PAGES_",
            "infoEmpty": "No Hay Registros",
            "infoFiltered": "(Filtrado De _MAX_ Registros Totales)",
            "search": "Buscar",
            "paginate": {
                "sFirst": "Primero",
                "sPrevious": "Anterior",
                "sNext": "Siguiente",
                "sLast": "Último"
            }      
        }
    } );
    
    const hours = document.getElementById("horasTotales");
    
    $('#inputBuscar').on( 'keyup', function () {
        tabla2.search( this.value ).draw();
        var datos=tabla2.rows({ search:'applied'});
        var array = datos.data().toArray();
        sumarHoras(array);      
    } );
    
    function sumarHoras(array){
        var horas = 0;
        var minutos = 0;
        for(var i=0;i<array.length;i++){
            var horasAsistidas = array[i][5];
            var arrayHora = horasAsistidas.split(":");
            horas += parseInt(arrayHora[0]);
            var m = minutos + parseInt(arrayHora[1]);
            if(m > 60){
                horas += 1;
                var v = m-60;
                minutos = v;
            }else{
                minutos += parseInt(arrayHora[1]);
            }
        }
        var horasString = horas.toString().padStart(2,'0');
        var minutosString = minutos.toString().padStart(2,'0');
        var horasTotales = horasString+":"+minutosString+":00";
        hours.innerHTML = horasTotales;
        console.log(horasTotales);
    }
} );
