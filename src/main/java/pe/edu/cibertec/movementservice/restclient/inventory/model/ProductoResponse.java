package pe.edu.cibertec.movementservice.restclient.inventory.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductoResponse {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String imagen;
    private double precio;
    private int cantidad;
    private Integer stockMinimo;
    private Integer usuarioId;
}
