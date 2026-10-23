package pe.edu.cibertec.movementservice.restclient.inventory.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class MovimientoResponse {
    private Integer id;
    private String tipo;
    private int cantidad;
    private String motivo;
    private Date fecha;
    private Integer productoId;
    private Integer usuarioId;
}
