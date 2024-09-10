package com.mavs.demopark.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EstacionamentoUtils {

    //2024-08-05T12:18:48.616463500
    //20240805-121848
    public static String gerarRecibo(){
        LocalDateTime date = LocalDateTime.now();
        String recibo = date.toString().substring(0,19);
        return recibo.replace("-","")
                .replace(":","")
                .replace("T","-");
    }
    private static final double PRIMEIROS_15_MINUTES = 5.00;
    private static final double PRIMEIROS_60_MINUTES = 9.25;
    private static final double ADICIONAL_15_MINUTES = 1.75;


    public static BigDecimal calcularCusto(LocalDateTime entrada, LocalDateTime saida) {
        long minutes = entrada.until(saida, ChronoUnit.MINUTES);
        double total = 0.0;

        if (minutes <= 15) {

            total = PRIMEIROS_15_MINUTES;

        } else if (minutes <= 60) {

            total = PRIMEIROS_60_MINUTES;

        } else {

            total = PRIMEIROS_60_MINUTES;
            double tempoEstacionado = 60;

            while (tempoEstacionado < minutes){
                total += ADICIONAL_15_MINUTES;
                tempoEstacionado += 15;
            }
        }
        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }


    private static final double DESCONTO_PERCENTUAL = 0.30;

    public static BigDecimal calcularDesconto(BigDecimal custo, long numeroDeVezes) {

        BigDecimal desconto = ( (numeroDeVezes > 0) && (numeroDeVezes % 10 == 0) )
                ? custo.multiply(new BigDecimal(DESCONTO_PERCENTUAL))
                : new BigDecimal(0);

        return desconto.setScale(2, RoundingMode.HALF_EVEN);
    }
}
