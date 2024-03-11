package com.example.examend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
    private String nombre ;
    private String sexo;
    private Double peso;
    private Integer edad;
    private LocalDate talla;
    private TipoActividad tipoActividad;
    private String observaciones;


}
