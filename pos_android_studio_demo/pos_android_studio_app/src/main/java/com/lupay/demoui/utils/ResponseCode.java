package com.lupay.demoui.utils;

import java.util.HashMap;

public class ResponseCode {
    private static HashMap<String, CodeDetails> CodeTable = new HashMap<>();

    public static void setCodeResponses() {
        CodeTable.put("00", new CodeDetails("Venta", true));
        CodeTable.put("01", new CodeDetails("Llame al Emisor", true));
        CodeTable.put("02", new CodeDetails("Llame al Emisor"));
        CodeTable.put("03", new CodeDetails("Comercio invalido"));
        CodeTable.put("04", new CodeDetails("Recoger Tarjeta"));
        CodeTable.put("05", new CodeDetails("Rechazada"));
        CodeTable.put("06", new CodeDetails("Rechazada"));
        CodeTable.put("07", new CodeDetails("Recoger Tarjeta"));
        CodeTable.put("09", new CodeDetails("Rechazada"));
        CodeTable.put("10", new CodeDetails("Aprobada parcialmente", true));
        CodeTable.put("11", new CodeDetails("Aprobada", true));
        CodeTable.put("12", new CodeDetails("Transaccion invalida"));
        CodeTable.put("13", new CodeDetails("Monto invalido"));
        CodeTable.put("14", new CodeDetails("Rechazada"));
        CodeTable.put("15", new CodeDetails("No existe emisor"));
        CodeTable.put("30", new CodeDetails("Error de Formato"));
        CodeTable.put("31", new CodeDetails("Banco No Soportado"));
        CodeTable.put("33", new CodeDetails("Tarjeta Expirada"));
        CodeTable.put("34", new CodeDetails("Rechazada"));
        CodeTable.put("35", new CodeDetails("Rechazada"));
        CodeTable.put("36", new CodeDetails("Tarjeta Restringida"));
        CodeTable.put("37", new CodeDetails("Rechazada"));
        CodeTable.put("38", new CodeDetails("Excede numero de intentos"));
        CodeTable.put("39", new CodeDetails("Rechazada"));
        CodeTable.put("41", new CodeDetails("Tarjeta Perdida"));
        CodeTable.put("43", new CodeDetails("Recoger Tarjeta"));
        CodeTable.put("51", new CodeDetails("Fondos Insuficientes"));
        CodeTable.put("54", new CodeDetails("Tarjeta Expirada"));
        CodeTable.put("55", new CodeDetails("Rechazada"));
        CodeTable.put("56", new CodeDetails("No Existe Registro"));
        CodeTable.put("57", new CodeDetails("Transaccion No Permitida"));
        CodeTable.put("58", new CodeDetails("Transaccion No Permitida"));
        CodeTable.put("61", new CodeDetails("Excede limite permitido"));
        CodeTable.put("62", new CodeDetails("Tarjeta Restringida"));
        CodeTable.put("65", new CodeDetails("Excede limite permitido"));
        CodeTable.put("68", new CodeDetails("Respuesta tardia"));
        CodeTable.put("75", new CodeDetails("Excede numero de intentos"));
        CodeTable.put("76", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("77", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("78", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("79", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("80", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("81", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("82", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("83", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("84", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("85", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("86", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("87", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("88", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("89", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("90", new CodeDetails("Servicio no disponible"));
        CodeTable.put("91", new CodeDetails("Servicio no disponible"));
        CodeTable.put("92", new CodeDetails("Rechazada"));
        CodeTable.put("94", new CodeDetails("Rechazada"));
        CodeTable.put("96", new CodeDetails("Servicio no disponible"));
        CodeTable.put("N0", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("N1", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("N2", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("N3", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("N4", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("N5", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("N6", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("N7", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("N8", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("N9", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("O0", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("O1", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("O2", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("O3", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("O4", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("O5", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("O6", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("O7", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("O8", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("O9", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("P0", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("P1", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("P2", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("P3", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("P4", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("P5", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("P6", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("P7", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("P8", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("P9", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("Q0", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("Q1", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("Q2", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("Q3", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("Q4", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("Q5", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("Q6", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("Q7", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("Q8", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("Q9", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("R0", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("R1", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("R2", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("R3", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("R4", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("R5", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("R6", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("R7", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("R8", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("S4", new CodeDetails("Rechazada"));
        CodeTable.put("S5", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("S6", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("S7", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("S8", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("S9", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("T1", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("T2", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("T3", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("T4", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("T5", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("T6", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("T7", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("T8", new CodeDetails("Reservado Uso Privado"));
        CodeTable.put("U0", new CodeDetails("ARQC declinado"));
        CodeTable.put("U1", new CodeDetails("Fallo en parametro de seguridad"));
        CodeTable.put("U2", new CodeDetails("Fallo en modulo de seguridad"));
        CodeTable.put("U3", new CodeDetails("KEY1 no encontrada"));
        CodeTable.put("U4", new CodeDetails("Error en ATC"));
        CodeTable.put("U5", new CodeDetails("CVR declinado"));
        CodeTable.put("U6", new CodeDetails("TVR declinado"));
        CodeTable.put("U7", new CodeDetails("Rechazo del codigo"));
        CodeTable.put("U8", new CodeDetails("Fallback declinado"));
        CodeTable.put("V0", new CodeDetails("Error en ARQC"));
        CodeTable.put("V1", new CodeDetails("Error en CVR"));
        CodeTable.put("V2", new CodeDetails("Error en TVR"));
        CodeTable.put("V3", new CodeDetails("Rechazo del codigo"));
        CodeTable.put("V4", new CodeDetails("Error en Fallback"));
        CodeTable.put("V7", new CodeDetails("Error en generar ARQC"));
        CodeTable.put("V8", new CodeDetails("Error en generar CVR"));
        CodeTable.put("V9", new CodeDetails("Error en generar TVR"));
        CodeTable.put("70", new CodeDetails("Error descifrado Track2"));
        CodeTable.put("72", new CodeDetails("Error en activación manual"));
        CodeTable.put("73", new CodeDetails("Error en CRC"));
        CodeTable.put("1A", new CodeDetails("Rebasaste el monto de pago sin contacto"));

        CodeTable.put("000", new CodeDetails("Venta", true));
        CodeTable.put("001", new CodeDetails("Aprobar con ID", true));
        CodeTable.put("002", new CodeDetails("Aprobación parcialmente"));
        CodeTable.put("100", new CodeDetails("Rechazada"));
        CodeTable.put("101", new CodeDetails("Tarjeta expirada"));
        CodeTable.put("106", new CodeDetails("Excede numero de intentos"));
        CodeTable.put("109", new CodeDetails("Comercio invalido"));
        CodeTable.put("110", new CodeDetails("Monto invalido"));
        CodeTable.put("111", new CodeDetails("Cuenta invalida"));
        CodeTable.put("115", new CodeDetails("Rechazada", true));
        CodeTable.put("116", new CodeDetails("Fondos insuficientes", true));
        CodeTable.put("117", new CodeDetails("PIN invalido"));
        CodeTable.put("119", new CodeDetails("Llame al Emisor"));
        CodeTable.put("121", new CodeDetails("Excede limite permitido"));
        CodeTable.put("122", new CodeDetails("Codigo de seguridad invalido"));
        CodeTable.put("125", new CodeDetails("Tarjeta expirada"));
        CodeTable.put("130", new CodeDetails("Rechazada"));
        CodeTable.put("181", new CodeDetails("Error de formato"));
        CodeTable.put("183", new CodeDetails("Moneda no valida"));
        CodeTable.put("187", new CodeDetails("Recojer tarjeta"));
        CodeTable.put("189", new CodeDetails("Rechazada"));
        CodeTable.put("190", new CodeDetails("Rechazada"));
        CodeTable.put("193", new CodeDetails("Pais no valido"));
        CodeTable.put("200", new CodeDetails("Recojer tarjeta"));
        CodeTable.put("900", new CodeDetails("Rechazada"));
        CodeTable.put("909", new CodeDetails("Servicio no disponible"));
        CodeTable.put("912", new CodeDetails("Servicio no disponible"));
        CodeTable.put("977", new CodeDetails("Plan de pago invalido"));
        CodeTable.put("978", new CodeDetails("Excede numero de intentos"));
        CodeTable.put("400", new CodeDetails("Transacción no permitida"));
    }

    public static HashMap<String, CodeDetails> getCodeResponses() { return CodeTable; }

    public static CodeDetails getCodeDetails(String key) {
        CodeDetails details = CodeTable.get(key);
        return details != null ? details : new CodeDetails("Error desconocido");
    }

    public static class CodeDetails {
        public String description;
        public boolean isSuccessCode;

        public CodeDetails(String _description,boolean... _isSuccessCode) {
            this.description = _description;
            this.isSuccessCode = _isSuccessCode.length > 0 ? _isSuccessCode[0] : false;
        }

    }

}
