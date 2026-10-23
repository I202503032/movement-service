package pe.edu.cibertec.movementservice.controller;

import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.movementservice.restclient.inventory.model.MovimientoResponse;
import pe.edu.cibertec.movementservice.restclient.inventory.model.ProductoResponse;
import pe.edu.cibertec.movementservice.service.ReporteService;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/reportes")
@RestController
public class ReporteController {
    private final ReporteService reporteService;

    @GetMapping("/stock-critico")
    public ResponseEntity<List<ProductoResponse>> getStockCritico() {
        return ResponseEntity.ok(reporteService.getStockCritico());
    }

    @GetMapping("/movimientos/{productoId}")
    public ResponseEntity<List<MovimientoResponse>> getMovimientos(@PathVariable Integer productoId) {
        return ResponseEntity.ok(reporteService.getMovimientos(productoId));
    }

    @GetMapping("/exportar")
    public void exportarAExcel(HttpServletResponse response) throws IOException {
        List<ProductoResponse> listaProductos = reporteService.getProductos();

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=inventario_mibodeguita.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Inventario Productos");

        Row headerRow = sheet.createRow(0);
        String[] columnas = {"ID", "Nombre", "Descripción", "Precio (S/)", "Stock", "Stock Mínimo"};

        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        for (int i = 0; i < columnas.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnas[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIdx = 1;
        for (ProductoResponse producto : listaProductos) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(producto.getId());
            row.createCell(1).setCellValue(producto.getNombre());
            row.createCell(2).setCellValue(producto.getDescripcion());
            row.createCell(3).setCellValue(producto.getPrecio());
            row.createCell(4).setCellValue(producto.getCantidad());
            row.createCell(5).setCellValue(reporteService.stockMinimo(producto));
        }

        for (int i = 0; i < columnas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/exportar/pdf")
    public void exportarAPDF(HttpServletResponse response) throws IOException {
        List<ProductoResponse> listaProductos = reporteService.getProductos();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=inventario_mibodeguita.pdf");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        com.lowagie.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
        Paragraph title = new Paragraph("Reporte de Inventario - MiBodeguita", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.addCell("ID");
        table.addCell("Producto");
        table.addCell("Precio (S/)");
        table.addCell("Stock");
        table.addCell("Stock Mín.");

        for (ProductoResponse p : listaProductos) {
            table.addCell(String.valueOf(p.getId()));
            table.addCell(p.getNombre());
            table.addCell(String.valueOf(p.getPrecio()));
            table.addCell(String.valueOf(p.getCantidad()));
            table.addCell(String.valueOf(reporteService.stockMinimo(p)));
        }

        document.add(table);
        document.close();
    }

    @GetMapping("/exportar/csv")
    public void exportarACSV(HttpServletResponse response) throws IOException {
        List<ProductoResponse> listaProductos = reporteService.getProductos();

        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=inventario_mibodeguita.csv");

        PrintWriter writer = response.getWriter();
        writer.println("ID,Producto,Descripcion,Precio,Stock,StockMinimo");

        for (ProductoResponse p : listaProductos) {
            writer.println(p.getId() + "," +
                    p.getNombre().replace(",", " ") + "," +
                    (p.getDescripcion() != null ? p.getDescripcion().replace(",", " ") : "") + "," +
                    p.getPrecio() + "," +
                    p.getCantidad() + "," +
                    reporteService.stockMinimo(p));
        }
        writer.flush();
        writer.close();
    }
}
