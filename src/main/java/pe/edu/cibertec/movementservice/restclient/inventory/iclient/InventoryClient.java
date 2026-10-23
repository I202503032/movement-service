package pe.edu.cibertec.movementservice.restclient.inventory.iclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.cibertec.movementservice.restclient.config.FeignConfig;
import pe.edu.cibertec.movementservice.restclient.inventory.model.MovimientoResponse;
import pe.edu.cibertec.movementservice.restclient.inventory.model.ProductoResponse;

import java.util.List;

@FeignClient(name = "inventory-service",
        configuration = FeignConfig.class
)
public interface InventoryClient {

    @GetMapping("/api/v1/productos")
    List<ProductoResponse> getProductos();

    @GetMapping("/api/v1/productos/{id}/movimientos")
    List<MovimientoResponse> getMovimientos(@PathVariable("id") Integer id);

}
