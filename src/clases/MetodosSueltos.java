package clases;

public class MetodosSueltos {

    public static double numeroHoras(String horaInicio, String horaFin) {

        double numHoraInicio;
        double numHoraFin;

        String[] horaMinutoInicio = horaInicio.split(":");

        numHoraInicio = Double.parseDouble(horaMinutoInicio[0]);
        if (Integer.parseInt(horaMinutoInicio[1]) == 30) {
            numHoraInicio += 0.5;
        }

        String[] horaMinutoFin = horaFin.split(":");

        numHoraFin = Double.parseDouble(horaMinutoFin[0]);
        if (Integer.parseInt(horaMinutoFin[1]) == 30) {
            numHoraFin += 0.5;
        }

        return numHoraFin - numHoraInicio;

    }

}
